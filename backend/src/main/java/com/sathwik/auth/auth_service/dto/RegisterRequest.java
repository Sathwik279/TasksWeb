package com.sathwik.auth.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest{
    private String userName;
    private String email;
    private String password;
}
