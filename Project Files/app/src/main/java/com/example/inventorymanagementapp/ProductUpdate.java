package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProductUpdate extends AppCompatActivity {

    EditText product_name, model_no, price_buy, price_sell, place, description,quantity;
    TextView card_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        product_name = findViewById(R.id.product_name);
        card_no = findViewById(R.id.card_no);
        model_no = findViewById(R.id.model_no);
        price_buy = findViewById(R.id.price_buy);
        price_sell = findViewById(R.id.price_sell);
        place = findViewById(R.id.place);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);

        Bundle extras = getIntent().getExtras();
        String p_name,c_no="",m_no,plc,desc;
        double p_buy,p_sell;
        int qnt;

        if(extras != null){
            p_name = extras.getString("p_name");
            c_no = extras.getString("card_no");
            m_no = extras.getString("model_no");
            p_buy = extras.getDouble("price_buy");
            p_sell = extras.getDouble("price_sell");
            plc = extras.getString("place");
            desc = extras.getString("description");
            qnt = extras.getInt("quantity");

            product_name.setText(p_name);
            card_no.setText(c_no);
            model_no.setText(m_no);
            price_buy.setText(String.valueOf(p_buy));
            price_sell.setText(String.valueOf(p_sell));
            place.setText(plc);
            description.setText(desc);
            quantity.setText(String.valueOf(qnt));
        }
    }

    public void save_product(View v) {

    }

    private int checkEditTextInputs(){
        return 1;
    }

    // show listproducts when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ProductUpdate.this,ProductView.class);
            TextView card_no = findViewById(R.id.card_no);
            intent.putExtra("clicked_item",card_no.getText().toString());
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
