package com.eaapps.thebesacademy.Teacher;

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
    public void onBackPressed() {

    }
}
