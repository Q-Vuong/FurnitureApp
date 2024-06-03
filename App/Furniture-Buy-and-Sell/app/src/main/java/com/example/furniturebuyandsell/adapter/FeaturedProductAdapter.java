package com.example.furniturebuyandsell.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.interfaces.FavoritesChangedListener;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ProductViewHolder>{

    private ArrayList<Product> productList;
    private Context context;
    private ProductItemClickListener listener;


    public void setListener(ProductItemClickListener listener) {
        this.listener = listener;
    }


    public FeaturedProductAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameProduct, tv_descriptionProduct, tv_priceProduct;
        ImageView img_product, img_favorite;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameProduct = itemView.findViewById(R.id.tv_nameProduct);
            tv_descriptionProduct  = itemView.findViewById(R.id.tv_descriptionProduct);
            tv_priceProduct  = itemView.findViewById(R.id.tv_priceProduct);
            img_product  = itemView.findViewById(R.id.img_product);
            //img_favorite  = itemView.findViewById(R.id.img_favorite);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_featured_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tv_nameProduct.setText(product.getNamePr());
        holder.tv_descriptionProduct.setText(product.getDescription());
        holder.tv_priceProduct.setText(String.valueOf(product.getPrice()) + "đ");
        Picasso.get().load(Constants.ip_Address + product.getImagePr()).into(holder.img_product);

//        // Kiểm tra xem sản phẩm có trong danh sách yêu thích của người dùng không
//        if (isProductInFavorites(product.getId())) {
//            holder.img_favorite.setImageResource(R.drawable.ic_heart_black);
//        } else {
//            holder.img_favorite.setImageResource(R.drawable.ic_favorite);
//
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

    // Phương thức kiểm tra xem sản phẩm có trong danh sách yêu thích của người dùng không
    private boolean isProductInFavorites(String productId) {
        return Constants.currentUser != null && Constants.currentUser.getFavoriteProducts() != null &&
                Constants.currentUser.getFavoriteProducts().contains(productId);
    }


    // Khi dữ liệu yêu thích thay đổi, cập nhật lại giao diện người dùng
    public void updateFavorites() {
        notifyDataSetChanged();
    }

}
