package com.example.inventorymanagementapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    EditText name_txt, surname_txt, email_txt, pw_txt, pw1_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_txt = findViewById(R.id.name);
        surname_txt = findViewById(R.id.surname);
        email_txt = findViewById(R.id.email);
        pw_txt = findViewById(R.id.password);
        pw1_txt = findViewById(R.id.password1);
    }

    public void register(View view) {
        String type, email, pw, pw1, name, surname;
        type = "register";
        email = email_txt.getText().toString();
        pw = pw_txt.getText().toString();
        pw1 = pw1_txt.getText().toString();
        name = name_txt.getText().toString();
        surname = surname_txt.getText().toString();

        if(!email.equals("") && !pw.equals("") && !pw1.equals("") && !name.equals("") && !surname.equals("")){
            if(pw.equals(pw1)){
                ApiHandler apiHandler = new ApiHandler(this);
                apiHandler.execute(type,email,pw,name,surname);
            }
        }
    }
}
