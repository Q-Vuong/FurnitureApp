package com.example.furniturebuyandsell;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;


import com.example.furniturebuyandsell.adapter.TadPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    ViewPager2 viewPager2;
    TadPagerAdapter tadPagerAdapter;
    TabLayout tabLayout;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            Toast.makeText(MainActivity.this, "Post notification permission granted", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewPager2Tab);
        tabLayout = findViewById(R.id.tabLayout);

        tadPagerAdapter = new TadPagerAdapter(this);
        viewPager2.setAdapter(tadPagerAdapter);
        viewPager2.setUserInputEnabled(false);

        FirebaseApp.initializeApp(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }else {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        // Đăng ký thiết bị vào topic "news" để nhận các thông báo liên quan
        // Kiểm tra xem thiết bị đã đăng ký vào chủ đề "news" chưa
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isSubscribedToNews = sharedPreferences.getBoolean("subscribed_to_news", false);

        if (!isSubscribedToNews) {
            // Nếu chưa đăng ký, thực hiện đăng ký
            FirebaseMessaging.getInstance().subscribeToTopic("news")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Subscribed to news topic");
                                Toast.makeText(MainActivity.this, "Subscribed to news topic", Toast.LENGTH_SHORT).show();

                                // Lưu trạng thái đã đăng ký vào SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("subscribed_to_news", true);
                                editor.apply();
                            } else {
                                Log.e(TAG, "Error subscribing to news topic", task.getException());
                                Toast.makeText(MainActivity.this, "Error subscribing to news topic", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: {
                        tab.setText("Mua");
                        tab.setIcon(R.drawable.ic_chair);
                        break;
                    }
                    case 1: {
                        tab.setText("Thông báo");
                        tab.setIcon(R.drawable.ic_notification);
                        break;
                    }
                    case 2: {
                        tab.setText("Tìm kiếm");
                        tab.setIcon(R.drawable.ic_search);
                        break;
                    }
                    case 3: {
                        tab.setText("Tôi");
                        tab.setIcon(R.drawable.ic_user);
                        break;
                    }
                }
            }
        }).attach();

    }




}
