package com.example.vbantublooddonationapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.Model.RewardTransaction;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.ViewModel.RewardTransactionViewModel;
import com.example.vbantublooddonationapp.ViewModel.RewardViewModel;
import com.example.vbantublooddonationapp.databinding.CardRewardBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardHolder> {
    private Activity mActivity;
    private List<Reward> mRewardList;
    private RewardViewModel mRewardViewModel;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private int rewardId;
    private RewardTransactionViewModel mRewardTransactionViewModel;

    public RewardAdapter(Activity activity) {
        mActivity = activity;
        mRewardViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(RewardViewModel.class);
    }

    public void setRewardList(List<Reward> rewardList) {
        mRewardList = rewardList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RewardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardRewardBinding binding = CardRewardBinding.inflate(mActivity.getLayoutInflater());
        return new RewardHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.RewardHolder holder, int position) {

        mPreferences = mActivity.getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        Reward reward = mRewardList.get(position);
        String status = updateRewardStatus(reward);
        String points = reward.getPoints() + " points";
        String minSpend = "Min. Spend RM " + reward.getMinSpend();
        rewardId = reward.getRewardID();
        holder.mTvDiscount.setText(reward.getDiscount());
        holder.mTvMinSpend.setText(minSpend);
        holder.mTvStoreName.setText(reward.getStoreName());
        holder.mTvPoints.setText(points);
        holder.mTvBtnStatus.setClickable(true);

        if (status.equals("Fully Redeemed")) {
            holder.mTvBtnStatus.setText(reward.getStatus());
            holder.mTvBtnStatus.setBackgroundResource(R.color.white_grey);
            holder.mTvBtnStatus.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        if (mRewardList == null) {
            return 0;
        }
        return mRewardList.size();
    }

    public class RewardHolder extends RecyclerView.ViewHolder {
        private TextView mTvDiscount, mTvMinSpend, mTvStoreName, mTvPoints, mTvBtnStatus;

        public RewardHolder(CardRewardBinding binding) {
            super(binding.getRoot());
            mTvDiscount = binding.crTvDiscount;
            mTvMinSpend = binding.crTvMinSpend;
            mTvStoreName = binding.crTvStoreName;
            mTvPoints = binding.crTvPoints;
            mTvBtnStatus = binding.crTvBtnStatus;
        }
    }

    private void confirm(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Confirm Redeem?");
        builder.setMessage("Do you want to redeem this reward?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setRewardQuantity(id);
                        updateUserRewardTrans(mUserID,id);
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setRewardQuantity(int id) {
        List<Reward> rewardList = mRewardViewModel.getRequestById(id);
        Reward reward = rewardList.get(0);
        reward.setQuantity(reward.getQuantity() - 1);
    }

    private String updateRewardStatus(Reward reward) {
        String status = reward.getStatus();
        int quantity = reward.getQuantity();
        if (quantity == 0) {
            reward.setStatus("Fully Redeemed");
            mRewardViewModel.updateReward(reward);
            status = "Fully Redeemed";
        }
        return status;
    }

    private void updateUserRewardTrans(int userId, int rewardId){
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.MONTH,1);
        Date expiryDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String curDate = sdf.format(currentDate);
        String expDate = sdf.format(expiryDate);
        RewardTransaction rewardTransaction = new RewardTransaction(rewardId,userId,curDate,expDate,"","Available");
        mRewardTransactionViewModel.insertRewardTransaction(rewardTransaction);
        Toast.makeText(mActivity, "Reward successfully claimed", Toast.LENGTH_SHORT).show();
    }

}

