package com.example.vbantublooddonationapp.adapter;

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

import com.example.vbantublooddonationapp.AppointmentDetailActivity;
import com.example.vbantublooddonationapp.CommunityCommentActivity;
import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.CommunityPostViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.CardCommunityPostBinding;

import java.util.List;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.CommunityPostHolder> {

    private final Activity mActivity;
    private List<CommunityPost> mCommunityPostList;
    private CommunityPostViewModel mCommunityPostViewModel;
    private List<Organiser> mOrganiserList;
    private OrganiserViewModel mOrganiserViewModel;
    private List<User> mUserList;
    private UserViewModel mUserViewModel;

    public CommunityPostAdapter(Activity activity){
        mActivity = activity;
        mCommunityPostViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(CommunityPostViewModel.class);
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(UserViewModel.class);
    }

    public void setCommunityPostList(List<CommunityPost> communityPostList){
        mCommunityPostList = communityPostList;
    }

    @NonNull
    @Override
    public CommunityPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityPostBinding mCardCommunityPostBinding = CardCommunityPostBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityPostAdapter.CommunityPostHolder(mCardCommunityPostBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityPostAdapter.CommunityPostHolder holder, int position) {
        CommunityPost communityPost = mCommunityPostList.get(position);

        if (communityPost.organiserID == 0){
            holder.mccpTvUsername.setText(getUserName(communityPost.getUserID()));
        }

        if (communityPost.userID == 0){
            holder.mccpTvUsername.setText(getOrganiserName(communityPost.getOrganiserID()));
        }
    }

    @Override
    public int getItemCount() {
        if(mCommunityPostList==null){
            return 0;
        }
        return mCommunityPostList.size();
    }

    public class CommunityPostHolder extends RecyclerView.ViewHolder{
        private TextView mccpTvUsername;
        private TextView mccpTvCaption;
        private TextView mccpTvDuration;
        private TextView mccpTvLikes;
        private TextView mccpTvComments;
        private ImageView mccpIvAvatar;
        private ImageView mccpIvPostImage;
        public CommunityPostHolder(@NonNull CardCommunityPostBinding mCardCommunityPostBinding) {
            super(mCardCommunityPostBinding.getRoot());
            mccpTvUsername = mCardCommunityPostBinding.ccpTvUsername;
            mccpTvCaption = mCardCommunityPostBinding.ccpTvCaption;
            mccpTvDuration = mCardCommunityPostBinding.ccpTvDuration;
            mccpTvLikes = mCardCommunityPostBinding.ccpTvLikes;
            mccpIvAvatar = mCardCommunityPostBinding.ccpIvAvatar;
            mccpIvPostImage = mCardCommunityPostBinding.ccpIvPostImage;
            mccpTvComments = mCardCommunityPostBinding.ccpTvComments;

            mccpTvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    communityPostComments(getAdapterPosition());
                }
            });
        }
    }

    public void communityPostComments(int position){
        CommunityPost currentPost = mCommunityPostList.get(position);
        Intent i = new Intent(mActivity, CommunityCommentActivity.class);
        i.putExtra("currentPostID", currentPost.getPostID());
        mActivity.startActivity(i);
    }

    public String getUserName(int id){
        List<User> userList = mUserViewModel.getUserById(id);
        User user = userList.get(0);
        return user.getUsername();
    }

    public String getOrganiserName(int id){
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
    }

    public String getCaption(int id){
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
    }
}
