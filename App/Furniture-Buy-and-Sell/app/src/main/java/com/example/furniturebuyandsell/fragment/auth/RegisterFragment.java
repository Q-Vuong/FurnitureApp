package com.example.furniturebuyandsell.fragment.auth;

import static android.content.ContentValues.TAG;

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
import com.example.furniturebuyandsell.model.OtpRequest;
import com.example.furniturebuyandsell.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        EditText edtUser = view.findViewById(R.id.edtUser);
        EditText edtPass = view.findViewById(R.id.edtPass);
        EditText edtRePass = view.findViewById(R.id.edtRePass);
        Button btn_Register = view.findViewById(R.id.btnRegister);


        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy địa chỉ email từ EditText
                String email = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString();
                String rePass = edtRePass.getText().toString();

                // Kiểm tra xem email có hợp lệ không
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(getContext(), "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(rePass)) {
                    Toast.makeText(getContext(), "Mật khẩu phải trùng nhau!", Toast.LENGTH_SHORT).show();
                } else {
                    // Gửi mã OTP đến email
                    //sendOTP(email, pass);
                    sendOtpRequest(email, pass);
                }
            }
        });


        return view;
    }

    private ActionCodeSettings getEmailActionCodeSettings() {
        return ActionCodeSettings.newBuilder()
                .setHandleCodeInApp(true)
                .setUrl("http://localhost")
                .setAndroidPackageName(
                        "com.example.furniturebuyandsell", // Tên gói của ứng dụng Android
                        false, // Ứng dụng có phải là ứng dụng cài đặt mặc định không
                        "1" // Phiên bản tối thiểu của ứng dụng
                )
                .build();

    }


    private void sendOTP(String email, String pass) {
        mAuth.sendSignInLinkToEmail(email, getEmailActionCodeSettings())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Gửi mã OTP thành công
                            Toast.makeText(requireContext(), "Mã OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();

                            // Chuyển sang màn hình xác nhận OTP
                            // Khởi tạo Bundle và đưa dữ liệu vào
                            Bundle bundle = new Bundle();
                            bundle.putString("Email", email);
                            bundle.putString("pass", pass);

                            EnterOTPFragment enterOTPFragment = new EnterOTPFragment();
                            enterOTPFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.fragmentLayout, enterOTPFragment)
                                    .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                                    .commit();
                        } else {
                            // Gửi mã OTP thất bại
                            Toast.makeText(requireContext(), "Có lỗi xảy ra. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                            Log.e("Send OTP", "Error sending OTP", task.getException());
                        }
                    }
                });
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void sendOtpRequest(String email, String pass) {
        OtpRequest request = new OtpRequest(email);

        ApiService apiService = Constants.getApiService();
        Call<AuthResponse> call = apiService.sendOtp(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    String otp = response.body().getOtp();
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "OTP: " + otp);

                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("pass", pass);

                    EnterOTPFragment enterOTPFragment = new EnterOTPFragment();
                    enterOTPFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.fragmentLayout, enterOTPFragment)
                            .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                            .commit();
                } else {
                    Toast.makeText(requireContext(), "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}