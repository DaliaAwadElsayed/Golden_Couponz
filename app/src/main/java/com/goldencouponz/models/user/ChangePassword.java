package com.goldencouponz.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassword {

    @SerializedName("current-password")
    @Expose
    private String oldPassword;
    @SerializedName("new-password")
    @Expose
    private String newPassword;
    @SerializedName("new-password_confirmation")
    @Expose
    private String newPasswordConfirmation;

    public ChangePassword() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
