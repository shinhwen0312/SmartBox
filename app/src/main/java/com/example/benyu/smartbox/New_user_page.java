package com.example.benyu.smartbox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
//import java.util.Date;



public class New_user_page extends AppCompatActivity {
    private account cur;
    private Device cur2;
    DatabaseReference databaseUsers;
    private Button add;
    private EditText personName;
    private EditText pin;
    private ImageButton generate;
    private String pinNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final MyEditTextDatePicker start_date = new MyEditTextDatePicker(this, R.id.start_date);
        final MyEditTextDatePicker end_date = new MyEditTextDatePicker(this, R.id.end_date);

        final TimeChooser start_time = new TimeChooser(this, R.id.start_time);
        final TimeChooser end_time = new TimeChooser(this, R.id.end_time);

        Control model = Control.getInstance();

        account current = getIntent().getParcelableExtra("user data");
        String deviceCurrentTest = getIntent().getStringExtra("device data");
        cur = model.updateAccount(current);
        final Device deviceCurrent = getDevice(current);
        cur2 = deviceCurrent;

        personName = (EditText) findViewById(R.id.person_name);
        personName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Utility.hideKeyboard(v);
                }
            }
        });
        pin = (EditText) findViewById(R.id.pin);
        pin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Utility.hideKeyboard(v);
                }
            }
        });
        generate = (ImageButton) findViewById(R.id.generate);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random ran = new Random();
                pinNumber = String.valueOf(100000 + ran.nextInt(899999));
                pin.setText(pinNumber);
            }
        });
        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(cur.getName()).child("devices")
                .child(cur2.getId()).child("Users");



        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String person_name = personName.getText().toString();
                if (pinNumber == null) {
                    pinNumber = pin.getText().toString();
                }
                Date start = start_date.getDate();
                Date end = end_date.getDate();
                Time startTime = start_time.getTime();
                Time endTime = end_time.getTime();
                User newUser = new User(person_name, pinNumber, start, end, startTime, endTime);
                deviceCurrent.addUser(newUser);
                Control model = Control.getInstance();
                cur = model.updateAccount(cur);
                addUser(newUser);
                finish();

/*                Control model = Control.getInstance();
                Intent addIntent = new Intent(New_user_page.this,
                        users_page.class);
                addIntent.putExtra("user data", model.updateAccount(cur));
                addIntent.putExtra("device data", deviceCurrent.getName());
                New_user_page.this.startActivity(addIntent);*/
            }
        });
    }


    /*  Handles the start and end date pickers.
     *
     *  https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
     */
    public class MyEditTextDatePicker  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        EditText _editText;
        private int _day;
        private int _month;
        private int _birthYear = -1;
        private Context _context;

        public MyEditTextDatePicker(Context context, int editTextViewID)
        {
            Activity act = (Activity)context;
            this._editText = (EditText)act.findViewById(editTextViewID);
            this._editText.setOnClickListener(this);
            this._context = context;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _birthYear = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }

        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog dialog;
            if(_birthYear == -1) {
                dialog = new DatePickerDialog(_context, this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            } else {
                dialog = new DatePickerDialog(_context, this,
                        _birthYear, _month,
                        _day);
            }
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            _editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_month + 1).append("/").append(_day).append("/").append(_birthYear).append(" "));
        }

        public Date getDate() {
            Calendar cal = Calendar.getInstance();
            cal.set(_birthYear, _month, _day);
            Log.d("Test", "Time during new: " + _birthYear);
            Date date = new Date(_birthYear - 1900, _month, _day);
            return date;
        }
    }

    /*  Handles the start and end date pickers.
     *
     *  https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
     */
    public class TimeChooser  implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
        ImageButton _editText;
        private int _hourOfDay = -1;
        private int _minute;
        private Context _context;

        public TimeChooser(Context context, int editTextViewID)
        {
            Activity act = (Activity)context;
            this._editText = (ImageButton)act.findViewById(editTextViewID);
            this._editText.setOnClickListener(this);
            this._context = context;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            _hourOfDay = hourOfDay;
            _minute = minute;
            //updateDisplay();
            Log.d("TEST", "MINUTE: " + _minute + ", HOUR: " + _hourOfDay);
        }

        @Override
        public void onClick(View v) {
            int hour;
            int minute;
            if (_hourOfDay == -1) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            } else {
                hour = _hourOfDay;
                minute = _minute;
            }

            TimePickerDialog dialog = new TimePickerDialog(_context, this, hour, minute, false);
            dialog.show();

        }

        // updates the date in the birth date EditText
/*        private void updateDisplay() {

            _editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_hourOfDay).append(":").append(_minute).append(" "));
        }*/

        public Time getTime() {
            Time current = new Time(_hourOfDay, _minute, 0);
            Log.d("TEST", "TIME: " + current.getTime());
            return current;
        }
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
    public void addUser(User user) {
        databaseUsers.child(user.getName()).child("name").setValue(user.getName());
        databaseUsers.child(user.getName()).child("pin").setValue(user.getPin());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String day = dateFormat.format(user.getStartDate());
        Log.d("TEST", "NAME OF DEVICE FROM INTENT: " + day);
        databaseUsers.child(user.getName()).child("startDate").setValue(day);
        day = dateFormat.format(user.getEndDate());
        databaseUsers.child(user.getName()).child("endDate").setValue(day);
        databaseUsers.child(user.getName()).child("startTime").setValue(user.getStartTime().toString());
        databaseUsers.child(user.getName()).child("endTime").setValue(user.getEndTime().toString());


    }

}
