package com.example.benyu.smartbox;

public class Device {
    private int id;
    private String name;
    private String location;
    private boolean lockStage;

    public Device(String name, String location, int id) {
        this.name = name;
        this.location = location;
        this.id = id;
        lockStage = true;
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
}
