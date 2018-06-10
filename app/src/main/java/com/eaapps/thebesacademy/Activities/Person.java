package com.eaapps.thebesacademy.Activities;

/**
 * Created by eslamandroid on 5/26/18.
 */

public class Person {

    String name;
    String tel;
    int img;

    public Person(String name, String tel, int img) {
        this.name = name;
        this.tel = tel;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
