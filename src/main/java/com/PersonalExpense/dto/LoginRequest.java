package com.PersonalExpense.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "E-mail is mandatory.")
    @Email(message = "E-mail should be valid.")
    private String email;

    @NotBlank(message = "You need a valid password.")
    private String password;

    public LoginRequest(){}

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
