package com.sasha.authenticationservice.service;

import lombok.Data;

@Data
public class LoginResult {
    private String error;
    private String userName;
}
