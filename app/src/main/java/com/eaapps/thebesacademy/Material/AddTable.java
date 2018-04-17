package com.eaapps.thebesacademy.Material;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.eaapps.thebesacademy.Activities.Home;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AddTable extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MaterialInterface.CallBackTable {


    public static MaterialInterface.CallBackTable callBackTable;
    private static boolean isEditTable = false;
    Spinner sSection, sSemester, sLevel, sDay, sMaterial;
    RecyclerView listTable;
    Button btnAdd;
    List<ModelTable> modelTableList = new ArrayList<>();
    List<String> materials = new ArrayList<>();
    RetrieveData<ModelMaterials> modelMaterialsRetrieveData;
    RetrieveData<FirebaseModelTables> modelTableRetrieveData;
    DatabaseReference ref;
    SpotsDialog spotsDialog;
    EditText from, to, name;
    AdapterTable adapterTable;
    StringBuilder msg;
    LinearLayout group1, group2, group3, group4;
    ArrayAdapter aMaterial;
    String t, f, nameDoctor, master, semester, level, day, material;


    String[] sectionArr = {"Computer Science", "Information Systems", "Accounting", "Business Administration"};
    String[] levelArr = {"1", "2", "3", "4"};
    String[] semesterArr = {"1", "2", "3", "4"};
    String[] dayArr = {"السبت", "الاحد", "الاثنين", "الثلاثاء", "الاربعاء", "الخميس"};
    Bundle bundle;
    String key, typeTable;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        bundle = getIntent().getExtras();
        key = bundle != null ? bundle.getString(Constants.TABLE_TYPE) : null;

        spotsDialog = new SpotsDialog(this, R.style.Custom);

        callBackTable = this;

        initToolBar();
        init();
        switch (key) {
            case Constants.EXAM:
                typeTable = "Exam";
                group3.setVisibility(View.GONE);

                break;
            case Constants.LECTURES:
                typeTable = "Lecture";

                break;
            case Constants.SECTIONS:
                typeTable = "Section";

                break;
        }

        modelMaterialsRetrieveData = new RetrieveData<ModelMaterials>(AddTable.this) {
        };
        modelTableRetrieveData = new RetrieveData<FirebaseModelTables>(AddTable.this) {
        };

        ref = FirebaseDatabase.getInstance().getReference();


    }

    private void init() {
        group1 = findViewById(R.id.group1);
        group2 = findViewById(R.id.group2);
        group3 = findViewById(R.id.group3);
        group4 = findViewById(R.id.group4);

        sSection = findViewById(R.id.spinner);
        sSection.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, sectionArr));
        sSection.setOnItemSelectedListener(this);


        sDay = findViewById(R.id.spinnerDay);
        sDay.setAdapter(new ArrayAdapter<>(AddTable.this, R.layout.text_spinner_type, dayArr));
        sDay.setOnItemSelectedListener(this);


        sMaterial = findViewById(R.id.spinnerMaterial);
        sMaterial.setOnItemSelectedListener(this);
        aMaterial = new ArrayAdapter<>(AddTable.this, R.layout.text_spinner_type, materials);
        sMaterial.setAdapter(aMaterial);


        sLevel = findViewById(R.id.spinner1);
        sLevel.setAdapter(new ArrayAdapter<>(AddTable.this, R.layout.text_spinner_type, levelArr));
        sLevel.setOnItemSelectedListener(this);

        sSemester = findViewById(R.id.spinner2);
        sSemester.setAdapter(new ArrayAdapter<>(AddTable.this, R.layout.text_spinner_type, semesterArr));
        sSemester.setOnItemSelectedListener(this);


        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        name = findViewById(R.id.nameDoctor);

        listTable = findViewById(R.id.listTable);
        listTable.setHasFixedSize(false);
        listTable.setLayoutManager(new LinearLayoutManager(AddTable.this));
        adapterTable = new AdapterTable(AddTable.this, modelTableList);
        listTable.setAdapter(adapterTable);
        //getTables();


        btnAdd = findViewById(R.id.floatAdd);
        btnAdd.setOnClickListener(v -> {

            DatabaseReference data = ref.child("Tables").child(typeTable).child(master).child(level);
            modelTableRetrieveData.haveChild(FirebaseModelTables.class, data, semester, (has, object) -> {

                if (has && !isEditTable) {
                    final Dialog dialog = new Dialog(AddTable.this);
                    Constants.customDialogAlter(AddTable.this, dialog, R.layout.custom_dialog, "This table already exists Do you want to edit", "Edit Table", new Constants.ClickButton() {
                        @Override
                        public void clickAction() {
                            isEditTable = true;
                            dialog.dismiss();
                            modelTableRetrieveData.RetrieveSingleTimes(FirebaseModelTables.class, data.child(semester), objects -> {
                                List<ModelTable> modelTable = objects.getTablesDetails();
                                for (ModelTable modelTables : modelTable) {
                                    modelTableList.add(modelTables);
                                    adapterTable.notifyDataSetChanged();
                                }

                            });
                        }
                    });

                } else {
                    if (hasEmpty()) {
                        modelTableList.add(new ModelTable(material, nameDoctor, f + " : " + t, day));
                        adapterTable.notifyDataSetChanged();
                    }
                }
            });

        });

    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.inflateMenu(R.menu.menu_add_material);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(AddTable.this, Home.class));
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.btn_add) {
                spotsDialog.show();

                DatabaseReference containers = ref.child("Tables").child(typeTable).child(master).push();
                FirebaseModelTables firebaseModelTables = new FirebaseModelTables();
                firebaseModelTables.setId(containers.getKey());
                firebaseModelTables.setLevel(level);
                firebaseModelTables.setMaster(master);
                firebaseModelTables.setSemester(semester);
                firebaseModelTables.setTablesDetails(modelTableList);
                containers.setValue(firebaseModelTables.addTable()).addOnSuccessListener(aVoid -> {
                    spotsDialog.dismiss();
                }).addOnFailureListener(e -> spotsDialog.dismiss());


            }
            return true;
        });
    }


    private boolean hasEmpty() {
        t = to.getText().toString();
        f = from.getText().toString();
        nameDoctor = name.getText().toString();

        f = from.getText().toString();
        if (!TextUtils.isEmpty(nameDoctor) && !TextUtils.isEmpty(t) && !TextUtils.isEmpty(f)) {
            return true;

        } else {
            ArrayList arr = Constants.getStringsEmpty(new String[]{nameDoctor, t, f}
                    , new String[]{"Name Doctor Field", "To Field", "From Field"});
            msg = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                msg.append(arr.get(i)).append(" Is Empty\n");
            }
            Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

        }
        return false;
    }

    private void getMaterial(String sections) {
        spotsDialog.show();
        materials.clear();
        modelMaterialsRetrieveData.RetrieveSingleTimesRepeat(ModelMaterials.class, ref.child("Materials").child(sections), objects -> {
            if (objects != null) {
                materials.add(objects.getText());
                aMaterial.notifyDataSetChanged();
                spotsDialog.dismiss();
            } else {
                spotsDialog.dismiss();
            }

        });
    }

    private void getTables() {

        Query data = ref.child("Tables").child(typeTable).child(master).orderByChild("level").equalTo(level);
        modelTableRetrieveData.RetrieveList(FirebaseModelTables.class, data, new RetrieveData.CallBackRetrieveList<FirebaseModelTables>() {
            @Override
            public void onDataList(List<FirebaseModelTables> object, String key) {
                FirebaseModelTables firebaseModelTables = object.get(0);
                if (firebaseModelTables != null) {
                    //error
                    if (firebaseModelTables.getSemester().equalsIgnoreCase(semester)) {
                        modelTableList.clear();
                        modelTableList.addAll(firebaseModelTables.getTablesDetails());
                        adapterTable.notifyDataSetChanged();
                    }
                } else {
                    modelTableList.clear();
                    adapterTable.notifyDataSetChanged();
                }

            }

            @Override
            public void onChangeList(List<FirebaseModelTables> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

        switch (parent.getId()) {
            case R.id.spinner:
                master = sSection.getItemAtPosition(i).toString();
                getMaterial(sSection.getItemAtPosition(i).toString());
                getTables();

                break;
            case R.id.spinner1:
                level = sLevel.getItemAtPosition(i).toString();
                getTables();

                break;

            case R.id.spinner2:
                semester = sSemester.getItemAtPosition(i).toString();
                getTables();

                break;

            case R.id.spinnerMaterial:
                material = sMaterial.getItemAtPosition(i).toString();
                break;

            case R.id.spinnerDay:
                day = sDay.getItemAtPosition(i).toString();

                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onInfoTable(List<ModelTable> list, int position) {
        this.modelTableList = list;
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
