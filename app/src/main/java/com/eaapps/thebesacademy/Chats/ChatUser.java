package com.eaapps.thebesacademy.Chats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eaapps.thebesacademy.Model.Chat;
import com.eaapps.thebesacademy.Model.ItemUser;
import com.eaapps.thebesacademy.Model.Messages;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatUser extends Fragment {


    FirebaseAuth mAuth;
    String mCurrentUserId;
    StoreKey storeKey;
    ItemUserAdapter itemUserAdapter;
    List<ItemUser> itemUserList = new ArrayList<>();
    DatabaseReference UserInteractMe, profileData, messageData;
    RetrieveData<Messages> messagesRetrieveData;
    RetrieveData<Profile> profileRetrieveData;
    RetrieveData<Chat> chatRetrieveData;
    List<Profile> profile = new ArrayList<>();
    List<Messages> messages = new ArrayList<>();

    public ChatUser() {
    }

    public static ChatUser newInstance() {
        ChatUser fragment = new ChatUser();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeKey = new StoreKey(getContext());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mCurrentUserId = user != null ? user.getUid() : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_user, container, false);
        RecyclerView recyclerChat = view.findViewById(R.id.chatUser);
        recyclerChat.setHasFixedSize(false);
        recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
        itemUserAdapter = new ItemUserAdapter(getContext(), itemUserList, "chatUser", mCurrentUserId);
        recyclerChat.setAdapter(itemUserAdapter);
        UserInteractMe = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrentUserId);
        profileData = FirebaseDatabase.getInstance().getReference().child("Profile");
        messageData = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUserId);

        UserInteractMe.keepSynced(true);
        profileData.keepSynced(true);
        messageData.keepSynced(true);

        profileRetrieveData = new RetrieveData<Profile>(getContext()) {
        };
        messagesRetrieveData = new RetrieveData<Messages>(getContext()) {
        };

        chatRetrieveData = new RetrieveData<Chat>(getContext()) {
        };


        return view;

    }

    @Override
    public void onStart() {

        chatRetrieveData.RetrieveList(Chat.class, UserInteractMe, new RetrieveData.CallBackRetrieveList<Chat>() {
            @Override
            public void onDataList(List<Chat> object, String key) {
                Chat conv = object.get(0);
                if (conv != null) {
                    profileRetrieveData.RetrieveSingleTimes(Profile.class, profileData.child(conv.getId()), new RetrieveData.CallBackRetrieveTimes<Profile>() {
                        @Override
                        public void onData(Profile objects) {
                            if (objects != null) {
                                Query queryMessage = messageData.child(objects.getId()).limitToLast(1);
                                messagesRetrieveData.RetrieveList(Messages.class, queryMessage, new RetrieveData.CallBackRetrieveList<Messages>() {
                                    @Override
                                    public void onDataList(List<Messages> object, String key) {
                                        Messages messages = object.get(0);
                                        if (messages != null) {
                                            System.out.println("idKey: " + key);
                                            itemUserList.add(new ItemUser(objects, messages));
                                            itemUserAdapter.notifyDataSetChanged();
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
                        }
                    });
                }
            }

            @Override
            public void onChangeList(List<Chat> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
