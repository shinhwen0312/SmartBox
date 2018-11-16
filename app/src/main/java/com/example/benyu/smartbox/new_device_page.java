package com.example.benyu.smartbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class new_device_page extends AppCompatActivity {
    private EditText name;
    private EditText location;
    private EditText id;
    private Button cancel;
    private Button add;
    private Device newDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        name = (EditText) findViewById(R.id.editText3);
        location = (EditText) findViewById(R.id.editText8);
        id = (EditText) findViewById(R.id.editText9);
        cancel = (Button)findViewById(R.id.button5);
        add = (Button)findViewById(R.id.button6);
        newDevice = new Device();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(new_device_page.this,
                        devices_page.class);
                new_device_page.this.startActivity(logoutIntent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Edit", "Add account");
                Control model = Control.getInstance();
                final account current = getIntent().getParcelableExtra("user data");
                    newDevice.setName(name.getText().toString());
                    newDevice.setLocation(location.getText().toString());
                    newDevice.setId(id.getText().toString());
                    Log.d("Edit", "Add account");
                    model.updateAccount(current).addDevice(newDevice);
                    finish();

            }
        });

    }

}
