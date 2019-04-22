package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v) {
        Intent intent = new Intent(Login.this,ListProducts.class);
        startActivity(intent);
    }

    public void register(View v) {
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }
}
