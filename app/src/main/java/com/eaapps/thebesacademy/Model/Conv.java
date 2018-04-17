package com.eaapps.thebesacademy.Model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AkshayeJH on 30/11/17.
 */

public class Conv {

    private static String ID = "id";
    private static String SEEN = "seen";
    private static String TIMESTAMP = "timestamp";

    public String id;
    public boolean seen;
    public long timestamp;

    public Conv() {

    }

    public Conv(String id, boolean seen, long timestamp) {
        this.id = id;
        this.seen = seen;
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> addStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, this.id);
        map.put(SEEN, this.seen);
        map.put(TIMESTAMP, this.timestamp);
        return map;
    }

    public void updateChild(DatabaseReference ref) {
        ref.child(SEEN).setValue(this.seen);
        ref.child(TIMESTAMP).setValue(this.timestamp);
    }
}
