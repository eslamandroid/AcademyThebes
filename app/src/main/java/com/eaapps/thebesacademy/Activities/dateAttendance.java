package com.eaapps.thebesacademy.Activities;

/**
 * Created by eslamandroid on 4/11/18.
 */

public class dateAttendance {
    String id;
    long timestamp;

    public dateAttendance() {
    }

    public dateAttendance(String id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
