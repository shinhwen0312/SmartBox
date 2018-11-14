package com.example.benyu.smartbox;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class User {

    private String name;
    private int pin;
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

    public User(String name, int pin, Date startDate, Date endDate, Time startTime, Time endTime) {
        this.name = name;
        this.pin = pin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;

    }
    public String getName() { return name; }
    public int getPin() { return pin; }
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

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    public String toString() {
        return String.format("%s     %d  ",name,pin);
    }

}



