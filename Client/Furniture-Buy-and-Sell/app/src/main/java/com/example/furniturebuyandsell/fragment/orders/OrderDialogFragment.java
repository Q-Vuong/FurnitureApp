package com.example.furniturebuyandsell.fragment.orders;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturebuyandsell.MainActivity;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.AllProductForOrderAdapter;
import com.example.furniturebuyandsell.adapter.CartAdapter;
import com.example.furniturebuyandsell.adapter.NewProductAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.UserFragment;
import com.example.furniturebuyandsell.fragment.products.DetailProductDialogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.Discount;
import com.example.furniturebuyandsell.model.Order;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDialogFragment extends DialogFragment {
    ArrayList<User.CartItem> cartItemArrayList;
    ArrayList<Product> productArrayList;
    AllProductForOrderAdapter allProductForOrderAdapter;
    RecyclerView recyV_allProductforOrder;
    ImageView img_pr;
    TextView txt_namepr, txt_price, txt_size, txt_quantity, txt_priceToPay, txt_TotalVoucher, txt_TotalPrice, txt_address;
    EditText edt_note, edt_discount;
    Button btn_buy;
    ConstraintLayout constrain_viewProduct;
    int totalBillToPay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialogfragment_order, container, false);
        txt_address = view.findViewById(R.id.txt_address);
        txt_namepr = view.findViewById(R.id.tv_namePr);
        txt_size = view.findViewById(R.id.tv_size);
        txt_price = view.findViewById(R.id.tv_price);
        txt_quantity = view.findViewById(R.id.tv_quantity);
        img_pr = view.findViewById(R.id.img_product);
        txt_priceToPay = view.findViewById(R.id.txt_priceToPay);
        txt_TotalVoucher = view.findViewById(R.id.txt_TotalVoucher);
        txt_TotalPrice = view.findViewById(R.id.txt_TotalPrice);
        edt_note = view.findViewById(R.id.edt_note);
        edt_discount = view.findViewById(R.id.edt_discount);
        btn_buy = view.findViewById(R.id.btn_buy);
        recyV_allProductforOrder = view.findViewById(R.id.recyV_productForOrder);
        constrain_viewProduct = view.findViewById(R.id.constrain_viewProduct);

        //ẩn
        recyV_allProductforOrder.setVisibility(View.GONE);
        constrain_viewProduct.setVisibility(View.GONE);

        txt_address.setText(Constants.currentUser.getFullName() + " | " + Constants.currentUser.getPhoneNumber() + "\n" + Constants.currentUser.getAddress());

        // Lấy dữ liệu sản phẩm từ Bundle
        cartItemArrayList = getArguments().getParcelableArrayList("selectedCart");
        if (cartItemArrayList != null) {
            for (User.CartItem cartItem : cartItemArrayList) {
                Log.d("cart", "ID: " + cartItem.getProductId() + ", Quantity: " + cartItem.getQuantity());
            }
        }
        Product selectedProduct = getArguments().getParcelable("selectedProduct");
        totalBillToPay = getArguments().getInt("totalBillToPay");

        int quantity = getArguments().getInt("quantity");

        if (selectedProduct != null && quantity > 0) {
            //hiện
            constrain_viewProduct.setVisibility(View.VISIBLE);

            Picasso.get().load(Constants.ip_Address + selectedProduct.getImagePr()).into(img_pr);
            txt_namepr.setText(selectedProduct.getNamePr());
            txt_size.setText(selectedProduct.getSize());
            txt_price.setText(String.valueOf(selectedProduct.getPrice()));
            txt_quantity.setText("x" + quantity);
            totalBillToPay = (int) (selectedProduct.getPrice() * quantity);
            txt_priceToPay.setText( totalBillToPay + "đ");
            txt_TotalPrice.setText( txt_priceToPay.getText() );



        }else if (cartItemArrayList != null){
            recyV_allProductforOrder.setVisibility(View.VISIBLE);
            getProductListForOrder();
            txt_priceToPay.setText(totalBillToPay + "đ");
            txt_TotalPrice.setText(totalBillToPay + "đ");
        }

        // Đặt sự kiện lắng nghe cho EditText
        edt_discount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Thực thi hành động khi người dùng nhấn Enter trên bàn phím ảo
                    String discountCode = edt_discount.getText().toString();
                    checkDiscount(discountCode);
                    return true; // Trả về true để báo rằng sự kiện này đã được xử lý
                }
                return false; // Trả về false để cho phép các sự kiện khác xử lý
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                String note = edt_note.getText().toString();
                String userId = Constants.idUser;
                String priceToPay = txt_TotalPrice.getText().toString();

                if(Constants.currentUser.getAddress().isEmpty() || Constants.currentUser.getPhoneNumber().isEmpty()){

                    AlertDialogFragment dialogFragment = new AlertDialogFragment();
                    dialogFragment.show(getChildFragmentManager(), "Dialogshow");

                }else {
                    if (selectedProduct != null && quantity > 0) {
                        String productId = selectedProduct.getId();
                        int quantity = getArguments().getInt("quantity");

                        Order request = new Order();
                        request.setDateOrder(currentDate);
                        request.setNote(note);
                        request.setUserId(userId);
                        ArrayList<Order.ProductItem> productItems = new ArrayList<>();
                        Order.ProductItem productItem = new Order.ProductItem(productId, quantity);
                        productItems.add(productItem);
                        request.setProducts(productItems);
                        request.setPriceToPay(priceToPay);

                        addOrder(request);

                    }else if (cartItemArrayList != null){
                        Order request = new Order();
                        request.setDateOrder(currentDate);
                        request.setNote(note);
                        request.setUserId(userId);

                        ArrayList<Order.ProductItem> productItems = new ArrayList<>();
                        for (User.CartItem cartItem : cartItemArrayList) {
                            Log.d("cart", "ID: " + cartItem.getProductId() + ", Quantity: " + cartItem.getQuantity());
                            Order.ProductItem productItem = new Order.ProductItem(cartItem.getProductId(), cartItem.getQuantity());
                            productItems.add(productItem);
                            deleteCartWhenOrderFinish(userId, cartItem.getProductId());
                        }
                        request.setProducts(productItems);
                        request.setPriceToPay(priceToPay);

                        addOrder(request);
                    }
                }

            }
        });

        return view;
    }


    private void checkDiscount(String code){

        ApiService apiService = Constants.getApiService();

        Discount request = new Discount();
        request.setCode(code);

        Call<AuthResponse> call = apiService.checkCodeDiscount(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        String message = authResponse.getMessage();
                        Discount thisDiscount = authResponse.getThisDiscount();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        txt_TotalVoucher.setText("- " + String.valueOf(thisDiscount.getAmount()) + "đ");
                        txt_TotalPrice.setText(String.valueOf(totalBillToPay - thisDiscount.getAmount()) + "đ");
                        if (thisDiscount != null) {
                            Log.d("discount", thisDiscount.getId() + "   " + thisDiscount.getCode());
                        }
                    }
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
                    Log.d("Loi check", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), ". Vui thòng thử lại sau!", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to check discount", t);
            }
        });
    }

    private void getProductListForOrder() {
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
            allProductForOrderAdapter = new AllProductForOrderAdapter(cartItemArrayList, productArrayList, requireContext());
            recyV_allProductforOrder.setAdapter(allProductForOrderAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyV_allProductforOrder.setLayoutManager(linearLayoutManager);
        }
    }

    private void addOrder(Order request){
        ApiService apiService = Constants.getApiService();
        Call<AuthResponse> call = apiService.createOrder(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        String message = authResponse.getMessage();
                        // Xử lý phản hồi thành công từ máy chủ
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        UserFragment userFragment = new UserFragment();
                        userFragment.getUserbyID();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
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
                    Log.d("LoiUp", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Có lỗi trên hệ thống. Vui thòng thử lại sau!", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Failed to login", t);
            }
        });
    }

    private void deleteCartWhenOrderFinish(String customerId, String productId){
        // Gửi yêu cầu HTTP để xóa sản phẩm khỏi giỏ hàng
        ApiService apiService = Constants.getApiService();
        Call<User.CartItem> deleteCartItemCall = apiService.deleteCart(customerId, productId);
        deleteCartItemCall.enqueue(new Callback<User.CartItem>() {
            @Override
            public void onResponse(Call<User.CartItem> call, Response<User.CartItem> response) {
                if (response.isSuccessful()) {

                } else {
                    // Xử lý lỗi nếu xảy ra
                    Toast.makeText(getContext(), "Lỗi khi xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User.CartItem> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                Log.e("API Call", "Lỗi kết nối, vui lòng thử lại sau", t);
            }
        });
    }

}