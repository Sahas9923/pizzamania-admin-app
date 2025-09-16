package com.example.pizzamaniaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerBranch = findViewById(R.id.spinnerBranch);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnFilter = findViewById(R.id.btnFilter);

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OrderActivity.this, HomeActivity.class));
                finish();
            }
        });

        orderAdapter = new OrderAdapter(this, filteredOrders);
        recyclerView.setAdapter(orderAdapter);

        fetchOrdersFromFirebase();

        btnFilter.setOnClickListener(v -> applyFilters());

        Spinner spinnerBranch = findViewById(R.id.spinnerBranch);
        Spinner spinnerStatus = findViewById(R.id.spinnerStatus);

        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.branches_array,
                R.layout.spinner_item
        );
        branchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerBranch.setAdapter(branchAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                R.layout.spinner_item
        );
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
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
        String selectedBranch = spinnerBranch.getSelectedItem().toString().trim();
        String selectedStatus = spinnerStatus.getSelectedItem().toString().trim();

        filteredOrders.clear();

        for (OrderModel order : allOrders) {
            String orderBranch = order.getBranch() != null ? order.getBranch().trim() : "";
            String orderStatus = order.getOrderStatus() != null ? order.getOrderStatus().trim() : "";

            boolean branchMatch = selectedBranch.equalsIgnoreCase("All") || orderBranch.equalsIgnoreCase(selectedBranch);
            boolean statusMatch = selectedStatus.equalsIgnoreCase("All") || orderStatus.equalsIgnoreCase(selectedStatus);

            if (branchMatch && statusMatch) {
                filteredOrders.add(order);
            }
        }

        orderAdapter.notifyDataSetChanged();

        if (filteredOrders.isEmpty()) {
            Toast.makeText(this, "No orders found with selected filters", Toast.LENGTH_SHORT).show();
        }
    }

}
