package com.eaapps.thebesacademy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
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

    Button sigIn, signUp;
    EATextInputEditText userField, passwordField;
    FirebaseAuth mAuth;
    RetrieveData<Profile> retrieveData;
    DatabaseReference databaseReference;
    Spinner spinner;
    List<String> items = new ArrayList<>();
    String sUserId, sPassword;
    StringBuilder msg;
    StoreKey storeKey;

    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        storeKey = new StoreKey(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(Login.this, StudentHome.class));
            //startActivity(new Intent(Login.this, Constants.homeClasses(storeKey.getUser())));
        }

        setContentView(R.layout.activity_login);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        items.add("Student");
        items.add("Admin");
        items.add("Teacher");
        init();
        retrieveData = new RetrieveData<Profile>(Login.this) {
        };
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Profile");


    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, items));
        sigIn = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.btn_signUp);
        userField = findViewById(R.id.userId_field);
        passwordField = findViewById(R.id.password_field);
        sigIn.setOnClickListener(this);
        signUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                if (hasEmpty()) {
                    spotsDialog.show();
                    Query query = databaseReference.orderByChild("userId").equalTo(sUserId);
                    retrieveData.hasChild(Profile.class, query, object -> {
                        if (object != null) {
                            String email = object.getEmail();
                            System.out.println("email: " + email);
                            mAuth.signInWithEmailAndPassword(email, sUserId).addOnSuccessListener(authResult -> {
                                storeKey.setUser(items.get(spinner.getSelectedItemPosition()));

                                //  startActivity(new Intent(Login.this,
                                //         Constants.homeClasses(items.get(spinner.getSelectedItemPosition()))));

                                spotsDialog.dismiss();
                                startActivity(new Intent(Login.this, StudentHome.class));

                            }).addOnFailureListener(e -> e.printStackTrace());

                        } else {
                            spotsDialog.dismiss();
                            Constants.customDialogAlter(Login.this, R.layout.custom_dialog2,
                                    "This UserId Is Not Registered In The App Before.");
                        }
                    });

                }
                break;
            case R.id.btn_signUp:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                break;
        }
    }


    private boolean hasEmpty() {
        sUserId = userField.getText().toString();
        sPassword = passwordField.getText().toString();
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
