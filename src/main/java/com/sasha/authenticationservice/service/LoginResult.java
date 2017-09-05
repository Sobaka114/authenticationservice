package com.sasha.authenticationservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResult {
    private String error;
    private String userName;
}
