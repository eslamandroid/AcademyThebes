package com.eaapps.thebesacademy.Chats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eaapps.thebesacademy.Model.ItemUser;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class AllUser extends Fragment {


    List<ItemUser> itemUserList = new ArrayList<>();
    ItemUserAdapter itemUserAdapter;

    RetrieveData<Profile> profileRetrieveData;
    DatabaseReference profileData;
    String uid, master, level;
    FirebaseAuth mAuth;

    public AllUser() {
    }

    public static AllUser newInstance() {
        return new AllUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user != null ? user.getUid() : null;

        profileData = FirebaseDatabase.getInstance().getReference().child("Profile");
        profileRetrieveData = new RetrieveData<Profile>(getContext()) {
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_user, container, false);
        RecyclerView recyclerUsers = view.findViewById(R.id.recyclerUsers);
        recyclerUsers.setHasFixedSize(false);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        itemUserAdapter = new ItemUserAdapter(getContext(), itemUserList, "all", uid);
        recyclerUsers.setAdapter(itemUserAdapter);

        profileRetrieveData.RetrieveSingleTimes(Profile.class, profileData.child(uid), objects -> {
            if (objects != null) {
                master = objects.getMaster();
                level = objects.getLevel();
            }
        });

        Query query = profileData.orderByChild("master").equalTo(master);
        profileRetrieveData.RetrieveList(Profile.class, query, new RetrieveData.CallBackRetrieveList<Profile>() {
            @Override
            public void onDataList(List<Profile> object, String key) {
                Profile profile = object.get(0);
                if (profile != null) {
                    if (profile.getLevel().equalsIgnoreCase(level)) {
                        itemUserList.add(new ItemUser(profile, null));
                        itemUserAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChangeList(List<Profile> object, int position) {

            }

            @Override
            public void onRemoveFromList(int removePosition) {

            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
