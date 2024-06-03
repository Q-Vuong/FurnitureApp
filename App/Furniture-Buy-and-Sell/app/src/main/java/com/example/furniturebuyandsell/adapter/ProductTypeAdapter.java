package com.example.furniturebuyandsell.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.interfaces.ProductTypeItemClickListener;
import com.example.furniturebuyandsell.model.ProductType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ProductTypeViewHolder> {
    private ArrayList<ProductType> productTypeList;
    private Context context;
    private ProductTypeItemClickListener itemClickListener;

    public ProductTypeAdapter(ArrayList<ProductType> productTypeList, Context context) {
        this.productTypeList = productTypeList;
        this.context = context;
    }

    public void setItemClickListener(ProductTypeItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ProductTypeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameType;
        ImageView img_productType;
        public ProductTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameType = itemView.findViewById(R.id.tv_nameType);
            img_productType = itemView.findViewById(R.id.img_productType);
        }
    }

    @NonNull
    @Override
    public ProductTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_product_type, parent, false);
        return new ProductTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeViewHolder holder, int position) {
          ProductType productType = productTypeList.get(position);
          holder.tv_nameType.setText(productType.getNameType());
          Picasso.get().load(Constants.ip_Address + productType.getImgPr_T()).into(holder.img_productType);
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Log.d("TAG", "onClick: ");
                  if (itemClickListener != null) {
                      itemClickListener.onItemClick(holder.getAdapterPosition());
                  }
              }
          });

    }



    @Override
    public int getItemCount() {
        return productTypeList.size();
    }




}
