package com.example.benyu.smartbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText Id;
    private EditText Password;
    private Switch rememberSwitch;
    private Button button;
    private TextView forget;
    private TextView newAccount;
    //private TextView idText;
    //private TextView passwordText;
    private account current;
    private int counter = 3;
    boolean check;
    account c;
    String passHolder;
    String userHolder;

    DatabaseReference databaseHosts;
    DatabaseReference databaseUsers;
    DatabaseReference databaseLocks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_main);

        databaseHosts = FirebaseDatabase.getInstance().getReference("path");

        databaseHosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous object list
                Control.getInstance().getAccountList().clear();

                //iterating through all the objects
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting object
                    account a = postSnapshot.getValue(account.class);
                    //adding object to the list
                    Control.getInstance().addAccount(a);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Control model = Control.getInstance();
        Id = (EditText) findViewById(R.id.Id);
        //hides keyboard when clicking on background
        Id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Utility.hideKeyboard(v);
                }
            }
        });
        Password = (EditText) findViewById(R.id.Password);
        //see above comment
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Utility.hideKeyboard(v);
                }
            }
        });
        //checkBox = (CheckBox) findViewById(R.id.checkBox);
        rememberSwitch = (Switch) findViewById(R.id.rememberSwitch);
        button = (Button) findViewById(R.id.button);
        forget = (TextView) findViewById(R.id.forget);
        newAccount = (TextView) findViewById(R.id.newAccount);
        //idText = (TextView) findViewById(R.id.text_id);
        //passwordText = (TextView) findViewById(R.id.text_password);

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent =
                        new Intent(MainActivity.this, Create_Account_page.class);
                MainActivity.this.startActivity(newIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
                                          //.child(Id.getText().toString())
                                          databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot dataSnapshot) {


                                                  if (dataSnapshot.exists() && dataSnapshot.child(Id.getText().toString()).exists()) {
                                                      /*Log.d("check",dataSnapshot.toString());
                                                      Log.d("check",Id.getText().toString());
                                                      Log.d("check",dataSnapshot.child(Id.getText().toString()).toString());
                                                      Log.d("check",dataSnapshot.child(Id.getText().toString()).child("password").getValue().toString());
                                                      Log.d("check",Password.getText().toString());*/


                                                      if(dataSnapshot.child(Id.getText().toString()).exists()) {
                                                          passHolder = dataSnapshot.child(Id.getText().toString()).child("password").getValue().toString();
                                                      }

                                                      //iterating through all the objects
                                                      if (passHolder.equals(Password.getText().toString())) {




                                                          check = true;
                                                          final Control model = Control.getInstance();
                                                          List<account> newList = model.getAccountList();
                                                          c = new account(Id.getText().toString(), passHolder, dataSnapshot.child(Id.getText().toString()).child("email").getValue().toString());

                                                          /*for (account c : newList) {    //checking username and password stored in userdata
                                                              if ((c.getName().equals(Id.getText().toString())) &&
                                                                      (c.getPassword().equals(Password.getText().toString()))) {
                                                                  check = true;
                                                                  current = c;
                                                              }
                                                          }*/
                                                          databaseLocks = FirebaseDatabase.getInstance().getReference("users").child(c.getName()).child("devices");

                                                          databaseLocks.addListenerForSingleValueEvent(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(DataSnapshot dataSnapshot) {

                                                                  //iterating through all the objects
                                                                  c.getDeviceList().clear();
                                                                  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                      //getting object
                                                                      Device a = postSnapshot.getValue(Device.class);
                                                                      //adding object to the list
                                                                      c.addDevice(a);
                                                                      Log.d("check","checking how many times");

                                                                  }
                                                              }
                                                              @Override
                                                              public void onCancelled(DatabaseError databaseError) {

                                                              }
                                                          });

                                                          model.addAccount(c);
                                                          Intent logIntent = new Intent(MainActivity.this,
                                                                  devices_page.class);
                                                          logIntent.putExtra("user data", c);
                                                          MainActivity.this.startActivity(logIntent);



                                                      } else{
                                                            Toast.makeText(MainActivity.this,
                                                                  "Incorrect Username and/or Password.",
                                                                  Toast.LENGTH_SHORT).show();
                                                          System.out.println(passHolder + " - " + Password.getText().toString());
                                                      }
                                                  }
                                              }
                                              @Override
                                              public void onCancelled(DatabaseError databaseError) {

                                              }
                                          });

                                          if(!check) {
                                              /*Toast.makeText(MainActivity.this,
                                                      "Incorrect Username and/or Password.",
                                                      Toast.LENGTH_SHORT).show();*/
                                          }
//                                              counter--;
//                                              if (counter == 0) {   //setting counters for password tries
//                                                  button.setEnabled(false);
//                                              }

                                      }
                                  }
        );
//        newAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent newIntent =
//                        new Intent(MainActivity.this, Create_Account_page.class);
//                MainActivity.this.startActivity(newIntent);
//            }
//        });

    }

    public void perform_click(View view) {
        newAccount = (TextView) findViewById(R.id.newAccount);
        Intent newIntent =
                new Intent(MainActivity.this, Create_Account_page.class);
        MainActivity.this.startActivity(newIntent);
    }

}