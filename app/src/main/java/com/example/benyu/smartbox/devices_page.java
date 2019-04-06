package com.example.benyu.smartbox;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class devices_page extends AppCompatActivity {
    private account cur;
    private ListView list;
    Dialog myDialog;
    DatabaseReference databaseLocks;
    DatabaseReference databaseLockStates;
    private final static int REQUEST_ENABLE_BT = 1;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    String btDeviceName;
    String btAddr;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket btSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Control model = Control.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_page);
        list = (ListView) findViewById(R.id.list);





        account current = getIntent().getParcelableExtra("user data");
        cur = model.updateAccount(current);
        databaseLocks = FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices");

        databaseLocks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //iterating through all the objects
                cur.getDeviceList().clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting object
                    Device a = postSnapshot.getValue(Device.class);
                    //adding object to the list
                    cur.addDevice(a);
                    Log.d("check","checking how many times");
                }
                
                final List<Device> deviceList = cur.getDeviceList();

                list.setAdapter(new MylistAdapter(devices_page.this,R.layout.list_item, deviceList));
                myDialog = new Dialog(devices_page.this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //no title, custom one in XML
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent =
                        new Intent(devices_page.this, new_device_page.class);
                newIntent.putExtra("user data", cur);
                devices_page.this.startActivity(newIntent);
            }
        });

        FloatingActionButton btButton = (FloatingActionButton) findViewById(R.id.bluetooth);
        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    if (!bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }

                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            if (device.getAddress().equals("98:D3:33:81:06:2D")) {
                                btDeviceName = device.getName();
                                btAddr = device.getAddress(); // MAC address
                            }
                        }
                        if (btAddr != null) {
                            Toast.makeText(getApplicationContext(), "SmartBox successfully paired.",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // this sets the click action for listview when click on the screen
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Edit", "add device");  //debugging msg
//                Context context = parent.getContext();  //making context to current context
//                Intent intent = new Intent(context, Edit_Device_Page.class);
//                intent.putExtra("detail data",deviceList.get(position));
//                intent.putExtra("user data", current);
//                context.startActivity(intent);
//            }
//        });
    }

    protected void onStart() {
        super.onStart();
        if (cur.getDeviceList() != null) { list.setAdapter(new devices_page.MylistAdapter(this, R.layout.list_item, cur.getDeviceList()));}
        myDialog = new Dialog(this);

    }


    public void logoutAction() {
        Intent logoutIntent = new Intent(devices_page.this,
                MainActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        devices_page.this.startActivity(logoutIntent);
        finish();
    }

    //handles the inflation of menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accountmenu, menu);
        return true;
    }

    //handles the menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MylistAdapter extends ArrayAdapter<Device> {
        private int layout;
        private List<Device> devicesList;
        public MylistAdapter(@NonNull Context context, int resource, @NonNull List<Device> device) {
            super(context, resource, device);
            this.devicesList = device;
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            databaseLockStates = FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices");

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                viewHolder.status = (TextView) convertView.findViewById(R.id.list_item_status);
                final Device device =  devicesList.get(position);
                viewHolder.name.setText(device.getName());
                viewHolder.lockButton = (ImageButton) convertView.findViewById(R.id.list_item_button);
                viewHolder.lockButton2 = (ImageButton) convertView.findViewById(R.id.list_item_button2);

                if (device.getLockStage()) {
                    viewHolder.lockButton.setImageResource(R.drawable.ic_locked_state);
                    viewHolder.status.setText(getResources().getString(R.string.locked));
                } else {
                    viewHolder.lockButton.setImageResource(R.drawable.ic_unlocked_state);
                    viewHolder.status.setText(getResources().getString(R.string.unlocked));
                }

                viewHolder.lockButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewDialog mDialog = new ViewDialog();
                        mDialog.showDialog(devices_page.this, device, viewHolder.lockButton, viewHolder.status);
                    }
                });

                viewHolder.lockButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent editIntent = new Intent(devices_page.this,
                                Edit_Device_Page.class);
                        editIntent.putExtra("user data", cur);
                        //pass along device name to edit_device_page
                        editIntent.putExtra("device name", viewHolder.name.getText().toString());
                        devices_page.this.startActivity(editIntent);
                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
            viewHolder = (ViewHolder)convertView.getTag();
            }
            return convertView;
        }
    }

    //class containing all the elements inside a listview item for devices
    public class ViewHolder {
        TextView name;
        TextView status;
        ImageButton lockButton;
        ImageButton lockButton2;
    }

    //class that pops up a custom dialog menu for toggling lock status
    public class ViewDialog {

        public void showDialog(Activity activity, final Device d, final ImageButton b, final TextView s){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.popup);

            //buttons for no and yes options in dialog menu
            Button noBtn = (Button) dialog.findViewById(R.id.no);
            Button yesBtn = (Button) dialog.findViewById(R.id.yes);

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (d.getLockStage()) {
                        d.setLockStage(false);
                        databaseLockStates.child(d.getId()).child("lockStage").setValue(false);
                        b.setImageResource(R.drawable.ic_unlocked_state);
                        s.setText(getResources().getString(R.string.unlocked));
                        dialog.dismiss();
                    } else {
                        d.setLockStage(true);
                        databaseLockStates.child(d.getId()).child("lockStage").setValue(true);
                        b.setImageResource(R.drawable.ic_locked_state);
                        s.setText(getResources().getString(R.string.locked));
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();

        }
    }

    private void pairedDevicesList() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
    }
}
