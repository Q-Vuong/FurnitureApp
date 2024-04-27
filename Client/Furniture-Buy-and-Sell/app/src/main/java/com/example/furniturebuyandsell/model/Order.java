package com.example.furniturebuyandsell.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    @SerializedName("_id")
    private String id;
    private Date dateOrder;
    private String note;
    private String userId;
    private ArrayList<ProductItem> products;
    private String priceToPay;

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductItem> products) {
        this.products = products;
    }

    public String getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(String priceToPay) {
        this.priceToPay = priceToPay;
    }

    public static class ProductItem {
        @SerializedName("_id")
        private String id;
        private String productId;
        private int quantity;

        public ProductItem() {
        }

        public ProductItem(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

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
    }

}
