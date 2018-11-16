package com.example.benyu.smartbox;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Device implements Parcelable {
    private int id;
    private String name;
    private String location;
    private boolean lockStage;
    private List<User> users;

    public Device(String name, String location, int id) {
        this.name = name;
        this.location = location;
        this.id = id;
        lockStage = true;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public int getId() {
        return id;
    }
    public boolean getLockStage() { return lockStage; }
    public List<User> getUserList() {
        return users;
    }

    public List getNameList() {
        List<String> nameList = new ArrayList<>();
        for (User c : users) {
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
        for (User c : users ) {
            if (c.equals(user)) return false;
        }
        users.add(user);
        return true;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setLockStage(boolean lockStage) { this.lockStage = lockStage; }

    public String toString() {
        return String.format("%s",name);
    }

    private Device(Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
     //   lockStage = in.readS();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeInt(id);
    }

    /**
     * allows you to receive the ClassLoader the object is being created in
     */
    public static final Parcelable.Creator<Device> CREATOR
            = new Parcelable.Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
