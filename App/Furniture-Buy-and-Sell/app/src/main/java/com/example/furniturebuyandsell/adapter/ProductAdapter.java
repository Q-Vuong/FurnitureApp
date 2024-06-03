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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> productList;
    private Context context;

    public ProductAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_product_for_order, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txt_ProductName.setText(product.getNamePr());
        //holder.txt_ProductQuantity.setText("x" + String.valueOf(product.getQuantity()));
        holder.txt_price.setText(String.valueOf(product.getPrice()) + "đ");
        holder.txt_size.setText(product.getSize());
        Picasso.get().load(Constants.ip_Address + product.getImagePr()).into(holder.img_product);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ProductName, txt_ProductQuantity, txt_price, txt_size;
        ImageView img_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ProductName = itemView.findViewById(R.id.tv_namePr);
            txt_ProductQuantity = itemView.findViewById(R.id.tv_quantity);
            txt_price = itemView.findViewById(R.id.tv_price);
            txt_size = itemView.findViewById(R.id.tv_size);
            img_product = itemView.findViewById(R.id.img_product);

        }
    }
}
