package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.databinding.CardCommunityCommentsBinding;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.CommunityCommentHolder>{

    private Activity mActivity;
    private List<Comments> mCommentsList;
    final FirebaseDatabase database;


    public CommunityCommentAdapter(Activity activity, ArrayList<Comments> mCommentsList) {
        mActivity = activity;
        this.mCommentsList = mCommentsList;

        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public CommunityCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityCommentsBinding mCardCommunityCommentsBinding = CardCommunityCommentsBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityCommentHolder(mCardCommunityCommentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityCommentAdapter.CommunityCommentHolder holder, int position) {
        Comments mComments = mCommentsList.get(position);
        holder.mcccTvComment.setText(mComments.getComment());

    }

    @Override
    public int getItemCount() {
        if (mCommentsList == null) {
            return 0;
        }
        return mCommentsList.size();
    }

    public static class CommunityCommentHolder extends RecyclerView.ViewHolder {

        private final TextView mcccTvComment, mcccTvUsername;

        public CommunityCommentHolder(@NonNull CardCommunityCommentsBinding mCardCommunityCommentsBinding) {
            super(mCardCommunityCommentsBinding.getRoot());
            mcccTvComment = mCardCommunityCommentsBinding.cccTvComment;
            mcccTvUsername = mCardCommunityCommentsBinding.cccTvUsername;
        }
    }
}
