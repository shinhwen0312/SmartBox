package com.example.benyu.smartbox;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
    private CheckBox checkBox;
    private Button button;
    private TextView forget;
    private TextView newAccount;
    private TextView idText;
    private TextView passwordText;
    private account current;
    private int counter = 3;

    DatabaseReference databaseHosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHosts = FirebaseDatabase.getInstance().getReference("path");

        databaseHosts.addValueEventListener(new ValueEventListener() {
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
        Password = (EditText) findViewById(R.id.Password);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        button = (Button) findViewById(R.id.button);
        forget = (TextView) findViewById(R.id.forget);
        newAccount = (TextView) findViewById(R.id.newAccount);
        idText = (TextView) findViewById(R.id.text_id);
        passwordText = (TextView) findViewById(R.id.text_password);

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          final Control model = Control.getInstance();
                                          List<account> newList = model.getAccountList();

                                          boolean check = false;
                                          for (account c : newList) {    //checking username and password stored in userdata
                                              if ((c.getName().equals(Id.getText().toString())) &&
                                                      (c.getPassword().equals(Password.getText().toString()))) {
                                                  check = true;
                                                  current = c;
                                              }
                                          }
                                          if (check) {  // if true, move to logout page
                                              //this intent changes page from this page to LogoutActivity page
                                              Intent logIntent = new Intent(MainActivity.this,
                                                      devices_page.class);
                                              logIntent.putExtra("user data", current);
                                              MainActivity.this.startActivity(logIntent);
                                          } else {  // toast is just pop-up msg
                                              Toast.makeText(MainActivity.this,
                                                      "Username and password is NOT correct",
                                                      Toast.LENGTH_SHORT).show();
//                                              counter--;
//                                              if (counter == 0) {   //setting counters for password tries
//                                                  button.setEnabled(false);
//                                              }
                                          }
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
