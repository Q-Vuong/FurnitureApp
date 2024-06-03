package com.example.furniturebuyandsell.fragment.producttypes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.AllProductByTypeAdapter;
import com.example.furniturebuyandsell.adapter.BannerAdapter;
import com.example.furniturebuyandsell.adapter.NewProductAdapter;
import com.example.furniturebuyandsell.fragment.orders.CartDiaLogFragment;
import com.example.furniturebuyandsell.fragment.products.DetailProductDialogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.interfaces.ProductTypeItemClickListener;
import com.example.furniturebuyandsell.model.Banner;
import com.example.furniturebuyandsell.model.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuyByProductTypeFragment extends Fragment implements ProductItemClickListener {

    RecyclerView recyclerViewNewProduct, recyAllProduct;
    BannerAdapter bannerAdapter;
    NewProductAdapter newProductAdapter;
    AllProductByTypeAdapter allProductByTypeAdapter;
    ViewPager2 bannerViewPager;
    ArrayList<Banner> bannerList;
    ArrayList<Product> productsList, productNewList;
    ImageButton btn_cart;



    int currentIndex = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private final int DELAY_TIME = 3000; // Thời gian chờ giữa các banner (ms)
    private String idProductType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_buy_by_product_type, container, false);
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        recyclerViewNewProduct = view.findViewById(R.id.recyV_newProduct);
        recyAllProduct = view.findViewById(R.id.recyV_allProduct);
        btn_cart = view.findViewById(R.id.btn_cart);
        recyAllProduct.setNestedScrollingEnabled(false);



        final BuyByProductTypeFragment self = this;

        // Kiểm tra xem Bundle có tồn tại không
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Trích xuất dữ liệu từ Bundle
            idProductType = bundle.getString("idProductType");
        }

        //gửi yêu cầu HTTP
        ApiService apiService = Constants.getApiService();

        //----------------------------------Hiển thị banner từ server nodejs--------------------------------------------//
        // Gọi API để lấy danh sách banner từ máy chủ
        Call<ArrayList<Banner>> callGetBanners = apiService.getBanners();
        callGetBanners.enqueue(new Callback<ArrayList<Banner>>() {
            @Override
            public void onResponse(Call<ArrayList<Banner>> call, Response<ArrayList<Banner>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lưu trữ danh sách banner nhận được từ máy chủ
                    bannerList = response.body();
                    // Hiển thị ảnh đầu tiên
                    for (Banner banner : bannerList) {
                        Log.d("Banner Data", "Link: " + banner.getBannerLink());
                    }
                    // Tạo và thiết lập adapter cho ViewPager2
                    bannerAdapter = new BannerAdapter(requireActivity(), bannerList);
                    bannerViewPager.setAdapter(bannerAdapter);

                    // Bắt đầu tự động chuyển đổi banner sau một khoảng thời gian
                    startAutoScroll();

                } else {
                    Toast.makeText(getContext(), "No banners found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Banner>> call, Throwable t) { // Sửa từ List sang ArrayList
                Toast.makeText(getContext(), "Failed to load banners", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load banners", t);
            }
        });

        //--------------------------------Hiển thị product từ server nodejs----------------------------------------------//
        // Gọi API để lấy danh sách sản phẩm từ máy chủ
        Call<ArrayList<Product>> callGetProducts = apiService.getProducts();
        callGetProducts.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy danh sách sản phẩm từ response
                    ArrayList<Product> allProducts = response.body();

                    productsList = new ArrayList<>();
                    for (Product product : allProducts) {
                        Log.d("Product Data", "Type ID"+ product.getType());
                        if (idProductType.equals(product.getType())) {
                            productsList.add(product);
                        }
                    }

                    // Kiểm tra xem danh sách sản phẩm có sản phẩm nào không
                    if (!productsList.isEmpty()) {
                        // Hiển thị sản phẩm trên RecyclerView
                        allProductByTypeAdapter = new AllProductByTypeAdapter(productsList, requireContext());
                        allProductByTypeAdapter.setListener(self);
                        recyAllProduct.setAdapter(allProductByTypeAdapter);


                        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyAllProduct.setLayoutManager(linearLayoutManager);

                    } else {
                        // Hiển thị thông báo nếu không có sản phẩm nào
                        Toast.makeText(getContext(), "No featured products found", Toast.LENGTH_SHORT).show();
                    }


                    productNewList = new ArrayList<>();
                    for (Product product : allProducts) {
                        Log.d("Product Data", "Type ID"+ product.getType());
                        if (product.isNew() && idProductType.equals(product.getType())) {
                            productNewList.add(product);
                        }
                    }

                    // Kiểm tra xem danh sách sản phẩm có sản phẩm nào không
                    if (!productNewList.isEmpty()) {
                        // Hiển thị sản phẩm trên RecyclerView
                        newProductAdapter = new NewProductAdapter(productNewList, requireContext());
                        newProductAdapter.setListener(self);
                        recyclerViewNewProduct.setAdapter(newProductAdapter);


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerViewNewProduct.setLayoutManager(linearLayoutManager);

                        PagerSnapHelper snapHelper = new PagerSnapHelper();
                        snapHelper.attachToRecyclerView(recyclerViewNewProduct);
                    } else {
                        // Hiển thị thông báo nếu không có sản phẩm nào
                        Toast.makeText(getContext(), "No featured products found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load products", t);
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartDiaLogFragment dialogFragment = new CartDiaLogFragment();
                dialogFragment.show(getParentFragmentManager(), "Dialogshow");
            }
        });


        return view;
    }

    // Phương thức để bắt đầu tự động chuyển đổi banner sau một khoảng thời gian
    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển đổi đến banner tiếp theo
                currentIndex = (currentIndex + 1) % bannerList.size();
                bannerViewPager.setCurrentItem(currentIndex);
                // Lặp lại quá trình sau khoảng thời gian DELAY_TIME
                handler.postDelayed(this, DELAY_TIME);
            }
        }, DELAY_TIME);
    }

    // Đảm bảo dừng tự động chuyển đổi banner khi Fragment bị hủy
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
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