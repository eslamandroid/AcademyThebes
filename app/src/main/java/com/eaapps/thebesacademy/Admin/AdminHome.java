package com.eaapps.thebesacademy.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaapps.thebesacademy.Activities.Login;
import com.eaapps.thebesacademy.Material.AddTable;
import com.eaapps.thebesacademy.Material.RegisterMaterial;
import com.eaapps.thebesacademy.Post.AddPost;
import com.eaapps.thebesacademy.Post.ShowNews;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.ThebesTables;
import com.eaapps.thebesacademy.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class AdminHome extends AppCompatActivity {

    RecyclerView recycleAdmin;
    String arrItem[] = {
            "Add Material",
            "Add a lectures schedule",
            "Add a sections schedule",
            "Add the exam schedule",
            "Add Latest news",
            "Show Tables",
            "Show News"
            , "Log Out"
    };
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signOut();
            startActivity(new Intent(AdminHome.this, Login.class));
        }

        String toks = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference tok = FirebaseDatabase.getInstance().getReference()
                .child("Token").child(user.getUid());
        tok.child("token").setValue(toks);

        setContentView(R.layout.activity_admin_home);
        recycleAdmin = findViewById(R.id.recycleAdmin);
        recycleAdmin.setHasFixedSize(true);
        recycleAdmin.setLayoutManager(new GridLayoutManager(this, 2));
        recycleAdmin.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(AdminHome.this).inflate(R.layout.item_recycle_admin, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                View view = holder.itemView;
                TextView titleItem = view.findViewById(R.id.titleItem);
                titleItem.setText(arrItem[position]);

                view.setOnClickListener(v -> {
                    switch (position) {
                        case 0:
                            startActivity(new Intent(AdminHome.this, RegisterMaterial.class));
                            break;
                        case 1:
                            startActivity(new Intent(AdminHome.this, AddTable.class)
                                    .putExtra(Constants.TABLE_TYPE, Constants.LECTURES));
                            break;
                        case 3:
                            startActivity(new Intent(AdminHome.this, AddTable.class)
                                    .putExtra(Constants.TABLE_TYPE, Constants.EXAM));
                            break;
                        case 2:
                            startActivity(new Intent(AdminHome.this, AddTable.class)
                                    .putExtra(Constants.TABLE_TYPE, Constants.SECTIONS));

                            break;
                        case 4:
                            startActivity(new Intent(AdminHome.this, AddPost.class));
                            break;
                        case 5:

                            String[] sectionArr = {"Computer Science", "Information Systems", "Accounting", "Business Administration"};


                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome.this);

                            builder.setTitle("Select Master");
                            builder.setItems(sectionArr, (dialogInterface, i) -> {
                                startActivity(new Intent(AdminHome.this, ThebesTables.class)
                                        .putExtra(Constants.MASTER, sectionArr[i]).putExtra("key", "admin"));


                            });

                            builder.show();

                            break;

                        case 6:
                            startActivity(new Intent(AdminHome.this, ShowNews.class).putExtra("key", "admin"));
                            break;

                        case 7:
                            mAuth.signOut();
                            startActivity(new Intent(AdminHome.this, Login.class));
                            break;
                    }
                });
            }

            @Override
            public int getItemCount() {
                return arrItem.length;
            }
        });


    }
}
