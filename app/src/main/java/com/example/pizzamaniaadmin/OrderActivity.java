package com.example.pizzamaniaadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderModel> allOrders = new ArrayList<>();
    private List<OrderModel> filteredOrders = new ArrayList<>();

    private Spinner spinnerBranch, spinnerStatus;
    private Button btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order); // make sure this matches your XML

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerBranch = findViewById(R.id.spinnerBranch);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnFilter = findViewById(R.id.btnFilter);

        // Pass 'this' as Context to OrderAdapter
        orderAdapter = new OrderAdapter(this, filteredOrders);
        recyclerView.setAdapter(orderAdapter);

        fetchOrdersFromFirebase();

        btnFilter.setOnClickListener(v -> applyFilters());
    }

    private void fetchOrdersFromFirebase() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOrders.clear();

                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    OrderModel order = new OrderModel();
                    order.setOrderId(orderSnap.getKey());
                    String userUid = orderSnap.child("userUid").getValue(String.class);
                    order.setUserUid(userUid);
                    order.setBranch(orderSnap.child("nearestBranch").getValue(String.class));
                    order.setOrderStatus(orderSnap.child("orderStatus").getValue(String.class));
                    order.setDeliveryAddress(orderSnap.child("deliveryAddress").getValue(String.class));
                    order.setDate(orderSnap.child("orderDate").getValue(String.class));
                    order.setTime(orderSnap.child("orderTime").getValue(String.class));
                    order.setTotalAmount(orderSnap.child("totalFee").getValue(Double.class) != null ?
                            orderSnap.child("totalFee").getValue(Double.class) : 0.0);

                    // Fetch order items
                    List<OrderItemModel> items = new ArrayList<>();
                    if (orderSnap.hasChild("order_items")) {
                        for (DataSnapshot itemSnap : orderSnap.child("order_items").getChildren()) {
                            OrderItemModel item = new OrderItemModel();
                            item.setItemName(itemSnap.child("name").getValue(String.class));
                            item.setSize(itemSnap.child("size").getValue(String.class));
                            item.setQuantity(itemSnap.child("quantity").getValue(Integer.class) != null ?
                                    itemSnap.child("quantity").getValue(Integer.class) : 1);
                            item.setPrice(itemSnap.child("price").getValue(Double.class) != null ?
                                    itemSnap.child("price").getValue(Double.class) : 0.0);
                            items.add(item);
                        }
                    }
                    order.setItems(items);

                    // Fetch user info
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userUid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnap) {
                            if (userSnap.exists()) {
                                order.setUserName(userSnap.child("fullName").getValue(String.class));
                                order.setUserEmail(userSnap.child("phone").getValue(String.class));
                            } else {
                                order.setUserName("Unknown");
                                order.setUserEmail("Unknown");
                            }

                            allOrders.add(order);
                            filteredOrders.clear();
                            filteredOrders.addAll(allOrders);
                            orderAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderActivity.this, "Failed to fetch orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilters() {
        String selectedBranch = spinnerBranch.getSelectedItem().toString();

        filteredOrders.clear();

        for (OrderModel order : allOrders) {
            if (selectedBranch.equals("All") || order.getBranch().equalsIgnoreCase(selectedBranch)) {
                filteredOrders.add(order);
            }
        }
        orderAdapter.notifyDataSetChanged();

        if (filteredOrders.isEmpty()) {
            Toast.makeText(this, "No orders found for selected branch", Toast.LENGTH_SHORT).show();
        }
    }
}
