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
    //initialize variable
    private Activity mActivity;
    private List<RewardTransaction> mRewardTransList;
    private List<Reward> mRewardList;
    private RewardViewModel mRewardViewModel;
    private RewardTransactionViewModel mRewardTransactionViewModel;

    //constructor
    public RewardTransactionAdapter(Activity activity){
        mActivity = activity;
        mRewardTransactionViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(RewardTransactionViewModel.class);
        mRewardViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(RewardViewModel.class);
    }

    //setting the reward trans list
    public void setRewardTransList(List<RewardTransaction> rewardTransactionList){
        mRewardTransList = rewardTransactionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RewardTransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding with the cardview layout xml file
        CardRewardBinding binding = CardRewardBinding.inflate(mActivity.getLayoutInflater());
        return new RewardTransactionHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardTransactionAdapter.RewardTransactionHolder holder, int position) {
        //get each reward transaction from the list using the position
        RewardTransaction rewardTransaction = mRewardTransList.get(position);
        //get reward id to retrieve from reward table
        int rewardId = rewardTransaction.getRewardID();
        //check if the reward has expired
        String status = updateRewardTransactionStatus(rewardTransaction);
        //getting list of reward using id
        mRewardList = mRewardViewModel.getRequestById(rewardId);
        Reward reward = mRewardList.get(0);

        //setting text according to the data from reward and reward trans
        String minSpend = "Min. Spend RM " + reward.getMinSpend();
        holder.mTvDiscount.setText(reward.getDiscount());
        holder.mTvMinSpend.setText(minSpend);
        holder.mTvStoreName.setText(reward.getStoreName());
        //set visiblity since the reward is claimed
        holder.mTvPoints.setVisibility(View.GONE);

        //validate the status of the reward
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
            //onclick listener for "Available" to confirm on redeeming reward
            holder.mTvBtnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirm(rewardTransaction);
                }
            });
        }
    }

    //get item count
    @Override
    public int getItemCount() {
        if (mRewardTransList == null) {
            return 0;
        }
        return mRewardTransList.size();
    }

    //Recylcer view holder
    public class RewardTransactionHolder extends RecyclerView.ViewHolder {
        private TextView mTvDiscount, mTvMinSpend, mTvStoreName, mTvPoints, mTvBtnStatus;

        public RewardTransactionHolder(CardRewardBinding binding) {
            super(binding.getRoot());
            //bind all attribute in the layout to a variable
            mTvDiscount = binding.crTvDiscount;
            mTvMinSpend = binding.crTvMinSpend;
            mTvStoreName = binding.crTvStoreName;
            mTvPoints = binding.crTvPoints;
            mTvBtnStatus = binding.crTvBtnStatus;
        }
    }

    //the confirm function to check if user want to redeem the reward
    private void confirm(RewardTransaction rewardTransaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Confirm Use?");
        builder.setMessage("Do you want to use this reward?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //call function if yes is clicked
                        updateRewardTrans(rewardTransaction);
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    //return if no is clicked
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //if user click yes this function would update the redeemed date and send them to RewardQR
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


    //validate if the reward is expired or not
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
