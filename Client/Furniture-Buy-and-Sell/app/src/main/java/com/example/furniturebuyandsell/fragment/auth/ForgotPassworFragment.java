package com.example.furniturebuyandsell.fragment.auth;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.OtpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPassworFragment extends Fragment {
    EditText edt_gmail;
    Button btn_Verify;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forgot_pass, container, false);

        btn_Verify = view.findViewById(R.id.btn_Verify);
        edt_gmail = view.findViewById(R.id.edt_gmail);

        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy địa chỉ email từ EditText
                String email = edt_gmail.getText().toString().trim();

                // Kiểm tra xem email có hợp lệ không
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(getContext(), "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
                }  else {
                    // Gửi mã OTP đến email
                    //sendOTP(email, pass);
                    sendOtpRequest(email);

                    Toast.makeText(getContext(), "Mã OTP đang được gửi đến bạn", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);

                    EnterOTPFragment enterOTPFragment = new EnterOTPFragment();
                    enterOTPFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.fragmentLayout, enterOTPFragment)
                            .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                            .commit();
                }
            }
        });

        return view;
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void sendOtpRequest(String email) {
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