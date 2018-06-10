package com.eaapps.thebesacademy.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslamandroid on 2/21/18.
 */

public class Profile implements Parcelable {
    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
    private static String ID = "id";
    private static String NAME = "name";
    private static String EMAIL = "email";
    private static String USERID = "userId";
    private static String PHONE = "phone";
    private static String MASTER = "master";
    private static String SECTION = "section";
    private static String URL = "imageUrl";
    private static String ONLINE = "online";
    private static String TYPE_ACCOUNT = "type_account";
    private static String SEMESTER = "semester";
    private static String LEVEL = "level";
    private static String USERCODE = "userCode";


    private String id;
    private String name;
    private String email;
    private String userId;
    private String phone;
    private String master;
    private String section;
    private String imageUrl;
    private String online;
    private String type_account;
    private String level;
    private String semester;
    private String userCode;


    public Profile() {
    }

    public Profile(String id, String name, String email, String userId, String phone, String master, String section, String imageUrl, String online, String type_account, String level, String semester, String userCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.phone = phone;
        this.master = master;
        this.section = section;
        this.imageUrl = imageUrl;
        this.online = online;
        this.type_account = type_account;
        this.level = level;
        this.semester = semester;
        this.userCode = userCode;
    }

    protected Profile(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        userId = in.readString();
        phone = in.readString();
        master = in.readString();
        section = in.readString();
        imageUrl = in.readString();
        online = in.readString();
        type_account = in.readString();
        level = in.readString();
        semester = in.readString();
        userCode = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getType_account() {
        return type_account;
    }

    public void setType_account(String type_account) {
        this.type_account = type_account;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Map<String, Object> addProfile() {
        Map<String, Object> profile = new HashMap<>();
        profile.put(ID, this.id);
        profile.put(NAME, this.name);
        profile.put(EMAIL, this.email);
        profile.put(USERID, this.userId);
        profile.put(PHONE, this.phone);
        profile.put(MASTER, this.master);
        profile.put(SECTION, this.section);
        profile.put(ONLINE, this.online);
        profile.put(TYPE_ACCOUNT, this.type_account);
        profile.put(USERCODE, userCode);


        return profile;
    }

    public void editProfile(DatabaseReference databaseReference) {

        if (name != null) {
            databaseReference.child(NAME).setValue(name);
        }

        if (phone != null) {
            databaseReference.child(PHONE).setValue(phone);
        }

        if (master != null) {
            databaseReference.child(MASTER).setValue(master);
        }


        if (section != null) {
            databaseReference.child(SECTION).setValue(section);
        }

        if (level != null) {
            databaseReference.child(LEVEL).setValue(level);
        }

        if (semester != null) {
            databaseReference.child(SEMESTER).setValue(semester);
        }
        if (imageUrl != null) {
            databaseReference.child(URL).setValue(imageUrl);
        }


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(userId);
        dest.writeString(phone);
        dest.writeString(master);
        dest.writeString(section);
        dest.writeString(imageUrl);
        dest.writeString(online);
        dest.writeString(type_account);
        dest.writeString(level);
        dest.writeString(semester);
        dest.writeString(userCode);
    }
}
