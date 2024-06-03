package com.example.furniturebuyandsell.common;

import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.User;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {

    public static final String ip_Address = "http://192.168.1.14:3000/";

    //Sử dụng Retrofit để gửi các yêu cầu HTTP
    private static Retrofit retrofit;
    private static ApiService apiService;
    public static synchronized ApiService getApiService() {
        if (apiService == null) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(ip_Address)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    //id này là id của người dùng sẽ dùng cho app
    public static String idUser;
    public static User currentUser;
    public static String idProduct;
}
