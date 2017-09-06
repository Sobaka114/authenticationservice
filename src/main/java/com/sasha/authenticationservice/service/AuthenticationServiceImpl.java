package com.sasha.authenticationservice.service;

import com.sasha.authenticationservice.crypto.AES;
import com.sasha.authenticationservice.service.entity.UserEntity;
import com.sasha.authenticationservice.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordSaltChecker passwordSaltChecker;

    public LoginResult userLogin(String userName, String password) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException {
        if (password.length() < 2) {
            throw new IllegalStateException("password should not be less 2 symbols");
        }
        String left = password.substring(0, password.length() / 2);
        String right = password.substring(password.length() / 2);
        getSalt(left);
        UserEntity oneByName = userRepository.findOneByName(userName);

        if (oneByName == null) {
            LoginResult loginResult = new LoginResult();
            loginResult.setError("Login_name_is_not_right");
            return loginResult;
        }
        //first check salt
        boolean checkSalts = checkSalts(left, oneByName);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserName(oneByName.getName());
        //if salt doest not equals to the original , return password error;
        if (!checkSalts) {
            loginResult.setError("password_is_not_right");
            return loginResult;
        }
        //check right part of password

        boolean checkDbPassword = checkDbPassword(right, oneByName);
        if (!checkDbPassword) {
            //code duplicate
            loginResult.setError("password_is_not_right");
            return loginResult;
        }
        log.info("Login is ok. " + userName);
        return loginResult;
    }

    private boolean checkDbPassword(String right, UserEntity userEntity) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(userEntity.getEncryption() == UserEntity.EncryptionType.AES) {
            String decrypt = AES.decrypt(userEntity.getPassword(), userEntity.getName());
            return decrypt != null && decrypt.equals(right);
        }
        throw new UnsupportedOperationException("Encryption " + userEntity.getEncryption() + " Such encryption is not supported eat.");
    }

    private boolean checkSalts(String left, UserEntity oneByName) throws NoSuchAlgorithmException {
        return passwordSaltChecker.checkSalt(left, oneByName);
    }

    public String getSalt(String halfPassword) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        byte[] digest = messageDigest.digest(halfPassword.getBytes());

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
