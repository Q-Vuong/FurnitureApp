package com.example.furniturebuyandsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.furniturebuyandsell.fragment.auth.InforUserFragment;
import com.example.furniturebuyandsell.fragment.auth.LoginFragment;
import com.example.furniturebuyandsell.fragment.auth.RegisterFragment;
import com.example.furniturebuyandsell.fragment.auth.ForgotPassworFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Tạo đối tượng FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Bắt đầu giao dịch Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Tạo đối tượng Fragment bạn muốn hiển thị
        LoginFragment fragment = new LoginFragment();
        // Thêm Fragment vào Activity bằng cách thay thế hoặc thêm
        fragmentTransaction.add(R.id.fragmentLayout, fragment)
                .commit(); // R.id.fragment_container là ID của Container Fragment trong layout của bạn

    }

    public void switchToMainRegisterFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, new RegisterFragment())
                .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                .commit();
    }

    public void goBackToPreviousFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); // Quay lại Fragment trước đó
        } else {
            super.onBackPressed(); // Nếu không có Fragment nào trong Stack, thì thực hiện hành động mặc định khi nhấn nút Back
        }
    }


    public void switchToInforUserFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, new InforUserFragment())
                .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                .commit();
    }

    public void switchToForgotFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, new ForgotPassworFragment())
                .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                .commit();
    }


    public void switchToLoginFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, new LoginFragment())
                .addToBackStack(null) // Thêm vào stack để có thể quay lại Fragment trước đó nếu cần
                .commit();
    }


}