package com.example.benyu.smartbox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.util.TimeZone;

//import java.util.Date;

public class New_user_page extends AppCompatActivity {
    private account cur;
    private Device cur2;

    private Button add;
    private EditText personName;
    private EditText pin;

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
        final Device deviceCurrent = getIntent().getParcelableExtra("device data");
        cur = model.updateAccount(current);
        cur2 = deviceCurrent;

        personName = (EditText) findViewById(R.id.person_name);
        pin = (EditText) findViewById(R.id.pin);

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String person_name = personName.getText().toString();
                int pinNumber = Integer.parseInt(pin.getText().toString());
                Date start = start_date.getDate();
                Date end = end_date.getDate();
                Time startTime = start_time.getTime();
                Time endTime = end_time.getTime();
                User newUser = new User(person_name, pinNumber, start, end, startTime, endTime);
                deviceCurrent.addUser(newUser);
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
        private int _birthYear;
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

            DatePickerDialog dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            _editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
        }

        public Date getDate() {
            Calendar cal = Calendar.getInstance();
            cal.set(_birthYear, _month, _day);
            Date date = new Date(_birthYear, _month, _day);
            return date;
        }
    }

    /*  Handles the start and end date pickers.
     *
     *  https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
     */
    public class TimeChooser  implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
        EditText _editText;
        private int _hourOfDay;
        private int _minute;
        private Context _context;

        public TimeChooser(Context context, int editTextViewID)
        {
            Activity act = (Activity)context;
            this._editText = (EditText)act.findViewById(editTextViewID);
            this._editText.setOnClickListener(this);
            this._context = context;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourOfDay = _hourOfDay;
            minute = _minute;
            updateDisplay();
        }

        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(_context, this, hour, minute, false);
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            _editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_hourOfDay).append(":").append(_minute).append(" "));
        }

        public Time getTime() {
            Time current = new Time(_hourOfDay, _minute, 0);
            return current;
        }
    }

}
