package com.robot.homeobot.services.pki;

import com.robot.homeobot.dtos.NewCertificateDTO;
import com.robot.homeobot.services.pki.certificates.CertificateGenerator;
import com.robot.homeobot.services.pki.data.IssuerData;
import com.robot.homeobot.services.pki.data.SubjectData;
import com.robot.homeobot.services.pki.keystores.KeyStoreReader;
import com.robot.homeobot.services.pki.keystores.KeyStoreWriter;
import org.apache.commons.lang.RandomStringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Service
public class CertificateService {

    @Autowired
    private KeyStoreReader keyStoreReader;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private CSRService csrService;

    @Autowired
    private CertificateGenerator certificateGenerator;

    private IssuerData getIssuerData() {
        return keyStoreReader.readIssuerFromStore("store/keystore.jks", "intermediateca",
                "password".toCharArray(), "password".toCharArray());
    }

    public void createCertificate(NewCertificateDTO dto, String csrFilename) throws Exception {
        // validate dto

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
}
