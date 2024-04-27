package com.example.furniturebuyandsell.fragment.option_user;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.FavoriteAdapter;
import com.example.furniturebuyandsell.adapter.ProductTypeAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.BuyFragment;
import com.example.furniturebuyandsell.fragment.UserFragment;
import com.example.furniturebuyandsell.fragment.products.DetailProductDialogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.model.Product;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteDiaLogFragment extends DialogFragment  implements ProductItemClickListener {

    RecyclerView result;
    FavoriteAdapter favoriteAdapter;
    ImageView btn_back;
    private final FavoriteDiaLogFragment self = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialogfragment_favorite, container, false);
        result = view.findViewById(R.id.result);
        btn_back = view.findViewById(R.id.btn_back);


        // Gọi API để lấy danh sách sản phẩm dựa trên danh sách ID
        getProductsByIds(Constants.currentUser.getFavoriteProducts());


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


    private void getProductsByIds(ArrayList<String> productIds) {
        ApiService apiService = Constants.getApiService();

        // Gọi API để lấy danh sách sản phẩm dựa trên danh sách ID
        Call<ArrayList<Product>> call = apiService.getProductById(productIds);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> products = response.body();
                    if (products != null && !products.isEmpty()) {
                        // Xử lý danh sách sản phẩm được trả về
                        // Ví dụ: cập nhật RecyclerView Adapter
                        favoriteAdapter = new FavoriteAdapter(products, requireContext());
                        favoriteAdapter.setListener(self);
                        result.setAdapter(favoriteAdapter);

                        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        result.setLayoutManager(linearLayoutManager);
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy sản phẩm nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy danh sách sản phẩm", Toast.LENGTH_SHORT).show();
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
                    Log.d("Loi", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onItemClick(Product product) {
        Constants.idProduct = product.getId();
        Log.d("Product Click", "Product ID: " + Constants.idProduct);

        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedProduct", product);

        DetailProductDialogFragment dialogFragment = new DetailProductDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }

}