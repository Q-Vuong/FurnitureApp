package com.example.furniturebuyandsell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.model.Order;
import com.example.furniturebuyandsell.model.Product;

import java.util.ArrayList;

public class AllOrderForUserAdapter extends RecyclerView.Adapter<AllOrderForUserAdapter.ViewHolder> {

    private ArrayList<Order> orderList;
    private ArrayList<Product> productList;
    private Context context;

    public AllOrderForUserAdapter( ArrayList<Product> productList, ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllOrderForUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oder, parent, false);
        return new AllOrderForUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrderForUserAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txt_idOrder.setText("Mã đơn: " + order.getId());
        holder.txt_TotalPrice.setText(order.getPriceToPay());

        // Hiển thị danh sách sản phẩm trong RecyclerView
        ArrayList<Order.ProductItem> productItems = order.getProducts();
        ProductAdapter productAdapter = new ProductAdapter(getProductsFromProductItems(productItems), context);
        holder.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewProducts.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_idOrder, txt_TotalPrice;
        RecyclerView recyclerViewProducts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_idOrder = itemView.findViewById(R.id.txt_idOrder);
            txt_TotalPrice = itemView.findViewById(R.id.txt_TotalPrice);
            recyclerViewProducts = itemView.findViewById(R.id.recyV_productForOrder);
        }
    }

    private ArrayList<Product> getProductsFromProductItems(ArrayList<Order.ProductItem> productItems) {
        ArrayList<Product> products = new ArrayList<>();
        for (Order.ProductItem productItem : productItems) {
            // Tìm sản phẩm tương ứng với productItem
            for (Product product : productList) {
                if (product.getId().equals(productItem.getProductId())) {
                    products.add(product);
                    break;
                }
            }
        }
        return products;
    }

}
