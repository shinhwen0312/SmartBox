package com.example.benyu.smartbox;
import android.os.Parcel;
import android.os.Parcelable;


public class History implements Parcelable{
    private String userName;
    private String accessDate;
    private String accessTime;

    public History(String userName, String accessDate, String accessTime) {
        this.userName = userName;
        this.accessDate = accessDate;
        this.accessTime = accessTime;
    }


    public String getUserName() {
        return userName;
    }

    public String getAccessDate() {
        return accessDate;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAccessDate(String accessDate) {
        this.accessDate = accessDate;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String toString() {
        return userName + "   " + accessDate + "   " + accessTime;
    }
    private History(Parcel in) {
        userName = in.readString();
        accessDate = in.readString();
        accessTime = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(accessDate);
        dest.writeString(accessTime);
    }

    /**
     * allows you to receive the ClassLoader the object is being created in
     */
    public static final Parcelable.Creator<History> CREATOR
            = new Parcelable.Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
