package com.eaapps.thebesacademy.Chats;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.eaapps.thebesacademy.Model.Chat;
import com.eaapps.thebesacademy.Model.Messages;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.ChatDoctors;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.Data;
import com.eaapps.thebesacademy.Utils.FCMResponse;
import com.eaapps.thebesacademy.Utils.GetTimeAgo;
import com.eaapps.thebesacademy.Utils.InterfaceRetrofit;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.eaapps.thebesacademy.Utils.SendNotifications;
import com.eaapps.thebesacademy.Utils.Sender;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

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
    String myName;
    DatabaseReference refRoot, messagesWithMe;
    FirebaseAuth mAuth;
    String uid;
    RetrieveData<Messages> messagesRetrieveData;
    List<Messages> messagesList = new ArrayList<>();
    SwipeRefreshLayout mRefreshLayout;
    StorageReference mImageStorage;
    String key;
    RetrieveData<Profile> profileRetrieveData;
    RetrieveData<Tokens> tokensRetrieveData;
    StoreKey storeKey;
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
        storeKey = new StoreKey(this);

        bundle = getIntent().getExtras();

        profile = bundle != null ? bundle.getParcelable(Constants.PROFILE) : null;
        key = bundle.getString("key");
        if (profile != null) {
            if (profile.getImageUrl() != null) {
                initToolbar(profile.getName(), profile.getOnline(), profile.getImageUrl());
            } else {
                initToolbar(profile.getName(), profile.getOnline(), null);
            }
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


        profileRetrieveData = new RetrieveData<Profile>(ChatViewActivity.this) {
        };

        profileRetrieveData.RetrieveSingleTimes(Profile.class, refRoot.child("Profile").child(uid), new RetrieveData.CallBackRetrieveTimes<Profile>() {
            @Override
            public void onData(Profile objects) {
                if (objects != null) {
                    myName = objects.getName();
                }
            }

            @Override
            public void hasChildren(boolean c) {

            }

            @Override
            public void exits(boolean e) {

            }
        });

        tokensRetrieveData = new RetrieveData<Tokens>(ChatViewActivity.this) {
        };

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
            public void onDataList(List<Messages> object, int countChild) {
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

            @Override
            public void exits(boolean e) {

            }

            @Override
            public void hasChildren(boolean c) {

            }
        });

    }

    private void loadMessage() {
        messagesWithMe = refRoot.child("messages").child(uid).child(profile.getId());
        Query messageQuery = messagesWithMe.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messagesRetrieveData.RetrieveList(Messages.class, messageQuery, new RetrieveData.CallBackRetrieveList<Messages>() {


            @Override
            public void onDataList(List<Messages> object, int countChild) {
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

            @Override
            public void exits(boolean e) {

            }

            @Override
            public void hasChildren(boolean c) {

            }
        });

    }

    private void initToolbar(String title, String lastSeenView, String url) {

        Toolbar mChatToolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        TextView mTitleView = findViewById(R.id.custom_bar_title);
        TextView mLastSeenView = findViewById(R.id.custom_bar_seen);
        CircleImageView mProfileImage = findViewById(R.id.custom_bar_image);

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
        if (key.equalsIgnoreCase("chatDoctors")) {

            mChatToolbar.setNavigationOnClickListener(v -> {
                startActivity(new Intent(ChatViewActivity.this, ChatDoctors.class)
                );
            });

        } else {

            mChatToolbar.setNavigationOnClickListener(v -> {
                startActivity(new Intent(ChatViewActivity.this, ChatHome.class)
                        .putExtra("key", storeKey.getUser().toLowerCase()));
            });
        }


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
        DatabaseReference chat_push = refRoot.child("Chat").child(profile.getId()).child(uid);
        DatabaseReference chat_to = refRoot.child("Chat").child(uid).child(profile.getId());


        if (key.equalsIgnoreCase("image")) {
            StorageReference filepath = mImageStorage.child("message_images").child(user_message_push.getKey() + ".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    String download_url = task.getResult().getDownloadUrl().toString();

                    Chat chat_p = new Chat();
                    chat_p.setSeen(true);
                    chat_p.setTimestamp(System.currentTimeMillis());
                    chat_p.setId(uid);
                    chat_push.setValue(chat_p.addStatus());

                    Chat chat_t = new Chat();
                    chat_t.setId(profile.getId());
                    chat_t.setSeen(true);
                    chat_t.setTimestamp(System.currentTimeMillis());
                    chat_to.setValue(chat_t.addStatus());


                    Messages messages = new Messages();
                    messages.setMessage(download_url);
                    messages.setType("image");
                    messages.setFrom(uid);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    messages.setTime(timestamp.getTime());
                    messages.setSeen(false);
                    user_message_push.setValue(messages.addMessage());
                    user_message_to.setValue(messages.addMessage());


                    Data data = new Data(myName, "Attachment");

                    tokensRetrieveData.RetrieveSingleTimes(Tokens.class, refRoot.child("Token").child(profile.getId()), new RetrieveData.CallBackRetrieveTimes<Tokens>() {
                        @Override
                        public void onData(Tokens objects) {
                            if (objects != null) {
                                Sender sender = new Sender(data, objects.getToken());
                                SendNotifications<FCMResponse> sendNotifications = new SendNotifications<>(ChatViewActivity.this);
                                sendNotifications.sendNotify(new InterfaceRetrofit.MapCallBack<FCMResponse>() {
                                    @Override
                                    public Call<FCMResponse> workService(InterfaceRetrofit service) {
                                        return service.sendMessage(sender);
                                    }

                                    @Override
                                    public void onDataMap(Response<FCMResponse> response, Retrofit retrofit) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void hasChildren(boolean c) {

                        }

                        @Override
                        public void exits(boolean e) {

                        }
                    });


                }

            });
        } else if (key.equalsIgnoreCase("text")) {
            if (!TextUtils.isEmpty(editChat.getText().toString())) {

                Chat chat_p = new Chat();
                chat_p.setSeen(true);
                chat_p.setTimestamp(System.currentTimeMillis());
                chat_p.setId(uid);
                chat_push.setValue(chat_p.addStatus());

                Chat chat_t = new Chat();
                chat_t.setId(profile.getId());
                chat_t.setSeen(true);
                chat_t.setTimestamp(System.currentTimeMillis());
                chat_to.setValue(chat_t.addStatus());


                Messages messages = new Messages();
                messages.setMessage(editChat.getText().toString());
                messages.setType("text");
                messages.setFrom(uid);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                messages.setTime(timestamp.getTime());
                messages.setSeen(false);
                user_message_push.setValue(messages.addMessage());
                user_message_to.setValue(messages.addMessage());

                Data data = new Data(myName, editChat.getText().toString());

                tokensRetrieveData.RetrieveSingleTimes(Tokens.class, refRoot.child("Token").child(profile.getId()), new RetrieveData.CallBackRetrieveTimes<Tokens>() {
                    @Override
                    public void onData(Tokens objects) {
                        if (objects != null) {

                            Sender sender = new Sender(data, objects.getToken());
                            SendNotifications<FCMResponse> sendNotifications = new SendNotifications<>(ChatViewActivity.this);
                            sendNotifications.sendNotify(new InterfaceRetrofit.MapCallBack<FCMResponse>() {
                                @Override
                                public Call<FCMResponse> workService(InterfaceRetrofit service) {
                                    return service.sendMessage(sender);
                                }

                                @Override
                                public void onDataMap(Response<FCMResponse> response, Retrofit retrofit) {
                                    if (response.isSuccess()) {
                                        System.out.println("send: yesSend");
                                        editChat.setText("");
                                    }
                                    System.out.println(response.message());
                                }
                            });


                        }
                    }

                    @Override
                    public void hasChildren(boolean c) {

                    }

                    @Override
                    public void exits(boolean e) {

                    }
                });


            }
        }


    }

    @Override
    public void onBackPressed() {

    }


}
