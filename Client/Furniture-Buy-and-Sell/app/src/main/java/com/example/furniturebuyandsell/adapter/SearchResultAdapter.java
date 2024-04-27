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

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private ArrayList<Product> productList;
    private Context context;
    private ProductItemClickListener listener;

    public void setListener(ProductItemClickListener listener) {
        this.listener = listener;
    }

    public SearchResultAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }


    @NonNull
    @Override
    public SearchResultAdapter.SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_all_productbytype, parent, false);
        return new SearchResultAdapter.SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.SearchResultViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tv_namePr.setText(product.getNamePr());
        holder.tv_pricePr.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(Constants.ip_Address + product.getImagePr()).into(holder.imgPr);
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

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView tv_namePr, tv_pricePr;
        ImageView imgPr;
        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namePr = itemView.findViewById(R.id.tv_namePr);
            tv_pricePr = itemView.findViewById(R.id.tv_priceProduct);
            imgPr = itemView.findViewById(R.id.img_product);

        }
    }
}
