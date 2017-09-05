package com.sasha.authenticationservice.service;

import com.sasha.authenticationservice.service.entity.UserEntity;
import com.sasha.authenticationservice.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
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
            loginResult.setError("Login name is not right");
            return loginResult;
        }
        //first check salt
        boolean checkSalts = checkSalts(left, oneByName);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserName(oneByName.getName());
        //if salt doest not equals to the original , return password error;
        if (!checkSalts) {
            loginResult.setError("password is not right");
            return loginResult;
        }
        //check right part of password

        boolean checkDbPassword = checkDbPassword(right, oneByName);
        if (!checkDbPassword) {
            //code duplicate
            loginResult.setError("password is not right");
            return loginResult;
        }

        return loginResult;
    }

    //can be depends on salt. Current realisation - only user name depends on encryption key
    private SecretKeySpec getKey( UserEntity userEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = (userEntity.getName()).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        return new SecretKeySpec(key, userEntity.getEncryption().toString());
    }

    private boolean checkDbPassword(String right, UserEntity userEntity) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance(userEntity.getEncryption().toString());
        encryptCipher.init(Cipher.DECRYPT_MODE, getKey(userEntity));
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(userEntity.getPassword());
        byte[] bytes = encryptCipher.doFinal(decordedValue);
        String realPassword = new String(bytes);
        return realPassword.equals(right);
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
