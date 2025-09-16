package com.example.pizzamaniaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<PizzaModel> pizzaList;
    PizzaAdapter adapter;
    Button btnAddPizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        databaseReference = FirebaseDatabase.getInstance().getReference("Pizzas");

        recyclerView = findViewById(R.id.recyclerView);
        btnAddPizza = findViewById(R.id.btnAddPizza);

        pizzaList = new ArrayList<>();
        adapter = new PizzaAdapter(this, pizzaList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadPizzas();

        btnAddPizza.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, AddPizzaActivity.class));
        });

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void loadPizzas() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pizzaList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    PizzaModel pizza = data.getValue(PizzaModel.class);
                    if(pizza != null) {
                        pizzaList.add(pizza);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
