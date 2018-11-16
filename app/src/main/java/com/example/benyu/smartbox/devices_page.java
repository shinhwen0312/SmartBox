package com.example.benyu.smartbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class devices_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button logOut;
        ListView list;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(devices_page.this,
                        new_device_page.class);
                devices_page.this.startActivity(addIntent);
            }
        });
        logOut = (Button) findViewById(R.id.button);
        list = (ListView) findViewById(R.id.list);
        Control model = Control.getInstance();
        final account current = getIntent().getParcelableExtra("user data");

        final List<Device> deviceList = current.getDeviceList();


        ListAdapter DeviceAdapter =
                new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                        model.updateAccount(current).getDeviceNameList());
        list.setAdapter(DeviceAdapter);
        // this sets the click action for listview when click on the screen
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Edit", "add device");  //debugging msg
                Context context = parent.getContext();  //making context to current context
                Intent intent = new Intent(context, Edit_Device_Page.class);
                intent.putExtra("detail data",deviceList.get(position));
                intent.putExtra("user data", current);
                context.startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(devices_page.this,
                        MainActivity.class);
                devices_page.this.startActivity(logoutIntent);
            }
        });

    }

}
