package com.example.springbootdemo.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class TeamNoEncryption {

    // 生成AES密钥（基于用户的token）
    private static SecretKeySpec generateKeyFromToken(String token) throws Exception {
        // 使用SHA-256生成32字节的密钥
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(token.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, "AES");
    }

    // 生成固定IV（实际应用中建议使用随机IV）
    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        System.arraycopy("1234567890abcdef".getBytes(StandardCharsets.UTF_8), 0, iv, 0, 16);
        return new IvParameterSpec(iv);
    }

    // 加密方法
    public static String encryptTeamNo(String teamNo, String token) throws Exception {
        SecretKeySpec secretKey = generateKeyFromToken(token);
        IvParameterSpec iv = generateIv();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(teamNo.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 解密方法
    public static String decryptTeamNo(String encryptedTeamNo, String token) throws Exception {
        SecretKeySpec secretKey = generateKeyFromToken(token);
        IvParameterSpec iv = generateIv();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedTeamNo);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try {
            // 模拟用户token
            String token = "user-unique-token";

            // 需要加密的team_no属性
            String teamNo = "Team12345";
            System.out.println("原始team_no: " + teamNo);

            // 加密
            String encryptedTeamNo = encryptTeamNo(teamNo, token);
            System.out.println("加密后的team_no: " + encryptedTeamNo);

            // 解密
            String decryptedTeamNo = decryptTeamNo(encryptedTeamNo, token);
            System.out.println("解密后的team_no: " + decryptedTeamNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
