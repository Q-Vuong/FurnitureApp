package com.example.furniturebuyandsell.fragment.option_user;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.ChangePasswordRequest;

import retrofit2.Call;


public class ChangePassFragment extends DialogFragment {

    Button  btn_change;
    EditText edt_passOld, edt_passNew, edt_rePass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        btn_change = view.findViewById(R.id.btn_Change);
        edt_passOld = view.findViewById(R.id.edt_passOld);
        edt_passNew = view.findViewById(R.id.edt_passNew);
        edt_rePass = view.findViewById(R.id.edt_passRe);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passOld = edt_passOld.getText().toString().trim();
                String passNew = edt_passNew.getText().toString();
                String rePass = edt_rePass.getText().toString();

                if (!passNew.equals(rePass)) {
                    Toast.makeText(getContext(), "Mật khẩu phải trùng nhau!", Toast.LENGTH_SHORT).show();
                } else{
                    ChangePasswordRequest request = new ChangePasswordRequest(passOld, passNew);
                    String customerId = Constants.idUser;

                    ApiService apiService = Constants.getApiService();
                    Call<Void> call = apiService.changePassword(request, customerId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                dismiss();
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
        });



        return view;
    }
}