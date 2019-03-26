package com.example.benyu.smartbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class devices_page extends AppCompatActivity {
    private account cur;
    private ListView list;
    Dialog myDialog;
    DatabaseReference databaseLocks;
    DatabaseReference databaseLockStates;

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

                list.setAdapter(new MylistAdpater(devices_page.this,R.layout.list_item, deviceList));
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
        if (cur.getDeviceList() != null) { list.setAdapter(new devices_page.MylistAdpater(this, R.layout.list_item, cur.getDeviceList()));}
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

    private class MylistAdpater extends ArrayAdapter<Device> {
        private int layout;
        private List<Device> devicesList;
        public MylistAdpater(@NonNull Context context, int resource, @NonNull List<Device> device) {
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
                      //  Toast.makeText(getContext(),"button clicked",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder altdial = new AlertDialog.Builder(devices_page.this);
                        altdial.setMessage("Are you sure?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (device.getLockStage()) {
                                            device.setLockStage(false);
                                            databaseLockStates.child(device.getId()).child("lockStage").setValue(false);
                                            viewHolder.lockButton.setImageResource(R.drawable.ic_unlocked_state);
                                            viewHolder.status.setText(getResources().getString(R.string.unlocked));
                                        } else {
                                            device.setLockStage(true);
                                            databaseLockStates.child(device.getId()).child("lockStage").setValue(true);
                                            viewHolder.lockButton.setImageResource(R.drawable.ic_locked_state);
                                            viewHolder.status.setText(getResources().getString(R.string.locked));
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (device.getLockStage()) {
                                            device.setLockStage(true);
                                            viewHolder.lockButton.setImageResource(R.drawable.ic_locked_state);
                                            viewHolder.status.setText(getResources().getString(R.string.locked));
                                        } else {
                                            device.setLockStage(false);
                                            viewHolder.lockButton.setImageResource(R.drawable.ic_unlocked_state);
                                            viewHolder.status.setText(getResources().getString(R.string.unlocked));
                                        }
                                    }
                                });
//                        if(device.getLockStage()) {
//                            viewHolder.lockButton.setImageResource(R.drawable.lock_state);
//                            //  device.setLockStage(false);
//                        } else {
//                            viewHolder.lockButton.setImageResource(R.drawable.unlock_state);
//                            //  device.setLockStage(true);
//                        }

                        AlertDialog alert = altdial.create();
                        alert.setTitle("Toggle Lock Status");
                        alert.show();
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

    public class ViewHolder {
        TextView name;
        TextView status;
        ImageButton lockButton;
        ImageButton lockButton2;
    }

}
