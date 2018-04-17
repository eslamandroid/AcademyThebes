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

import com.eaapps.thebesacademy.Chats.ChatViewActivity;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDoctors extends AppCompatActivity {


    RecyclerView recycleDoctors;
    RecyclerView.Adapter adapter;

    List<Profile> itemUsers = new ArrayList<>();
    String key = "some";
    DatabaseReference ref;
    RetrieveData<Profile> profileRetrieveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_doctors);

        initToolbar();

        ref = FirebaseDatabase.getInstance().getReference();
        profileRetrieveData = new RetrieveData<Profile>(ChatDoctors.this) {
        };

        recycleDoctors = findViewById(R.id.recycleDoctors);
        recycleDoctors.setHasFixedSize(false);
        recycleDoctors.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(ChatDoctors.this).inflate(R.layout.custom_user_chat_item, parent, false)) {
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
                if (profile != null) {
                    nameUser.setText(profile.getName());
                    Picasso.with(ChatDoctors.this).load(profile.getImageUrl())
                            .placeholder(R.drawable.default_avatar).into(profileUser);
                }
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(ChatDoctors.this, ChatViewActivity.class);
                    intent.putExtra(Constants.PROFILE, profile);
                    startActivity(intent);
                });

            }

            @Override
            public int getItemCount() {
                return itemUsers.size();
            }
        };

        recycleDoctors.setAdapter(adapter);

        profileRetrieveData.RetrieveList(Profile.class, ref.child("Profile").orderByChild("type_account").equalTo("Doctor"), new RetrieveData.CallBackRetrieveList<Profile>() {
            @Override
            public void onDataList(List<Profile> object, String key) {
                Profile profile = object.get(0);
                if (profile != null) {
                    itemUsers.add(profile);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChangeList(List<Profile> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(ChatDoctors.this, StudentHome.class));
        });
    }

    @Override
    public void onBackPressed() {

    }
}
