package com.example.furniturebuyandsell.fragment.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturebuyandsell.LoginActivity;
import com.example.furniturebuyandsell.MainActivity;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        EditText edtUser = view.findViewById(R.id.edtUser);
        EditText edtPass = view.findViewById(R.id.edtPass);
        TextView txtForgot = view.findViewById(R.id.txtForgot);
        TextView txtSignUp = view.findViewById(R.id.txtSignup);
        Button btnLogin = view.findViewById(R.id.btnLogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();

                ApiService apiService = Constants.getApiService();

                User request = new User();
                request.setEmail(user);
                request.setPassword(pass);

                Call<AuthResponse> call = apiService.loginUser(request);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            AuthResponse authResponse = response.body();
                            if (authResponse != null) {
                                String message = authResponse.getMessage();
                                User thisCustomer = authResponse.getThisCustomer();
                                if (thisCustomer != null) {
                                    //Nhận thông tin của khách hàng sau khi đăng nhập thành công
                                    Constants.currentUser = thisCustomer;
                                    Constants.idUser = thisCustomer.getId();
                                    Log.d("currentUser", String.valueOf(Constants.currentUser.getAvatar()));
                                }
                                // Xử lý phản hồi thành công từ máy chủ
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        } else {
                            // Xử lý lỗi khi gửi yêu cầu đăng ký
                            Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Không thể đăng nhập bây giờ. Vui thòng thử lại sau!", Toast.LENGTH_SHORT).show();
                        Log.e("API Call", "Failed to login", t);
                    }
                });
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) requireActivity()).switchToMainRegisterFragment();
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) requireActivity()).switchToForgotFragment();
            }
        });
        return view;
    }
}