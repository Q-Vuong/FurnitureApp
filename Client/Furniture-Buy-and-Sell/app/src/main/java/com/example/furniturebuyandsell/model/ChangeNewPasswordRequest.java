package com.example.furniturebuyandsell.model;

import com.google.gson.annotations.SerializedName;

public class ChangeNewPasswordRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("newPassword")
    private String newPassword;


    public ChangeNewPasswordRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

}
