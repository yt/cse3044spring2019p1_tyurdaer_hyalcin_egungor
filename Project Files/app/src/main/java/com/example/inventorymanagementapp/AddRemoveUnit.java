package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddRemoveUnit extends AppCompatActivity {

    TextView product_name_txt, card_no_txt, model_no_txt, price_buy_txt, price_sell_txt, place_txt, description_txt, quantity_txt;
    EditText quantity_add_remove, customer_in;
    String email, c_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_unit);

        product_name_txt = findViewById(R.id.product_name);
        card_no_txt = findViewById(R.id.card_no);
        model_no_txt = findViewById(R.id.model_no);
        price_buy_txt = findViewById(R.id.price_buy);
        price_sell_txt = findViewById(R.id.price_sell);
        place_txt = findViewById(R.id.place);
        description_txt = findViewById(R.id.description);
        quantity_txt = findViewById(R.id.quantity);

        quantity_add_remove = findViewById(R.id.unit);
        customer_in = findViewById(R.id.customer);

        Bundle extras = getIntent().getExtras();
        String p_name,m_no,plc,desc;
        double p_buy,p_sell;
        int quantity;

        if(extras != null){
            this.email = extras.getString("email");
            p_name = extras.getString("p_name");
            this.c_no = extras.getString("card_no");
            m_no = extras.getString("model_no");
            p_buy = extras.getDouble("price_buy");
            p_sell = extras.getDouble("price_sell");
            plc = extras.getString("place");
            desc = extras.getString("description");
            quantity = extras.getInt("quantity");

            product_name_txt.setText(p_name);
            card_no_txt.setText(c_no);
            model_no_txt.setText(m_no);
            price_buy_txt.setText(String.valueOf(p_buy));
            price_sell_txt.setText(String.valueOf(p_sell));
            place_txt.setText(plc);
            description_txt.setText(desc);
            quantity_txt.setText(String.valueOf(quantity));
        }

    }

    public void add_unit(View v) {
        if(checkEditTextInputs()) {
            String type = "add_unit";
            String customer = customer_in.getText().toString();
            String quantity = quantity_add_remove.getText().toString();
            Date cDate = new Date();
            String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
            ApiHandler apiHandler = new ApiHandler(this);
            apiHandler.execute(type, this.email, this.c_no, customer, quantity, currentTime);
        }else{
            Toast.makeText(this, "Fill the Blanks!", Toast.LENGTH_SHORT).show();
        }
    }

    public void remove_unit(View v) {
        if(checkEditTextInputs()) {
            String type = "remove_unit";
            String customer = customer_in.getText().toString();
            String quantity = quantity_add_remove.getText().toString();
            Date cDate = new Date();
            String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

            if (Integer.parseInt(quantity_txt.getText().toString()) >= Integer.parseInt(quantity_add_remove.getText().toString())){
                ApiHandler apiHandler = new ApiHandler(this);
                apiHandler.execute(type, this.email, this.c_no, customer, quantity, currentTime);
            }else{
                Toast.makeText(this, "Not enough product!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Fill the Blanks!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkEditTextInputs() {
        return !customer_in.getText().toString().equals("") && !quantity_add_remove.getText().toString().equals("");
    }

    // show listproducts when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(AddRemoveUnit.this,ProductView.class);
            intent.putExtra("c_no", this.c_no);
            intent.putExtra("email", this.email);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
