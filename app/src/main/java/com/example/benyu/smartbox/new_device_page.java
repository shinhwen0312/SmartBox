package com.example.benyu.smartbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class new_device_page extends AppCompatActivity {
    //list of device details
    private EditText name;
    private EditText location;
    private EditText id;
    private Button cancel;
    private Button add;
    private Device newDevice;
    DatabaseReference databaseHosts = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        name = (EditText) findViewById(R.id.editText3);
        location = (EditText) findViewById(R.id.editText8);
        id = (EditText) findViewById(R.id.editText9);
        //cancel = (Button)findViewById(R.id.button5);
        add = (Button)findViewById(R.id.button6);
        newDevice = new Device();

       /* cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

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
                Intent addIntent = new Intent(new_device_page.this,
                        devices_page.class);
                databaseHosts.child(current.getName()).child("devices").child(newDevice.getId()).setValue(newDevice);
                addIntent.putExtra("user data", model.updateAccount(current));
                new_device_page.this.startActivity(addIntent);
            }
        });

    }

}
