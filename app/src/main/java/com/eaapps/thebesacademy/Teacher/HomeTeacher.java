package com.eaapps.thebesacademy.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaapps.thebesacademy.Activities.AttendanceList;
import com.eaapps.thebesacademy.Activities.EditProfile;
import com.eaapps.thebesacademy.Activities.Login;
import com.eaapps.thebesacademy.Chats.ChatHome;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class HomeTeacher extends AppCompatActivity {


    RecyclerView recycleTeacher;
    String[] items = {"Communiction", "Upload lecture", "My File", "Attendance List", "LogOut"};
    int icon[] = {R.drawable.communication_icon64, R.drawable.cloud_upload64,
            R.drawable.folder64, R.drawable.attendance_icon64, R.drawable.logout64};

    String uid;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    RetrieveData<Profile> profileRetrieveData;
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    TextView titleName;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        } else {
            mAuth.signOut();
            startActivity(new Intent(HomeTeacher.this, Login.class));
        }
        setContentView(R.layout.activity_home_teacher);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        spotsDialog.show();


        String toks = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference tok = FirebaseDatabase.getInstance().getReference()
                .child("Token").child(uid);
        tok.child("token").setValue(toks);

        profileImage = findViewById(R.id.profileImage);
        titleName = findViewById(R.id.titleName);
        profileImage.setOnClickListener(v -> {
            startActivity(new Intent(HomeTeacher.this, EditProfile.class));
        });

        ref = FirebaseDatabase.getInstance().getReference().child("Profile").child(uid);
        profileRetrieveData = new RetrieveData<Profile>(HomeTeacher.this) {
        };


        profileRetrieveData.RetrieveSingleTimes(Profile.class, ref, new RetrieveData.CallBackRetrieveTimes<Profile>() {
            @Override
            public void onData(Profile objects) {
                if (objects != null) {
                    titleName.setText(objects.getName());
                    if (objects.getImageUrl() != null) {
                        Picasso.with(HomeTeacher.this).load(objects.getImageUrl()).placeholder(R.drawable.user256).into(profileImage);
                    }
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


        recycleTeacher = findViewById(R.id.recycleTeacher);
        recycleTeacher.setHasFixedSize(false);
        recycleTeacher.setLayoutManager(new LinearLayoutManager(this));
        recycleTeacher.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(HomeTeacher.this).inflate(R.layout.student_item, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                View view = holder.itemView;
                TextView title = view.findViewById(R.id.title);
                ImageView img = view.findViewById(R.id.img);
                title.setText(items[position]);
                img.setImageResource(icon[position]);

                view.setOnClickListener(v -> {
                    switch (position) {
                        case 0:
                            startActivity(new Intent(HomeTeacher.this, ChatHome.class)
                                    .putExtra("key", "doctor"));
                            break;
                        case 1:
                            startActivity(new Intent(HomeTeacher.this, UploadFiles.class));
                            break;
                        case 2:
                            startActivity(new Intent(HomeTeacher.this, TeacherListFile.class).putExtra(Constants.UID, uid));
                            break;
                        case 3:
                            startActivity(new Intent(HomeTeacher.this, AttendanceList.class));
                            break;

                        case 4:
                            mAuth.signOut();
                            startActivity(new Intent(HomeTeacher.this, Login.class));
                            break;
                    }
                });

            }

            @Override
            public int getItemCount() {
                return items.length;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
