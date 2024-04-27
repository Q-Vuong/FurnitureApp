package com.example.furniturebuyandsell.model;

import com.google.gson.annotations.SerializedName;

public class ProductType {
    @SerializedName("_id")
    private String id;
    private String nameType;

    private String imgPr_T;

    public ProductType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getImgPr_T() {
        return imgPr_T;
    }

    public void setImgPr_T(String imgPr_T) {
        this.imgPr_T = imgPr_T;
    }
}
