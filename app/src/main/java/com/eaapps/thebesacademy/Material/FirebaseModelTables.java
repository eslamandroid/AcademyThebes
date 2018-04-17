package com.eaapps.thebesacademy.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eslamandroid on 3/14/18.
 */

public class FirebaseModelTables {


    private static final String ID = "id";
    private static final String MASTER = "master";
    private static final String LEVEL = "level";
    private static final String SEMESTER = "semester";
    private static final String TABLESDETAILS = "tablesDetails";

    String id;
    String master;
    String level;
    String semester;
    List<ModelTable> tablesDetails;


    public FirebaseModelTables() {
    }

    public FirebaseModelTables(String id, String master, String level, String semester, List<ModelTable> tablesDetails) {
        this.id = id;
        this.master = master;
        this.level = level;
        this.semester = semester;
        this.tablesDetails = tablesDetails;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
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

    public List<ModelTable> getTablesDetails() {
        return tablesDetails;
    }

    public void setTablesDetails(List<ModelTable> tablesDetails) {
        this.tablesDetails = tablesDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> addTable() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, this.id);
        map.put(MASTER, this.master);
        map.put(LEVEL, this.level);
        map.put(SEMESTER, this.semester);
        map.put(TABLESDETAILS, tablesDetails);
        return map;
    }
}
