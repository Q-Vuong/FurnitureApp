package com.example.furniturebuyandsell.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.furniturebuyandsell.LoginActivity;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.option_user.AddressFragment;
import com.example.furniturebuyandsell.fragment.option_user.AllOrderDialogFragment;
import com.example.furniturebuyandsell.fragment.option_user.ChangePassFragment;
import com.example.furniturebuyandsell.fragment.option_user.FavoriteDiaLogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private LinearLayout btn_bought, btn_favorite, btn_changepass, btn_account, btn_aboutUs, btn_contact;
    private TextView txt_nameuser, txt_email;
    private ImageView img_user;
    Button btn_logout;
    private ArrayList<String> listFavorite = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        txt_nameuser = view.findViewById(R.id.txt_nameuser);
        txt_email = view.findViewById(R.id.txt_email);
        img_user = view.findViewById(R.id.img_user);
        btn_bought = view.findViewById(R.id.bought);
        btn_favorite = view.findViewById(R.id.favorite);
        btn_account = view.findViewById(R.id.account);
        btn_aboutUs = view.findViewById(R.id.us);
        btn_contact = view.findViewById(R.id.contact);
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_changepass  = view.findViewById(R.id.changePass);

        txt_nameuser.setText(Constants.currentUser.getFullName());
        txt_email.setText(Constants.currentUser.getEmail());
        Picasso.get().load(Constants.ip_Address + Constants.currentUser.getAvatar()).into(img_user);


        btn_bought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToAllOrderDialogFragment();
            }
        });

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFavoriteDialogFragment();
            }
        });

        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToInforUserDialogFragment();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToChangePassFragment();
            }
        });

        return view;
    }


    private void switchToFavoriteDialogFragment() {
        FavoriteDiaLogFragment dialogFragment = new FavoriteDiaLogFragment();
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }

    private void switchToAllOrderDialogFragment() {
        AllOrderDialogFragment dialogFragment = new AllOrderDialogFragment();
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }

    private void switchToInforUserDialogFragment() {
        AddressFragment dialogFragment = new AddressFragment();
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }
    private void switchToChangePassFragment() {
        ChangePassFragment dialogFragment = new ChangePassFragment();
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }


    public void getUserbyID() {
        String idUser = Constants.idUser;
        ApiService apiService = Constants.getApiService();

        Call<AuthResponse> call = apiService.getUserById(idUser);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        String message = authResponse.getMessage();
                        // Xử lý phản hồi thành công từ máy chủ
                        User thisCustomer = authResponse.getThisCustomer();
                        if (thisCustomer != null) {
                            //Nhận thông tin của khách hàng sau khi đăng nhập thành công
                            Constants.currentUser = thisCustomer;
                            Log.d("currentUser", String.valueOf(Constants.currentUser.getId()));
                        }
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
