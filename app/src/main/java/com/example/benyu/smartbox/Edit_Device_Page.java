package com.example.benyu.smartbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class Edit_Device_Page extends AppCompatActivity {
    private account cur;
    private Device cur2;

    private EditText name;
    private EditText location;
    private EditText statusLabel;
    private EditText statusInfo;
    private Button manageButton;
    private Button history;
    private Button add;
    private Button delete;

    Control model = Control.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__device__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //no toolbar title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        manageButton = (Button) findViewById(R.id.button13);

        account current = getIntent().getParcelableExtra("user data");
        cur = model.updateAccount(current);
        Device deviceCurrent = getDevice(current);
        cur2 = deviceCurrent;
        Log.d("TEST", "ITEM RETURNED FROM METHOD: " + cur2.toString());

        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent editIntent = new Intent(Edit_Device_Page.this,
                        users_page.class);
                editIntent.putExtra("user data", cur);
                editIntent.putExtra("device data", cur2.getName());
                Edit_Device_Page.this.startActivity(editIntent);
            }
        });

    }

    /*  Takes the current account and looks through the devices to find matching name
     *
     *  current      account name
     *
     *  returns      device currently on
     */
    public Device getDevice(account current) {
        List<Device> devices = cur.getDeviceList();
        String deviceName = getIntent().getStringExtra("device name");
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

    //handles the inflation of menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_device_menu, menu);
        return true;
    }

    //handles the menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_history:
                //OPEN INTENT FOR NEW HISTORY PAGE
                return true;
            case R.id.action_delete:
                //DELETE THE DEVICE FROM USER ACCOUNT
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
