package com.eaapps.thebesacademy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
import com.eaapps.thebesacademy.Teacher.HomeTeacher;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.EATextInputEditText;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_REQUEST = 1;
    static String keys = "";
    SpotsDialog spotsDialog;
    EATextInputEditText fName, fLevel, fSemester, fPhone, fMaster, fSection;
    CircleImageView selectImage;
    String sName, sSemester, sLevel, sPhone, sMaster, sSection, uid;

    DatabaseReference ref;
    RetrieveData<Profile> profileRetrieveData;
    FirebaseAuth mAuth;
    StoreKey storeKey;
    boolean editPhoto = false;
    Uri resultUri;
    StringBuilder msg;
    StorageReference refStorage;
    boolean isDoctor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user != null ? user.getUid() : null;
        setContentView(R.layout.activity_edit_profile);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        spotsDialog.show();
        storeKey = new StoreKey(this);

        initToolbar();

        ref = FirebaseDatabase.getInstance().getReference().child("Profile");
        refStorage = FirebaseStorage.getInstance().getReference().child("Profile");

        profileRetrieveData = new RetrieveData<Profile>(EditProfile.this) {
        };

        selectImage = findViewById(R.id.selectImage);
        fName = findViewById(R.id.nameField);
        fLevel = findViewById(R.id.level_Field);
        fSemester = findViewById(R.id.semester_Field);
        fPhone = findViewById(R.id.phone_field);
        fMaster = findViewById(R.id.master_field);
        fSection = findViewById(R.id.section_Field);

        fMaster.setOnClickListener(this);
        fSection.setOnClickListener(this);
        fLevel.setOnClickListener(this);
        fSemester.setOnClickListener(this);
        selectImage.setOnClickListener(this);


        profileRetrieveData.RetrieveSingleTimes(Profile.class, ref.child(uid), new RetrieveData.CallBackRetrieveTimes<Profile>() {
            @Override
            public void onData(Profile objects) {
                if (objects != null) {
                    sName = objects.getName();
                    fName.setText(sName);

                    sPhone = objects.getPhone();
                    fPhone.setText(sPhone);


                    sMaster = objects.getMaster();
                    fMaster.setText(sMaster);
                    if (objects.getType_account().equalsIgnoreCase("doctor")) {
                        fSemester.setVisibility(View.GONE);
                        fLevel.setVisibility(View.GONE);
                        isDoctor = true;
                    }

                    sSection = objects.getSection();
                    fSection.setText(sSection);

                    if (objects.getImageUrl() != null) {
                        Picasso.with(EditProfile.this).load(objects.getImageUrl()).placeholder(R.drawable.user256).into(selectImage);
                    }

                    if (objects.getLevel() != null && objects.getSemester() != null) {

                        sLevel = objects.getLevel();
                        fLevel.setText(sLevel);

                        sSemester = objects.getSemester();
                        fSemester.setText(sSemester);

                    }
                    spotsDialog.dismiss();

                }
            }

            @Override
            public void hasChildren(boolean c) {

            }

            @Override
            public void exits(boolean e) {

                if (!e) {
                    spotsDialog.dismiss();
                }
            }
        });
        

    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.inflateMenu(R.menu.menu_add_material);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> {
            if (storeKey.getUser().equalsIgnoreCase("Doctor")) {
                startActivity(new Intent(EditProfile.this, HomeTeacher.class));

            } else {
                startActivity(new Intent(EditProfile.this, StudentHome.class));

            }
        });

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.btn_add) {
                if (editPhoto) {
                    spotsDialog.show();
                    refStorage.child(resultUri.getLastPathSegment()).putFile(resultUri).addOnSuccessListener(result -> {
                        Uri downloadUrl = result.getDownloadUrl();
                        edit(downloadUrl.toString());
                        spotsDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Successful Edit", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    spotsDialog.show();
                    edit("");
                    spotsDialog.dismiss();
                    Toast.makeText(EditProfile.this, "Successful Edit", Toast.LENGTH_SHORT).show();
                }


            }
            return true;
        });
    }

    private void edit(String url) {
        Profile profile = new Profile();
        if (hasEmpty()) {
            profile.setName(sName);
            profile.setPhone(sPhone);
            profile.setSection(sSection);
            profile.setMaster(sMaster);

            profile.setSection(sSection);
            if (!isDoctor) {
                profile.setLevel(sLevel);
                profile.setSemester(sSemester);
            }

            if (!TextUtils.isEmpty(url)) {
                profile.setImageUrl(url);
            }
            profile.editProfile(ref.child(uid));

        }
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

                    switch (keys) {
                        case "master":
                            sMaster = listChoice[position];
                            fMaster.setText(sMaster);
                            break;
                        case "section":
                            sSection = listChoice[position];
                            fSection.setText(sSection);
                            break;
                        case "level":
                            sLevel = listChoice[position];
                            fLevel.setText(sLevel);
                            break;
                        case "semester":
                            sSemester = listChoice[position];
                            fSemester.setText(sSemester);
                            break;
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
                keys = "master";
                selectList(EditProfile.this, new String[]{"Computer Science", "Information Systems", "Accounting", "Business Administration"});

                break;
            case R.id.section_Field:
                keys = "section";
                selectList(EditProfile.this, new String[]{"English", "Arabic"});

                break;
            case R.id.selectImage:
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST);
                break;

            case R.id.level_Field:
                keys = "level";
                selectList(EditProfile.this, new String[]{"1", "2", "3", "4"});

                break;
            case R.id.semester_Field:
                keys = "semester";
                selectList(EditProfile.this, new String[]{"1", "2", "3", "4"});

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_material, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(EditProfile.this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                selectImage.setImageURI(resultUri);
                editPhoto = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Error Activity Crop", error.getMessage());
            }
        }


    }

    private boolean hasEmpty() {
        sName = fName.getText().toString();
        sLevel = fLevel.getText().toString();
        sSemester = fSemester.getText().toString();
        sPhone = fPhone.getText().toString();
        sMaster = fMaster.getText().toString();
        sSection = fSection.getText().toString();

        if (storeKey.getUser().equalsIgnoreCase("Doctor")) {
            if (!TextUtils.isEmpty(sName) &&
                    !TextUtils.isEmpty(sPhone) && !TextUtils.isEmpty(sMaster) &&
                    !TextUtils.isEmpty(sSection)) {
                return true;

            } else {
                ArrayList arr = Constants.getStringsEmpty(new String[]{sName, sPhone, sMaster, sSection}
                        , new String[]{"Name", "Phone", "Master", "Section"});
                msg = new StringBuilder();
                for (int i = 0; i < arr.size(); i++) {
                    msg.append(arr.get(i)).append(" Is Empty Field\n");
                }
                Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

            }
        } else {
            if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sLevel) && !TextUtils.isEmpty(sSemester) &&
                    !TextUtils.isEmpty(sPhone) && !TextUtils.isEmpty(sMaster) &&
                    !TextUtils.isEmpty(sSection)) {
                return true;

            } else {
                ArrayList arr = Constants.getStringsEmpty(new String[]{sName, sLevel, sSemester, sPhone, sMaster, sSection}
                        , new String[]{"Name", "Level", "Semester", "Phone", "Master", "Section"});
                msg = new StringBuilder();
                for (int i = 0; i < arr.size(); i++) {
                    msg.append(arr.get(i)).append(" Is Empty Field\n");
                }
                Constants.customDialogAlter(this, R.layout.custom_dialog2, "Please Complete Fields \n\n" + msg.toString());

            }
        }

        return false;
    }

    @Override
    public void onBackPressed() {

    }
}
