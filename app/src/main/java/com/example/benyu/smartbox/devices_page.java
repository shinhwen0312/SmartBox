package com.example.benyu.smartbox;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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
    boolean firstSet = false;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket btSocket = null;
    Boolean BluetoothConnected = false;
    ProgressDialog progress;
    Device firstDevice;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private InputStream inputStream;
    Handler handler = new Handler();
    boolean kill_worker = false;
    DatabaseReference inThreadRef;
    Device d;


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
                firstSet = false;
                //iterating through all the objects
                cur.getDeviceList().clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting object
                    Device a = postSnapshot.getValue(Device.class);
                    //adding object to the list
                    cur.addDevice(a);
                    if(!firstSet){
                        firstDevice = a;
                        firstSet = true;
                    }
                    Log.d("check", "checking how many times");
                }

                final List<Device> deviceList = cur.getDeviceList();

                list.setAdapter(new MylistAdapter(devices_page.this, R.layout.list_item, deviceList));
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

        //bluetooth connect
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
                            new ConnectBT().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected void onStart() {
        super.onStart();
        if (cur.getDeviceList() != null) {
            list.setAdapter(new devices_page.MylistAdapter(this, R.layout.list_item, cur.getDeviceList()));
        }
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
                convertView = inflater.inflate(layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                viewHolder.status = (TextView) convertView.findViewById(R.id.list_item_status);
                final Device device = devicesList.get(position);
                d = devicesList.get(devicesList.size() - 1); //used for bluetooth
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
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
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

        public void showDialog(Activity activity, final Device d, final ImageButton b, final TextView s) {
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
                    //locked->unlocked
                    if (d.getLockStage()) {
                        if (BluetoothConnected) {
                            if (btSocket != null && d.getName().equals("test")) {
                                try {
                                    btSocket.getOutputStream().write("0".getBytes());
                                    d.setLockStage(false);
                                    databaseLockStates.child(d.getId()).child("lockStage").setValue(false);
                                    b.setImageResource(R.drawable.ic_unlocked_state);
                                    s.setText(getResources().getString(R.string.unlocked));
                                    dialog.dismiss();
                                } catch (IOException e) {
                                    msg("Error");
                                    dialog.dismiss();
                                }
                            } else {
                                dialog.dismiss();
                                msg("Please connect to bluetooth.");
                            }
                        } else {
                            dialog.dismiss();
                            msg("Please connect to bluetooth.");
                        }
                        //unlocked->locked
                    } else {
                        if (BluetoothConnected) {
                            if (btSocket != null  && d.getName().equals("test")) {
                                try {
                                    btSocket.getOutputStream().write("1".getBytes());
                                    d.setLockStage(true);
                                    databaseLockStates.child(d.getId()).child("lockStage").setValue(true);
                                    b.setImageResource(R.drawable.ic_locked_state);
                                    s.setText(getResources().getString(R.string.locked));
                                    dialog.dismiss();
                                } catch (IOException e) {
                                    msg("Error");
                                    dialog.dismiss();
                                }
                            } else {
                                dialog.dismiss();
                                msg("Please connect to bluetooth.");
                            }
                        } else {
                            dialog.dismiss();
                            msg("Please connect to bluetooth.");
                        }
                    }
                }
            });
            dialog.show();
        }
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(devices_page.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !BluetoothConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(btAddr);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection

                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            } else {
                BluetoothConnected = true;
            }
            progress.dismiss();
            startListeningThread();
        }
    }


    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    void startListeningThread()  {
        try {
            inputStream = btSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread worker = new Thread(new Runnable()   {
            @Override
            public void run()   {
                while(!Thread.currentThread().isInterrupted() && !kill_worker) {
                    try {
                        //Read the incoming response
                        if (inputStream.available() > 0) {
                            try {
                                Thread.currentThread().sleep(2000);
                            } catch (InterruptedException e) {

                            }
                        }
                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            Log.d("****BYTE COUNT: ", Integer.toString(byteCount));
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);

                            final String data = new String(rawBytes,"UTF-8");

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("********DATA: ", data);
                                    FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices").child(d.getName()).child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Boolean pinEquals = false;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String pin = (String) snapshot.child("pin").getValue();
                                                Log.d("*******PIN: ", pin);
                                                if (pin.equals(data)) {
                                                    pinEquals = true;
                                                    FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices").child(d.getName()).child("accessedby").child((String)snapshot.child("name").getValue()).setValue(snapshot.child("name").getValue());
                                                    FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices").child(d.getName()).child("accessedby").child((String)snapshot.child("name").getValue()).child("accesstime").setValue(Calendar.getInstance().getTime());
                                                    try {
                                                        btSocket.getOutputStream().write("0".getBytes());
                                                    } catch (IOException e) {

                                                    }
                                                }
                                            }
                                            if (!pinEquals) {
                                                try {
                                                    btSocket.getOutputStream().write("2".getBytes());
                                                } catch (IOException e) {

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    } catch (IOException e) {
                        kill_worker = true;
                    }
                }
            }
        });

        worker.start();
    }

}