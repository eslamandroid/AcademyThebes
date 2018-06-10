package com.eaapps.thebesacademy.Post;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;

import com.eaapps.thebesacademy.Admin.AdminHome;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowNews extends AppCompatActivity {


    RecyclerView rcNews;
    AdapterNew adapterNew;
    List<Post> postList = new ArrayList<>();
    String uid;
    RetrieveData<Post> postRetrieveData;
    DatabaseReference dataPost;
    FirebaseAuth mAuth;
    Bundle bundle;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_show_news);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("key");
        }


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int sst = width - 20;

        initToolbar();
        dataPost = FirebaseDatabase.getInstance().getReference().child("News");
        dataPost.keepSynced(true);
        rcNews = findViewById(R.id.recycleNews);
        rcNews.setHasFixedSize(false);
        rcNews.setLayoutManager(new LinearLayoutManager(this));
        adapterNew = new AdapterNew(ShowNews.this, postList, uid, width);
        rcNews.setAdapter(adapterNew);

        postRetrieveData = new RetrieveData<Post>(ShowNews.this) {
            @Override
            public boolean hasKey(Post object, List<Post> listRetrieve) {
                for (Post post : listRetrieve) {
                    if (post.getId().contains(object.getId())) {
                        return true;
                    }
                }
                return false;
            }
        };

        postRetrieveData.RetrieveList(Post.class, dataPost, new RetrieveData.CallBackRetrieveList<Post>() {

            @Override
            public void onDataList(List<Post> object, int countChild) {
                Post post = object.get(0);
                if (post != null && !postRetrieveData.hasKey(post, postList)) {
                    postList.add(post);
                    adapterNew.notifyDataSetChanged();
                }
            }

            @Override
            public void onChangeList(List<Post> object, int position) {

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


    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(v -> {
            if (key.equalsIgnoreCase("admin")) {
                startActivity(new Intent(ShowNews.this, AdminHome.class));
            } else {
                startActivity(new Intent(ShowNews.this, StudentHome.class));

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
