package com.robot.homeobot.services.pki;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.robot.homeobot.dtos.CSRDTO;
import com.robot.homeobot.services.pki.data.SubjectData;
import com.robot.homeobot.services.pki.keystores.KeyStoreReader;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;

/**
 * This class generates PKCS10 certificate signing request
 *
 * @author Pankaj@JournalDev.com
 * @version 1.0
 */
@Service
public class CSRService {
    public CSRService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    private KeyStoreReader keyStoreReader;

    /**
     *
     * @param CN
     *            Common Name, is X.509 speak for the name that distinguishes
     *            the Certificate best, and ties it to your Organization
     * @param OU
     *            Organizational unit
     * @param O
     *            Organization NAME
     * @param L
     *            Location
     * @param ST
     *            State
     * @param C
     *            Country
     * @throws Exception
     */
    public void generateCSR(String CN, String OU, String O,
                                         String L, String ST, String C) throws Exception {
        if (this.commonNameExists(CN)) {
            throw new Exception("Common name is not unique!");
        }

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                new X500Principal(String.format("CN=%s, OU=%s, O=%s, L=%s, ST=%s, C=%s", CN, OU, O, L, ST, C)), keyPair.getPublic());

        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());
        PKCS10CertificationRequest csr = p10Builder.build(signer);

        JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new FileWriter(String.format("store/csr/%s.csr", CN)));
        jcaPEMWriter.writeObject(csr);
        jcaPEMWriter.close();

        JcaPEMWriter writer = new JcaPEMWriter(new PrintWriter(String.format("store/private_keys_to_distribute/%s.key", CN)));
        writer.writeObject(keyPair.getPrivate());
        writer.close();
    }

    public SubjectData getSubjectDataFromCSR(String filename) throws Exception {
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(Files.readString(Paths.get(String.format("store/csr/%s.csr", filename))).getBytes());
             final InputStreamReader isr = new InputStreamReader(bais, StandardCharsets.UTF_8);
             final PEMParser pem = new PEMParser(isr))
        {
            PKCS10CertificationRequest csr = (PKCS10CertificationRequest) pem.readObject();

            SubjectPublicKeyInfo pkInfo = csr.getSubjectPublicKeyInfo();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PublicKey pubKey = converter.getPublicKey(pkInfo);

            SubjectData subjectData = new SubjectData();
            subjectData.setPublicKey(pubKey);
            subjectData.setX500name(csr.getSubject());

            return subjectData;
        }
    }

    public boolean deleteCSR(String filename) throws Exception {
        return new File(String.format("store/csr/%s.csr", filename)).delete();
    }

    public boolean deleteCSRAndPrivateKey(String filename) throws Exception {
        if (deleteCSR(filename)) {
            return new File(String.format("store/private_keys_to_distribute/%s.key", filename)).delete();
        }
        else return false;
    }

    public List<CSRDTO> getAllCSRs() throws Exception {
        List<CSRDTO> CSRs = new ArrayList<>();

        File dir = new File("store/csr");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".csr"))) {
                String filename = file.getName().substring(0, file.getName().length() - 4);
                SubjectData subjectDataFromCSR = this.getSubjectDataFromCSR(filename);
                String CN = subjectDataFromCSR.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
                String OU = subjectDataFromCSR.getX500name().getRDNs(BCStyle.OU)[0].getFirst().getValue().toString();
                String O = subjectDataFromCSR.getX500name().getRDNs(BCStyle.O)[0].getFirst().getValue().toString();
                String L = subjectDataFromCSR.getX500name().getRDNs(BCStyle.L)[0].getFirst().getValue().toString();
                String ST = subjectDataFromCSR.getX500name().getRDNs(BCStyle.ST)[0].getFirst().getValue().toString();
                String C = subjectDataFromCSR.getX500name().getRDNs(BCStyle.C)[0].getFirst().getValue().toString();

                CSRDTO csr = new CSRDTO(CN, OU, O, L, ST, C);
                CSRs.add(csr);
            }
        }

        return CSRs;
    }

    public boolean commonNameExists(String CN) throws Exception {
        if (commonNameExistsInCSRFiles(CN)) {
            return true;
        }

        // add: if certificate invalid, common name does not exist
        Enumeration<String> aliases = keyStoreReader.getAllAliases("store/keystore.jks", "password");
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (CN.equals(alias)) {
                return true;
            }
        }

        return false;
    }

    public boolean commonNameExistsInCSRFiles(String CN) throws Exception {
        File dir = new File("store/csr");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".csr"))) {
                String filename = file.getName().substring(0, file.getName().length() - 4);
                SubjectData subjectDataFromCSR = this.getSubjectDataFromCSR(filename);
                String existingCN = subjectDataFromCSR.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();

                if (CN.equals(existingCN)) {
                    return true;
                }
            }
        }

        return false;
    }
}
