package com.eaapps.thebesacademy.Student;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.eaapps.thebesacademy.Material.FirebaseModelTables;
import com.eaapps.thebesacademy.Material.ModelTable;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Attendance extends AppCompatActivity {

    final int RequestCameraPermissionID = 1001;
    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    DatabaseReference refAttendance, ref;
    RetrieveData<FirebaseModelTables> retrieveData;
    String material, nameDoctor;
    Bundle bundle;
    Profile profile;
    SpotsDialog spotsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        spotsDialog.show();
        initToobar();
        bundle = getIntent().getExtras();
        profile = bundle != null ? bundle.getParcelable(Constants.PROFILE) : null;
        if (profile == null) {
            startActivity(new Intent(Attendance.this, StudentHome.class));
        }


        refAttendance = FirebaseDatabase.getInstance().getReference().child("Attendance");
        ref = FirebaseDatabase.getInstance().getReference();
        retrieveData = new RetrieveData<FirebaseModelTables>(Attendance.this) {
        };
        Query query = ref.child("Tables").child("Lecture").child(profile.getMaster()).orderByChild("level").equalTo(profile.getLevel());

        retrieveData.RetrieveList(FirebaseModelTables.class, query, new RetrieveData.CallBackRetrieveList<FirebaseModelTables>() {

            @Override
            public void onDataList(List<FirebaseModelTables> object, int countChild) {
                FirebaseModelTables firebaseModelTables = object.get(0);
                if (firebaseModelTables != null) {
                    for (ModelTable m : firebaseModelTables.getTablesDetails()) {
                        if (m.getDay().contains(getDayEn(getDay()))) {
                            material = m.getName_material();
                            nameDoctor = m.getName_teacher();
                            spotsDialog.dismiss();
                            break;
                        }
                    }

                }
            }

            @Override
            public void onChangeList(List<FirebaseModelTables> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }

            @Override
            public void exits(boolean e) {

            }

            @Override
            public void hasChildren(boolean c) {

            }
        });

        cameraPreview = findViewById(R.id.cameraPreview);
        txtResult = findViewById(R.id.txtResult);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();


        //Add Event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if (ActivityCompat.checkSelfPermission(Attendance.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    ActivityCompat.requestPermissions(Attendance.this, new String[]{android.Manifest.permission.CAMERA},
                            50);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            public boolean isValidDate(String inDate) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateFormat.setLenient(false);
                try {
                    dateFormat.parse(inDate.trim());
                } catch (ParseException pe) {
                    return false;
                }
                return true;
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0) {
                    txtResult.post(() -> {
                        //Create vibrate
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        assert vibrator != null;
                        vibrator.vibrate(1000);

                        if (isValidDate(qrcodes.valueAt(0).displayValue)) {
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            DatabaseReference data = refAttendance.child(profile.getMaster()).child(material).child(profile.getId());
                            data.child("name_student").setValue(profile.getName());
                            data.child("name_doctor").setValue(nameDoctor);
                            data.child("id").setValue(profile.getId());
                            DatabaseReference dateSign = data.child("dateAttendance").child(qrcodes.valueAt(0).displayValue);
                            dateSign.child("id").setValue(qrcodes.valueAt(0).displayValue);
                            dateSign.child("timestamp").setValue(timestamp.getTime());
                            txtResult.setText("Attendance were successful \n can you continue lecture :)");

                        } else {
                            txtResult.setText("Please Try Again Because QR Code Wrong :(");

                        }
                    });
                }
            }
        });

    }

    private String getDayAr(String s) {
        switch (s) {
            case "السبت":
                return "Sunday";
            case "الاحد":
                return "Saturday";
            case "الجمعه":
                return "Friday";
            case "الخميس":
                return "Thursday";
            case "الاربعاء":
                return "Wednesday";
            case "الثلاثاء":
                return "Tuesday";
            case "الاثنين":
                return "Monday";
            default:
                return "";
        }
    }

    private String getDayEn(String s) {
        switch (s) {
            case "Sunday":
                return "السبت";
            case "Saturday":
                return "الاحد";
            case "Friday":
                return "الجمعه";
            case "Thursday":
                return "الخميس";
            case "Wednesday":
                return "الاربعاء";
            case "Tuesday":
                return "الثلاثاء";
            case "Monday":
                return "الاثنين";
            default:
                return "";
        }
    }

    private String getDay() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayString = sdf.format(new Date());
        return dayString;
    }

    private void initToobar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(Attendance.this, StudentHome.class));
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 50:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start your camera handling here
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constants.customToast(Attendance.this, "Please allow the permission So the app works successfully");
                }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
