package com.eaapps.thebesacademy.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Teacher.Files;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class FilesUploadShow extends AppCompatActivity {

    RecyclerView recycleFile;
    RecyclerView.Adapter adapter;
    List<Profile> itemUsers = new ArrayList<>();
    RetrieveData<Profile> profileRetrieveData;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    String uid;
    String master;
    SpotsDialog spotsDialog;
    Bundle bundle;
    RetrieveData<Files> filesRetrieveData;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            uid = user.getUid();
        }

        setContentView(R.layout.activity_files_upload_show);

        spotsDialog = new SpotsDialog(FilesUploadShow.this, R.style.Custom);
        spotsDialog.show();
        bundle = getIntent().getExtras();

        assert bundle != null;
        master = bundle.getString("master");
        initToolbar();
        ref = FirebaseDatabase.getInstance().getReference();
        profileRetrieveData = new RetrieveData<Profile>(FilesUploadShow.this) {
        };
        filesRetrieveData = new RetrieveData<Files>(FilesUploadShow.this) {
        };


        ref.child("Files").child(master).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                profileRetrieveData.RetrieveSingleTimes(Profile.class, ref.child("Profile").child(dataSnapshot.getKey()), new RetrieveData.CallBackRetrieveTimes<Profile>() {
                    @Override
                    public void onData(Profile objects) {
                        if (objects != null) {
                            itemUsers.add(objects);
                            adapter.notifyDataSetChanged();
                            spotsDialog.dismiss();
                        }
                    }

                    @Override
                    public void hasChildren(boolean c) {

                    }

                    @Override
                    public void exits(boolean e) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recycleFile = findViewById(R.id.recycleFile);
        recycleFile.setHasFixedSize(false);
        recycleFile.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(FilesUploadShow.this).inflate(R.layout.custom_user_chat_item, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                Profile profile = itemUsers.get(position);
                View itemView = holder.itemView;

                TextView nameUser = itemView.findViewById(R.id.nameUser);
                TextView lastMessage = itemView.findViewById(R.id.lastMessage);
                CircleImageView profileUser = itemView.findViewById(R.id.profileUser);
                CircleImageView circleOnline = itemView.findViewById(R.id.circleOnline);

                circleOnline.setVisibility(View.GONE);
                lastMessage.setVisibility(View.GONE);


                if (profile != null) {
                    nameUser.setText(profile.getName());
                    Picasso.with(FilesUploadShow.this).load(profile.getImageUrl())
                            .placeholder(R.drawable.default_avatar).into(profileUser);
                }


                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(FilesUploadShow.this, FilesShow.class);
                    intent.putExtra("master", master);
                    intent.putExtra("id", profile.getId());
                    startActivity(intent);
                });

            }

            @Override
            public int getItemCount() {
                return itemUsers.size();
            }
        };

        recycleFile.setAdapter(adapter);


    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(FilesUploadShow.this, StudentHome.class));
        });
    }

    @Override
    public void onBackPressed() {

    }
}
