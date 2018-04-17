package com.eaapps.thebesacademy.Activities;

import java.util.Map;

/**
 * Created by eslamandroid on 4/11/18.
 */

public class Attendances {

    String id;
    String name_doctor;
    String name_student;
    String id_academy;
    String code_academy;
    Map<String, Object> dateAttendance;

    public Attendances() {
    }

    public Attendances(String id, String name_doctor, String name_student, String id_academy, String code_academy, Map<String, Object> dateAttendance) {
        this.id = id;
        this.name_doctor = name_doctor;
        this.name_student = name_student;
        this.id_academy = id_academy;
        this.code_academy = code_academy;
        this.dateAttendance = dateAttendance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_doctor() {
        return name_doctor;
    }

    public void setName_doctor(String name_doctor) {
        this.name_doctor = name_doctor;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public Map<String, Object> getDateAttendance() {
        return dateAttendance;
    }

    public void setDateAttendance(Map<String, Object> dateAttendance) {
        this.dateAttendance = dateAttendance;
    }

    public String getCode_academy() {
        return code_academy;
    }

    public void setCode_academy(String code_academy) {
        this.code_academy = code_academy;
    }

    public String getId_academy() {
        return id_academy;
    }

    public void setId_academy(String id_academy) {
        this.id_academy = id_academy;
    }
}
