package com.example.benyu.smartbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Control {

    private static final Control _instance = new Control();

    public static Control getInstance() { return _instance; }

    private final List<Device> deviceList;
    private final List<User> userList;
    private final List<account> accountList;

    private Control() {
        userList = new ArrayList<>();
        deviceList = new ArrayList<>();
        accountList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return Collections.unmodifiableList(userList);
    }
    public List<Device> getDeviceList() {
        return Collections.unmodifiableList(deviceList);
    }
    public List<account> getAccountList() {
        return Collections.unmodifiableList(accountList);
    }

    public List getUserNameList() {
        List<String> nameList = new ArrayList<>();
        for (User c : userList) {
            if (c == null) {
                //nothing
            } else {
                nameList.add(c.getName());
            }
        }
        return nameList;
    }

    public List getDeviceNameList() {
        List<String> nameList = new ArrayList<>();
        for (Device c : deviceList) {
            if (c == null) {
                //nothing
            } else {
                nameList.add(c.getName());
            }
        }
        return nameList;
    }

    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        for (User c : userList ) {
            if (c.equals(user)) return false;
        }
        userList.add(user);
        //database.getReference("userList").setValue(userList);
        return true;
    }

    public boolean addDevice(Device device) {
        if (device == null) {
            return false;
        }
        for (Device c : deviceList ) {
            if (c.equals(device)) return false;
        }

        deviceList.add(device);
        //database.getReference("shelterList").setValue(shelterList);
        return true;
    }

    public boolean addAccount(account account) {
        if (account == null) {
            return false;
        }
        for (account c : accountList ) {
            if (c.equals(account)) return false;
        }
        accountList.add(account);
        //database.getReference("userList").setValue(userList);
        return true;
    }
}
