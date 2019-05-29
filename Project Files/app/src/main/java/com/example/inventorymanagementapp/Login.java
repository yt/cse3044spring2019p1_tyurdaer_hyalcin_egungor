package com.example.inventorymanagementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText email_txt, pw_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_txt = findViewById(R.id.email);
        pw_txt = findViewById(R.id.password);

        // TODO Delete this lines
        email_txt.setText("erhan@erhan.com");
        pw_txt.setText("321");
    }

    public void login(View v) {
        String type, email, pw;
        type = "login";
        email = email_txt.getText().toString();
        pw = pw_txt.getText().toString();

        if(!email.equals("") && !pw.equals("")){
            ApiHandler apiHandler = new ApiHandler(this);
            apiHandler.execute(type,email,pw);
        }
    }

    public void register(View v) {
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }
}
