package com.robot.homeobot.services.pki;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

import com.robot.homeobot.services.pki.data.SubjectData;
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
     * @param S
     *            State
     * @param C
     *            Country
     * @throws Exception
     */
    public void generateCSR(String CN, String OU, String O,
                                         String L, String ST, String C) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                new X500Principal(String.format("CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s", CN, OU, O, L, ST, C)), keyPair.getPublic());

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
}
