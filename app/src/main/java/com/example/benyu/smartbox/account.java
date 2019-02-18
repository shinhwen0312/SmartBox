package com.example.benyu.smartbox;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class account implements Parcelable {
    private String name;
    private String password;
    private String email;

    private List<Device> devices;

    public account(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;

        devices = new ArrayList<>();
    }

    public account() { this("Enter a name", "Enter a password", "Enter email");
    }

    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public List<Device> getDeviceList() {
        return devices;
    }


    public List getDeviceNameList() {
        List<String> nameList = new ArrayList<>();
        for (Device c : devices) {
            if (c == null) {
                //nothing
            } else {
                nameList.add(c.getName());
            }
        }
        return nameList;
    }



    public boolean addDevice(Device device) {
        if (device == null) {
            return false;
        }
        for (Device c : devices ) {
            if (c.equals(device)) return false;
        }

        devices.add(device);
        return true;
    }

    public void setName(String name) {
        if (name == null) {
        throw new java.lang.IllegalArgumentException("username can't be null");
    }
        this.name = name;
    }
    public void setPassword(String password) {
        if (password == null) {
            throw new java.lang.IllegalArgumentException("username can't be null");
        }
        this.password = password;
    }
    public void setEmail(String email) {
        if (email == null) {
            throw new java.lang.IllegalArgumentException("username can't be null");
        }
        this.email = email;
    }



    public String toString() {
        return name + "  " + password + "   " + email;
    }

    private account(Parcel in) {
        name = in.readString();
        password = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(email);
    }

    /**
     * allows you to receive the ClassLoader the object is being created in
     */
    public static final Parcelable.Creator<account> CREATOR
            = new Parcelable.Creator<account>() {
        @Override
        public account createFromParcel(Parcel in) {
            return new account(in);
        }

        @Override
        public account[] newArray(int size) {
            return new account[size];
        }
    };

}
