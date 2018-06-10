package com.eaapps.thebesacademy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.EATextInputEditText;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button sigIn, signUp, dev;
    EATextInputEditText userField, passwordField;
    FirebaseAuth mAuth;
    RetrieveData<Profile> retrieveData;
    DatabaseReference databaseReference;
    Spinner spinner;
    List<String> items = new ArrayList<>();
    String sUserId, sPassword;
    StringBuilder msg;
    StoreKey storeKey;
    String type_account;

    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        storeKey = new StoreKey(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(Login.this, Constants.homeClasses(storeKey.getUser())));
        }

        setContentView(R.layout.activity_login);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        items.add("Student");
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            items.add("Admin");

        }
        items.add("Doctor");
        init();
        retrieveData = new RetrieveData<Profile>(Login.this) {
        };
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Profile");


    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_account = spinner.getItemAtPosition(position).toString();

                if (type_account.equalsIgnoreCase("Doctor") || type_account.equalsIgnoreCase("Admin")) {
                    userField.setHint("Email");
                } else {
                    userField.setHint("UserID");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sigIn = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.btn_signUp);
        dev = findViewById(R.id.dev);
        userField = findViewById(R.id.userId_field);
        passwordField = findViewById(R.id.password_field);
        sigIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        dev.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dev:
                openDialogDev();

                break;
            case R.id.btn_login:

                if (type_account.equalsIgnoreCase("Doctor") || type_account.equalsIgnoreCase("Admin")) {
                    userField.setHint("Email");
                } else {
                    userField.setHint("UserID");

                }
                if (hasEmpty()) {
                    spotsDialog.show();
                    if (type_account.equalsIgnoreCase("Doctor") || type_account.equalsIgnoreCase("Admin")) {
                        mAuth.signInWithEmailAndPassword(sUserId, sPassword).addOnSuccessListener(authResult -> {
                            storeKey.setUser(type_account);

                            startActivity(new Intent(Login.this,
                                    Constants.homeClasses(type_account)));

                            spotsDialog.dismiss();

                        }).addOnFailureListener(e -> e.printStackTrace());


                    } else {
                        Query query = databaseReference
                                .orderByChild("userId").equalTo(sUserId);

                        retrieveData.RetrieveList(Profile.class, query, new RetrieveData.CallBackRetrieveList<Profile>() {
                            @Override
                            public void onDataList(List<Profile> object, int countChild) {
                                Profile profile = object.get(0);
                                if (profile != null) {
                                    mAuth.signInWithEmailAndPassword(profile.getEmail(), profile.getUserId()).addOnSuccessListener(authResult -> {
                                        storeKey.setUser(type_account);
                                        startActivity(new Intent(Login.this,
                                                Constants.homeClasses(type_account)));

                                        spotsDialog.dismiss();

                                    }).addOnFailureListener(e -> e.printStackTrace());

                                }
                            }

                            @Override
                            public void onChangeList(List<Profile> object, int position) {

                            }

                            @Override
                            public void onRemoveFromList(int removePosition) {

                            }

                            @Override
                            public void exits(boolean e) {

                                if (!e) {
                                    spotsDialog.dismiss();
                                    Constants.customDialogAlter(Login.this, R.layout.custom_dialog2, "This UserId or Email not Register before. \n\n" + msg.toString());

                                }
                            }

                            @Override
                            public void hasChildren(boolean c) {

                            }
                        });


                    }

                }
                break;
            case R.id.btn_signUp:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                break;
        }
    }


    private void openDialogDev() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Osama Ezzat", "01276743441", R.drawable.osama));
        people.add(new Person("Mustafa Mahmoud", "01110565443", R.drawable.musafa));
        people.add(new Person("Ahmed Ibrahim", "01158865594", R.drawable.ibrahim));
        people.add(new Person("Ahmed Faysal", "01025524456", R.drawable.fasal));
        people.add(new Person("Ahmed Maher", "01271554776", R.drawable.mahar));


        Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dev);
        dialog.show();
        dialog.setCancelable(false);

        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        RecyclerView recyclerView = dialog.findViewById(R.id.recycleDev);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Login.this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(Login.this).inflate(R.layout.custom_d, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                Person person = people.get(position);
                TextView id = holder.itemView.findViewById(R.id.id);
                ImageView im = holder.itemView.findViewById(R.id.im);

                im.setImageResource(person.getImg());
                id.setText("Name: " + person.getName() + "\n" + "Tel: " + person.getTel());


            }

            @Override
            public int getItemCount() {
                return people.size();
            }
        });


    }

    private boolean hasEmpty() {
        sUserId = userField.getText().toString().trim();
        sPassword = passwordField.getText().toString().trim();
        if (!TextUtils.isEmpty(sUserId) && !TextUtils.isEmpty(sPassword)) {
            return true;

        } else {
            ArrayList arr = Constants.getStringsEmpty(new String[]{sUserId, sPassword}
                    , new String[]{"UserId", "Password"});
            msg = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                msg.append(arr.get(i)).append(" Is Empty Field\n");
            }
            Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

        }
        return false;
    }

}
