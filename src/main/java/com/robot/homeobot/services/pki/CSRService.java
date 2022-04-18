//package com.robot.homeobot.services.pki;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.SecureRandom;
//import java.security.Signature;
//
//import sun.security.pkcs.PKCS10;
//import sun.security.x509.X500Name;
//import sun.security.x509.X500Signer;
//
///**
// * This class generates PKCS10 certificate signing request
// *
// * @author Pankaj@JournalDev.com
// * @version 1.0
// */
//public class CSRService {
//    private static PublicKey publicKey = null;
//    private static PrivateKey privateKey = null;
//    private static KeyPairGenerator keyGen = null;
//    private static CSRService gcsr = null;
//
//    private CSRService() {
//        try {
//            keyGen = KeyPairGenerator.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        keyGen.initialize(2048, new SecureRandom());
//        KeyPair keypair = keyGen.generateKeyPair();
//        publicKey = keypair.getPublic();
//        privateKey = keypair.getPrivate();
//    }
//
//    public static CSRService getInstance() {
//        if (gcsr == null)
//            gcsr = new CSRService();
//        return gcsr;
//    }
//
//    public String getCSR(String cn) throws Exception {
//        byte[] csr = generatePKCS10(cn, "Java", "JournalDev", "Cupertino",
//                "California", "USA");
//        return new String(csr);
//    }
//
//    /**
//     *
//     * @param CN
//     *            Common Name, is X.509 speak for the name that distinguishes
//     *            the Certificate best, and ties it to your Organization
//     * @param OU
//     *            Organizational unit
//     * @param O
//     *            Organization NAME
//     * @param L
//     *            Location
//     * @param S
//     *            State
//     * @param C
//     *            Country
//     * @return
//     * @throws Exception
//     */
//    private static byte[] generatePKCS10(String CN, String OU, String O,
//                                         String L, String S, String C) throws Exception {
//        // generate PKCS10 certificate request
//        String sigAlg = "MD5WithRSA";
//        PKCS10 pkcs10 = new PKCS10(publicKey);
//        Signature signature = Signature.getInstance(sigAlg);
//        signature.initSign(privateKey);
//        // common, orgUnit, org, locality, state, country
//        X500Name x500Name = new X500Name(CN, OU, O, L, S, C);
//        pkcs10.encodeAndSign(new X500Signer(signature, x500Name));
//        ByteArrayOutputStream bs = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(bs);
//        pkcs10.print(ps);
//        byte[] c = bs.toByteArray();
//        try {
//            if (ps != null)
//                ps.close();
//            if (bs != null)
//                bs.close();
//        } catch (Throwable th) {
//        }
//        return c;
//    }
//
//    public PublicKey getPublicKey() {
//        return publicKey;
//    }
//
//    public PrivateKey getPrivateKey() {
//        return privateKey;
//    }
//
//    public static void main(String[] args) throws Exception {
//        CSRService gcsr = CSRService.getInstance();
//
//        System.out.println("Public Key:\n"+gcsr.getPublicKey().toString());
//
//        System.out.println("Private Key:\n"+gcsr.getPrivateKey().toString());
//        String csr = gcsr.getCSR("journaldev.com <https://www.journaldev.com>");
//        System.out.println("CSR Request Generated!!");
//        System.out.println(csr);
//    }
//
//}
