package com.eaapps.thebesacademy.Material;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eaapps.thebesacademy.R;

import java.util.List;

/**
 * Created by eslamandroid on 3/13/18.
 */

public class AdapterMaterial extends BaseAdapter {

    Context context;
    List<MaterialModel> modelList;

    public AdapterMaterial(Context context, List<MaterialModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.edit_tutorial, parent, false);
        EditText editTutorial = convertView.findViewById(R.id.editTutorial);
        ImageButton btnMinus = convertView.findViewById(R.id.btnMinus);
        editTutorial.setText(modelList.get(position).getValue());


        editTutorial.setCursorVisible(false);


        editTutorial.setOnEditorActionListener((v, actionId, event) -> {
            editTutorial.setCursorVisible(false);
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (in != null) {
                    in.hideSoftInputFromWindow(editTutorial.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return false;
        });


        editTutorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                RegisterMaterial.tutorialInterface.onAddTutorial(editTutorial, modelList, position);
                editTutorial.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editTutorial.getText().toString())) {
                    modelList.set(position, new MaterialModel("tutorial", editTutorial.getText().toString()));
                    RegisterMaterial.tutorialInterface.onAddTutorial(editTutorial, modelList, position);
                } else {
                    modelList.set(position, new MaterialModel("tutorial", editTutorial.getText().toString()));
                    RegisterMaterial.tutorialInterface.onAddTutorial(editTutorial, modelList, position);
                }
                editTutorial.setCursorVisible(false);
            }
        });

        btnMinus.setOnClickListener(v -> {
            modelList.remove(position);
            RegisterMaterial.tutorialInterface.onAddTutorial(editTutorial, modelList, position);
            notifyDataSetChanged();
        });


        return convertView;
    }
}
