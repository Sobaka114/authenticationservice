package com.sasha.authenticationservice.service;

import com.sasha.authenticationservice.service.entity.UserEntity;

import java.security.NoSuchAlgorithmException;

public interface PasswordSaltChecker {
    boolean checkSalt(String passwordLeft, UserEntity userEntity) throws NoSuchAlgorithmException;
}
