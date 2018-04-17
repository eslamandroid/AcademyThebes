package com.eaapps.thebesacademy.Chats;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.Messages;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.GetTimeAgo;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewActivity extends AppCompatActivity {

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private static final int GALLERY_PICK = 1;
    private static boolean isSelectPhoto = false;
    private static int itemPos = 0;
    EditText editChat;
    RecyclerView messageRecycle;
    ImageButton chat_send_btn, chat_add_btn;
    Profile profile;
    Bundle bundle;
    DatabaseReference refRoot, messagesWithMe;
    FirebaseAuth mAuth;
    String uid;
    RetrieveData<Messages> messagesRetrieveData;
    List<Messages> messagesList = new ArrayList<>();
    SwipeRefreshLayout mRefreshLayout;
    StorageReference mImageStorage;
    private String mLastKey = "";
    private String mPrevKey = "";
    private int mCurrentPage = 1;
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user != null ? user.getUid() : null;

        setContentView(R.layout.activity_chat_view);
        bundle = getIntent().getExtras();
        profile = bundle != null ? bundle.getParcelable(Constants.PROFILE) : null;
        if (profile != null) {
            initToolbar(profile.getName(), profile.getOnline(), profile.getImageUrl());
        }

        chat_add_btn = findViewById(R.id.chat_add_btn);
        chat_send_btn = findViewById(R.id.chat_send_btn);
        editChat = findViewById(R.id.editChat);

        mRefreshLayout = findViewById(R.id.message_swipe_layout);

        //recycleListChat
        messageRecycle = findViewById(R.id.messages_list);
        messageRecycle.setHasFixedSize(false);
        mLinearLayout = new LinearLayoutManager(this);
        messageRecycle.setLayoutManager(mLinearLayout);
        mAdapter = new MessageAdapter(messagesList, ChatViewActivity.this, uid);
        messageRecycle.setAdapter(mAdapter);


        messagesRetrieveData = new RetrieveData<Messages>(ChatViewActivity.this) {
        };
        mImageStorage = FirebaseStorage.getInstance().getReference();
        refRoot = FirebaseDatabase.getInstance().getReference();

        loadMessage();

        mRefreshLayout.setOnRefreshListener(() -> {

            mCurrentPage++;

            itemPos = 0;

            loadMoreMessage();


        });


        chat_add_btn.setOnClickListener(view -> {

            isSelectPhoto = true;
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

        });
        chat_send_btn.setOnClickListener(v -> {
            sendMessage(null, "text");
        });


    }

    private void loadMoreMessage() {
        messagesWithMe = refRoot.child("messages").child(uid).child(profile.getId());
        Query messageQuery = messagesWithMe.orderByKey().endAt(mLastKey).limitToLast(10);
        messagesRetrieveData.RetrieveList(Messages.class, messageQuery, new RetrieveData.CallBackRetrieveList<Messages>() {
            @Override
            public void onDataList(List<Messages> object, String key) {
                Messages messages = object.get(0);
                if (messages != null) {
                    if (!mPrevKey.equals(key)) {

                        messagesList.add(itemPos++, messages);

                    } else {

                        mPrevKey = mLastKey;

                    }


                    if (itemPos == 1) {

                        mLastKey = key;

                    }

                    mAdapter.notifyDataSetChanged();

                    mRefreshLayout.setRefreshing(false);

                    mLinearLayout.scrollToPositionWithOffset(10, 0);
                }
            }

            @Override
            public void onChangeList(List<Messages> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });

    }

    private void loadMessage() {
        messagesWithMe = refRoot.child("messages").child(uid).child(profile.getId());
        Query messageQuery = messagesWithMe.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messagesRetrieveData.RetrieveList(Messages.class, messageQuery, new RetrieveData.CallBackRetrieveList<Messages>() {
            @Override
            public void onDataList(List<Messages> object, String key) {
                Messages messages = object.get(0);
                if (messages != null) {
                    itemPos++;
                    if (itemPos == 1) {
                        mLastKey = key;
                        mPrevKey = key;
                    }

                    messagesList.add(messages);
                    mAdapter.notifyDataSetChanged();
                    messageRecycle.scrollToPosition(messagesList.size() - 1);
                    mRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onChangeList(List<Messages> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });

    }

    private void initToolbar(String title, String lastSeenView, String url) {

        Toolbar mChatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        TextView mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        TextView mLastSeenView = (TextView) findViewById(R.id.custom_bar_seen);
        CircleImageView mProfileImage = (CircleImageView) findViewById(R.id.custom_bar_image);

        Picasso.with(ChatViewActivity.this).load(url)
                .placeholder(R.drawable.default_avatar).into(mProfileImage);
        mTitleView.setText(title);
        mLastSeenView.setText(lastSeenView);
        if (lastSeenView.equalsIgnoreCase("true")) {
            mLastSeenView.setText("Online");
        } else {
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            long lastTime = Long.parseLong(lastSeenView);
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, ChatViewActivity.this);
            mLastSeenView.setText(lastSeenTime);
        }
        mChatToolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(ChatViewActivity.this, ChatHome.class));
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            sendMessage(imageUri, "image");

        }

    }

    private void sendMessage(Uri imageUri, String key) {
        DatabaseReference user_message_push = refRoot.child("messages").child(uid).child(profile.getId()).push();
        DatabaseReference user_message_to = refRoot.child("messages").child(profile.getId()).child(uid).child(user_message_push.getKey());
        if (key.equalsIgnoreCase("image")) {
            StorageReference filepath = mImageStorage.child("message_images").child(user_message_push.getKey() + ".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    if (task.isSuccessful()) {
                        String download_url = task.getResult().getDownloadUrl().toString();
                        Messages messages = new Messages();
                        messages.setMessage(download_url);
                        messages.setType("image");
                        messages.setFrom(uid);
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        messages.setTime(timestamp.getTime());
                        messages.setSeen(false);
                        user_message_push.setValue(messages.addMessage());
                        user_message_to.setValue(messages.addMessage());
                    }

                }
            });
        } else if (key.equalsIgnoreCase("text")) {
            if (!TextUtils.isEmpty(editChat.getText().toString())) {
                Messages messages = new Messages();
                messages.setMessage(editChat.getText().toString());
                messages.setType("text");
                messages.setFrom(uid);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                messages.setTime(timestamp.getTime());
                messages.setSeen(false);
                user_message_push.setValue(messages.addMessage());
                user_message_to.setValue(messages.addMessage());
                editChat.setText("");
            }
        }


    }

}
