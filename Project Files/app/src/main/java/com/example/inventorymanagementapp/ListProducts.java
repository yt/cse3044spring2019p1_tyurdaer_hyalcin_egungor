package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

public class ListProducts extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        this.email = getIntent().getStringExtra("email");
        String type = "list_products";
        ApiHandler apiHandler = new ApiHandler(this);
        apiHandler.execute(type, email);

    }

    public void add_new_product(View v) {
        Intent intent = new Intent(ListProducts.this, NewProduct.class);
        startActivity(intent);
        finish();
    }

    private void fillTable(){

    }
}
