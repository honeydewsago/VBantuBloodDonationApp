package com.example.vbantublooddonationapp.adapter;

import android.annotation.SuppressLint;
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
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.databinding.CardLeaderboardRowBinding;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardHolder> {

    private Activity mActivity;
    private List<LeaderboardUser> mLeaderboardUserList;

    public LeaderboardAdapter(Activity activity) {
        mActivity = activity;
    }

    @SuppressLint("NotifyDataSetChanged")
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderboardHolder holder, int position) {
        LeaderboardUser leaderboardUser = mLeaderboardUserList.get(position);
        holder.mTvUsername.setText(leaderboardUser.getUsername());
        holder.mTvBloodAmt.setText(leaderboardUser.getBloodAmt() + "ml");

        if (position == 0) {
            holder.mIvPlaceNoBg.setImageResource(R.color.yellow);
            holder.mIvPlaceBg.setImageResource(R.color.yellow);
            holder.mTvPlaceNo.setText("1st");
        } else if (position == 1) {
            holder.mTvPlaceNo.setText("2nd");
        } else if (position == 2) {
            holder.mTvPlaceNo.setText("3rd");
        } else {
            holder.mTvPlaceNo.setText((position + 1) + "th");
        }
    }

    @Override
    public int getItemCount() {
        if (mLeaderboardUserList == null) {
            return 0;
        }
        return mLeaderboardUserList.size();
    }

    public class LeaderboardHolder extends RecyclerView.ViewHolder {
        private ImageView mIvPlaceBg;
        private ImageView mIvPlaceNoBg;
        private TextView mTvPlaceNo;
        private TextView mTvUsername;
        private TextView mTvBloodAmt;

        public LeaderboardHolder(CardLeaderboardRowBinding itemBinding) {
            super(itemBinding.getRoot());
            mIvPlaceBg = itemBinding.clrIvPlaceBackground;
            mIvPlaceNoBg = itemBinding.clrIvPlaceNo;
            mTvPlaceNo = itemBinding.clrTvPlaceNo;
            mTvUsername = itemBinding.clrTvPlaceUsername;
            mTvBloodAmt = itemBinding.clrTvPlaceBloodAmount;
        }
    }
}
