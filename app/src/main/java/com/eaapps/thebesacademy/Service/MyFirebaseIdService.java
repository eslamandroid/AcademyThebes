package com.eaapps.thebesacademy.Service;

import com.eaapps.thebesacademy.Chats.Tokens;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by eslamandroid on 12/12/17.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToServer(refreshToken);
    }

    private void updateTokenToServer(String refreshToken) {
        System.out.println("eslamgamal1");
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Token");
        Tokens token = new Tokens(refreshToken);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            tokens.child(user.getUid()).setValue(token);
        }

    }
}
