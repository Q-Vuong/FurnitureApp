package com.example.furniturebuyandsell.fragment.auth;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniturebuyandsell.LoginActivity;
import com.example.furniturebuyandsell.MainActivity;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.User;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InforUserFragment extends Fragment {
    EditText edt_fullName, edt_phoneNumber, edt_address, edt_streetName, edt_birthday, edt_id;
    Button btn_finish, btn_cancel;
    AutoCompleteTextView autocomplete_gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_infor_user, container, false);

        edt_fullName = view.findViewById(R.id.edt_fullName);
        edt_phoneNumber = view.findViewById(R.id.edt_phoneNumber);
        autocomplete_gender = view.findViewById(R.id.autocomplete_gender);
        edt_address = view.findViewById(R.id.edt_address);
        edt_streetName = view.findViewById(R.id.edt_streetName);
        edt_birthday = view.findViewById(R.id.edt_birthday);
        btn_finish = view.findViewById(R.id.btn_finish);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        edt_id = view.findViewById(R.id.edt_id);

        String idValue = Constants.idUser;
        edt_id.setText(idValue);


        autocomplete_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autocomplete_gender);
                String[] genders = {"Nam", "Nữ", "Khác"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, genders);
                autoCompleteTextView.setAdapter(adapter);
                autocomplete_gender.showDropDown();
            }
        });

        edt_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edt_id.getText().toString();
                String fullName = edt_fullName.getText().toString();
                String phoneNumber = edt_phoneNumber.getText().toString();
                String gender = autocomplete_gender.getText().toString();
                String birthDate = edt_birthday.getText().toString();
                String address = edt_streetName.getText().toString() + " - " + edt_address.getText().toString();

                Log.d("id nee", id);

                ApiService apiService = Constants.getApiService();

                User request = new User();
                request.setFullName(fullName);
                request.setPhoneNumber(phoneNumber);
                request.setGender(gender);
                request.setBirthDate(birthDate);
                request.setAddress(address);

                Call<AuthResponse> call = apiService.updateUser(id, request);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            AuthResponse authResponse = response.body();
                            if (authResponse != null) {
                                String message = authResponse.getMessage();
                                // Xử lý phản hồi thành công từ máy chủ
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        } else {
                            // Hiển thị thông báo lỗi dựa trên mã phản hồi
                            String errorMessage = "Có lỗi xảy ra. Vui lòng thử lại sau.";
                            if (response.errorBody() != null) {
                                try {
                                    errorMessage = response.errorBody().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            Log.d("LoiUp", errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Có lỗi trên hệ thống. Vui thòng thử lại sau!", Toast.LENGTH_SHORT).show();
                        Log.e("API Call", "Failed to login", t);
                    }
                });
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, yearSelected, monthOfYear, dayOfMonthOfMonth) -> {
                    // Xử lý khi người dùng chọn ngày
                    String selectedDate = dayOfMonthOfMonth + "/" + (monthOfYear + 1) + "/" + yearSelected;
                    edt_birthday.setText(selectedDate);
                }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }


}