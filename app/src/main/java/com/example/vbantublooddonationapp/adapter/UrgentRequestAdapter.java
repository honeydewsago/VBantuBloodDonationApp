package com.example.vbantublooddonationapp.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.databinding.CardUrgentRequestBinding;

public class UrgentRequestAdapter extends RecyclerView.Adapter<UrgentRequestAdapter.UrgentRequestHolder> {

    @NonNull
    @Override
    public UrgentRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UrgentRequestHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
}
