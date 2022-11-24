package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Reward;
import com.example.vbantublooddonationapp.Model.RewardTransaction;
import com.example.vbantublooddonationapp.ViewModel.RewardTransactionViewModel;
import com.example.vbantublooddonationapp.ViewModel.RewardViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityRewardQrBinding;

import java.util.List;
import java.util.Objects;

public class RewardQR extends AppCompatActivity {

    private ActivityRewardQrBinding binding;
    private Reward mReward;
    private RewardTransaction mRewardTransaction;
    private RewardViewModel mRewardViewModel;
    private RewardTransactionViewModel mRewardTransactionViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRewardQrBinding binding = ActivityRewardQrBinding.inflate(getLayoutInflater());

        View v = binding.getRoot();
        setContentView(v);

        Toolbar toolbar = binding.arqToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        Intent i = getIntent();
        int rewardTransactionID = i.getIntExtra("rewardTransID",1);
        mRewardViewModel = new ViewModelProvider(this).get(RewardViewModel.class);
        mRewardTransactionViewModel = new ViewModelProvider(this).get(RewardTransactionViewModel.class);

        List<RewardTransaction> mRewardTransactionList = mRewardTransactionViewModel.getRequestById(rewardTransactionID);
        mRewardTransaction = mRewardTransactionList.get(0);
        List<Reward> mRewardList = mRewardViewModel.getRequestById(mRewardTransaction.getUserID());
        mReward = mRewardList.get(0);
        binding.arqTvStoreName.setText(mReward.getStoreName());
        String text = mReward.getDiscount() + " discount with a min. spend of RM " + mReward.getMinSpend();
        binding.arqTvDiscount.setText(text);
        binding.arqTvExpiryDate.setText(getFullDate(mRewardTransaction.getExpiryDate()));
    }

    public String getFullDate(String dateTime) {
        String year = dateTime.substring(0,4);
        int month = Integer.parseInt(dateTime.substring(4,6));
        String day = dateTime.substring(6,8);

        return day + " "+ getMonthName(month) + " " + year;
    }

    public String getMonthName(int month_value){
        switch (month_value) {
            case 1:
                return getResources().getString(R.string.january);
            case 2:
                return getResources().getString(R.string.february);
            case 3:
                return getResources().getString(R.string.march);
            case 4:
                return getResources().getString(R.string.april);
            case 5:
                return getResources().getString(R.string.may);
            case 6:
                return getResources().getString(R.string.june);
            case 7:
                return getResources().getString(R.string.july);
            case 8:
                return getResources().getString(R.string.august);
            case 9:
                return getResources().getString(R.string.september);
            case 10:
                return getResources().getString(R.string.october);
            case 11:
                return getResources().getString(R.string.november);
            case 12:
                return getResources().getString(R.string.december);
            default:
                return getResources().getString(R.string.month);
        }
    }
}