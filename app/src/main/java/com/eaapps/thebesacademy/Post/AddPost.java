package com.eaapps.thebesacademy.Post;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eaapps.thebesacademy.Admin.AdminHome;
import com.eaapps.thebesacademy.Chats.Tokens;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class AddPost extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    static boolean isPhoto;
    EditText editPost;
    Button addPhoto, publishNews;
    DatabaseReference refPost;
    StorageReference mStorage;
    Uri uriImage = null;
    SpotsDialog spotsDialog;
    RecyclerView recyclePhoto;
    RecyclerView.Adapter adapter;
    List<Uri> uriList = new ArrayList<>();
    List<String> urlDownload = new ArrayList<>();
    RetrieveData<Tokens> tokensRetrieveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initToolbar();
        tokensRetrieveData = new RetrieveData<Tokens>(AddPost.this) {
        };

        spotsDialog = new SpotsDialog(this, R.style.Custom);
        editPost = findViewById(R.id.editPost);
        addPhoto = findViewById(R.id.addPhoto);
        publishNews = findViewById(R.id.publishNews);
        recyclePhoto = findViewById(R.id.recyclePhoto);
        recyclePhoto.setHasFixedSize(true);
        recyclePhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(AddPost.this).inflate(R.layout.item_upload, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                View view = holder.itemView;
                ImageView img = view.findViewById(R.id.img);
                img.setImageURI(uriList.get(position));


            }

            @Override
            public int getItemCount() {
                return uriList.size();
            }
        };
        recyclePhoto.setAdapter(adapter);


        refPost = FirebaseDatabase.getInstance().getReference().child("News");

        mStorage = FirebaseStorage.getInstance().getReference().child("News");


        addPhoto.setOnClickListener(v -> {
            isPhoto = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
        });

        publishNews.setOnClickListener(v -> {
            addNews();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                Toast.makeText(AddPost.this, "Select Single Image", Toast.LENGTH_SHORT).show();
                Uri imageUri = data.getData();
                uriImage = imageUri;
                uriList.add(imageUri);
                adapter.notifyDataSetChanged();
                isPhoto = true;
            }

            if (data.getClipData() != null) {
                Toast.makeText(AddPost.this, "Select MultiMadia Image", Toast.LENGTH_SHORT).show();
                isPhoto = true;
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uriFile = data.getClipData().getItemAt(i).getUri();
                    uriList.add(uriFile);
                    adapter.notifyDataSetChanged();


                }


            }
        }

    }


    private void addNews() {
        DatabaseReference sData = refPost.push();
        if (isPhoto && uriList.size() > 0) {
            spotsDialog.show();
            for (Uri uri : uriList) {
                StorageReference filepath = mStorage.child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnCompleteListener(task -> {
                    String download_url = task.getResult().getDownloadUrl().toString();
                    urlDownload.add(download_url);
                    if (urlDownload != null && urlDownload.size() > 0) {
                        Post post = new Post();
                        if (!TextUtils.isEmpty(editPost.getText().toString())) {
                            post.setText(editPost.getText().toString());
                        }
                        post.setUrl(urlDownload);
                        post.setLike(0);
                        post.setId(sData.getKey());
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        post.setTime_post(timestamp.getTime());
                        sData.setValue(post.publishNews()).addOnCompleteListener(task1 -> {

                            if (task1.isSuccessful()) {
                                // startActivity
                                spotsDialog.dismiss();
                            }
                        });
                    }


                });
            }


        } else {
            spotsDialog.show();
            Post post = new Post();
            if (!TextUtils.isEmpty(editPost.getText().toString())) {
                post.setText(editPost.getText().toString());
                post.setLike(0);
                post.setId(sData.getKey());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                post.setTime_post(timestamp.getTime());
                sData.setValue(post.publishNews()).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        // startActivity
                        editPost.setText("");
                        uriList.clear();
                        adapter.notifyDataSetChanged();
                        spotsDialog.dismiss();
                    }
                });

            } else {
                spotsDialog.dismiss();
                Constants.customDialogAlter(AddPost.this, R.layout.custom_dialog2, "Please Enter Text or Choose Photo to Upload :)");
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {

            startActivity(new Intent(AddPost.this, AdminHome.class));

        });

    }
}
