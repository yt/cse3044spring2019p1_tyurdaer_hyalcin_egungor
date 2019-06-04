package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductUpdate extends AppCompatActivity {

    EditText product_name, model_no, price_buy, price_sell, place, description,quantity;
    TextView card_no;
    String email;
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
            this.email = extras.getString("email");

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
        String type = "update_product";
        String p_name, c_no, m_no, p_b, p_s, plc, desc, qnt;
        p_name = product_name.getText().toString();
        c_no = card_no.getText().toString();
        m_no = model_no.getText().toString();
        p_b = price_buy.getText().toString();
        p_s = price_sell.getText().toString();
        plc = place.getText().toString();
        desc = description.getText().toString();
        qnt = quantity.getText().toString();

        if(checkEditTextInputs(p_name,c_no,m_no,p_b,p_s,plc,qnt)) {
            ApiHandler apiHandler = new ApiHandler(this);
            apiHandler.execute(type, this.email, p_name, c_no, m_no, p_b, p_s, plc, desc, qnt);
        }else{
            Toast.makeText(this, "Fill the Blanks!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkEditTextInputs(String p_name, String c_no, String m_no, String p_b, String p_s, String plc, String qnt){
        return !p_name.equals("") && !c_no.equals("") && !m_no.equals("") && !p_b.equals("") && !p_s.equals("") && !plc.equals("") && !qnt.equals("");
    }

    // show listproducts when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ProductUpdate.this,ProductView.class);
            TextView card_no = findViewById(R.id.card_no);
            intent.putExtra("c_no",card_no.getText().toString());
            intent.putExtra("email",this.email);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
