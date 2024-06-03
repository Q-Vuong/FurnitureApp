package com.example.furniturebuyandsell.fragment;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.BannerAdapter;
import com.example.furniturebuyandsell.adapter.FeaturedProductAdapter;
import com.example.furniturebuyandsell.adapter.NewProductAdapter;
import com.example.furniturebuyandsell.adapter.ProductTypeAdapter;
import com.example.furniturebuyandsell.fragment.orders.CartDiaLogFragment;
import com.example.furniturebuyandsell.fragment.products.DetailProductDialogFragment;
import com.example.furniturebuyandsell.fragment.producttypes.BuyByProductTypeFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.interfaces.ProductTypeItemClickListener;
import com.example.furniturebuyandsell.model.Banner;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.ProductType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyFragment extends Fragment implements ProductTypeItemClickListener, ProductItemClickListener {

    RecyclerView recyclerViewNewProduct, recyclerViewFeaturedProduct, recyProductType;
    BannerAdapter bannerAdapter;
    NewProductAdapter newProductAdapter;
    FeaturedProductAdapter featuredProductAdapter;
    ProductTypeAdapter productTypeAdapter;
    ViewPager2 bannerViewPager;
    ArrayList<Banner> bannerList;
    ArrayList<Product> productsList;
    ArrayList<ProductType> productTypeArrayList;
    ImageButton btn_getCart;



    int currentIndex = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private final int DELAY_TIME = 3000; // Thời gian chờ giữa các banner (ms)
    // Khai báo biến final để lưu trữ tham chiếu của BuyFragment
    private final BuyFragment self = this;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        btn_getCart = view.findViewById(R.id.btn_getCart);
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        recyclerViewNewProduct = view.findViewById(R.id.recyV_newProduct);
        recyclerViewFeaturedProduct = view.findViewById(R.id.recyV_featuredProduct);
        recyProductType = view.findViewById(R.id.recyV_productType);
        recyProductType.setNestedScrollingEnabled(false);



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
        showProduct();

        //--------------------------------Hiển thị productType từ server nodejs----------------------------------------------//
        Call<ArrayList<ProductType>> callGetProductType = apiService.getProductTypes();
        callGetProductType.enqueue(new Callback<ArrayList<ProductType>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductType>> call, Response<ArrayList<ProductType>> response) {

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lưu trữ danh sách banner nhận được từ máy chủ
                    productTypeArrayList = response.body();
                    // Hiển thị ảnh đầu tiên
                    for (ProductType productType : productTypeArrayList) {
                        Log.d("productType", "Name: " + productType.getNameType() + "   Img: " + productType.getImgPr_T());
                    }
                    productTypeAdapter = new ProductTypeAdapter(productTypeArrayList, requireContext());
                    productTypeAdapter.setItemClickListener(self);
                    recyProductType.setAdapter(productTypeAdapter);

                    LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyProductType.setLayoutManager(linearLayoutManager);

                } else {
                    Toast.makeText(getContext(), "No banners found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductType>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to load products", t);
            }

        });


        btn_getCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartDiaLogFragment dialogFragment = new CartDiaLogFragment();
                dialogFragment.show(requireActivity().getSupportFragmentManager(), "Dialogshow");
            }
        });



        return view;
    }



    private void showProduct(){
        ApiService apiService = Constants.getApiService();
        Call<ArrayList<Product>> callGetProducts = apiService.getProducts();
        callGetProducts.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy danh sách sản phẩm từ response
                    ArrayList<Product> allProducts = response.body();

                    // Lọc danh sách sản phẩm để chỉ lấy những sản phẩm có thuộc tính featured bằng true
                    productsList = new ArrayList<>();
                    for (Product product : allProducts) {
                        Log.d("Product Data", "Type ID"+ product.getType());
                        if (product.isNew()) {
                            productsList.add(product);
                        }
                    }

                    // Kiểm tra xem danh sách sản phẩm có sản phẩm nào không
                    if (!productsList.isEmpty()) {
                        // Hiển thị sản phẩm trên RecyclerView
                        newProductAdapter = new NewProductAdapter(productsList, requireContext());
                        newProductAdapter.setListener(self);
                        recyclerViewNewProduct.setAdapter(newProductAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerViewNewProduct.setLayoutManager(linearLayoutManager);

                    } else {
                        // Hiển thị thông báo nếu không có sản phẩm nào
                        Toast.makeText(getContext(), "No featured products found", Toast.LENGTH_SHORT).show();
                    }


                    productsList = new ArrayList<>();
                    for (Product product : allProducts) {
                        if (product.isFeatured()) {
                            productsList.add(product);
                        }
                    }

                    // Kiểm tra xem danh sách sản phẩm có sản phẩm nào không
                    if (!productsList.isEmpty()) {
                        // Hiển thị sản phẩm trên RecyclerView
                        featuredProductAdapter = new FeaturedProductAdapter(productsList, requireContext());
                        featuredProductAdapter.setListener(self);
                        recyclerViewFeaturedProduct.setAdapter(featuredProductAdapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerViewFeaturedProduct.setLayoutManager(linearLayoutManager);

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
    }

    public void updateListProduct(){
        featuredProductAdapter.updateFavorites();
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
    public void onItemClick(int position) {
        // Kiểm tra xem vị trí được chọn có hợp lệ không
        if (position >= 0 && position < productTypeArrayList.size()) {
            // Lấy đối tượng ProductType tại vị trí được chọn
            ProductType selectedProductType = productTypeArrayList.get(position);
            // Lấy nameType của đối tượng ProductType
            String id = selectedProductType.getId();

            Log.d("click replace fragment", id);

            // Bây giờ bạn có thể sử dụng nameType như bạn muốn, ví dụ như truyền vào Fragment hoặc xử lý nó.

            // Ví dụ: Truyền nameType tới Fragment mới
            Bundle bundle = new Bundle();
            bundle.putString("idProductType", id);
            BuyByProductTypeFragment fragment = new BuyByProductTypeFragment();
            fragment.setArguments(bundle);

            // Thay đổi Fragment hiện tại bằng Fragment mới khi một mục trong RecyclerView được nhấn
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment) // Thêm BuyByProductTypeFragment vào stack
                    .addToBackStack(null)
                    .commit();
        } else {
            Log.e("Item Click", "Invalid position: " + position);
        }
    }

    @Override
    public void onItemClick(Product product) {
        Constants.idProduct = product.getId();
        Log.d("Product Click", "Product ID: " + Constants.idProduct);

        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedProduct", product);

        DetailProductDialogFragment dialogFragment = new DetailProductDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getChildFragmentManager(), "Dialogshow");

    }
}
