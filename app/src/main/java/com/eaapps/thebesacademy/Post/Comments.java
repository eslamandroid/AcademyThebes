package com.eaapps.thebesacademy.Post;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslamandroid on 3/15/18.
 */

public class Comments {
    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String TIME_COMMENT = "time_comment";
    private static final String LIKE = "like";

    String id;
    String text;
    long time_comment;
    int like;

    public Comments() {
    }

    public Comments(String id, String text, long time_comment, int like) {
        this.id = id;
        this.text = text;
        this.time_comment = time_comment;
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime_comment() {
        return time_comment;
    }

    public void setTime_comment(long time_comment) {
        this.time_comment = time_comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Map<String, Object> addComment() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, this.id);
        map.put(TEXT, this.text);
        map.put(TIME_COMMENT, this.time_comment);
        return map;
    }

    public void makeLike(DatabaseReference dataLike) {
        dataLike.child(LIKE).setValue(this.like);
    }
}
