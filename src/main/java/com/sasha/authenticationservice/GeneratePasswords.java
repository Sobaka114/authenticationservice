package com.sasha.authenticationservice;

import com.sasha.authenticationservice.crypto.AES;

public class GeneratePasswords {
    public static void main(String[] args) {
        final String secretKey = "test";

        String originalString = "222";
        String encryptedString = AES.encrypt(originalString, secretKey);
        String decryptedString = AES.decrypt(encryptedString, secretKey);

        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);

        System.out.println();

        final String secretKey2 = "test2";
        String originalString2 = "333";
        String encryptedString2 = AES.encrypt(originalString2, secretKey2);
        String decryptedString2 = AES.decrypt(encryptedString2, secretKey2);

        System.out.println(originalString2);
        System.out.println(encryptedString2);
        System.out.println(decryptedString2);

    }
}
