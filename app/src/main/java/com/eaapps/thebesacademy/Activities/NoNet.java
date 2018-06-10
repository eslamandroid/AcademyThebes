package com.eaapps.thebesacademy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;

public class NoNet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_net);
    }

    @Override
    public void onBackPressed() {
    }

    public void onCheck(View view) {

        if (Constants.CheckInternet(NoNet.this)) {
            startActivity(new Intent(NoNet.this, Login.class));
        } else {
            Toast.makeText(NoNet.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }
}
