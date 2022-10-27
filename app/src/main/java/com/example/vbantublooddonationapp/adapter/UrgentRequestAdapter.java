package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;
import com.example.vbantublooddonationapp.databinding.CardUrgentRequestBinding;

import java.util.ArrayList;
import java.util.List;

public class UrgentRequestAdapter extends RecyclerView.Adapter<UrgentRequestAdapter.UrgentRequestHolder> {

    private Activity mActivity;
    private List<BloodRequest> mRequestList;
    private OrganiserViewModel mOrganiserViewModel;

    public UrgentRequestAdapter(Activity activity) {
        mActivity = activity;
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(OrganiserViewModel.class);
    }

    public void setRequestList(List<BloodRequest> requestList) {
        mRequestList = requestList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UrgentRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardUrgentRequestBinding itemBinding = CardUrgentRequestBinding.inflate(mActivity.getLayoutInflater());
        return new UrgentRequestHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UrgentRequestHolder holder, int position) {
        BloodRequest request = mRequestList.get(position);
        holder.mTvOrganiser.setText(getOrganiserName(request.getOrganiserID()));
        holder.mTvRequestInfo.setText(request.getRequestInfo());
    }

    @Override
    public int getItemCount() {
        if (mRequestList == null) {
            return 0;
        }
        return mRequestList.size();
    }

    public class UrgentRequestHolder extends RecyclerView.ViewHolder {
        private TextView mTvOrganiser;
        private TextView mTvRequestInfo;
        private RecyclerView mRvBloodType;
        private Button mBtnMoreInfo;

        public UrgentRequestHolder(CardUrgentRequestBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvOrganiser = itemBinding.curTvOrganiser;
            mTvRequestInfo = itemBinding.curTvRequestInfo;
            mRvBloodType = itemBinding.curRvBloodType;
            mBtnMoreInfo = itemBinding.curBtnRequestMoreInfo;

            mBtnMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public String getOrganiserName(int id){
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
    }
}
