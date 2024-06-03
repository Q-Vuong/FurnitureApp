package com.example.furniturebuyandsell.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {
    @SerializedName("_id")
    private String id;
    private String namePr;
    private String imagePr;
    private boolean featured;
    private boolean isNew;
    private float price;
    private String discount;
    private String size;
    private int quantity;
    private String description;
    private String type;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamePr() {
        return namePr;
    }

    public void setNamePr(String namePr) {
        this.namePr = namePr;
    }

    public String getImagePr() {
        return imagePr;
    }

    public void setImagePr(String imagePr) {
        this.imagePr = imagePr;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readString();
        namePr = in.readString();
        imagePr = in.readString();
        featured = in.readByte() != 0;
        isNew = in.readByte() != 0;
        price = in.readFloat();
        discount = in.readString();
        size = in.readString();
        quantity = in.readInt();
        description = in.readString();
        type = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(namePr);
        dest.writeString(imagePr);
        dest.writeByte((byte) (featured ? 1 : 0));
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeFloat(price);
        dest.writeString(discount);
        dest.writeString(size);
        dest.writeInt(quantity);
        dest.writeString(description);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
