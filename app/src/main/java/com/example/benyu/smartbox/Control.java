package com.example.benyu.smartbox;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Control {

    private static final Control _instance = new Control();

    public static Control getInstance() { return _instance; }


    private final List<account> accountList;
    private account currentAccout;

    private Control() {

        accountList = new ArrayList<>();
        loadDummyData();
    }

    private void loadDummyData() {
        accountList.add(new account("test", "1234","test@something.com"));
        accountList.add(new account("ben", "abcd","ben@something.com"));
        Device device = new Device("Device 1","house", "123");
        Device device2 = new Device("Device 2","house", "123");
        Device device3 = new Device("Device 3","house", "123");
        Device device4 = new Device("Device 3","house", "123");
        device2.setLockStage(true);
        device3.setLockStage(true);
        accountList.get(0).addDevice(device);
        accountList.get(0).addDevice(device2);
        accountList.get(0).addDevice(device3);
        accountList.get(0).addDevice(device4);
     //   accountList.get(0).getDeviceList().add(new Device("Device 1","house", 12345));
        Date start = new Date(2018,11,13);
        Date end = new Date(2018,12,10);
        Time startT = new Time(5,12,39);
        Time endT = new Time(8,3,58);
        //accountList.get(0).getDeviceList().get(0).getUserList().add(new User("bob",1234, start, end, startT, endT));
    }

    public List<account> getAccountList() {
        return accountList;
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

    public account getCurrentAccout() { return currentAccout;}

    public void setCurrentAccout(account account) { currentAccout = account; }

    public account updateAccount(final account account) {
        for (account c : accountList) {
            if (c.getName().equals(account.getName())) {
                return c;

            }
        }
        return account;
    }

}
