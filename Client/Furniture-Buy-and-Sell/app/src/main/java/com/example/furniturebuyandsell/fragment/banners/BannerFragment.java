package com.example.furniturebuyandsell.fragment.banners;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.R;
import com.squareup.picasso.Picasso;

public class BannerFragment extends Fragment {
    private String bannerLink;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bannerLink = getArguments().getString("bannerLink");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        ImageView imageView = view.findViewById(R.id.bannerImageView);
        Picasso.get().load(Constants.ip_Address + bannerLink).into(imageView);
        return view;
    }
}
