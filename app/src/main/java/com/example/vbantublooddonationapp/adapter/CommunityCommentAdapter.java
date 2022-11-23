package com.example.vbantublooddonationapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.databinding.CardCommunityCommentsBinding;

import java.util.ArrayList;
import java.util.List;


public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.CommunityCommentHolder>{

    private final Activity mActivity;
    public final List<Comments> mCommentsList;

    public CommunityCommentAdapter(Activity activity, ArrayList<Comments> mCommentsList) {
        mActivity = activity;
        this.mCommentsList = mCommentsList;
    }

    @NonNull
    @Override
    public CommunityCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityCommentsBinding mCardCommunityCommentsBinding = CardCommunityCommentsBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityCommentHolder(mCardCommunityCommentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityCommentAdapter.CommunityCommentHolder holder, @SuppressLint("RecyclerView") int position) {
        Comments mComments = mCommentsList.get(position);

        String comment = mComments.getComment().toString();
        String name = mComments.getUserName().toString();
        holder.mcccTvUsername.setText(name);
        holder.mcccTvComment.setText(comment);
    }

    @Override
    public int getItemCount() {
        if (mCommentsList == null) {
            return 0;
        }
        return mCommentsList.size();
    }

    public static class CommunityCommentHolder extends RecyclerView.ViewHolder {

        public final TextView mcccTvComment, mcccTvUsername;
        //private final ImageView mcccIvAvatar;

        public CommunityCommentHolder(@NonNull CardCommunityCommentsBinding mCardCommunityCommentsBinding) {
            super(mCardCommunityCommentsBinding.getRoot());
            mcccTvComment = mCardCommunityCommentsBinding.cccTvComment;
            mcccTvUsername = mCardCommunityCommentsBinding.cccTvUsername;
            //mcccIvAvatar = mCardCommunityCommentsBinding.cccIvAvatar;
        }
    }
}
