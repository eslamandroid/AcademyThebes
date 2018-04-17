package com.eaapps.thebesacademy.Teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.util.ArrayList;

public class UploadFiles extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    int FILE_Request_Code = 7;
    ProgressBar progressBar;
    Button uploadFile, cancelUpload, pauseUpload;
    TextView nameFile, titleSize, titlePresent;
    EditText editChapter, editDescription;
    Spinner spinner, spinnerLevel, spinnerType;
    String[] typeFile = {".pdf", ".xls", ".png", ".jpg", ".mp4", ".mp3", ".doc", ".zip", ".rar"};
    String[] sectionArr = {"Computer Science", "Information Systems", "Accounting", "Business Administration"};
    String[] levelArr = {"1", "2", "3", "4"};

    Uri FilePathUri;
    StorageTask storageTask;
    StorageReference storageReference;
    String fType, uid, flevel, fsection;
    FirebaseAuth mAuth;
    DatabaseReference data;
    StringBuilder msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            uid = mAuth.getCurrentUser().getUid();
        }

        initToolBar();

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(UploadFiles.this, R.layout.text_spinner_type, sectionArr));
        spinner.setOnItemSelectedListener(this);


        spinnerLevel = findViewById(R.id.spinner1);
        spinnerLevel.setAdapter(new ArrayAdapter<>(UploadFiles.this, R.layout.text_spinner_type, levelArr));
        spinnerLevel.setOnItemSelectedListener(this);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerType.setAdapter(new ArrayAdapter<>(UploadFiles.this, R.layout.text_spinner_type, typeFile));
        spinnerType.setOnItemSelectedListener(this);


        progressBar = findViewById(R.id.progressBar2);

        progressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

        uploadFile = findViewById(R.id.uploadFile);
        cancelUpload = findViewById(R.id.cancelUpload);
        pauseUpload = findViewById(R.id.pauseUpload);

        nameFile = findViewById(R.id.titleFile);
        titleSize = findViewById(R.id.titleSize);
        titlePresent = findViewById(R.id.titlePresent);


        editChapter = findViewById(R.id.nameChapter);
        editDescription = findViewById(R.id.textDescription);

        uploadFile.setOnClickListener(this);
        cancelUpload.setOnClickListener(this);
        pauseUpload.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference().child("Subjects");
        data = FirebaseDatabase.getInstance().getReference().child("Files").child(uid);


    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(UploadFiles.this, HomeTeacher.class));
        });

    }

    @Override
    public void onBackPressed() {

    }

    private boolean hasEmpty() {


        if (!TextUtils.isEmpty(editChapter.getText().toString()) && !TextUtils.isEmpty(editDescription.getText().toString())) {
            return true;

        } else {
            ArrayList arr = Constants.getStringsEmpty(new String[]{editChapter.getText().toString(), editDescription.getText().toString()}
                    , new String[]{"Enter Name Chapter", "Enter Description File"});
            msg = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                msg.append(arr.get(i)).append(" Is Empty\n");
            }
            Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

        }
        return false;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadFile:
                if (hasEmpty()) {
                    Intent i = new Intent(UploadFiles.this, BrowserFile.class);
                    i.putExtra(Constants.TABLE_TYPE, fType);
                    startActivityForResult(i, FILE_Request_Code);
                }
                break;
            case R.id.cancelUpload:
                storageTask.cancel();

                break;
            case R.id.pauseUpload:

                if (pauseUpload.getText().toString().equalsIgnoreCase("pause upload")) {
                    pauseUpload.setText("Resume Upload");
                    storageTask.pause();

                } else {
                    pauseUpload.setText("Pause Upload");
                    storageTask.resume();
                }
                break;

        }

    }

    @SuppressLint("SetTextI18n")
    private void uploadFiles() {
        Uri file = Uri.fromFile(new File(String.valueOf(FilePathUri)));
        storageTask = storageReference.child(System.currentTimeMillis() + fType).putFile(file).addOnCompleteListener(task -> {
            String url = task.getResult().getDownloadUrl().toString();


            DatabaseReference d = data.push();
            Files files = new Files();
            files.setDescription(editDescription.getText().toString());
            files.setTitle(editChapter.getText().toString());
            files.setLevel(flevel);
            files.setSection(fsection);
            files.setUrl(url);
            files.setId(d.getKey());
            files.setType_file(fType);
            d.setValue(files.addFile());
            Toast.makeText(getApplicationContext(), "Successful Upload :)", Toast.LENGTH_SHORT).show();


        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed Upload File Try Again :(", Toast.LENGTH_SHORT).show();
            Log.e("ErrorUpload", e.getMessage());


        }).addOnProgressListener(taskSnapshot -> {

            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressBar.setProgress((int) progress);

            String size = taskSnapshot.getBytesTransferred() / (1024 * 1024) + " / " + taskSnapshot.getTotalByteCount() / (1024 * 1024) + " mb";
            titleSize.setText(size);
            nameFile.setText(System.currentTimeMillis() + fType);
            titlePresent.setText((int) progress + "%");

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_Request_Code && resultCode == RESULT_OK &&
                data != null && data.getStringExtra("filepath") != null) {

            if (!data.getStringExtra("filepath").equalsIgnoreCase("back")) {
                FilePathUri = Uri.parse(data.getStringExtra("filepath"));
                uploadFiles();
            } else {
                Toast.makeText(getApplicationContext(), "You did not select any file to upload", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinnerType:
                fType = spinnerType.getItemAtPosition(position).toString();
                break;
            case R.id.spinner:
                fsection = spinner.getItemAtPosition(position).toString();
                break;
            case R.id.spinner1:
                flevel = spinnerLevel.getItemAtPosition(position).toString();
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
