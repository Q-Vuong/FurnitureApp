package com.example.furniturebuyandsell.fragment.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Debug;
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
import com.example.furniturebuyandsell.model.ChangeNewPasswordRequest;
import com.example.furniturebuyandsell.model.ChangePasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangeNewPassFragment extends Fragment {

    EditText edt_newPass, edt_rePass;
    Button btn_changePass;

    private String email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_new_pass, container, false);
        edt_newPass = view.findViewById(R.id.edt_passNew);
        edt_rePass = view.findViewById(R.id.edt_passRe);
        btn_changePass = view.findViewById(R.id.btn_Change);

        if (getArguments() != null) {
            // Xác thực mã OTP
            email = getArguments().getString("email");
            Log.d("emailforchangepass", email);
        }


        btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword(email);
            }
        });



        return view;
    }

    private void forgotPassword(String email){
        String newPass = edt_newPass.getText().toString();
        String rePass = edt_rePass.getText().toString();

        if (!newPass.equals(rePass)) {
            Toast.makeText(getContext(), "Mật khẩu phải trùng nhau!", Toast.LENGTH_SHORT).show();
        } else{
            ApiService apiService = Constants.getApiService();

            ChangeNewPasswordRequest request = new ChangeNewPasswordRequest(email, newPass);

            Call<Void> call = apiService.forgotPass(request);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        ((LoginActivity) requireActivity()).switchToLoginFragment();
                    } else {
                        Toast.makeText(requireContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}