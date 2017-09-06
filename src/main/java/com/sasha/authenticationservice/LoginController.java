package com.sasha.authenticationservice;

import com.sasha.authenticationservice.service.AuthenticationService;
import com.sasha.authenticationservice.service.LoginResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public String userLogin(String userName, String password, HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, IOException {
        log.info("in login {} {} \n", userName, password);
        LoginResult loginResult = authenticationService.userLogin(userName, password);
        if(loginResult.getError() != null && !loginResult.getError().isEmpty()) {
            //is not right!!!!
            response.addCookie(new Cookie("MyAuthenticationService.error", loginResult.getError()));
            return "error";
        } else {
            response.addCookie(new Cookie("MyAuthenticationService.userName", userName));
            return "hello";
        }
    }

}
