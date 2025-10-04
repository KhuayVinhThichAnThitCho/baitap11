package com.example.demo3.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;
}