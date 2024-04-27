package com.example.furniturebuyandsell.model;

import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("_id") // Annotation để liên kết trường này với trường "_id" từ MongoDB
    private String id;
    private String bannerLink;

    public Banner() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }
}


