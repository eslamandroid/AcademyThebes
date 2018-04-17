package com.eaapps.thebesacademy.Model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslamandroid on 3/12/18.
 */

public class Chat {


    private static String ID = "id";
    private static String SEEN = "seen";
    private static String TIMESTAMP = "timestamp";

    String id;
    boolean seen;
    long timestamp;

    public Chat() {

    }

    public Chat(String id, boolean seen, long timestamp) {
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
