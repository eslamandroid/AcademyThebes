package com.eaapps.thebesacademy.Material;

/**
 * Created by eslamandroid on 3/14/18.
 */

public class ModelTable {

    String name_material;
    String name_teacher;
    String time;
    String day;


    public ModelTable() {
    }

    public ModelTable(String name_material, String name_teacher, String time, String day) {
        this.name_material = name_material;
        this.name_teacher = name_teacher;
        this.time = time;
        this.day = day;
    }

    public String getName_material() {
        return name_material;
    }

    public void setName_material(String name_material) {
        this.name_material = name_material;
    }

    public String getName_teacher() {
        return name_teacher;
    }

    public void setName_teacher(String name_teacher) {
        this.name_teacher = name_teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
