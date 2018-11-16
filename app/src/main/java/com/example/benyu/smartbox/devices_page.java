package com.example.benyu.smartbox;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        logOut = (Button) findViewById(R.id.button2);
      //  list = (ListView) findViewById(R.id.list);
        Control model = Control.getInstance();
        final List<Device> SearchShelter = model.getDeviceList();
        final ArrayList<Device> nameList = new ArrayList<>();
    }

}
