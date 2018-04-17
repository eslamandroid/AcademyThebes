package com.eaapps.thebesacademy.Material;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.eaapps.thebesacademy.Activities.Home;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class RegisterMaterial extends AppCompatActivity implements MaterialInterface {


    public static MaterialInterface tutorialInterface;
    DatabaseReference refMaterial;
    Spinner spinnerSections;
    String[] sectionArr = {"Computer Science", "Information Systems", "Accounting", "Business Administration"};
    ListView recycleTutorials;
    FloatingActionButton btnAdd;
    List<MaterialModel> models = new ArrayList<>();
    List<MaterialModel> itemFirebase = new ArrayList<>();
    EditText editText;
    int position;
    AdapterMaterial adapterTutorial;
    RetrieveData<ModelMaterials> modelMaterialsRetrieveData;
    String sections;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tutorial);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        modelMaterialsRetrieveData = new RetrieveData<ModelMaterials>(RegisterMaterial.this) {
        };

        init();

        refMaterial = FirebaseDatabase.getInstance().getReference().child("Materials");


        spinnerSections = findViewById(R.id.spinner);
        spinnerSections.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, sectionArr));
        sections = spinnerSections.getItemAtPosition(0).toString();
        spinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                sections = spinnerSections.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recycleTutorials = findViewById(R.id.recycleTutorialAdd);
        adapterTutorial = new AdapterMaterial(RegisterMaterial.this, models);
        recycleTutorials.setAdapter(adapterTutorial);
        tutorialInterface = this;

        getMaterial();

        btnAdd = findViewById(R.id.floatingActionButton);
        btnAdd.setOnClickListener(v -> {
            Toast.makeText(RegisterMaterial.this, "eslam", Toast.LENGTH_SHORT).show();
            models.add(new MaterialModel("tutorial", ""));
            adapterTutorial.notifyDataSetChanged();
        });


    }

    private void addMaterial(String section, String text) {
        DatabaseReference container = refMaterial.child(section).push();
        ModelMaterials materials = new ModelMaterials();
        materials.setText(text);
        container.setValue(materials.addMaterial());
    }

    private boolean hasItem(String item) {
        for (MaterialModel materialModel : itemFirebase) {
            if (item.equalsIgnoreCase(materialModel.getValue())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void onAddTutorial(EditText editTutorial, List<MaterialModel> modelList, int position) {
        this.editText = editTutorial;
        this.models = modelList;
        this.position = position;
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.inflateMenu(R.menu.menu_add_material);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(RegisterMaterial.this, Home.class));
        });
        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.btn_add) {
                Toast.makeText(RegisterMaterial.this, "add", Toast.LENGTH_SHORT).show();
                refMaterial.child(spinnerSections.getItemAtPosition(0).toString()).removeValue();
                if (models.size() > 0) {
                    for (MaterialModel materialModel : models) {
                        addMaterial(spinnerSections.getItemAtPosition(0).toString(), materialModel.getValue());
                    }
                }
            }
            return true;
        });
    }

    private void getMaterial() {
        spotsDialog.show();
        modelMaterialsRetrieveData.RetrieveSingleTimesRepeat(ModelMaterials.class, refMaterial.child(sections), objects -> {
            if (objects != null) {
                models.add(new MaterialModel("tutorial", objects.getText()));
                itemFirebase.add(new MaterialModel("tutorial", objects.getText()));
                adapterTutorial.notifyDataSetChanged();
                spotsDialog.dismiss();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_material, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

    }
}
