package com.example.furniturebuyandsell.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.interfaces.ProductTypeItemClickListener;
import com.example.furniturebuyandsell.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllProductByTypeAdapter extends RecyclerView.Adapter<AllProductByTypeAdapter.ProductViewHolder>{
    private ArrayList<Product> productList;
    private Context context;
    private ProductItemClickListener listener;

    public void setListener(ProductItemClickListener listener) {
        this.listener = listener;
    }

    public AllProductByTypeAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_namePr, tv_pricePr;
        ImageView imgPr, img_favorite;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namePr = itemView.findViewById(R.id.tv_namePr);
            tv_pricePr = itemView.findViewById(R.id.tv_priceProduct);
            imgPr = itemView.findViewById(R.id.img_product);
            //img_favorite  = itemView.findViewById(R.id.img_favorite);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_all_productbytype, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tv_namePr.setText(product.getNamePr());
        holder.tv_pricePr.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(Constants.ip_Address + product.getImagePr()).into(holder.imgPr);
        // Kiểm tra xem sản phẩm có trong danh sách yêu thích của người dùng không
//        if (isProductInFavorites(product.getId())) {
//            holder.img_favorite.setImageResource(R.drawable.ic_heart_black);
//        } else {
//            holder.img_favorite.setImageResource(R.drawable.ic_favorite);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(product);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    private boolean isProductInFavorites(String productId) {
        return Constants.currentUser != null && Constants.currentUser.getFavoriteProducts() != null &&
                Constants.currentUser.getFavoriteProducts().contains(productId);
    }


}
