package com.example.pizzamaniaadmin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<OrderModel> orderList;
    private final Context context;

    public OrderAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.textOrderId.setText(order.getOrderId());
        holder.textUser.setText("Customer: " + order.getUserName());
        holder.textEmail.setText("Phone: " + order.getUserEmail());
        holder.textBranch.setText("Branch: " + order.getBranch());
        holder.textStatus.setText(order.getOrderStatus());
        holder.textAddress.setText("Delivery: " + order.getDeliveryAddress());
        holder.textDate.setText("Date: " + order.getDate());
        holder.textTime.setText("Time: " + order.getTime());
        holder.textTotal.setText("Total: LKR " + order.getTotalAmount());

        switch (order.getOrderStatus().toLowerCase()) {
            case "pending":
                holder.textStatus.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
                break;
            case "approved":
                holder.textStatus.setBackgroundColor(Color.parseColor("#2196F3")); // Blue
                break;
            case "processing":
                holder.textStatus.setBackgroundColor(Color.parseColor("#9C27B0")); // Purple
                break;
            case "dispatched":
                holder.textStatus.setBackgroundColor(Color.parseColor("#03A9F4")); // Light Blue
                break;
            case "delivered":
                holder.textStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
                break;
            case "cancelled":
                holder.textStatus.setBackgroundColor(Color.parseColor("#F44336")); // Red
                break;
            default:
                holder.textStatus.setBackgroundColor(Color.GRAY); // Default gray
                break;
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textUser, textEmail, textBranch, textStatus, textAddress, textDate, textTime, textTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.textOrderId);
            textUser = itemView.findViewById(R.id.textUser);
            textEmail = itemView.findViewById(R.id.textEmail);
            textBranch = itemView.findViewById(R.id.textBranch);
            textStatus = itemView.findViewById(R.id.textStatus);
            textAddress = itemView.findViewById(R.id.textAddress);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
            textTotal = itemView.findViewById(R.id.textTotal);
        }
    }
}
