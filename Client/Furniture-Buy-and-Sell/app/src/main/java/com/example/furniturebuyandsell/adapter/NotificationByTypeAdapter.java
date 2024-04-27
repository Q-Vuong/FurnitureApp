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
import com.example.furniturebuyandsell.model.Notification;
import com.example.furniturebuyandsell.model.Product;
import com.example.furniturebuyandsell.model.ProductType;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class NotificationByTypeAdapter extends RecyclerView.Adapter<NotificationByTypeAdapter.ViewHolder> {

    private ArrayList<Notification> notificationArrayList;
    private Context context;

    public NotificationByTypeAdapter(ArrayList<Notification> notificationArrayList, Context context) {
        this.notificationArrayList = notificationArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationByTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationByTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationByTypeAdapter.ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);
        holder.txt_title.setText(notification.getTitle());
        holder.txt_content.setText(notification.getContent());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(notification.getDate());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeString = timeFormat.format(notification.getDate());
        holder.txt_date.setText(timeString + " " + dateString);
        Picasso.get().load(Constants.ip_Address + notification.getImage()).into(holder.img_notification);
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_notification;
        TextView txt_title, txt_content, txt_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_notification = itemView.findViewById(R.id.img_notification);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_content = itemView.findViewById(R.id.txt_content);
            txt_date = itemView.findViewById(R.id.txt_date);
        }
    }
}
