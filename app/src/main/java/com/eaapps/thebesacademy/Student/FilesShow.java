package com.eaapps.thebesacademy.Student;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Teacher.Files;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class FilesShow extends AppCompatActivity {

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    RecyclerView recycleFile;
    RecyclerView.Adapter adapter;
    List<Files> filesList = new ArrayList<>();
    RetrieveData<Files> filesRetrieveData;
    DatabaseReference ref;
    String uid;
    String master;
    Bundle bundle;
    SpotsDialog spotsDialog;
    TextView textView;
    // Progress Dialog
    StorageReference storageRef;
    private ProgressDialog pDialog, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_show);
        spotsDialog = new SpotsDialog(FilesShow.this, R.style.Custom);
        spotsDialog.show();
        bundle = getIntent().getExtras();
        assert bundle != null;
        uid = bundle.getString("id");
        master = bundle.getString("master");
        initToolbar();

        progressDialog = new ProgressDialog(this);
        ref = FirebaseDatabase.getInstance().getReference();
        filesRetrieveData = new RetrieveData<Files>(FilesShow.this) {
        };

        textView = findViewById(R.id.textFind);
        recycleFile = findViewById(R.id.recycleFile);
        recycleFile.setHasFixedSize(false);
        recycleFile.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(FilesShow.this).inflate(R.layout.custom_list_files, parent, false)) {
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
                    StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(files.getUrl());
                    downloadToLocalFile(httpsReference, System.currentTimeMillis() + files.getType_file());

                });
            }

            @Override
            public int getItemCount() {
                return filesList.size();
            }
        };
        recycleFile.setAdapter(adapter);

        filesRetrieveData.RetrieveList(Files.class, ref.child("Files")
                .child(master).child(uid), new RetrieveData.CallBackRetrieveList<Files>() {


            @Override
            public void onDataList(List<Files> object, int countChild) {
                Files files = object.get(0);
                if (files != null) {
                    filesList.add(files);
                    adapter.notifyDataSetChanged();
                    spotsDialog.dismiss();
                } else {
                    textView.setVisibility(View.VISIBLE);
                    spotsDialog.dismiss();

                }

                if (filesList.size() > 0) {
                    textView.setVisibility(View.GONE);
                    spotsDialog.dismiss();
                }
            }

            @Override
            public void onChangeList(List<Files> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }

            @Override
            public void exits(boolean e) {
                if(!e){
                    textView.setVisibility(View.VISIBLE);
                    spotsDialog.dismiss();
                }else{
                    textView.setVisibility(View.GONE);
                    spotsDialog.dismiss();
                }
            }

            @Override
            public void hasChildren(boolean c) {
                if(!c){
                    textView.setVisibility(View.VISIBLE);
                    spotsDialog.dismiss();
                }else{
                    textView.setVisibility(View.GONE);
                    spotsDialog.dismiss();
                }
            }
        });


    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(FilesShow.this, StudentHome.class).putExtra("master", master));
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }

    }

    private void downloadToLocalFile(StorageReference fileRef, String nameFile) {
        if (fileRef != null) {
            progressDialog.setTitle("Downloading...");
            progressDialog.setMessage(null);
            progressDialog.show();

            try {


                long s = 1024 * 1024;

                fileRef.getBytes(s).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        storeFiles(nameFile, bytes);
                        Toast.makeText(FilesShow.this, "ssssss", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(FilesShow.this, "Upload file before downloading", Toast.LENGTH_LONG).show();
        }
    }


    public void storeFiles(String nameFile, byte[] bytes) {
        File file, storage;
        FileOutputStream outputStream;
        try {
            storage = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                            + "Thebes Academy");
            if (!storage.exists()) {
                storage.mkdir();
            }
            file = new File(storage, nameFile);

            //Create New File if not present
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

