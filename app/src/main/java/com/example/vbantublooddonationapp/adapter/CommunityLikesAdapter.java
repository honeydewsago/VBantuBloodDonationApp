package com.example.vbantublooddonationapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.CommunityLikes;
import com.example.vbantublooddonationapp.databinding.CardCommunityLikesBinding;

import java.util.ArrayList;
import java.util.List;

public class CommunityLikesAdapter extends RecyclerView.Adapter<CommunityLikesAdapter.CommunityLikesHolder> {

    private final Activity mActivity;
    public final List<CommunityLikes> mLikeList;

    public CommunityLikesAdapter(Activity activity, ArrayList<CommunityLikes> mLikesList) {
        mActivity = activity;
        this.mLikeList = mLikesList;
    }

    @NonNull
    @Override
    public CommunityLikesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityLikesBinding mCardCommunityLikesBinding = CardCommunityLikesBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityLikesHolder(mCardCommunityLikesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityLikesAdapter.CommunityLikesHolder holder, @SuppressLint("RecyclerView") int position) {
        CommunityLikes communityLikes = mLikeList.get(position);

        String userName = communityLikes.getUserName().toString();
        holder.claTvUsername.setText(userName);
    }

    @Override
    public int getItemCount() {
        if (mLikeList == null) {
            return 0;
        }
        return mLikeList.size();
    }

    public static class CommunityLikesHolder extends RecyclerView.ViewHolder {

        public final TextView claTvUsername;

        public CommunityLikesHolder(@NonNull CardCommunityLikesBinding mCardCommunityLikesBinding) {
            super(mCardCommunityLikesBinding.getRoot());
            claTvUsername = mCardCommunityLikesBinding.cclTvUsername;
        }
    }
}
