package com.example.benyu.smartbox;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class Utility {
    //function to hide keyboard when pressing anywhere other than keyboard
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //searches the devices users to find matching user
    //d         takes the device to search
    //name      user's name to search for
    public static User getUser(Device d, String name) {
        List<User> users = d.getUserList();
        Log.d("TEST", "NAME OF DEVICE FROM INTENT: " + name);

        for (User item : users) {
            if (item.getName().equals(name)) {
                Log.d("TEST", "ITEM FOUND: " + item.toString());
                return item;
            }
        }

        //no device found (error?)
        return null;
    }
}
