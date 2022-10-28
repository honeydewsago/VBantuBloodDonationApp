package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.CardBloodRequestHistoryBinding;
import com.example.vbantublooddonationapp.databinding.CardUrgentRequestBinding;

import java.util.Arrays;
import java.util.List;

public class BloodRequestHistoryAdapter extends RecyclerView.Adapter<BloodRequestHistoryAdapter.BloodRequestHolder>{

    private Activity mActivity;
    private List<BloodRequest> mRequestList;
    private List<String> mBloodTypeList;
    private BloodTypeAdapter mBloodTypeAdapter;
    private BloodRequestViewModel mBloodRequestViewModel;

    public BloodRequestHistoryAdapter(Activity activity) {
        mActivity = activity;
        mBloodTypeAdapter = new BloodTypeAdapter(mActivity);
        mBloodRequestViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(BloodRequestViewModel.class);
    }

    public void setRequestList(List<BloodRequest> requestList) {
        mRequestList = requestList;
    }

    @NonNull
    @Override
    public BloodRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardBloodRequestHistoryBinding itemBinding = CardBloodRequestHistoryBinding.inflate(mActivity.getLayoutInflater());
        return new BloodRequestHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestHolder holder, int position) {
        BloodRequest request = mRequestList.get(position);
        holder.mTvRequestInfo.setText(mActivity.getString(R.string.fullRequestInformation,request.getRequestInfo()));
        holder.mTvDate.setText(getFullDate(request.getDateTime()));
        holder.mTvTime.setText(convertTimeTo12HFormat(request.getDateTime()));

        String bloodShortage = request.getShortageType();
        mBloodTypeList = Arrays.asList(bloodShortage.split(","));

        mBloodTypeAdapter.setBloodTypeList(mBloodTypeList);
        holder.mRvBloodType.setAdapter(mBloodTypeAdapter);

        //set the layout manager
        holder.mRvBloodType.setLayoutManager(new GridLayoutManager(mActivity.getApplicationContext(), 4));

        setActiveSwitchListener(request, holder.mScActive);
    }

    @Override
    public int getItemCount() {
        if (mRequestList == null) {
            return 0;
        }
        return mRequestList.size();
    }

    public class BloodRequestHolder extends RecyclerView.ViewHolder {
        private TextView mTvRequestInfo;
        private TextView mTvDate;
        private TextView mTvTime;
        private RecyclerView mRvBloodType;
        private SwitchCompat mScActive;

        public BloodRequestHolder(CardBloodRequestHistoryBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvRequestInfo = itemBinding.cbrhTvRequestInfo;
            mTvDate = itemBinding.cbrhTvDate;
            mTvTime = itemBinding.cbrhTvTime;
            mRvBloodType = itemBinding.cbrhRvBloodType;
            mScActive = itemBinding.cbrhScActive;
        }
    }

    public void setActiveSwitchListener(BloodRequest request, SwitchCompat activeSwitch){
        if(request.getActive() == 1) {
            activeSwitch.setChecked(true);
        }

        activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    request.setActive(1);
                }
                else {
                    request.setActive(0);
                }
                mBloodRequestViewModel.updateBloodRequest(request);
            }
        });
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
                return mActivity.getResources().getString(R.string.january);
            case 2:
                return mActivity.getResources().getString(R.string.february);
            case 3:
                return mActivity.getResources().getString(R.string.march);
            case 4:
                return mActivity.getResources().getString(R.string.april);
            case 5:
                return mActivity.getResources().getString(R.string.may);
            case 6:
                return mActivity.getResources().getString(R.string.june);
            case 7:
                return mActivity.getResources().getString(R.string.july);
            case 8:
                return mActivity.getResources().getString(R.string.august);
            case 9:
                return mActivity.getResources().getString(R.string.september);
            case 10:
                return mActivity.getResources().getString(R.string.october);
            case 11:
                return mActivity.getResources().getString(R.string.november);
            case 12:
                return mActivity.getResources().getString(R.string.december);
            default:
                return mActivity.getResources().getString(R.string.month);
        }
    }

    public String convertTimeTo12HFormat(String dateTime) {
        String time24H = dateTime.substring(9,15);
        int hour = Integer.parseInt(time24H.substring(0,2));
        String minute = time24H.substring(2,4);
        String period="";

        if (hour > 13) {
            hour = hour - 12;
            period = "PM";
        }
        else {
            period = "AM";
        }

        return String.valueOf(hour) + ":" + minute + " " + period;
    }
}
