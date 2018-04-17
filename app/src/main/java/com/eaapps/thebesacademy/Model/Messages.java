package com.eaapps.thebesacademy.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class Messages {
    private static final String MESSAGE = "message";
    private static final String TYPE = "type";
    private static final String TIME = "time";
    private static final String SEEN = "seen";
    private static final String FROM = "from";

    private String message, type;
    private long time;
    private boolean seen;

    private String from;

    public Messages() {

    }

    public Messages(String from) {
        this.from = from;
    }

    public Messages(String message, String type, long time, boolean seen) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Map<String, Object> addMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, this.message);
        map.put(TYPE, this.type);
        map.put(SEEN, this.seen);
        map.put(TIME, this.time);
        map.put(FROM, this.from);
        return map;

    }

}
