package com.example.furniturebuyandsell.fragment.notifications;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.FeaturedProductAdapter;
import com.example.furniturebuyandsell.adapter.NewProductAdapter;
import com.example.furniturebuyandsell.adapter.NotificationByTypeAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.Notification;
import com.example.furniturebuyandsell.model.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationByTypeDialogFragment extends DialogFragment {

    NotificationByTypeAdapter notificationByTypeAdapter;
    RecyclerView notificationRecyclerView;

    ImageView btn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialogfragment_notification_by_type, container, false);
        notificationRecyclerView = view.findViewById(R.id.recyV_notification);
        btn_back = view.findViewById(R.id.btn_back);

        ArrayList<Notification> selectList = getArguments().getParcelableArrayList("notificationList");
        // Hiển thị sản phẩm trên RecyclerView
        notificationByTypeAdapter = new NotificationByTypeAdapter(selectList, requireContext());
        notificationRecyclerView.setAdapter(notificationByTypeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        notificationRecyclerView.setLayoutManager(linearLayoutManager);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }
}