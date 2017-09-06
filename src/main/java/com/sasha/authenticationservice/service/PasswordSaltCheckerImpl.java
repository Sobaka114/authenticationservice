package com.sasha.authenticationservice.service;

import com.sasha.authenticationservice.service.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class PasswordSaltCheckerImpl implements PasswordSaltChecker {
    @Value("${salt.file.path}")
    String filePath;

    @Override
    public boolean checkSalt(String passwordLeft, UserEntity userEntity) throws NoSuchAlgorithmException {
        String salt = getSalt(passwordLeft);
        String realSalt = getRealSalt(userEntity);
        return salt.equals(realSalt);
    }

    private String getRealSalt(UserEntity userEntity) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(getFilePath(userEntity)));
            return new String(bytes);
        } catch (IOException e) {
            log.error("file read exception", e);
        }
        return "";
    }

    private String getFilePath(UserEntity userEntity) {
        return filePath + "/" + userEntity.getName() + userEntity.getId() + ".txt";
    }

    private String getSalt(String halfPassword) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        byte[] digest = messageDigest.digest(halfPassword.getBytes());

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return  sb.toString();
    }
}
