package com.example.inventorymanagementapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductView extends AppCompatActivity {

    TextView product_name_txt, card_no_txt, model_no_txt, price_buy_txt, price_sell_txt, place_txt, description_txt, quantity_txt;
    private String card_no;
    private String email;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        this.card_no = getIntent().getStringExtra("c_no");
        this.email = getIntent().getStringExtra("email");

        product_name_txt = findViewById(R.id.product_name);
        card_no_txt = findViewById(R.id.card_no);
        model_no_txt = findViewById(R.id.model_no);
        price_buy_txt = findViewById(R.id.price_buy);
        price_sell_txt = findViewById(R.id.price_sell);
        place_txt = findViewById(R.id.place);
        description_txt = findViewById(R.id.description);
        quantity_txt = findViewById(R.id.quantity);


        String type = "view_product";
        ApiHandler apiHandler = new ApiHandler(this);
        apiHandler.execute(type, this.email, this.card_no);


        // Create alert dialog for delete
        builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setTitle("Ürün Silme");
        builder.setMessage("Ürünü silmek istediğinize emin misiniz?");

        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete product from database
                execueDelete();
                Toast toast = Toast.makeText(ProductView.this, "Ürün silindi!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.parseColor("#008000"), PorterDuff.Mode.SRC_IN);
                toast.show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(ProductView.this, "Ürün silinemedi!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"), PorterDuff.Mode.SRC_IN);
                toast.show();
                dialog.dismiss();
            }
        });

    }

    public void execueDelete(){
        String type = "delete_product";
        ApiHandler apiHandler = new ApiHandler(this);
        apiHandler.execute(type, this.email, this.card_no);
    }

    public void update_product(View v) {
        Intent intent = new Intent(this, ProductUpdate.class);
        intent.putExtra("email",this.email);
        intent.putExtra("p_name",product_name_txt.getText().toString());
        intent.putExtra("card_no",card_no_txt.getText().toString());
        intent.putExtra("model_no",model_no_txt.getText().toString());
        intent.putExtra("price_buy",Double.parseDouble(price_buy_txt.getText().toString()));
        intent.putExtra("price_sell",Double.parseDouble(price_sell_txt.getText().toString()));
        intent.putExtra("place",place_txt.getText().toString());
        intent.putExtra("description",description_txt.getText().toString());
        intent.putExtra("quantity",Integer.parseInt(quantity_txt.getText().toString()));
        startActivity(intent);
        finish();
    }

    public void delete_product(View v) {
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void add_remove_unit(View v) {
        Intent intent = new Intent(this, AddRemoveUnit.class);
        intent.putExtra("email",this.email);
        intent.putExtra("p_name",product_name_txt.getText().toString());
        intent.putExtra("card_no",card_no_txt.getText().toString());
        intent.putExtra("model_no",model_no_txt.getText().toString());
        intent.putExtra("price_buy",Double.parseDouble(price_buy_txt.getText().toString()));
        intent.putExtra("price_sell",Double.parseDouble(price_sell_txt.getText().toString()));
        intent.putExtra("place",place_txt.getText().toString());
        intent.putExtra("description",description_txt.getText().toString());
        intent.putExtra("quantity",Integer.parseInt(quantity_txt.getText().toString()));
        startActivity(intent);
        finish();
    }

    public void fill(JSONObject response){
        JSONArray log = null;

        try {
            // initialize text views
            product_name_txt.setText(response.getString("modelName"));
            card_no_txt.setText(response.getString("cardNo"));
            model_no_txt.setText(response.getString("modelNo"));
            price_buy_txt.setText(response.getString("priceBuy"));
            price_sell_txt.setText(response.getString("priceSell"));
            place_txt.setText(response.getString("place"));
            description_txt.setText(response.getString("description"));
            quantity_txt.setText(response.getString("quantity"));

            // get log array!
            log = response.getJSONArray("log");

        } catch (JSONException e) {
            Log.e("Initialize", "initialize failed!");
            e.printStackTrace();
        }

        // Fill log table
        LinearLayout tl = findViewById(R.id.log_table_layout);
        View tablerow = null;
        TextView customer_txt, date_txt, qnt_change_txt, customer_label, date_label, qnt_change_label, label;

        try{

            for (int i = 0; i < log.length(); i++){

                // Get a single json object from log
                JSONObject log_row = (JSONObject) log.get(i);

                // Parse json object
                String customer = log_row.getString("customer");
                String quantity = log_row.getString("quantity");
                String date = log_row.getString("date");
                boolean sob = log_row.getBoolean("sob");

                // Create table row for each log row, add it to the log list
                tablerow = View.inflate(this, R.layout.log_list_row, null);
                customer_txt = tablerow.findViewById(R.id.customer);
                date_txt = tablerow.findViewById(R.id.date);
                qnt_change_txt = tablerow.findViewById(R.id.quantity);
                customer_label = tablerow.findViewById(R.id.customer_label);
                date_label = tablerow.findViewById(R.id.date_label);
                qnt_change_label = tablerow.findViewById(R.id.quantity_label);
                label = tablerow.findViewById(R.id.label);

                customer_txt.setText(customer);
                date_txt.setText(date);
                qnt_change_txt.setText(quantity);

                if (!sob){
                    customer_label.setBackgroundColor(Color.parseColor("#ff2b2b"));
                    date_label.setBackgroundColor(Color.parseColor("#ff2b2b"));
                    qnt_change_label.setBackgroundColor(Color.parseColor("#ff2b2b"));
                    label.setText("Stok Çıkışı");
                } else{
                    customer_label.setBackgroundColor(Color.parseColor("#32CD32"));
                    date_label.setBackgroundColor(Color.parseColor("#32CD32"));
                    qnt_change_label.setBackgroundColor(Color.parseColor("#32CD32"));
                    label.setText("Stok Girişi");
                }

                tablerow.setTag(i);
                //add TableRows to Layout
                tl.addView(tablerow);

            }

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    // show product list when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ProductView.this,ListProducts.class);
            intent.putExtra("email",this.email);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
