package com.example.furniturebuyandsell.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.NotificationByTypeAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.notifications.NotificationByTypeDialogFragment;
import com.example.furniturebuyandsell.fragment.products.DetailProductDialogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.Notification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {
    ConstraintLayout btn_constr1, btn_constr2, btn_constr3, btn_constr4;
    TextView txt_view1, txt_view2, txt_view3, txt_view4;
    ArrayList<Notification> promotionList, productList, storeList, otherList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        btn_constr1 = view.findViewById(R.id.click1);
        btn_constr2 = view.findViewById(R.id.click2);
        btn_constr3 = view.findViewById(R.id.click3);
        btn_constr4 = view.findViewById(R.id.click4);
        txt_view1 = view.findViewById(R.id.txt_view1);
        txt_view2 = view.findViewById(R.id.txt_view2);
        txt_view3 = view.findViewById(R.id.txt_view3);
        txt_view4 = view.findViewById(R.id.txt_view4);

        getNotificationByType();




        btn_constr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDialogFragment(promotionList);
            }
        });

        btn_constr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDialogFragment(productList);
            }
        });

        btn_constr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDialogFragment(storeList);
            }
        });

        btn_constr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDialogFragment(otherList);
            }
        });

        return view;

    }

    private void switchToDialogFragment(ArrayList<Notification> list){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("notificationList", list);

        NotificationByTypeDialogFragment dialogFragment = new NotificationByTypeDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }

    private void getNotificationByType(){
        ApiService apiService = Constants.getApiService();
        Call<ArrayList<Notification>> callGetNotification = apiService.getNotifications();
        callGetNotification.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy danh sách sản phẩm từ response
                    ArrayList<Notification> allNotification = response.body();

                    if (promotionList == null){
                        promotionList = new ArrayList<>();
                        productList = new ArrayList<>();
                        storeList = new ArrayList<>();
                        otherList = new ArrayList<>();
                    }
                    for (Notification notification : allNotification) {
                        if (notification.getNotificationType().equals("Khuyến mãi")) {
                            promotionList.add(notification);
                        } else if (notification.getNotificationType().equals("Sản phẩm")) {
                            productList.add(notification);
                        }else if (notification.getNotificationType().equals("Cửa hàng")) {
                            storeList.add(notification);
                        }else {
                            otherList.add(notification);
                        }
                    }

                    displayLastItemContent(promotionList, txt_view1, "Không có thông báo khuyến mãi");
                    displayLastItemContent(productList, txt_view2, "Không có thông báo sản phẩm");
                    displayLastItemContent(storeList, txt_view3, "Không có thông báo cửa hàng");
                    displayLastItemContent(otherList, txt_view4, "Không có thông báo khác");

                } else {
                    Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load products", t);
            }
        });
    }

    private void displayLastItemContent(ArrayList<Notification> list, TextView textView, String emptyMessage) {
        if (list != null && !list.isEmpty()) {
            Notification lastNotification = list.get(list.size() - 1);
            String lastNotificationContent = lastNotification.getContent();
            textView.setText(lastNotificationContent);
        } else {
            textView.setText(emptyMessage);
        }
    }

}