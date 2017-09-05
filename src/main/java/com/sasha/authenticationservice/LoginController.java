package com.sasha.authenticationservice;

import com.sasha.authenticationservice.service.AuthenticationService;
import com.sasha.authenticationservice.service.entity.UserEntity;
import com.sasha.authenticationservice.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
@Slf4j
public class LoginController {
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/login",  method = RequestMethod.GET)
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void userLogin(String userName, String password) throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, IOException {
        log.info("in login {} {} \n", userName, password);
        authenticationService.userLogin(userName, password);
        /*UserEntity oneByName = userRepository.findOneByName(userName);
        if(oneByName != null) {
            log.info("find user by name {}. id {}", userName, oneByName.getId());
        }*/
    }

}
