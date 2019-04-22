package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ProductView extends AppCompatActivity {

    TextView product_name_txt, card_no_txt, model_no_txt, price_buy_txt, price_sell_txt, place_txt, description_txt, quantity_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        product_name_txt = findViewById(R.id.product_name);
        card_no_txt = findViewById(R.id.card_no);
        model_no_txt = findViewById(R.id.model_no);
        price_buy_txt = findViewById(R.id.price_buy);
        price_sell_txt = findViewById(R.id.price_sell);
        place_txt = findViewById(R.id.place);
        description_txt = findViewById(R.id.description);
        quantity_txt = findViewById(R.id.quantity);
    }

    public void update_product(View v) {

    }

    public void delete_product(View v) {

    }

    public void add_remove_unit(View v) {

    }

    private void fillLogTable(){

    }

    // show product list when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ProductView.this,ListProducts.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
