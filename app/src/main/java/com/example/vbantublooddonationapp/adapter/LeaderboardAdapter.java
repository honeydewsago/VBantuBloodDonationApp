package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.LeaderboardUser;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.databinding.CardLeaderboardRowBinding;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardHolder>{

    private Activity mActivity;
    private List<LeaderboardUser> mLeaderboardUserList;

    public LeaderboardAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setLeaderboardUserList(List<LeaderboardUser> leaderboardUserList) {
        mLeaderboardUserList = leaderboardUserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardLeaderboardRowBinding itemBinding = CardLeaderboardRowBinding.inflate(mActivity.getLayoutInflater());
        return new LeaderboardHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardHolder holder, int position) {
        LeaderboardUser leaderboardUser = mLeaderboardUserList.get(position);
        holder.mTvUsername.setText(leaderboardUser.getUsername());
        holder.mTvBloodAmt.setText(leaderboardUser.getBloodAmt()+"ml");
    }

    @Override
    public int getItemCount() {
        if (mLeaderboardUserList == null) {
            return 0;
        }
        return mLeaderboardUserList.size();
    }

    public class LeaderboardHolder extends RecyclerView.ViewHolder{
        private TextView mTvPlaceNo;
        private TextView mTvUsername;
        private TextView mTvBloodAmt;

        public LeaderboardHolder(CardLeaderboardRowBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvPlaceNo = itemBinding.clrTvPlaceNo;
            mTvUsername = itemBinding.clrTvPlaceUsername;
            mTvBloodAmt = itemBinding.clrTvPlaceBloodAmount;
        }
    }
}
