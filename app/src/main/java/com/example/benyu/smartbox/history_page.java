package com.example.benyu.smartbox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class history_page extends AppCompatActivity {

    private account cur;
    private Device cur2;
    private Device deviceCurrent;
    private ListView list;
    DatabaseReference databaseUsers;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Control model = Control.getInstance();

        account current = getIntent().getParcelableExtra("user data");
        String deviceCurrentTest = getIntent().getStringExtra("device data");
        Log.d("TEST", "DEVICE FROM EDIT_DEVICE_CURRENT: " + deviceCurrentTest);
        cur = model.updateAccount(current);
        deviceCurrent = getDevice(current);

        cur2 = deviceCurrent;
        Log.d("TEST", "DEVICE FROM EDIT_DEVICE: " + cur2.toString());
        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices")
                .child(deviceCurrent.getId()).child("history");

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //iterating through all the objects
                deviceCurrent.getHistoryList().clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    String userName = postSnapshot.child("userName").getValue().toString();

                    String date = postSnapshot.child("accessDate").getValue().toString();
                    String time = postSnapshot.child("accessTime").getValue().toString();



                    History a = new History(userName, date, time);

                    deviceCurrent.addHistory(a);


                    if (deviceCurrent.getHistoryList() != null) { list.setAdapter(new history_page.MylistAdpater(history_page.this, R.layout.list_item_history, deviceCurrent.getHistoryList()));}
                    myDialog = new Dialog(history_page.this);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list = (ListView) findViewById(R.id.list);
    }

    protected void onStart() {
        super.onStart();
        if (deviceCurrent.getHistoryList() != null) { list.setAdapter(new history_page.MylistAdpater(this, R.layout.list_item_history, deviceCurrent.getHistoryList()));}
        myDialog = new Dialog(this);
    }

    private class MylistAdpater extends ArrayAdapter<History> {
        private int layout;
        private List<History> historyList;
        public MylistAdpater(@NonNull Context context, int resource, @NonNull List<History> history) {
            super(context, resource, history);
            this.historyList = history;
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final history_page.ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                viewHolder = new history_page.ViewHolder();

                //list item content
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                viewHolder.date = (TextView) convertView.findViewById(R.id.list_item_date);
                viewHolder.time = (TextView) convertView.findViewById(R.id.list_item_time);


                final History history =  historyList.get(position);
                viewHolder.name.setText(history.getUserName());
                /*                    viewHolder.lockButton = (ImageButton) convertView.findViewById(R.id.list_item_button);*/


                viewHolder.date.setText(history.getAccessDate());


                viewHolder.time.setText(history.getAccessTime());



                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (history_page.ViewHolder)convertView.getTag();
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView name;
        TextView date;
        TextView time;
        //ImageButton lockButton;
    }

    /*  Takes the current account and looks through the devices to find matching name
     *
     *  current      account name
     *
     *  returns      device currently on
     */
    public Device getDevice(account current) {
        List<Device> devices = cur.getDeviceList();
        String deviceName = getIntent().getStringExtra("device data");
        Log.d("TEST", "NAME OF DEVICE FROM INTENT: " + deviceName);

        for (Device item : devices) {
            if (item.getName().equals(deviceName)) {
                Log.d("TEST", "ITEM FOUND: " + item.toString());
                return item;
            }
        }

        //no device found (error?)
        return null;
    }

}