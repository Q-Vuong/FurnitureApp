package com.example.furniturebuyandsell.fragment.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniturebuyandsell.LoginActivity;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.ChangePasswordRequest;
import com.example.furniturebuyandsell.model.OtpRequest;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnterOTPFragment extends Fragment {

    private EditText edt_OTP;
    private Button btn_Verify;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_o_t_p, container, false);

        mAuth = FirebaseAuth.getInstance();

        edt_OTP = view.findViewById(R.id.edt_OTP);
        btn_Verify = view.findViewById(R.id.btn_Verify);


        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy mã OTP từ EditText
                String otp = edt_OTP.getText().toString().trim();

                if (otp.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
                } else if (getArguments() != null) {
                    // Xác thực mã OTP
                    String email = getArguments().getString("email");
                    String pass = getArguments().getString("pass");
                    verifyOTP(email, pass, otp);
                }
            }
        });

        return view;
    }

    public void verifyOTP(String email,String pass, String otp) {
        ApiService apiService = Constants.getApiService();

        OtpRequest otpRequest = new OtpRequest(email, otp);

        Call<AuthResponse> call = apiService.verifyOTP(otpRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    // Xác thực thành công, có thể chuyển sang màn hình tiếp theo
                    if (pass != null){
                        register(email, pass);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        ChangeNewPassFragment changeNewPassFragment = new ChangeNewPassFragment();
                        changeNewPassFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.fragmentLayout, changeNewPassFragment)
                                .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                                .commit();
                    }


                } else {
                    // Xử lý khi xác thực không thành công
                    Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String user, String pass) {
        ApiService apiService = Constants.getApiService();

        User request = new User();
        request.setEmail(user);
        request.setPassword(pass);

        Call<AuthResponse> call = apiService.registerUser(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        User thisCustomer = authResponse.getThisCustomer();
                        String message = authResponse.getMessage();
                        if (thisCustomer != null) {
                            //Nhận thông tin của khách hàng sau khi đăng kí thành công
                            Constants.idUser = thisCustomer.getId();
                            Constants.currentUser = thisCustomer;
                            Log.d("idUser", Constants.idUser);
                        }
                        // Xử lý phản hồi thành công từ máy chủ
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        // Lập lịch chuyển Fragment sau một khoảng thời gian ngắn
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Chuyển Fragment
                                ((LoginActivity) requireActivity()).switchToInforUserFragment();
                            }
                        }, 2000); // 2000 milliseconds tương đương với Toast.LENGTH_SHORT
                    }
                } else {
                    // Xử lý lỗi khi gửi yêu cầu đăng ký
                    Toast.makeText(getContext(), "Không thể đăng ký", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể đăng ký bây giờ. Vui thòng thử lại sau!", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to register", t);
            }
        });
    }


}