package com.robot.homeobot.services.pki;

import com.robot.homeobot.dtos.AllCertificatesDTO;
import com.robot.homeobot.dtos.NewCertificateDTO;
import com.robot.homeobot.models.Revocation;
import com.robot.homeobot.repositories.RevocationRepository;
import com.robot.homeobot.services.pki.certificates.CertificateGenerator;
import com.robot.homeobot.services.pki.data.IssuerData;
import com.robot.homeobot.services.pki.data.SubjectData;
import com.robot.homeobot.services.pki.keystores.KeyStoreReader;
import com.robot.homeobot.services.pki.keystores.KeyStoreWriter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.jni.Local;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jcajce.provider.asymmetric.X509;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class CertificateService {

    public static final String[] VALID_REVOCATION_REASONS = {"unspecified",
                                                            "keyCompromise",
                                                            "cACompromise",
                                                            "affiliationChanged",
                                                            "superseded",
                                                            "cessationOfOperation",
                                                            "certificateHold",
                                                            "removeFromCRL",
                                                            "privilegeWithdrawn",
                                                            "aACompromise"};

    @Autowired
    private KeyStoreReader keyStoreReader;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private CSRService csrService;

    @Autowired
    private CertificateGenerator certificateGenerator;

    @Autowired
    private RevocationRepository revocationRepository;

    private IssuerData getIssuerData() {
        return keyStoreReader.readIssuerFromStore("store/keystore.jks", "intermediateca",
                "password".toCharArray(), "password".toCharArray());
    }

    public void createCertificate(NewCertificateDTO dto, String csrFilename) throws Exception {
        // validate dto

        if (!csrService.commonNameExistsInCSRFiles(csrFilename)) {
            throw new Exception("CSR file with this common name does not exist!");
        }

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, dto.getCN());
        builder.addRDN(BCStyle.OU, dto.getOU());
        builder.addRDN(BCStyle.O, dto.getO());
        builder.addRDN(BCStyle.L, dto.getL());
        builder.addRDN(BCStyle.ST, dto.getST());
        builder.addRDN(BCStyle.C, dto.getC());

        X500Name x500Name = builder.build();

        LocalDateTime startDate = dto.getStart();
        LocalDateTime endDate = dto.getEnd();
        if (endDate.isBefore(startDate)) {
            throw new Exception("Invalid dates.");
        }
        Date startDateConverted = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date endDateConverted = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

        PublicKey subjPublicKey = csrService.getSubjectDataFromCSR(csrFilename).getPublicKey();

        String serialNumber = RandomStringUtils.random(20, false, true);

        SubjectData subjectData = new SubjectData(subjPublicKey, x500Name, serialNumber, startDateConverted, endDateConverted);

        X509Certificate cert = certificateGenerator.generateCertificate(subjectData, this.getIssuerData());

        if (cert != null) {
            keyStoreWriter.loadKeyStore("store/keystore.jks", "password".toCharArray());
            keyStoreWriter.writeCertificateOnly(dto.getCN(), cert);
            keyStoreWriter.saveKeyStore("store/keystore.jks", "password".toCharArray());

            csrService.deleteCSR(csrFilename);
        }
    }

    public void revokeCertificate(String CN, String reason) throws Exception {
        if (CN.equals("rootca") || CN.equals("intermediateca")) {
            throw new Exception("Insufficient permissions!");
        }

        if (revocationRepository.findByCN(CN) != null) {
            throw new Exception("Certificate already revoked!");
        }

        List<String> validReasons = new ArrayList<>(Arrays.asList(VALID_REVOCATION_REASONS));
        if (!validReasons.contains(reason)) {
            throw new Exception("Invalid revocation reason!");
        }

        boolean permanent = true;
        if (reason.equals("certificateHold")) {
            permanent = false;
        }

        revocationRepository.save(new Revocation(CN, reason, permanent, LocalDateTime.now()));
    }

    public void unrevokeCertificateOnHold(String CN) throws Exception {
        Revocation revocation = revocationRepository.findByCN(CN);
        if (revocation == null) {
            throw new Exception("Certificate not in revocation list!");
        }

        if (revocation.isPermanent()) {
            throw new Exception("Certificate is permanently revoked!");
        }

        revocationRepository.deleteById(revocation.getId());
    }

    public String getCertificateStatusAndVerify(String CN) throws Exception {
        Certificate certificate = keyStoreReader.readCertificate("store/keystore.jks", "password", CN);

        if (certificate == null) {
            throw new Exception("Certificate with that alias does not exist!");
        }

        Revocation revocation = revocationRepository.findByCN(CN);
        if (revocation != null) {
            return String.format("Revoked (%s)", revocation.getReason());
        }

        // This checks that current time is within cert's validity window:
        try {
            ((X509Certificate) certificate).checkValidity();
        } catch (CertificateException e) {
            return "Invalid";
        }

        // CA public key
        PublicKey caPubKey = keyStoreReader.readCertificate("store/keystore.jks", "password", "intermediateca").getPublicKey();
        try {
            certificate.verify(caPubKey);
        } catch (GeneralSecurityException e) {
            return "Corrupted";
        }

        return "Valid";
    }

    public PublicKey getPublicKeyFromCertificate(String CN) {
        Certificate certificate = keyStoreReader.readCertificate("store/keystore.jks", "password", CN);

        return certificate.getPublicKey();
    }

    public List<AllCertificatesDTO> getAllCertificates() throws Exception {
        List<AllCertificatesDTO> dtos = new ArrayList<>();

        X509Certificate rootCA = (X509Certificate) keyStoreReader.readCertificate("store/keystore.jks", "password", "rootca");
        X500Name x500name = new JcaX509CertificateHolder(rootCA).getSubject();
        String CN = x500name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
        String OU = x500name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString();
        String O = x500name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString();
        String L = x500name.getRDNs(BCStyle.L)[0].getFirst().getValue().toString();
        String ST = x500name.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString();
        String C = x500name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString();
        LocalDateTime start = LocalDateTime.ofInstant(rootCA.getNotBefore().toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(rootCA.getNotAfter().toInstant(), ZoneId.systemDefault());
        String status = "Valid (ROOT)";
        AllCertificatesDTO dto = new AllCertificatesDTO(CN, OU, O, L, ST, C, start, end, status);
        dtos.add(dto);

        X509Certificate intermediateCA = (X509Certificate) keyStoreReader.readCertificate("store/keystore.jks", "password", "intermediateca");
        x500name = new JcaX509CertificateHolder(intermediateCA).getSubject();
        CN = x500name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
        OU = x500name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString();
        O = x500name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString();
        L = x500name.getRDNs(BCStyle.L)[0].getFirst().getValue().toString();
        ST = x500name.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString();
        C = x500name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString();
        start = LocalDateTime.ofInstant(intermediateCA.getNotBefore().toInstant(), ZoneId.systemDefault());
        end = LocalDateTime.ofInstant(intermediateCA.getNotAfter().toInstant(), ZoneId.systemDefault());
        status = "Valid (INTERMEDIATE)";
        dto = new AllCertificatesDTO(CN, OU, O, L, ST, C, start, end, status);
        dtos.add(dto);

        Enumeration<String> aliases = keyStoreReader.getAllAliases("store/keystore.jks", "password");
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.equals("rootca") || alias.equals("intermediateca")) {
                continue;
            }
            X509Certificate leafCertificate = (X509Certificate) keyStoreReader.readCertificate("store/keystore.jks", "password", alias);
            x500name = new JcaX509CertificateHolder(leafCertificate).getSubject();
            System.out.println(alias);
            CN = x500name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
            OU = x500name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString();
            O = x500name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString();
            L = x500name.getRDNs(BCStyle.L)[0].getFirst().getValue().toString();
            ST = x500name.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString();
            C = x500name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString();
            start = LocalDateTime.ofInstant(leafCertificate.getNotBefore().toInstant(), ZoneId.systemDefault());
            end = LocalDateTime.ofInstant(leafCertificate.getNotAfter().toInstant(), ZoneId.systemDefault());
            status = this.getCertificateStatusAndVerify(CN);
            dto = new AllCertificatesDTO(CN, OU, O, L, ST, C, start, end, status);
            dtos.add(dto);
        }

        return dtos;
    }
}
