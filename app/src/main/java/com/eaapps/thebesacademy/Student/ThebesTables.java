package com.eaapps.thebesacademy.Student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.eaapps.thebesacademy.Admin.AdminHome;
import com.eaapps.thebesacademy.Material.FirebaseModelTables;
import com.eaapps.thebesacademy.Material.ModelTable;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ThebesTables extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spSemester, spLevel, spType;
    RecyclerView recycleTables;
    String[] levelArr = {"1", "2", "3", "4"};
    String[] semesterArr = {"1", "2", "3", "4"};
    String[] tableType = {"Lecture", "Section", "Exam"};

    String semester = semesterArr[0], level = levelArr[0], type = tableType[0], master;
    RetrieveData<FirebaseModelTables> modelTableRetrieveData;
    List<ModelTable> modelTableList = new ArrayList<>();
    DatabaseReference ref;
    RecyclerView.Adapter adapter;
    Bundle bundle;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thebes_tables);

        bundle = getIntent().getExtras();
        master = bundle != null ? bundle.getString(Constants.MASTER) : null;
        if (bundle != null) {
            key = bundle.getString("key");
        }


        initToolbar();

        spLevel = findViewById(R.id.spinnerLevel);
        spLevel.setAdapter(new ArrayAdapter<>(ThebesTables.this, R.layout.text_spinner_type, levelArr));
        spLevel.setOnItemSelectedListener(this);

        spSemester = findViewById(R.id.spinnerSemester);
        spSemester.setAdapter(new ArrayAdapter<>(ThebesTables.this, R.layout.text_spinner_type, semesterArr));
        spSemester.setOnItemSelectedListener(this);

        spType = findViewById(R.id.spinnerType);
        spType.setAdapter(new ArrayAdapter<>(ThebesTables.this, R.layout.text_spinner_type, tableType));
        spType.setOnItemSelectedListener(this);


        recycleTables = findViewById(R.id.recycleTables);
        recycleTables.setHasFixedSize(true);
        recycleTables.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RecyclerView.ViewHolder(LayoutInflater.from(ThebesTables.this).inflate(R.layout.item_table_add, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                ModelTable modelTable = modelTableList.get(position);
                View view = holder.itemView;
                TextView tName = view.findViewById(R.id.nameTeacher);
                TextView tMaterial = view.findViewById(R.id.nameMaterial);
                TextView tDay = view.findViewById(R.id.nameDay);
                TextView tTime = view.findViewById(R.id.nameTime);
                Button deleteItem = view.findViewById(R.id.deleteItem);
                deleteItem.setVisibility(View.GONE);

                tName.setText("  Name Doctor : " + modelTable.getName_teacher());
                tMaterial.setText("  Name Material : " + modelTable.getName_material());
                tDay.setText("  Day : " + modelTable.getDay());
                tTime.setText("  Time : " + modelTable.getTime());


            }

            @Override
            public int getItemCount() {
                return modelTableList.size();
            }
        };
        recycleTables.setAdapter(adapter);


        modelTableRetrieveData = new RetrieveData<FirebaseModelTables>(ThebesTables.this) {
        };

        ref = FirebaseDatabase.getInstance().getReference();
        getTables();
    }

    private void getTables() {

        Query data = ref.child("Tables").child(type).child(master).orderByChild("level").equalTo(level);
        modelTableRetrieveData.RetrieveList(FirebaseModelTables.class, data, new RetrieveData.CallBackRetrieveList<FirebaseModelTables>() {

            @Override
            public void onDataList(List<FirebaseModelTables> object, int countChild) {
                FirebaseModelTables firebaseModelTables = object.get(0);
                if (firebaseModelTables != null) {
                    if (firebaseModelTables.getSemester().equalsIgnoreCase(semester)) {
                        modelTableList.clear();
                        modelTableList.addAll(firebaseModelTables.getTablesDetails());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    modelTableList.clear();
                }
            }

            @Override
            public void onChangeList(List<FirebaseModelTables> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }

            @Override
            public void exits(boolean e) {

            }

            @Override
            public void hasChildren(boolean c) {

            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

        switch (parent.getId()) {
            case R.id.spinnerLevel:
                level = spLevel.getItemAtPosition(i).toString();
                getTables();

                break;

            case R.id.spinnerSemester:
                semester = spSemester.getItemAtPosition(i).toString();
                getTables();

                break;

            case R.id.spinnerType:
                type = spType.getItemAtPosition(i).toString();
                getTables();
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            if(key.equalsIgnoreCase("admin")){
                startActivity(new Intent(ThebesTables.this, AdminHome.class));

            }else {
                startActivity(new Intent(ThebesTables.this, StudentHome.class));
            }

        });
    }

    @Override
    public void onBackPressed() {

    }
}
