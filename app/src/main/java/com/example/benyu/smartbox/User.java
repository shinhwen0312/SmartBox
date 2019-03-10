package com.example.benyu.smartbox;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class User implements Parcelable {

    private String name;
    private String pin;
//    private int startMonth;
//    private int startDate;
//    private int startHour;
//    private int startMin;
//    private int endMonth;
//    private int endDate;
//    private int endHour;
//    private int endMin;
    private Date startDate;
    private Date endDate;

    private Time startTime;
    private Time endTime;

    public User(String name, String pin, Date startDate, Date endDate, Time startTime, Time endTime) {
        this.name = name;
        this.pin = pin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;

    }
    public String getName() { return name; }
    public String getPin() { return pin; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }

    public long getRemainDate() {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffDay = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diffDay;
    }
    public Time getRemainTime() {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffMin = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long diffHour = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long diffSec = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Time newTime = new Time((int) diffHour, (int)diffMin, (int)diffSec);
        return newTime;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    public String toString() {
        return String.format("%s     %s  ",name,pin);
    }

    private User(Parcel in) {
        pin = in.readString();
        name = in.readString();
    //    startDate = in.read();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(pin);
    }

    /**
     * allows you to receive the ClassLoader the object is being created in
     */
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}



