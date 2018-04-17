package com.eaapps.thebesacademy.Chats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatHome extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    FirebaseAuth mAuth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user != null ? user.getUid() : null;
        setContentView(R.layout.activity_chat_home);
        initToolbar();

        tabLayout = findViewById(R.id.tabChat);
        viewPager = findViewById(R.id.viewChat);

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("Profile").child(uid);
        mUserRef.child("online").setValue("true");

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(ChatHome.this, StudentHome.class));
        });
    }

    @Override
    public void onBackPressed() {

    }

    private static class TabsAdapter extends FragmentPagerAdapter {
        String[] arr = {"Messages", "All your colleagues"};

        Fragment[] arrF = {ChatUser.newInstance(), AllUser.newInstance()};

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Fragment getItem(int i) {

            return arrF[i];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arr[position];
        }

    }
}
