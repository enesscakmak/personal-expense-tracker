package com.PersonalExpense.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {
    @NotBlank(message = "Name is mandatory!")
    private String name;
    @NotBlank(message = "E-mail is mandatory!")
    @Email(message = "E-mail should be valid.")
    private String email;

    public UserUpdateRequest(){

    }

    public UserUpdateRequest(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
