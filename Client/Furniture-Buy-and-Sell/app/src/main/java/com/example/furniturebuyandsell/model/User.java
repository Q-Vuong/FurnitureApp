package com.example.furniturebuyandsell.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.Date;
import java.util.ArrayList;

public class User implements Parcelable {
    @SerializedName("_id")
    private String id;
    private String googleId;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthDate;
    private String avatar;
    private ArrayList<String> favoriteProducts;
    private ArrayList<CartItem> cart;


    public User() {
    }

    protected User(Parcel in) {
        id = in.readString();
        googleId = in.readString();
        email = in.readString();
        password = in.readString();
        fullName = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        gender = in.readString();
        birthDate = in.readString();
        avatar = in.readString();
        favoriteProducts = in.createStringArrayList();
        cart = in.createTypedArrayList(CartItem.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<String> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(ArrayList<String> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(googleId);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(fullName);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        parcel.writeString(gender);
        parcel.writeString(birthDate);
        parcel.writeString(avatar);
        parcel.writeStringList(favoriteProducts);
        parcel.writeTypedList(cart);
    }

    // Định nghĩa lớp con hoặc lớp bên trong để đại diện cho mỗi mục trong giỏ hàng
    public static class CartItem implements Parcelable {
        @SerializedName("_id")
        private String id;
        private String productId;
        private int quantity;

        public CartItem() {
        }

        public CartItem(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        protected CartItem(Parcel in) {
            id = in.readString();
            productId = in.readString();
            quantity = in.readInt();
        }

        public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
            @Override
            public CartItem createFromParcel(Parcel in) {
                return new CartItem(in);
            }

            @Override
            public CartItem[] newArray(int size) {
                return new CartItem[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(productId);
            parcel.writeInt(quantity);
        }
    }
}
