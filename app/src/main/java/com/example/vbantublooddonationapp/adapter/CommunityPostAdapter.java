package com.example.vbantublooddonationapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.CommunityCommentActivity;
import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.CommunityPostViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.CardCommunityPostBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.CommunityPostHolder> {

    private final Activity mActivity;
    private List<CommunityPost> mCommunityPostList;
    private final CommunityPostViewModel mCommunityPostViewModel;
    private final OrganiserViewModel mOrganiserViewModel;
    private final UserViewModel mUserViewModel;

    public CommunityPostAdapter(Activity activity) {
        mActivity = activity;
        mCommunityPostViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(CommunityPostViewModel.class);
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(UserViewModel.class);
    }

    public void setCommunityPostList(List<CommunityPost> communityPostList) {
        mCommunityPostList = communityPostList;
    }

    @NonNull
    @Override
    public CommunityPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityPostBinding mCardCommunityPostBinding = CardCommunityPostBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityPostHolder(mCardCommunityPostBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityPostAdapter.CommunityPostHolder holder, @SuppressLint("RecyclerView") int position) {
        CommunityPost communityPost = mCommunityPostList.get(position);

        //show post details
        if (communityPost.organiserID == 0) {
            holder.mccpTvUsername.setText(getUserName(communityPost.getUserID()));
        }
        if (communityPost.userID == 0) {
            holder.mccpTvUsername.setText(getOrganiserName(communityPost.getOrganiserID()));
        }
        holder.mccpTvCaption.setText(communityPost.getPostDesc());
        //holder.mccpIvPostImage

        //holder.mccpIvAvatar
        //holder.mccpTvLikes

        getPostDuration(position, holder.mccpTvDuration);

        holder.mccpIvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPost communityPost = mCommunityPostList.get(position);
                communityPostComments(communityPost);
            }
        });

        holder.mccpTvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPost communityPost = mCommunityPostList.get(position);
                communityPostComments(communityPost);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCommunityPostList == null) {
            return 0;
        }
        return mCommunityPostList.size();
    }

    public static class CommunityPostHolder extends RecyclerView.ViewHolder {
        private final TextView mccpTvUsername;
        private final TextView mccpTvCaption;
        private final TextView mccpTvDuration;
        private TextView mccpTvLikes;
        private final TextView mccpTvComments;
        private ImageView mccpIvAvatar;
        private ImageView mccpIvPostImage;
        private final ImageView mccpIvComment;

        public CommunityPostHolder(@NonNull CardCommunityPostBinding mCardCommunityPostBinding) {
            super(mCardCommunityPostBinding.getRoot());
            mccpTvUsername = mCardCommunityPostBinding.ccpTvUsername;
            mccpTvCaption = mCardCommunityPostBinding.ccpTvCaption;
            mccpTvDuration = mCardCommunityPostBinding.ccpTvDuration;
            mccpTvLikes = mCardCommunityPostBinding.ccpTvLikes;
            mccpIvAvatar = mCardCommunityPostBinding.ccpIvAvatar;
            mccpIvPostImage = mCardCommunityPostBinding.ccpIvPostImage;
            mccpTvComments = mCardCommunityPostBinding.ccpTvComments;
            mccpIvComment = mCardCommunityPostBinding.ccpIvComment;

            //Likes
        }
    }

    public void communityPostComments(CommunityPost mCommunityPost) {
        //CommunityPost currentPost = mCommunityPostList.get(position);
        Intent i = new Intent(mActivity, CommunityCommentActivity.class);
        i.putExtra("currentPostID", mCommunityPost.getPostID());
        System.out.println(mCommunityPost.getPostID());
        mActivity.startActivity(i);
    }

    public String getUserName(int id) {
        List<User> userList = mUserViewModel.getUserById(id);
        User user = userList.get(0);
        return user.getUsername();
    }

    public String getOrganiserName(int id) {
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
    }

    @SuppressLint("SetTextI18n")
    public void getPostDuration(int position, TextView mccpTvDuration) {
        CommunityPost currentPost = mCommunityPostList.get(position);
        List<CommunityPost> communityPostList = mCommunityPostViewModel.getCommunityPostByID(currentPost.getPostID());
        CommunityPost communityPost = communityPostList.get(0);

        //get current date time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        try {
            Date postTime = sdf.parse(communityPost.postDateTime);
            Date end = sdf.parse(currentTime);
            long diff = Objects.requireNonNull(end).getTime() - Objects.requireNonNull(postTime).getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            int hours = (int) (diff / (1000 * 60 * 60));
            int minutes = (int) (diff / (1000 * 60));
            int months = days / 31;

            if (months > 1) {
                mccpTvDuration.setText(months + " months ago");
            } else if (months == 1) {
                mccpTvDuration.setText(months + " month ago");
            } else if (days > 1) {
                mccpTvDuration.setText(days + " days ago");
            } else if (days == 1) {
                mccpTvDuration.setText(days + " day ago");
            } else if (hours > 1) {
                mccpTvDuration.setText(hours + " hours ago");
            } else if (hours == 1) {
                mccpTvDuration.setText(hours + " hour ago");
            } else if (minutes > 1) {
                mccpTvDuration.setText(minutes + " minutes ago");
            } else if (minutes == 1) {
                mccpTvDuration.setText(minutes + " minute ago");
            } else {
                mccpTvDuration.setText("just now");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
