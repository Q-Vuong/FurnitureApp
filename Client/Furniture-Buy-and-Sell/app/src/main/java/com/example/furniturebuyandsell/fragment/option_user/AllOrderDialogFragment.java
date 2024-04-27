package com.example.furniturebuyandsell.fragment.option_user;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.AllOrderForUserAdapter;
import com.example.furniturebuyandsell.adapter.AllProductForOrderAdapter;
import com.example.furniturebuyandsell.adapter.FeaturedProductAdapter;
import com.example.furniturebuyandsell.adapter.NewProductAdapter;
import com.example.furniturebuyandsell.adapter.ProductAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.UserFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.Order;
import com.example.furniturebuyandsell.model.Product;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllOrderDialogFragment extends DialogFragment {

    ArrayList<Order> orderList;
    ArrayList<Product> productArrayList;
    AllOrderForUserAdapter allOrderForUserAdapter;
    RecyclerView recyV_orderOfUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialogfragment_all_order, container, false);
        recyV_orderOfUser = view.findViewById(R.id.recyV_orderOfUser);
        recyV_orderOfUser.setNestedScrollingEnabled(false);


        getProductListForOrder();
        return view;
    }

    private void getAllOrder() {
        // Kiểm tra xem productArrayList đã được khởi tạo chưa
        if (productArrayList == null) {
            // Nếu productArrayList chưa được khởi tạo, hiển thị thông báo hoặc xử lý khác
            Toast.makeText(getContext(), "Product list is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = Constants.getApiService();
        Call<ArrayList<Order>> call = apiService.getAllOrders();
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ArrayList<Order> allOrder = response.body();

                    orderList = new ArrayList<>();
                    for (Order order : allOrder) {
                        String userId = order.getUserId().toString();
                        if (userId.equals(Constants.idUser)) {
                            orderList.add(order);
                        }
                    }

                    if (!orderList.isEmpty()) {
                        // Tạo adapter sau khi orderList đã được cập nhật
                        AllOrderForUserAdapter adapter = new AllOrderForUserAdapter(productArrayList, orderList, requireContext());
                        recyV_orderOfUser.setAdapter(adapter);
                        recyV_orderOfUser.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        Toast.makeText(getContext(), "No orders found for the current user", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load orders", t);
            }
        });
    }

    private void getProductListForOrder() {
        ApiService apiService = Constants.getApiService();
        Call<ArrayList<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy danh sách sản phẩm từ response
                    productArrayList = response.body();

                    // Sau khi nhận được danh sách sản phẩm, gọi hàm để lấy danh sách đơn hàng
                    getAllOrder();
                } else {
                    String errorMessage = "Error occurred. Please try again later.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Error get products", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load products", t);
            }
        });
    }



}