package com.example.furniturebuyandsell.fragment.orders;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.AllProductByTypeAdapter;
import com.example.furniturebuyandsell.adapter.CartAdapter;
import com.example.furniturebuyandsell.adapter.NewProductAdapter;
import com.example.furniturebuyandsell.adapter.ProductTypeAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.BuyFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.ProductType;
import com.example.furniturebuyandsell.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartDiaLogFragment extends DialogFragment {

    RecyclerView recyV_cart;
    ArrayList<User.CartItem> cartItemArrayList;
    ArrayList<Product> productArrayList;
    CartAdapter cartAdapter;
    TextView txt_pay;
    Button btn_buy;

    private int totalBillToPay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialogfragment_cart, container, false);
        recyV_cart = view.findViewById(R.id.recyV_cart);
        txt_pay = view.findViewById(R.id.tv_pay);
        btn_buy = view.findViewById(R.id.btn_buy);

        final CartDiaLogFragment self = this;



        String idUser = Constants.idUser;
        //gửi yêu cầu HTTP
        ApiService apiService = Constants.getApiService();

        Call<ArrayList<User.CartItem>> callGetProductType = apiService.getCart(idUser);
        callGetProductType.enqueue(new Callback<ArrayList<User.CartItem>>() {
            @Override
            public void onResponse(Call<ArrayList<User.CartItem>> call, Response<ArrayList<User.CartItem>> response) {

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    cartItemArrayList = response.body();
                    // Hiển thị ảnh đầu tiên
                    for (User.CartItem cartItem : cartItemArrayList) {
                        Log.d("ItemCart", "ID: " + cartItem.getProductId() + "   Quantity: " + cartItem.getQuantity());
                    }
                    getProductListForCart();

                } else {
                    // Hiển thị thông báo lỗi dựa trên mã phản hồi
                    String errorMessage = "Giỏ hàng rỗng";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Loi getcart", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User.CartItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load cart", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load cart", t);
            }

        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("selectedCart", cartItemArrayList);
                bundle.putInt("totalBillToPay", totalBillToPay);

                OrderDialogFragment dialogFragment = new OrderDialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(requireFragmentManager(), "Dialogshow");
            }
        });

        return view;
    }


    private void getProductListForCart() {
        // Gửi yêu cầu HTTP để lấy danh sách sản phẩm
        ApiService apiService = Constants.getApiService();
        Call<ArrayList<Product>> callGetProducts = apiService.getProducts();
        callGetProducts.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy danh sách sản phẩm từ response
                    productArrayList = response.body();
                    Log.d("ProductforCard", String.valueOf(productArrayList));
                    //productArrayList = response.body();
                    showCartItems();
                    showBillToPay();
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
                    Log.d("Loi getcart", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load products", t);
            }
        });
    }

    private void showCartItems() {
        if (cartItemArrayList != null && productArrayList != null) {
            cartAdapter = new CartAdapter(cartItemArrayList, productArrayList, requireContext());
            recyV_cart.setAdapter(cartAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyV_cart.setLayoutManager(linearLayoutManager);
        }
    }

    public void showBillToPay() {
        if (cartItemArrayList != null && productArrayList != null) {
            totalBillToPay = 0;
            for (User.CartItem cartItem : cartItemArrayList) {
                for (Product product : productArrayList) {
                    if (product.getId().equals(cartItem.getProductId())) {
                        totalBillToPay += (product.getPrice() * cartItem.getQuantity());
                        break;
                    }
                }
            }
            txt_pay.setText(String.valueOf(totalBillToPay) + "đ");
        }
    }

}