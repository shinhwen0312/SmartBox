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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class devices_page extends AppCompatActivity {
    private account cur;
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button logOut;
        ListView list;
        Control model = Control.getInstance();

        final account current = getIntent().getParcelableExtra("user data");
         cur = model.updateAccount(current);
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

                addIntent.putExtra("user data", cur);
                devices_page.this.startActivity(addIntent);
            }
        });
        logOut = (Button) findViewById(R.id.button);
        list = (ListView) findViewById(R.id.list);


        final List<Device> deviceList = current.getDeviceList();


//        ListAdapter DeviceAdapter =
//                new ArrayAdapter(this,android.R.layout.simple_list_item_1,
//                        model.updateAccount(current).getDeviceNameList());
        list.setAdapter(new MylistAdpater(this,R.layout.list_item, model.updateAccount(current).getDeviceList()));
        myDialog = new Dialog(this);

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
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(devices_page.this,
                        MainActivity.class);
                devices_page.this.startActivity(logoutIntent);
            }
        });

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

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                final Device device =  devicesList.get(position);
                viewHolder.name.setText(device.getName());
                viewHolder.lockButton = (ImageButton) convertView.findViewById(R.id.list_item_button);

                if (device.getLockStage()) {
                    viewHolder.lockButton.setImageResource(R.drawable.lock_state);
                } else {
                    viewHolder.lockButton.setImageResource(R.drawable.unlock_state);
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
                                            viewHolder.lockButton.setImageResource(R.drawable.unlock_state);
                                        } else {
                                            device.setLockStage(true);
                                            viewHolder.lockButton.setImageResource(R.drawable.lock_state);
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (device.getLockStage()) {
                                            device.setLockStage(true);
                                            viewHolder.lockButton.setImageResource(R.drawable.lock_state);
                                        } else {
                                            device.setLockStage(false);
                                            viewHolder.lockButton.setImageResource(R.drawable.unlock_state);
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
        ImageButton lockButton;
    }



}
