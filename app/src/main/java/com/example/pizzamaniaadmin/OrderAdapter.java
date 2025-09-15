package com.example.pizzamaniaadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // ✅ Bind main order details
        holder.textOrderId.setText("Order ID: " + order.getOrderId());
        holder.textUser.setText("Customer: " + order.getUserName());
        holder.textEmail.setText("Email: " + order.getUserEmail());
        holder.textBranch.setText("Branch: " + order.getBranch());
        holder.textStatus.setText("Status: " + order.getOrderStatus());
        holder.textAddress.setText("Delivery: " + order.getDeliveryAddress());
        holder.textDate.setText("Date: " + order.getDate());
        holder.textTime.setText("Time: " + order.getTime());
        holder.textTotal.setText("Total: LKR " + order.getTotalAmount());

        holder.layoutItems.removeAllViews();

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textUser, textEmail, textBranch, textStatus, textAddress, textDate, textTime, textTotal;
        LinearLayout layoutItems;
        Button btnViewMore;

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
