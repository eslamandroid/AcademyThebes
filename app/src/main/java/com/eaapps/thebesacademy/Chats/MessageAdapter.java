package com.eaapps.thebesacademy.Chats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.Messages;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.RetrieveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private Context context;
    private String mCurrentUserId;
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private RetrieveData<Profile> retrieveDataProfile;

    public MessageAdapter(List<Messages> mMessageList, Context context, String mCurrentUserId) {

        this.mMessageList = mMessageList;
        this.context = context;
        this.mCurrentUserId = mCurrentUserId;
        retrieveDataProfile = new RetrieveData<Profile>(context) {
        };
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            case 0:
                return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_single_layout, parent, false));

            case 1:
                return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_single_layout2, parent, false));

            default:
                return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_single_layout, parent, false));
        }

    }

    @Override
    public int getItemViewType(int position) {
        Messages c = mMessageList.get(position);
        if (c.getFrom().equalsIgnoreCase(mCurrentUserId)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);
        String from_user = c.getFrom();
        String message_type = c.getType();
        long time = c.getTime();
        Boolean seen = c.isSeen();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Profile").child(from_user);

        retrieveDataProfile.RetrieveSingleTimes(Profile.class, mUserDatabase, new RetrieveData.CallBackRetrieveTimes<Profile>() {
            @Override
            public void onData(Profile objects) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String formattedDate = dateFormat.format(time);
                viewHolder.time_text_layout.setText(formattedDate);
                Picasso.with(viewHolder.profileImage.getContext()).load(objects.getImageUrl())
                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);
            }

            @Override
            public void hasChildren(boolean c) {

            }

            @Override
            public void exits(boolean e) {

            }
        });

        if (from_user.equals(mCurrentUserId)) {
            viewHolder.read_msg.setVisibility(View.INVISIBLE);
            //viewHolder.message_single_layout.setBackgroundColor(R.color.white);
        } else {
            // viewHolder.message_single_layout.setBackgroundColor(R.color.hint);


        }

        if (seen) {
            viewHolder.read_msg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.read_msg.setVisibility(View.INVISIBLE);
        }

        if (message_type.equals("text")) {
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.GONE);
        } else {
            viewHolder.messageText.setVisibility(View.GONE);
            viewHolder.messageImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(c.getMessage()).into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView time_text_layout;
        CircleImageView profileImage;
        //TextView displayName;
        ImageView messageImage;
        ImageView read_msg;
        LinearLayout message_single_layout;

        private MessageViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            time_text_layout = view.findViewById(R.id.time_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            //displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
            read_msg = view.findViewById(R.id.read_msg);
            message_single_layout = view.findViewById(R.id.message_single_layout);

        }
    }


}
