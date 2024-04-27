package com.example.furniturebuyandsell.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.furniturebuyandsell.fragment.BuyFragment;
import com.example.furniturebuyandsell.fragment.NotificationFragment;
import com.example.furniturebuyandsell.fragment.SearchFragment;
import com.example.furniturebuyandsell.fragment.UserFragment;

public class TadPagerAdapter extends FragmentStateAdapter{
    private BuyFragment buyFragment;
    private NotificationFragment notificationFragment;
    private SearchFragment searchFragment;
    private UserFragment userFragment;

    public TadPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (buyFragment == null) {
                    buyFragment = new BuyFragment();
                }
                return buyFragment;
            case 1:
                if (notificationFragment == null) {
                    notificationFragment = new NotificationFragment();
                }
                return notificationFragment;
            case 2:
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                }
                return searchFragment;
            case 3:
                if (userFragment == null) {
                    userFragment = new UserFragment();
                }
                return userFragment;
        }
        return new BuyFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
