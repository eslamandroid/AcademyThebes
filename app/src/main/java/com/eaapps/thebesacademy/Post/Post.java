package com.eaapps.thebesacademy.Post;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eslamandroid on 3/15/18.
 */

public class Post {

    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String URL = "url";
    private static final String LIKE = "like";
    private static final String TIME_POST = "time_post";
    private static final String OWN_POST = "own_post";


    String id;
    String own_post;
    String text;
    List<String> url;
    int like;
    long time_post;
    List<Comments> comments;

    public Post() {
    }

    public Post(String id, String own_post, String text, List<String> url, int like, long time_post, List<Comments> comments) {
        this.id = id;
        this.own_post = own_post;
        this.text = text;
        this.url = url;
        this.like = like;
        this.time_post = time_post;
        this.comments = comments;
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

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public long getTime_post() {
        return time_post;
    }

    public void setTime_post(long time_post) {
        this.time_post = time_post;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public String getOwn_post() {
        return own_post;
    }

    public void setOwn_post(String own_post) {
        this.own_post = own_post;
    }

    public Map<String, Object> publishNews() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, this.id);
        if (this.text != null) {
            map.put(TEXT, this.text);
        }
        if (url != null) {
            map.put(URL, this.url);
        }
        map.put(LIKE, like);
        map.put(TIME_POST, this.time_post);
        map.put(OWN_POST, this.own_post);
        return map;
    }

    public void makeLike(DatabaseReference dataLike, String id) {
        DatabaseReference like=dataLike.child("UserLikes").child(id);
        like.child(id).setValue(true);
    }

}
