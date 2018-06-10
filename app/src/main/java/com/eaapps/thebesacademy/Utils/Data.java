package com.eaapps.thebesacademy.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
    String name;
    String message;


    public Data() {
    }

    public Data(String name, String message) {
        this.name = name;
        this.message = message;
    }

    protected Data(Parcel in) {
        name = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
