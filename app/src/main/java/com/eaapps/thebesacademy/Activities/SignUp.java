package com.eaapps.thebesacademy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.EATextInputEditText;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_REQUEST = 1;
    static boolean isMaster = false;
    Button btn_register;
    Spinner spinner;
    EATextInputEditText fName, fEmail, fUserId, fPhone, fMaster, fSection, fUserCode;
    TextInputLayout layId, layCode;
    FirebaseAuth mAuth;
    String sName, sEmail, sUserId, sUserCode, sPhone, sMaster, sSection, type_account;
    StringBuilder msg;
    DatabaseReference userProfile;
    StorageReference mStorage;
    List<String> items = new ArrayList<>();
    Profile profile;
    SpotsDialog spotsDialog;
    StoreKey storeKey;
    String password = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUp.this, Login.class));
        }
        setContentView(R.layout.activity_sign_up);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        items.add("Student");
        items.add("Doctor");
        mStorage = FirebaseStorage.getInstance().getReference();
        userProfile = FirebaseDatabase.getInstance().getReference().child("Profile");
        storeKey = new StoreKey(this);
        initToolbar();
        init();


    }

    private void init() {
        btn_register = findViewById(R.id.btn_register);
        layCode = findViewById(R.id.lay_userCode);
        layId = findViewById(R.id.lay_userId);
        fName = findViewById(R.id.nameField);
        fEmail = findViewById(R.id.emailField);
        fUserId = findViewById(R.id.userId_field);
        fUserCode = findViewById(R.id.userCode_field);
        fPhone = findViewById(R.id.phone_field);
        fMaster = findViewById(R.id.master_field);
        fSection = findViewById(R.id.section_Field);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_account = spinner.getItemAtPosition(position).toString();
                if (type_account.equalsIgnoreCase("Doctor")) {
                    fUserCode.setVisibility(View.GONE);
                    fUserId.setVisibility(View.GONE);
                    layCode.setVisibility(View.GONE);
                    layId.setVisibility(View.GONE);
                } else {
                    fUserCode.setVisibility(View.VISIBLE);
                    fUserId.setVisibility(View.VISIBLE);
                    layCode.setVisibility(View.VISIBLE);
                    layId.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fSection.setOnClickListener(this);
        fMaster.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });
    }

    private void createAccount() {
        if (hasEmpty()) {
            if (type_account.equalsIgnoreCase("Doctor")) {
                password = sEmail;
            } else {
                password = sUserId;
            }



            mAuth.createUserWithEmailAndPassword(sEmail, password).addOnSuccessListener(authResult -> {
                spotsDialog.show();
                DatabaseReference container = userProfile.child(authResult.getUser().getUid());
                profile = new Profile();
                profile.setName(sName);
                profile.setEmail(sEmail);
                profile.setId(authResult.getUser().getUid());

                if (type_account.equalsIgnoreCase("Student")) {
                    profile.setUserCode(sUserCode);
                    profile.setUserId(sUserId);
                }

                profile.setPhone(sPhone);
                profile.setMaster(sMaster);
                profile.setSection(sSection);
                profile.setOnline("true");
                profile.setType_account(type_account);
                container.setValue(profile.addProfile()).addOnCompleteListener(task -> {
                    storeKey.setUser(type_account);
                    startActivity(new Intent(SignUp.this,
                            Constants.homeClasses(type_account)));
                    if (task.isSuccessful()) {
                        if (spotsDialog.isShowing()) {
                            spotsDialog.dismiss();
                        }

                    }
                });


            });

        }

    }

    private boolean hasEmpty() {
        sName = fName.getText().toString();
        sEmail = fEmail.getText().toString();
        sUserId = fUserId.getText().toString();
        sUserCode = fUserCode.getText().toString();
        sPhone = fPhone.getText().toString();
        sMaster = fMaster.getText().toString();
        sSection = fSection.getText().toString();

        if (!type_account.equalsIgnoreCase("Doctor")) {
            if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sEmail) &&
                    !TextUtils.isEmpty(sUserId) && !TextUtils.isEmpty(sUserCode) &&
                    !TextUtils.isEmpty(sPhone) && !TextUtils.isEmpty(sMaster) &&
                    !TextUtils.isEmpty(sSection)) {
                return true;

            } else {
                ArrayList arr = Constants.getStringsEmpty(new String[]{sName, sEmail, sUserId, sUserCode, sPhone, sMaster, sSection}
                        , new String[]{"Name", "Email", "UserId", "UserCode", "Phone", "Master", "Section"});
                msg = new StringBuilder();
                for (int i = 0; i < arr.size(); i++) {
                    msg.append(arr.get(i)).append(" Is Empty Field\n");
                }
                Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

            }
        } else {
            if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sEmail) &&
                    !TextUtils.isEmpty(sPhone) && !TextUtils.isEmpty(sMaster) &&
                    !TextUtils.isEmpty(sSection)) {
                return true;

            } else {
                ArrayList arr = Constants.getStringsEmpty(new String[]{sName, sEmail, sPhone, sMaster, sSection}
                        , new String[]{"Name", "Email", "Phone", "Master", "Section"});
                msg = new StringBuilder();
                for (int i = 0; i < arr.size(); i++) {
                    msg.append(arr.get(i)).append(" Is Empty Field\n");
                }
                Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

            }
        }

        return false;
    }


    private void selectList(final Context context, String[] listChoice) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_choice_type);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        dialog.show();
        dialog.setCancelable(false);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_list_choice_type, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView = holder.itemView.findViewById(R.id.txt);
                textView.setText("  " + listChoice[position]);
                holder.itemView.setOnClickListener(v -> {

                    if (isMaster) {
                        sMaster = listChoice[position];
                        fMaster.setText(sMaster);

                    } else {
                        sSection = listChoice[position];
                        fSection.setText(sSection);
                    }
                    dialog.dismiss();
                });

            }

            @Override
            public int getItemCount() {
                return listChoice.length;
            }
        });
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.master_field:
                isMaster = true;
                selectList(SignUp.this, new String[]{"Computer Science", "Information Systems", "Accounting", "Business Administration"});
                break;
            case R.id.section_Field:
                isMaster = false;
                selectList(SignUp.this, new String[]{"English", "Arabic"});
                break;
            case R.id.btn_register:
                createAccount();
                break;

        }
    }

}
