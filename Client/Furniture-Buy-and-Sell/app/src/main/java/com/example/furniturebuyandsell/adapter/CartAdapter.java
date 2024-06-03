package com.example.furniturebuyandsell.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.orders.CartDiaLogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.model.Banner;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<User.CartItem> userList;
    private ArrayList<Product> productList;
    private Context context;

    public CartAdapter(ArrayList<User.CartItem> userList, ArrayList<Product> productList, Context context) {
        this.userList = userList;
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        User.CartItem cartItem = userList.get(position);
        // Lấy productId của mục CartItem
        String productId = cartItem.getProductId();
        // Tìm sản phẩm tương ứng từ productId trong danh sách sản phẩm hoặc từ cơ sở dữ liệu
        Product product = findProductById(productId);
        String customerId = Constants.idUser;

        // Gán các giá trị của sản phẩm vào ViewHolder
        if (product != null) {
            holder.txt_namePr.setText(product.getNamePr());
            holder.txt_price.setText(String.valueOf(product.getPrice()));
            holder.txt_quantity.setText(String.valueOf(cartItem.getQuantity()));
            holder.txt_size.setText(product.getSize());
            // Cài đặt hình ảnh sản phẩm vào ImageView, bạn có thể sử dụng thư viện Picasso hoặc Glide để load ảnh
            Picasso.get().load(Constants.ip_Address + product.getImagePr()).into(holder.img_pr);

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Gửi yêu cầu HTTP để xóa sản phẩm khỏi giỏ hàng
                    ApiService apiService = Constants.getApiService();
                    Call<User.CartItem> deleteCartItemCall = apiService.deleteCart(customerId, productId);
                    deleteCartItemCall.enqueue(new Callback<User.CartItem>() {
                        @Override
                        public void onResponse(Call<User.CartItem> call, Response<User.CartItem> response) {
                            if (response.isSuccessful()) {
                                // Xóa sản phẩm khỏi danh sách hiển thị và thông báo người dùng
                                int position = holder.getAdapterPosition();
                                userList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, userList.size());
                                Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý lỗi nếu xảy ra
                                Toast.makeText(context, "Lỗi khi xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User.CartItem> call, Throwable t) {
                            // Xử lý lỗi kết nối
                            Toast.makeText(context, "Lỗi kết nối, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                            Log.e("API Call", "Lỗi kết nối, vui lòng thử lại sau", t);
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_price, txt_namePr, txt_quantity, txt_size;
        ImageView img_pr;
        Button btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_price = itemView.findViewById(R.id.tv_price);
            txt_namePr = itemView.findViewById(R.id.tv_namePr);
            txt_quantity = itemView.findViewById(R.id.tv_quantity);
            txt_size = itemView.findViewById(R.id.tv_size);
            img_pr = itemView.findViewById(R.id.img_product);
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }
    }

    private Product findProductById(String productId) {
        // Duyệt qua danh sách sản phẩm để tìm sản phẩm có productId tương ứng
        for (Product product : productList) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }


}
