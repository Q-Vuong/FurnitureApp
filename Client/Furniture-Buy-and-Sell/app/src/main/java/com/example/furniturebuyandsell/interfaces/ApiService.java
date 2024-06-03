package com.example.furniturebuyandsell.interfaces;

import com.example.furniturebuyandsell.model.Banner;
import com.example.furniturebuyandsell.model.ChangeNewPasswordRequest;
import com.example.furniturebuyandsell.model.ChangePasswordRequest;
import com.example.furniturebuyandsell.model.Discount;
import com.example.furniturebuyandsell.model.Notification;
import com.example.furniturebuyandsell.model.Order;
import com.example.furniturebuyandsell.model.OtpRequest;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.ProductType;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.User;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    //____________banner________________
    @GET("banner/view-app")
    Call<ArrayList<Banner>> getBanners();
    //___________________product____________
    @GET("product/view-app")
    Call<ArrayList<Product>> getProducts();
    @POST("product/productbyid")
    Call<ArrayList<Product>> getProductById(@Body ArrayList<String> productIds);
    //_____________product type_______________________
    @GET("productType/view-app")
    Call<ArrayList<ProductType>> getProductTypes();
    //_________________customer________________________
    @POST("customer/changePassword/{customerId}")
    Call<Void> changePassword(@Body ChangePasswordRequest request, @Path("customerId") String customerId);
    @POST("customer/forgotPassword")
    Call<Void> forgotPass(@Body ChangeNewPasswordRequest request);
    @POST("customer/register")
    Call<AuthResponse> registerUser(@Body User request);
    @POST("customer/login")
    Call<AuthResponse> loginUser(@Body User request);
    @PUT("customer/update/{id}")
    Call<AuthResponse> updateUser(@Path("id") String id, @Body User request);
    @PUT("customer/updatefavoriteproduct/{id}")
    Call<AuthResponse> updateFavoriteProduct(@Path("id") String id, @Body User request);
    @GET("customer/getbyid/{userId}")
    Call<AuthResponse> getUserById(@Path("userId") String userId);
    //______________________search__________________
    @GET("search/infor")
    Call<ArrayList<Product>> searchInfor(@Query("q") String keyword);
    //_____________________Cart_______________________
    @PUT("cart/updatecart/{id}")
    Call<AuthResponse> updateCart(@Path("id") String id, @Body User.CartItem request);
    @GET("cart/view/{id}")
    Call<ArrayList<User.CartItem>> getCart(@Path("id") String id);
    @DELETE("cart/customer/{id}/cart/{productid}")
    Call<User.CartItem> deleteCart(@Path("id") String id, @Path("productid") String productId);
    //_____________________Discount_____________________________________________
    @GET("discount/view-app")
    Call<ArrayList<Discount>> getDiscounts();
    @POST("discount/check")
    Call<AuthResponse> checkCodeDiscount(@Body Discount request);
    //_______________________Order___________________________________
    @POST("order/add") // Đặt tên endpoint tương ứng với createOrder
    Call<AuthResponse> createOrder(@Body Order order);
    @GET("order/view-app")
    Call<ArrayList<Order>> getAllOrders();
    //_______________________Notification__________________________
    @GET("notification/view-app")
    Call<ArrayList<Notification>> getNotifications();
    //_________
    @POST("/auth/send-otp")
    Call<AuthResponse> sendOtp(@Body OtpRequest request);
    @POST("/auth/verify-otp")
    Call<AuthResponse> verifyOTP(@Body OtpRequest request);

}
