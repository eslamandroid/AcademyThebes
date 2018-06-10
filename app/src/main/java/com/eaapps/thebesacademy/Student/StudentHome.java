package com.eaapps.thebesacademy.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaapps.thebesacademy.Activities.EditProfile;
import com.eaapps.thebesacademy.Activities.Login;
import com.eaapps.thebesacademy.Chats.ChatHome;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.Post.ShowNews;
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


public class StudentHome extends AppCompatActivity {


    RecyclerView recycleStudent;
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    TextView titleName;


    // error communication with doctor


    String titles[] = {"News", "Tables", "Attendance", "Lectures", "Chat With Friends", "Communication With Doctor"};
    int icon[] = {R.drawable.world_news64, R.drawable.table_icon64,
            R.drawable.attendance_icon64, R.drawable.lecture_icon64, R.drawable.group64, R.drawable.communication_icon64};

    FirebaseAuth mAuth;
    String uid, master, level, semester;
    DatabaseReference ref;
    Profile profile;
    RetrieveData<Profile> retrieveData;
    SpotsDialog spotsDialog;
    ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            uid = user.getUid();
        } else {
            mAuth.signOut();
            startActivity(new Intent(StudentHome.this, Login.class));
        }

        setContentView(R.layout.activity_student_home);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        spotsDialog.show();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(StudentHome.this, Login.class));

        });

        String toks = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference tok = FirebaseDatabase.getInstance().getReference()
                .child("Token").child(uid);
        tok.child("token").setValue(toks);


        retrieveData = new RetrieveData<Profile>(StudentHome.this) {
        };

        ref = FirebaseDatabase.getInstance().getReference();

        profileImage = findViewById(R.id.profileImage);
        titleName = findViewById(R.id.titleName);


        retrieveData.RetrieveSingleTimes(Profile.class, ref.child("Profile").child(uid), new RetrieveData.CallBackRetrieveTimes<Profile>() {
            @Override
            public void onData(Profile objects) {
                if (objects != null) {
                    master = objects.getMaster();
                    profile = objects;
                    semester = objects.getSemester();
                    level = objects.getLevel();
                    titleName.setText(profile.getName());
                    if (objects.getImageUrl() != null) {
                        Picasso.with(StudentHome.this).load(objects.getImageUrl()).placeholder(R.drawable.user256).into(profileImage);
                    }
                    spotsDialog.dismiss();
                    if (TextUtils.isEmpty(level) && TextUtils.isEmpty(semester)) {
                        startActivity(new Intent(StudentHome.this, EditProfile.class));
                    }
                }
            }

            @Override
            public void hasChildren(boolean c) {

            }

            @Override
            public void exits(boolean e) {

            }
        });


        profileImage.setOnClickListener(v -> {
            startActivity(new Intent(StudentHome.this, EditProfile.class));
        });

        recycleStudent = findViewById(R.id.recycleStudent);
        recycleStudent.setHasFixedSize(true);
        recycleStudent.setLayoutManager(new GridLayoutManager(this, 2));
        recycleStudent.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(StudentHome.this).inflate(R.layout.student_item, parent, false)) {
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
                title.setText(titles[position]);
                img.setImageResource(icon[position]);


                view.setOnClickListener(v -> {

                    switch (position) {
                        case 0:
                            startActivity(new Intent(StudentHome.this, ShowNews.class).putExtra("key","student"));
                            break;
                        case 1:
                            startActivity(new Intent(StudentHome.this, ThebesTables.class).putExtra(Constants.MASTER, master).putExtra("key", "student"));
                            break;
                        case 2:
                            startActivity(new Intent(StudentHome.this, Attendance.class).putExtra(Constants.PROFILE, profile));
                            break;
                        case 3:
                            startActivity(new Intent(StudentHome.this, FilesUploadShow.class).putExtra("master", master));
                            break;
                        case 4:
                            startActivity(new Intent(StudentHome.this, ChatHome.class)
                                    .putExtra("key", "student"));
                            break;
                        case 5:
                            startActivity(new Intent(StudentHome.this, ChatDoctors.class));
                            break;
                    }


                });

            }

            @Override
            public int getItemCount() {
                return titles.length;
            }
        });


    }


}
