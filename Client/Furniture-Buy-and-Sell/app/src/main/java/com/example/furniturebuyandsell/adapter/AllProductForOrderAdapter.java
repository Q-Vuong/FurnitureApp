package com.example.furniturebuyandsell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllProductForOrderAdapter extends RecyclerView.Adapter<AllProductForOrderAdapter.ViewHolder> {

    ArrayList<User.CartItem> cartItemArrayList;
    ArrayList<Product> productList;
    private Context context;

    public AllProductForOrderAdapter(ArrayList<User.CartItem> cartItemArrayList, ArrayList<Product> productList, Context context) {
        this.cartItemArrayList = cartItemArrayList;
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllProductForOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_all_product_for_order, parent, false);
        return new AllProductForOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductForOrderAdapter.ViewHolder holder, int position) {
        User.CartItem cartItem = cartItemArrayList.get(position);
        String productId = cartItem.getProductId();
        Product product = findProductById(productId);

        holder.txt_namepr.setText(product.getNamePr());
        holder.txt_size.setText(product.getSize());
        holder.txt_quantity.setText("x" + product.getQuantity());
        holder.txt_price.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(Constants.ip_Address + product.getImagePr()).into(holder.img_pr);
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_namepr, txt_size, txt_quantity, txt_price;
        ImageView img_pr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_namepr = itemView.findViewById(R.id.tv_namePr);
            txt_size = itemView.findViewById(R.id.tv_size);
            txt_quantity = itemView.findViewById(R.id.tv_quantity);
            txt_price = itemView.findViewById(R.id.tv_price);
            img_pr = itemView.findViewById(R.id.img_product);
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
