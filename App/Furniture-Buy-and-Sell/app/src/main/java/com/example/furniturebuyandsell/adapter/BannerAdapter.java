package com.example.furniturebuyandsell.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.furniturebuyandsell.fragment.banners.BannerFragment;
import com.example.furniturebuyandsell.model.Banner;

import java.util.List;

public class BannerAdapter extends FragmentStateAdapter {
    private List<Banner> bannerList;

    public BannerAdapter(FragmentActivity fragmentActivity, List<Banner> bannerList) {
        super(fragmentActivity);
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        BannerFragment bannerFragment = new BannerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bannerLink", bannerList.get(position).getBannerLink());
        bannerFragment.setArguments(bundle);
        return bannerFragment;
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }
}
