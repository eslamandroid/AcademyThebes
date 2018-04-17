package com.eaapps.thebesacademy.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.eaapps.thebesacademy.Material.FirebaseModelTables;
import com.eaapps.thebesacademy.Material.ModelTable;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AttendanceList extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, AdapterView.OnItemSelectedListener {

    static boolean hasFind = false;
    String strDate;
    RecyclerView recycleAttendance;
    boolean isFrom = false;
    DatabaseReference ref;
    RetrieveData<Profile> profileRetrieveData;
    RetrieveData<Attendances> attendancesRetrieveData;
    List<Attendances> attendancesList = new ArrayList<>();
    List<Attendances> attendancesListFilter = new ArrayList<>();
    RecyclerView.Adapter adapter;
    EditText edit_Search;
    ImageButton btn_Search;
    boolean hasSearch = false;
    Spinner spinner, spinner1;
    String section, material, uid, nameTeacher;
    List<String> materials = new ArrayList<>();
    ArrayAdapter aMaterial;
    String[] sectionArr = {"Computer Science", "Information Systems", "Accounting", "Business Administration"};

    SpotsDialog spotsDialog;

    RetrieveData<FirebaseModelTables> modelTableRetrieveData;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            uid = user.getUid();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        spotsDialog.show();
        initToolbar();

        modelTableRetrieveData = new RetrieveData<FirebaseModelTables>(AttendanceList.this) {
        };


        edit_Search = findViewById(R.id.edit_Search);
        btn_Search = findViewById(R.id.btn_Search);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.text_spinner_type, sectionArr));
        spinner.setOnItemSelectedListener(this);


        spinner1 = findViewById(R.id.spinner1);
        aMaterial = new ArrayAdapter<>(this, R.layout.text_spinner_type, materials);
        spinner1.setAdapter(aMaterial);
        spinner1.setOnItemSelectedListener(this);


        recycleAttendance = findViewById(R.id.recycleAttendance);
        recycleAttendance.setHasFixedSize(false);
        recycleAttendance.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance().getReference();

        attendancesRetrieveData = new RetrieveData<Attendances>(AttendanceList.this) {
        };
        profileRetrieveData = new RetrieveData<Profile>(AttendanceList.this) {
        };

        profileRetrieveData.RetrieveSingleTimes(Profile.class, ref.child("Profile").child(uid), objects -> {
            if (objects != null) {
                nameTeacher = objects.getName();
                spotsDialog.dismiss();
            }
        });

        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(AttendanceList.this).inflate(R.layout.custom_list_attendance, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                Attendances attendances = attendancesListFilter.get(position);
                View view = holder.itemView;
                TextView name = view.findViewById(R.id.sName);
                TextView id = view.findViewById(R.id.sId);
                TextView numAttendance = view.findViewById(R.id.sAttendance);

                name.setText("Student Name: " + attendances.getName_student());
                id.setText("Student Academy Code: " + attendances.getCode_academy());
                numAttendance.setText("Number Attendance: " + attendances.getDateAttendance().size());

            }

            @Override
            public int getItemCount() {
                return attendancesListFilter.size();
            }
        };

        recycleAttendance.setAdapter(adapter);

        edit_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (attendancesList.size() > 0) {
                    attendancesListFilter.clear();
                    adapter.notifyDataSetChanged();
                    for (Attendances at : attendancesList) {
                        if (at.getCode_academy().equals(edit_Search.getText().toString())) {
                            attendancesListFilter.add(at);
                            hasSearch = true;
                        }
                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_Search.setOnClickListener(v -> {

            if (edit_Search.isEnabled()) {
                edit_Search.setEnabled(false);
                btn_Search.setImageResource(R.drawable.cancel32);

            } else {
                edit_Search.setEnabled(true);
                btn_Search.setImageResource(R.drawable.search32);


            }
        });


    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        strDate = dayOfMonth + "-" + ++monthOfYear + "-" + year;

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(AttendanceList.this, StudentHome.class));
        });

    }

    private void openPickerDate() {
        final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
        DateTime now = DateTime.now();
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(AttendanceList.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth())
                .setDateRange(null, null)
                .setDoneText("OK")
                .setCancelText("Cancel")
                .setThemeCustom(R.style.MyCustomBetterPickersDialogs);
        cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

        switch (parent.getId()) {
            case R.id.spinner:
                materials.clear();
                spotsDialog.show();
                aMaterial.notifyDataSetChanged();
                section = spinner.getItemAtPosition(i).toString();
                getMaterial(spinner.getItemAtPosition(i).toString());
                break;
            case R.id.spinner1:
                material = spinner1.getItemAtPosition(i).toString();
                getListAttendance();
                break;


        }

    }

    private void getListAttendance() {
        attendancesListFilter.clear();
        attendancesRetrieveData.RetrieveList(Attendances.class, ref.child("Attendance").child("Computer Science").child("math2")
                , new RetrieveData.CallBackRetrieveList<Attendances>() {
                    @Override
                    public void onDataList(List<Attendances> object, String key) {
                        Attendances attendances = object.get(0);
                        if (attendances != null)
                            attendancesList.add(attendances);
                        attendancesListFilter.addAll(attendancesList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChangeList(List<Attendances> object, int position) {

                    }

                    @Override
                    public void onRemoveFromList(int removePosition) {

                    }
                });
    }

    private void getMaterial(String s) {
        materials.clear();
        modelTableRetrieveData.RetrieveList(FirebaseModelTables.class, ref.child("Tables").child("Lecture").child(s), new RetrieveData.CallBackRetrieveList<FirebaseModelTables>() {
            @Override
            public void onDataList(List<FirebaseModelTables> object, String key) {
                FirebaseModelTables firebaseModelTables = object.get(0);
                if (firebaseModelTables != null) {
                    for (ModelTable t : firebaseModelTables.getTablesDetails()) {
                        if (t.getName_teacher().equals(nameTeacher)) {
                            materials.add(t.getName_material());
                            aMaterial.notifyDataSetChanged();
                            hasFind = true;
                            spotsDialog.dismiss();
                        }
                    }
                } else {
                    spotsDialog.dismiss();
                }
                if (!hasFind) {
                    spotsDialog.dismiss();
                }

            }

            @Override
            public void onChangeList(List<FirebaseModelTables> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
