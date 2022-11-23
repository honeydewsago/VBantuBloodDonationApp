package com.example.vbantublooddonationapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommunityLikesAdapter extends RecyclerView.Adapter<CommunityLikesAdapter.LikesHolder> {

    final Context context;
    final List<String> likesList;
    final FirebaseDatabase database;
    private List<CommunityPost> mCommunityLikesList;
    String postID = "";

    public CommunityLikesAdapter(Context context, List<String> likesList) {
        this.context = context;
        this.likesList = likesList;

        database = FirebaseDatabase.getInstance();
    }

    public void setCommunityLikesList(List<CommunityPost> communityLikesList) {
        mCommunityLikesList = communityLikesList;
    }

    @NonNull
    @Override
    public CommunityLikesAdapter.LikesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_community_likes, parent, false);
        return new CommunityLikesAdapter.LikesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityLikesAdapter.LikesHolder holder, int position) {
        CommunityPost communityPost = mCommunityLikesList.get(position);
        postID = String.valueOf(communityPost.getPostID());
        System.out.println(postID);

        String userID = likesList.get(position);

        DatabaseReference refUser = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(String.valueOf(postID)).child(userID);

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    holder.username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return likesList.size();
    }

    public static class LikesHolder extends RecyclerView.ViewHolder {
        final TextView username;

        public LikesHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.ccl_tvUsername);
        }
    }
}