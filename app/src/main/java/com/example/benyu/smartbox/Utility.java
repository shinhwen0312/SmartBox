package com.example.benyu.smartbox;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utility {
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
