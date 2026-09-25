package com.PersonalExpense.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequest {
    @NotBlank(message = "Refresh token is mandatory.")
    private String refreshToken;

    public RefreshRequest(){}

    public RefreshRequest(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String token){
        this.refreshToken = token; 
    }
}
