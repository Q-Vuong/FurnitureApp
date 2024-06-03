package com.example.furniturebuyandsell.model;

import com.google.gson.annotations.SerializedName;

public class Discount {
    @SerializedName("_id")
    private String id;
    private String code;
    private int amount;

    public Discount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
