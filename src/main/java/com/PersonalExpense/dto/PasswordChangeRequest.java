package com.PersonalExpense.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeRequest {
    @NotBlank(message = "Need the old password")
    private String currentPassword;
    @NotBlank(message = "Need the new password")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
