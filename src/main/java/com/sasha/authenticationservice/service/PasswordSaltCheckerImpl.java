package com.sasha.authenticationservice.service;

import com.sasha.authenticationservice.service.entity.UserEntity;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordSaltCheckerImpl implements PasswordSaltChecker {
    @Override
    public boolean checkSalt(String passwordLeft, UserEntity userEntity) throws NoSuchAlgorithmException {
        String salt = getSalt(passwordLeft);
        String realSalt = getRealSalt(userEntity);
        return salt.equals(realSalt);
    }

    //Mock. get md5 for password from data_sql.
    //TODO get it from file
    private String getRealSalt(UserEntity userEntity) {
        if(userEntity.getId() == 1) {
            return "bcbe3365e6ac95ea2c0343a2395834dd";
        }
        if(userEntity.getId() == 2) {
            return "310dcbbf4cce62f762a2aaa148d556bd";
        }
        throw new UnsupportedOperationException("such user does not mocked.");
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
