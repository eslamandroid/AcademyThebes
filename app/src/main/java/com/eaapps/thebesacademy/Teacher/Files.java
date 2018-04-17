package com.eaapps.thebesacademy.Teacher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslamandroid on 4/10/18.
 */

public class Files {

    static final String DESCRIPTION = "description";
    static final String URL = "url";
    static final String TITLE = "title";
    static final String ID = "id";
    static final String TYPE_FILE = "type_file";
    static final String LEVEL = "level";
    static final String SECTION = "section";


    String id;
    String title;
    String description;
    String url;
    String type_file;
    String level;
    String section;

    public Files() {
    }

    public Files(String id, String title, String description, String url, String type_file, String level, String section) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.type_file = type_file;
        this.level = level;
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType_file() {
        return type_file;
    }

    public void setType_file(String type_file) {
        this.type_file = type_file;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Map<String, Object> addFile() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, id);
        map.put(TITLE, title);
        map.put(DESCRIPTION, description);
        map.put(URL, url);
        map.put(TYPE_FILE, type_file);
        map.put(SECTION, section);
        map.put(LEVEL, level);
        return map;
    }
}
