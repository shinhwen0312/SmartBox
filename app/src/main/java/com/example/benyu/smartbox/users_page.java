package com.example.benyu.smartbox;

import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class users_page extends AppCompatActivity {
    private account cur;
    private Device cur2;
    private Device deviceCurrent;
    private ListView list;
    Dialog myDialog;

/*    DatabaseReference databaseLocks;
    DatabaseReference databaseLockStates;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Button logOut;
        ImageButton add;
        Control model = Control.getInstance();

        account current = getIntent().getParcelableExtra("user data");
        String deviceCurrentTest = getIntent().getStringExtra("device data");
        Log.d("TEST", "DEVICE FROM EDIT_DEVICE_CURRENT: " + deviceCurrentTest);
        cur = model.updateAccount(current);
        deviceCurrent = getDevice(current);

        cur2 = deviceCurrent;
        Log.d("TEST", "DEVICE FROM EDIT_DEVICE: " + cur2.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //no title, custom one in XML
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = (ListView) findViewById(R.id.list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent =
                        new Intent(users_page.this, New_user_page.class);
                newIntent.putExtra("user data", cur);
                newIntent.putExtra("device data", cur2.getName());
                users_page.this.startActivity(newIntent);
            }
        });


        final List<User> userList = deviceCurrent.getUserList();


//        ListAdapter DeviceAdapter =
//                new ArrayAdapter(this,android.R.layout.simple_list_item_1,
//                        model.updateAccount(current).getDeviceNameList());
        if (deviceCurrent.getUserList() != null) { list.setAdapter(new MylistAdpater(this, R.layout.list_item_user, deviceCurrent.getUserList()));}
        myDialog = new Dialog(this);
    }

    protected void onStart() {
        super.onStart();
        final List<User> userList = deviceCurrent.getUserList();
        if (deviceCurrent.getUserList() != null) { list.setAdapter(new MylistAdpater(this, R.layout.list_item_user, deviceCurrent.getUserList()));}
        myDialog = new Dialog(this);
    }

        private class MylistAdpater extends ArrayAdapter<User> {
            private int layout;
            private List<User> usersList;
            public MylistAdpater(@NonNull Context context, int resource, @NonNull List<User> user) {
                super(context, resource, user);
                this.usersList = user;
                layout = resource;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                final users_page.ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(layout,parent,false);
                    viewHolder = new ViewHolder();

                    //list item content
                    viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                    viewHolder.start = (TextView) convertView.findViewById(R.id.list_item_start);
                    viewHolder.end = (TextView) convertView.findViewById(R.id.list_item_end);
                    viewHolder.time_start = (TextView) convertView.findViewById(R.id.list_item_starttime);
                    viewHolder.time_end = (TextView) convertView.findViewById(R.id.list_item_endtime);

                    final User user =  usersList.get(position);
                    viewHolder.name.setText(user.getName());
/*                    viewHolder.lockButton = (ImageButton) convertView.findViewById(R.id.list_item_button);*/
                    viewHolder.lockButton2 = (ImageButton) convertView.findViewById(R.id.list_item_button2);

                    viewHolder.start.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(user.getStartDate().getMonth() + 1).append("/").append(user.getStartDate().getDay()).append("/").append(user.getStartDate().getYear()).append(" "));
                    viewHolder.end.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(user.getEndDate().getMonth() + 1).append("/").append(user.getEndDate().getDay()).append("/").append(user.getEndDate().getYear()).append(" "));

                    long hour = TimeUnit.HOURS.convert(user.getStartTime().getTime(), TimeUnit.MILLISECONDS);
                    long minutes = TimeUnit.MINUTES.convert(user.getStartTime().getTime(), TimeUnit.MILLISECONDS);
                    viewHolder.time_start.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(hour).append(":").append(minutes));

/*                    if (user.getLockStage()) {
                        viewHolder.lockButton.setImageResource(R.drawable.ic_locked_state);
                        viewHolder.status.setText(getResources().getString(R.string.locked));
                    } else {
                        viewHolder.lockButton.setImageResource(R.drawable.ic_unlocked_state);
                        viewHolder.status.setText(getResources().getString(R.string.unlocked));
                    }*/

                    /*viewHolder.lockButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  Toast.makeText(getContext(),"button clicked",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder altdial = new AlertDialog.Builder(users_page.this);
                            altdial.setMessage("Are you sure?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (user.getLockStage()) {
                                                user.setLockStage(false);
                                                viewHolder.lockButton.setImageResource(R.drawable.ic_unlocked_state);
                                                viewHolder.status.setText(getResources().getString(R.string.unlocked));
                                            } else {
                                                user.setLockStage(true);
                                                viewHolder.lockButton.setImageResource(R.drawable.ic_locked_state);
                                                viewHolder.status.setText(getResources().getString(R.string.locked));
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (user.getLockStage()) {
                                                user.setLockStage(true);
                                                viewHolder.lockButton.setImageResource(R.drawable.ic_locked_state);
                                                viewHolder.status.setText(getResources().getString(R.string.locked));
                                            } else {
                                                user.setLockStage(false);
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
                    });*/

                    viewHolder.lockButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
/*                            Intent editIntent = new Intent(users_page.this,
                                    Edit_Device_Page.class);
                            editIntent.putExtra("user data", cur);
                            users_page.this.startActivity(editIntent);*/
                        }
                    });
                    convertView.setTag(viewHolder);
                }
                else {
                    viewHolder = (users_page.ViewHolder)convertView.getTag();
                }
                return convertView;
            }
        }

        public class ViewHolder {
            TextView name;
            TextView start;
            TextView end;
            TextView time_start;
            TextView time_end;
            //ImageButton lockButton;
            ImageButton lockButton2;
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
