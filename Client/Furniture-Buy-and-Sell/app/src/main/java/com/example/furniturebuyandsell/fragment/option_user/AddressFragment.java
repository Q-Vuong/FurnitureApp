package com.example.furniturebuyandsell.fragment.option_user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.furniturebuyandsell.MainActivity;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.UserFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddressFragment extends DialogFragment {

    EditText edt_fullName, edt_phoneNumber, edt_address, edt_birthday, edt_streetName;
    AutoCompleteTextView autocomplete_gender;
    ImageView img_user;

    Button btn_updateUser;
    private DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialogfragment_address, container, false);

        edt_fullName = view.findViewById(R.id.edt_fullName);
        edt_phoneNumber = view.findViewById(R.id.edt_phoneNumber);
        autocomplete_gender = view.findViewById(R.id.autocomplete_gender);
        edt_address = view.findViewById(R.id.edt_address);
        edt_birthday = view.findViewById(R.id.edt_birthday);
        img_user = view.findViewById(R.id.img_user);
        btn_updateUser = view.findViewById(R.id.btn_updateUser);
        edt_streetName = view.findViewById(R.id.edt_streetName);




        Picasso.get().load(Constants.ip_Address + Constants.currentUser.getAvatar()).into(img_user);
        edt_fullName.setText(Constants.currentUser.getFullName());
        edt_phoneNumber.setText(Constants.currentUser.getPhoneNumber());
        autocomplete_gender.setText(Constants.currentUser.getGender());
        edt_address.setText(Constants.currentUser.getAddress());
        edt_birthday.setText(Constants.currentUser.getBirthDate());


        autocomplete_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hideSoftKeyboard(requireContext(), autocomplete_gender);
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
                //hideSoftKeyboard(requireContext(), autocomplete_gender);
                showDatePickerDialog();
            }
        });

        btn_updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });


        return view;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(requireContext(),
                (view, yearSelected, monthOfYear, dayOfMonthOfMonth) -> {
                    // Xử lý khi người dùng chọn ngày
                    String selectedDate = dayOfMonthOfMonth + "/" + (monthOfYear + 1) + "/" + yearSelected;
                    edt_birthday.setText(selectedDate);
                }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private void updateUser(){
        String id = Constants.idUser;
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
                        UserFragment userFragment = new UserFragment();
                        userFragment.getUserbyID();
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


}