package com.example.benyu.smartbox;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_Account_page extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private EditText email;
    private EditText confirm;
    private account newAccount;
    DatabaseReference databaseHosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button cancel;
        Button create;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHosts = FirebaseDatabase.getInstance().getReference("path");

/*
        ButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String id = databaseHosts.push().getKey();
                databaseHosts.child(id).setValue(object to be added);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
*/

        name = (EditText) findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        confirm = (EditText) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);
        create = (Button) findViewById(R.id.create);
        newAccount = new account();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Edit", "Add account");
                Control model = Control.getInstance();
                if(password.getText().toString().equals(confirm.getText().toString()))
                {
                    newAccount.setName(name.getText().toString());
                    newAccount.setPassword(password.getText().toString());
                    newAccount.setEmail(email.getText().toString());
                    Log.d("Edit", "Add account");
                    model.addAccount(newAccount);
                    finish();
                }
                else {
                    Toast.makeText(Create_Account_page.this,
                            "passwords does not match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
}
}
