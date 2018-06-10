package com.eaapps.thebesacademy.Chats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaapps.thebesacademy.Model.ItemUser;
import com.eaapps.thebesacademy.Model.Messages;
import com.eaapps.thebesacademy.Model.Profile;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemUserAdapter extends RecyclerView.Adapter<ItemUserAdapter.viewHolder> {
    Context context;
    List<ItemUser> itemUsers;
    String key;
    String myUid;

    public ItemUserAdapter(Context context, List<ItemUser> itemUsers, String key, String myUid) {
        this.context = context;
        this.itemUsers = itemUsers;
        this.key = key;
        this.myUid = myUid;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.custom_user_chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        ItemUser itemUser = itemUsers.get(position);
        Profile profile = itemUser.getProfile();

        if (profile != null) {
            holder.nameUser.setText(profile.getName());
            Picasso.with(context).load(profile.getImageUrl()).placeholder(R.drawable.default_avatar).into(holder.profileUser);
        }

        Messages messages = itemUser.getMessages();
        if (!key.equalsIgnoreCase("all") && messages != null) {

            if (messages.getType().equals("image")) {
                holder.lastMessage.setText("photo....");

            } else {
                holder.lastMessage.setText(messages.getMessage());
            }
            holder.lastMessage.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessage.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatViewActivity.class);
            intent.putExtra(Constants.PROFILE, profile);
            intent.putExtra("key","chat");
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return itemUsers.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView nameUser, lastMessage;
        CircleImageView profileUser, circleOnline;

        public viewHolder(View itemView) {
            super(itemView);

            nameUser = itemView.findViewById(R.id.nameUser);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            profileUser = itemView.findViewById(R.id.profileUser);
            circleOnline = itemView.findViewById(R.id.circleOnline);

        }
    }
}
