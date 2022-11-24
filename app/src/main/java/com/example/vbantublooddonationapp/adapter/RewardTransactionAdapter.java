package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
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
import com.example.vbantublooddonationapp.RewardQR;
import com.example.vbantublooddonationapp.ViewModel.RewardTransactionViewModel;
import com.example.vbantublooddonationapp.ViewModel.RewardViewModel;
import com.example.vbantublooddonationapp.databinding.CardRewardBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RewardTransactionAdapter extends RecyclerView.Adapter<RewardTransactionAdapter.RewardTransactionHolder> {
    private Activity mActivity;
    private List<RewardTransaction> mRewardTransList;
    private List<Reward> mRewardList;
    private RewardViewModel mRewardViewModel;
    private RewardTransactionViewModel mRewardTransactionViewModel;

    public RewardTransactionAdapter(Activity activity){
        mActivity = activity;
        mRewardTransactionViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(RewardTransactionViewModel.class);
        mRewardViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(RewardViewModel.class);
    }

    public void setRewardTransList(List<RewardTransaction> rewardTransactionList){
        mRewardTransList = rewardTransactionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RewardTransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardRewardBinding binding = CardRewardBinding.inflate(mActivity.getLayoutInflater());
        return new RewardTransactionHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardTransactionAdapter.RewardTransactionHolder holder, int position) {
        RewardTransaction rewardTransaction = mRewardTransList.get(position);
        int rewardId = rewardTransaction.getRewardID();
        String status = updateRewardTransactionStatus(rewardTransaction);
        mRewardList = mRewardViewModel.getRequestById(rewardId);
        Reward reward = mRewardList.get(0);
        String minSpend = "Min. Spend RM " + reward.getMinSpend();
        holder.mTvDiscount.setText(reward.getDiscount());
        holder.mTvMinSpend.setText(minSpend);
        holder.mTvStoreName.setText(reward.getStoreName());
        holder.mTvPoints.setVisibility(View.GONE);
        if(status.equals("Available")){
            holder.mTvBtnStatus.setText(R.string.use_now);
        }else{
            holder.mTvBtnStatus.setText(status);
        }
        if(status.equals("Used")){
            holder.mTvBtnStatus.setBackgroundResource(R.color.white_grey);
        }else if(status.equals("Expired")){
            holder.mTvBtnStatus.setBackgroundResource(R.color.white_grey);
        }else {
            holder.mTvBtnStatus.setBackgroundResource(R.color.medium_pink);
            holder.mTvBtnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirm(rewardTransaction);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mRewardTransList == null) {
            return 0;
        }
        return mRewardTransList.size();
    }

    public class RewardTransactionHolder extends RecyclerView.ViewHolder {
        private TextView mTvDiscount, mTvMinSpend, mTvStoreName, mTvPoints, mTvBtnStatus;

        public RewardTransactionHolder(CardRewardBinding binding) {
            super(binding.getRoot());
            mTvDiscount = binding.crTvDiscount;
            mTvMinSpend = binding.crTvMinSpend;
            mTvStoreName = binding.crTvStoreName;
            mTvPoints = binding.crTvPoints;
            mTvBtnStatus = binding.crTvBtnStatus;
        }
    }
    private void confirm(RewardTransaction rewardTransaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Confirm Use?");
        builder.setMessage("Do you want to use this reward?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateRewardTrans(rewardTransaction);
                        dialog.cancel();
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

    private void updateRewardTrans(RewardTransaction rewardTransaction) {
        rewardTransaction.setStatus("Used");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        String curDate = sdf.format(currentDate);
        rewardTransaction.setRedeemDate(curDate);
        mRewardTransactionViewModel.updateRewardTransaction(rewardTransaction);
        Toast.makeText(mActivity, "Reward successfully redeemed", Toast.LENGTH_SHORT).show();
        int rewardTransID = rewardTransaction.getReward_transID();
        Intent i = new Intent(mActivity, RewardQR.class );
        i.putExtra("rewardTransID", rewardTransID);
        mActivity.startActivity(i);
    }

    private String updateRewardTransactionStatus(RewardTransaction rewardTransaction) {
        String status = rewardTransaction.getStatus();

        int claimDate = Integer.parseInt(rewardTransaction.getClaimDate());
        int expiryDate = Integer.parseInt(rewardTransaction.getExpiryDate());

        if (status.equals("Available") && claimDate > expiryDate) {
            rewardTransaction.setStatus("Expired");
            mRewardTransactionViewModel.updateRewardTransaction(rewardTransaction);
            status = "Expired";
        }

        return status;
    }


}
