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
import android.widget.Toast;

import java.util.List;

public class Edit_Device_Page extends AppCompatActivity {
    private account cur;
    private Device cur2;
    //DatabaseReference databaseDeviceStates;

    private EditText deviceName;
    private EditText deviceLocation;
    private Button manageButton;
    private Button saveButton;

    Control model = Control.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__device__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //no toolbar title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //back arrow functionality in toolbar
        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        //local storage
        account current = getIntent().getParcelableExtra("user data");
        cur = model.updateAccount(current);
        final Device deviceCurrent = getDevice(current); //method that searches all device names to find matching one
        cur2 = deviceCurrent;
        //Log.d("TEST", "ITEM RETURNED FROM METHOD: " + cur2.toString());

        deviceName = (EditText) findViewById(R.id.device_name);
        if(deviceCurrent.getName() != null)
        deviceName.setText(deviceCurrent.getName());
        deviceLocation = (EditText) findViewById(R.id.device_location);
        if(deviceCurrent.getName() != null)
        deviceLocation.setText(deviceCurrent.getLocation());

        //manage users button
        manageButton = (Button) findViewById(R.id.manage_users);
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

        //save button functionality
        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //TODO More In-Depth Error Checking
                //TODO Update in Database
                String rename = deviceName.getText().toString();
                String renameLocation = deviceLocation.getText().toString();
                if(rename.equals("") || renameLocation.equals("")) {
                    Toast.makeText(Edit_Device_Page.this,
                            "Cannot have empty name or location.",
                            Toast.LENGTH_SHORT).show();
                } else {
/*                    databaseDeviceStates = FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices");
                    databaseDeviceStates.child(cur2.getName()).child("name").setValue(rename);*/
                    cur2.setName(rename);
                    cur2.setLocation(renameLocation);

                    Toast.makeText(Edit_Device_Page.this,
                            "Successfully saved.",
                            Toast.LENGTH_SHORT).show();
                }
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
        //TODO Handle Errors
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
                //TODO Manage History
                return true;
            case R.id.action_delete:
                //TODO Delete Device
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
