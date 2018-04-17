package com.eaapps.thebesacademy.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslamandroid on 3/13/18.
 */

public class ModelMaterials {
    private static final String TEXT = "text";
    String text;

    public ModelMaterials() {
    }

    public ModelMaterials(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> addMaterial() {
        Map<String, Object> map = new HashMap<>();
        map.put(TEXT, text);
        return map;
    }

}
