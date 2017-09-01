package com.sasha.authenticationservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/")
public class AuthenticationService {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testRest() {
        return "Hello world!";
    }

}
