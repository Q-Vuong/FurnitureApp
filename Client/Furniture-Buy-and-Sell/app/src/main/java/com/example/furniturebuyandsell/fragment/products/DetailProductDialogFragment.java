package com.example.furniturebuyandsell.fragment.products;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.FeaturedProductAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.BuyFragment;
import com.example.furniturebuyandsell.fragment.UserFragment;
import com.example.furniturebuyandsell.fragment.orders.OrderDialogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class DetailProductDialogFragment extends DialogFragment {
    TextView tv_namePr, tv_pricePr, tv_size, tv_describe;
    Button  btn_addCart, btn_order;
    ImageView img_pr, btn_favorite, btn_ok;
    private int defautQuantity = 1;
    BuyFragment buyFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_detail_product, container, false);


        tv_describe = view.findViewById(R.id.tv_describle);
        tv_namePr = view.findViewById(R.id.tv_productName);
        tv_size = view.findViewById(R.id.tv_size);
        img_pr = view.findViewById(R.id.img_pr);
        tv_pricePr = view.findViewById(R.id.tv_productPrice);
        btn_ok = view.findViewById(R.id.btn_OK);
        btn_addCart = view.findViewById(R.id.btn_addCart);
        btn_favorite = view.findViewById(R.id.btn_favorite);
        btn_order = view.findViewById(R.id.btn_order);


        // Lấy dữ liệu sản phẩm từ Bundle
        Product selectedProduct = getArguments().getParcelable("selectedProduct");
        if (selectedProduct != null) {
            // Đặt thông tin sản phẩm cho các TextView
            tv_namePr.setText(selectedProduct.getNamePr());
            tv_size.setText(selectedProduct.getSize());
            tv_describe.setText(selectedProduct.getDescription());
            tv_pricePr.setText(String.valueOf(selectedProduct.getPrice()) + "đ");
            Picasso.get().load(Constants.ip_Address + selectedProduct.getImagePr()).into(img_pr);

            // Kiểm tra xem sản phẩm có trong danh sách yêu thích của người dùng không
            if (isProductInFavorites(selectedProduct.getId())) {
                btn_favorite.setImageResource(R.drawable.ic_heart_black);
            } else {
                btn_favorite.setImageResource(R.drawable.ic_favorite);
            }

            // Xử lý sự kiện click cho nút OK để đóng dialog
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                }

            });

            btn_addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    defautQuantity = 1;
                    DialogChooseQuantityForCart(selectedProduct);
                }
            });



            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idUser = Constants.idUser;
                    String idProduct = Constants.idProduct;

                    ApiService apiService = Constants.getApiService();

                    User user = new User();
                    // Lấy danh sách giỏ hàng từ đối tượng User
                    ArrayList<String> favorite = user.getFavoriteProducts();
                    // Kiểm tra nếu danh sách giỏ hàng là null, tạo một danh sách mới
                    if (favorite == null) {
                        favorite = new ArrayList<>();
                    }
                    // Thêm idProduct vào danh sách giỏ hàng
                    favorite.add(idProduct);
                    // Cập nhật danh sách giỏ hàng của người dùng trong đối tượng User
                    user.setFavoriteProducts(favorite);

                    Call<AuthResponse> call = apiService.updateFavoriteProduct(idUser, user);
                    call.enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                            if (response.isSuccessful()) {
                                AuthResponse authResponse = response.body();
                                if (authResponse != null) {
                                    String message = authResponse.getMessage();
                                    // Xử lý phản hồi thành công từ máy chủ
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    if(message.equals("Thêm vào danh sách yêu thích")){
                                        btn_favorite.setImageResource(R.drawable.ic_heart_black);
                                    }else {
                                        btn_favorite.setImageResource(R.drawable.ic_favorite);
                                    }
                                    buyFragment.updateListProduct();

                                    UserFragment userFragment = new UserFragment();
                                    userFragment.getUserbyID();
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
            });

            btn_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    defautQuantity = 1;
                    DialogChooseQuantityForOrder(selectedProduct);
                }
            });

        }

        return view;
    }

    private void DialogChooseQuantityForCart(Product selectedProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.FullScreenDialogStyle);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choose_quantity, null); // Sử dụng cùng layout dialog_order
        builder.setView(view);

        AlertDialog dialog = builder.create();

        // Lấy các view trong layout của Dialog
        ImageView img_this = view.findViewById(R.id.img_this);
        EditText edt_quantity = view.findViewById(R.id.edt_quantity);
        Button btn_increase = view.findViewById(R.id.btn_increase);
        Button btn_decrease = view.findViewById(R.id.btn_decrease);
        Button btn_enter = view.findViewById(R.id.btn_enter);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        Picasso.get().load(Constants.ip_Address + selectedProduct.getImagePr()).into(img_this);
        // Thiết lập giá trị ban đầu của EditText từ tham số truyền vào
        edt_quantity.setText(String.valueOf(defautQuantity));

        // Xử lý sự kiện khi nhấn vào nút tăng số lượng
        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(); // Tăng giá trị
                edt_quantity.setText(String.valueOf(defautQuantity)); // Cập nhật EditText
            }
        });

        // Xử lý sự kiện khi nhấn vào nút giảm số lượng
        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity(); // Giảm giá trị
                edt_quantity.setText(String.valueOf(defautQuantity)); // Cập nhật EditText
            }
        });

        // Xử lý sự kiện khi nhấn vào nút "Thêm"
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idUser = Constants.idUser;
                String idProduct = Constants.idProduct;
                int quantity = Integer.parseInt(edt_quantity.getText().toString());

                ApiService apiService = Constants.getApiService();

                // Tạo một đối tượng CartItem với idProduct và quantity
                User.CartItem cartItem = new User.CartItem(idProduct, quantity);

                Log.d("data Cart", "ProductId: " + cartItem.getProductId() + ", Quantity: " + cartItem.getQuantity());

                Call<AuthResponse> call = apiService.updateCart(idUser, cartItem);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            AuthResponse authResponse = response.body();
                            if (authResponse != null) {
                                String message = authResponse.getMessage();
                                // Xử lý phản hồi thành công từ máy chủ
                                Toast.makeText(getContext(), message + idProduct, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
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
        });

        // Xử lý sự kiện khi nhấn vào nút "Huỷ"
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Đóng Dialog khi nhấn nút "Huỷ"
            }
        });

        // Thiết lập kích thước cho Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Thiết lập vị trí hiển thị của Dialog ở dưới cùng của màn hình
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Hiển thị Dialog
        dialog.show();
    }


    private void DialogChooseQuantityForOrder(Product selectedProduct) {
        // Tạo một Dialog mới
        Dialog dialog = new Dialog(requireContext(), R.style.FullScreenDialogStyle);

        // Đặt layout cho Dialog
        dialog.setContentView(R.layout.dialog_choose_quantity);

        // Lấy các view trong layout của Dialog
        ImageView img_this = dialog.findViewById(R.id.img_this);
        EditText edt_quantity = dialog.findViewById(R.id.edt_quantity);
        Button btn_increase = dialog.findViewById(R.id.btn_increase);
        Button btn_decrease = dialog.findViewById(R.id.btn_decrease);
        Button btn_enter = dialog.findViewById(R.id.btn_enter);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        // Load hình ảnh sản phẩm vào ImageView bằng Picasso
        Picasso.get().load(Constants.ip_Address + selectedProduct.getImagePr()).into(img_this);

        // Thiết lập giá trị ban đầu của EditText từ tham số truyền vào
        edt_quantity.setText(String.valueOf(defautQuantity));

        // Xử lý sự kiện khi nhấn vào nút tăng số lượng
        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(); // Tăng giá trị
                edt_quantity.setText(String.valueOf(defautQuantity)); // Cập nhật EditText
            }
        });

        // Xử lý sự kiện khi nhấn vào nút giảm số lượng
        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity(); // Giảm giá trị
                edt_quantity.setText(String.valueOf(defautQuantity)); // Cập nhật EditText
            }
        });

        // Xử lý sự kiện khi nhấn vào nút "Mua ngay"
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedProduct", selectedProduct);
                bundle.putInt("quantity", defautQuantity);

                OrderDialogFragment dialogFragment = new OrderDialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getParentFragmentManager(), "Dialogshow");

                dialog.dismiss(); // Đóng Dialog sau khi mở Fragment mới
            }
        });

        // Xử lý sự kiện khi nhấn vào nút "Huỷ"
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Đóng Dialog khi nhấn nút "Huỷ"
            }
        });

        // Thiết lập kích thước cho Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Thiết lập vị trí hiển thị của Dialog ở dưới cùng của màn hình
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Hiển thị Dialog
        dialog.show();
    }


    public void increaseQuantity() {
        defautQuantity++;
    }

    public void decreaseQuantity() {
        if (defautQuantity > 1) {
            defautQuantity--;
        }
    }

    private boolean isProductInFavorites(String productId) {
        return Constants.currentUser != null && Constants.currentUser.getFavoriteProducts() != null &&
                Constants.currentUser.getFavoriteProducts().contains(productId);
    }



}
