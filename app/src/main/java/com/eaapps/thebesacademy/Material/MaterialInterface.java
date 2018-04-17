package com.eaapps.thebesacademy.Material;

import android.widget.EditText;

import java.util.List;

/**
 * Created by eslamandroid on 3/13/18.
 */

public interface MaterialInterface {
    void onAddTutorial(EditText editTutorial, List<MaterialModel> modelList, int position);

    public interface CallBackTable {

        void onInfoTable(List<ModelTable> list, int position);
    }
}
