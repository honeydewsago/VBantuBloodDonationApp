package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.CardCommunityCommentsBinding;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.CommunityCommentHolder>{

    private final Activity mActivity;
    private final List<Comments> mCommentsList;
    final FirebaseDatabase database;
    private final OrganiserViewModel mOrganiserViewModel;
    private final UserViewModel mUserViewModel;


    public CommunityCommentAdapter(Activity activity, ArrayList<Comments> mCommentsList) {
        mActivity = activity;
        this.mCommentsList = mCommentsList;
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(UserViewModel.class);
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

        if (mComments.getOrganiserID() == 0) {
            holder.mcccTvUsername.setText(getUserName(mComments.getUserID()));
        }
        if (mComments.getUserID() == 0) {
            holder.mcccTvUsername.setText(getOrganiserName(mComments.getOrganiserID()));
        }

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
        //private final ImageView mcccIvAvatar;

        public CommunityCommentHolder(@NonNull CardCommunityCommentsBinding mCardCommunityCommentsBinding) {
            super(mCardCommunityCommentsBinding.getRoot());
            mcccTvComment = mCardCommunityCommentsBinding.cccTvComment;
            mcccTvUsername = mCardCommunityCommentsBinding.cccTvUsername;
            //mcccIvAvatar = mCardCommunityCommentsBinding.cccIvAvatar;
        }
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
}
