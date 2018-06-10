package com.eaapps.thebesacademy.Post;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaapps.thebesacademy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by eslamandroid on 3/15/18.
 */

public class AdapterNew extends RecyclerView.Adapter<AdapterNew.viewHolder> {
    Context context;
    List<Post> posts;
    String id;
    DatabaseReference dataLike;
    int width;

    public AdapterNew(Context context, List<Post> posts, String id, int width) {
        this.context = context;
        this.posts = posts;
        this.id = id;
        this.width = width;
        dataLike = FirebaseDatabase.getInstance().getReference().child("News");

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Post post = posts.get(position);
        if (TextUtils.isEmpty(post.getText())) {
            holder.nText.setVisibility(View.GONE);
        } else {
            holder.nText.setText(post.getText());
        }

        if (post.getUrl() != null) {
            if (post.getUrl().size() > 0) {
                if (post.getUrl().size() == 1) {
                    ImageView imageView = new ImageView(context);
                    Picasso.with(context).load(post.getUrl().get(0)).into(imageView);
                    imageView.setPadding(0, 5, 0, 0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(width, 400));
                    holder.nImage.addView(imageView);
                    imageView.setOnClickListener(v -> dialog(post.getUrl().get(0)));
                }
                if (post.getUrl().size() == 2) {
                    ImageView imageView = new ImageView(context);
                    Picasso.with(context).load(post.getUrl().get(0)).into(imageView);
                    imageView.setPadding(0, 5, 0, 0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                            400));
                    ImageView imageView1 = new ImageView(context);
                    Picasso.with(context).load(post.getUrl().get(1)).into(imageView1);
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                            400));
                    imageView1.setX(width / 2);
                    imageView1.setPadding(5, 5, 0, 0);
                    holder.nImage.addView(imageView);
                    holder.nImage.addView(imageView1);
                    imageView.setOnClickListener(v -> dialog(post.getUrl().get(0)));
                    imageView1.setOnClickListener(v -> dialog(post.getUrl().get(1)));
                }
                if (post.getUrl().size() == 3) {
                    ImageView imageView = new ImageView(context);
                    imageView.setId(0);
                    Picasso.with(context).load(post.getUrl().get(0)).into(imageView);
                    imageView.setPadding(0, 5, 0, 0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                            400));
                    ImageView imageView1 = new ImageView(context);
                    imageView1.setId(1);
                    Picasso.with(context).load(post.getUrl().get(1)).into(imageView1);
                    imageView1.setX(width / 2);
                    imageView1.setPadding(5, 5, 0, 0);
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                            200));
                    ImageView imageView2 = new ImageView(context);
                    imageView2.setId(2);
                    Picasso.with(context).load(post.getUrl().get(2)).into(imageView2);
                    imageView2.setX(width / 2);
                    imageView2.setY(200);
                    imageView2.setPadding(5, 5, 0, 0);
                    imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                            200));
                    holder.nImage.addView(imageView);
                    holder.nImage.addView(imageView1);
                    holder.nImage.addView(imageView2);


                    imageView.setOnClickListener(v -> dialog(post.getUrl().get(0)));
                    imageView1.setOnClickListener(v -> dialog(post.getUrl().get(1)));
                    imageView2.setOnClickListener(v -> dialog(post.getUrl().get(2)));


                }
            }


        } else {
            holder.nImage.setVisibility(View.GONE);
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedDate = dateFormat.format(post.getTime_post());

        holder.nTime.setText(formattedDate);

        dataLike.child(post.getId()).child("UserLikes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild(id)) {
                        holder.nLike.setImageResource(R.drawable.liker64);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.nLike.setOnClickListener(v -> {
            Post like = new Post();
            like.makeLike(dataLike.child(post.getId()), id);
            holder.nLike.setImageResource(R.drawable.liker64);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void dialog(String url) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_image);
        ImageView img = dialog.findViewById(R.id.imgFull);
        Picasso.with(context).load(url).into(img);
        dialog.show();
        dialog.setCancelable(true);
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView nTime, nText;
        FrameLayout nImage;
        ImageButton nLike;

        private viewHolder(View itemView) {
            super(itemView);
            nText = itemView.findViewById(R.id.nText);
            nTime = itemView.findViewById(R.id.nTime);
            nImage = itemView.findViewById(R.id.nImage);
            nLike = itemView.findViewById(R.id.nLike);
        }
    }


}
