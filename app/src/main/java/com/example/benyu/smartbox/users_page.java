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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class users_page extends AppCompatActivity {
    private account cur;
    private Device cur2;
    Dialog myDialog;

/*    DatabaseReference databaseLocks;
    DatabaseReference databaseLockStates;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Button logOut;
        ListView list;
        ImageButton add;
        Control model = Control.getInstance();

        account current = getIntent().getParcelableExtra("user data");
        Device deviceCurrent = getIntent().getParcelableExtra("device data");
        cur = model.updateAccount(current);
        cur2 = deviceCurrent;

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
                //newIntent.putExtra("user data", cur);
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
                    viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                    viewHolder.status = (TextView) convertView.findViewById(R.id.list_item_status);
                    final User user =  usersList.get(position);
                    viewHolder.name.setText(user.getName());
/*                    viewHolder.lockButton = (ImageButton) convertView.findViewById(R.id.list_item_button);
                    viewHolder.lockButton2 = (ImageButton) convertView.findViewById(R.id.list_item_button2);*/

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

/*                    viewHolder.lockButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent editIntent = new Intent(users_page.this,
                                    Edit_Device_Page.class);
                            editIntent.putExtra("user data", cur);
                            users_page.this.startActivity(editIntent);
                        }
                    });*/
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
            TextView status;
            //ImageButton lockButton;
            //ImageButton lockButton2;
        }

    }
