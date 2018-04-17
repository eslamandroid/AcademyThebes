package com.eaapps.thebesacademy.Material;

/**
 * Created by eslamandroid on 3/13/18.
 */

public class MaterialModel {
    String key;
    String value;

    public MaterialModel() {
    }

    public MaterialModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
