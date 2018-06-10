package com.eaapps.thebesacademy.Teacher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;

import java.io.File;
import java.util.ArrayList;

public class BrowserFile extends AppCompatActivity {

    RecyclerView recyleFile;
    RecyclerView.Adapter adapter;
    String fType;
    Bundle bundle;
    private File root;
    private ArrayList<File> fileList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> pathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_file);
        bundle = getIntent().getExtras();

        assert bundle != null;
        fType = bundle.getString(Constants.TABLE_TYPE);

        initToolbar();
        recyleFile = findViewById(R.id.recycleFile);
        recyleFile.setHasFixedSize(false);
        recyleFile.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(BrowserFile.this).inflate(R.layout.custom_browser_file, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                View view = holder.itemView;
                TextView titlFile = view.findViewById(R.id.titleFile);
                titlFile.setText(nameList.get(position));
                view.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.putExtra("filepath", pathList.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                });

            }

            @Override
            public int getItemCount() {
                return nameList.size();
            }
        };
        recyleFile.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(BrowserFile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(BrowserFile.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    50);
            return;
        }
        root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        getfile(root);


    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("filepath", "back");
            setResult(RESULT_OK, intent);
            finish();

        });
    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(fType)) {
                        fileList.add(listFile[i]);
                        nameList.add(listFile[i].getName());
                        pathList.add(listFile[i].getPath());
                    }
                }

            }
        }
        adapter.notifyDataSetChanged();

        return fileList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 50:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start your camera handling here
                    root = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath());
                    getfile(root);
                } else {
                    Constants.customToast(BrowserFile.this, "Please allow the permission So the app works successfully");
                }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
