package com.eaapps.thebesacademy.Teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.eaapps.thebesacademy.Files.DownloadTask;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class TeacherListFile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    RecyclerView recycleDoctorFiles;
    RecyclerView.Adapter adapter;
    List<Files> filesList;
    DatabaseReference ref;
    Bundle bundle;
    String myDoctor;
    RetrieveData<Files> filesRetrieveData;
    String[] sectionArr = {"Computer Science", "Information Systems", "Accounting", "Business Administration"};
    String[] levelArr = {"1", "2", "3", "4"};
    String master, level;
    Spinner sSection, sLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list_file);
        bundle = getIntent().getExtras();
        assert bundle != null;
        myDoctor = bundle.getString(Constants.UID);


        ref = FirebaseDatabase.getInstance().getReference().child("Files").child(myDoctor);
        filesRetrieveData = new RetrieveData<Files>(TeacherListFile.this) {
        };

        sSection = findViewById(R.id.spinner);
        sSection.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, sectionArr));
        sSection.setOnItemSelectedListener(this);

        sLevel = findViewById(R.id.spinner1);
        sLevel.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, levelArr));
        sLevel.setOnItemSelectedListener(this);


        recycleDoctorFiles = findViewById(R.id.recycleFileDoctor);
        recycleDoctorFiles.setHasFixedSize(false);
        recycleDoctorFiles.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(TeacherListFile.this).inflate(R.layout.custom_list_files, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }


            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Files files = filesList.get(position);

                View view = holder.itemView;

                TextView title = view.findViewById(R.id.title);
                TextView description = view.findViewById(R.id.description);
                TextView type_file = view.findViewById(R.id.type_file);
                Button downloadFile = view.findViewById(R.id.downloadFile);
                title.setText("Title : " + files.getTitle());
                description.setText("Description : " + files.getDescription());
                type_file.setText(files.getType_file());

                downloadFile.setOnClickListener(v -> {

                    new DownloadTask(TeacherListFile.this, files.getUrl());

                });

            }

            @Override
            public int getItemCount() {
                return filesList.size();
            }
        };
        recycleDoctorFiles.setAdapter(adapter);

        Query query = ref.orderByChild("section").equalTo(master);
        filesRetrieveData.RetrieveList(Files.class, query, new RetrieveData.CallBackRetrieveList<Files>() {
            @Override
            public void onDataList(List<Files> object, String key) {
                Files files = object.get(0);
                if (files != null) {
                    if (files.getLevel() != null && files.getLevel().equals(level))
                        filesList.add(files);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChangeList(List<Files> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                master = sSection.getItemAtPosition(i).toString();
                break;
            case R.id.spinner1:
                level = sLevel.getItemAtPosition(i).toString();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
