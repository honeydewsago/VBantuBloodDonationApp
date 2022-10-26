package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.SingleBloodBankLocation;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Activity mActivity;
    private List<Organiser> mOrganiserList;
    private ArrayList<String> mBloodTypeList;
    private BloodTypeAdapter mBloodTypeAdapter;

    public LocationAdapter(Activity activity) {
        mActivity = activity;

        mBloodTypeAdapter = new BloodTypeAdapter(mActivity);

        mBloodTypeList = new ArrayList<String>();
        mBloodTypeList.add("AB");
        mBloodTypeList.add("A");
        mBloodTypeList.add("B");
        mBloodTypeList.add("O");
    }

    public void setOrganiserList(List<Organiser> organiserList) {
        mOrganiserList = organiserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardLocationBinding itemBinding = CardLocationBinding.inflate(mActivity.getLayoutInflater());
        return new LocationHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Organiser organiser = mOrganiserList.get(position);
        holder.mTvOrganiser.setText(organiser.getCompanyName());

        String address = organiser.getAddress();
        if (address.length() > 50) {
            address = address.substring(0,47);
            address = address + "...";
        }
        holder.mTvAddress.setText(address);

        mBloodTypeAdapter.setBloodTypeList(mBloodTypeList);
        holder.mRvBloodType.setAdapter(mBloodTypeAdapter);
    }

    @Override
    public int getItemCount() {
        if (mOrganiserList == null) {
            return 0;
        }
        return mOrganiserList.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder{
        private TextView mTvOrganiser;
        private TextView mTvAddress;
        private RecyclerView mRvBloodType;
        private Button mBtnMoreInfo;

        public LocationHolder(CardLocationBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvOrganiser = itemBinding.clTvOrganiser;
            mTvAddress = itemBinding.clTvAddress;
            mRvBloodType = itemBinding.clRvBloodType;
            mBtnMoreInfo = itemBinding.clBtnOrganiserMoreInfo;

            mBtnMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Organiser currentOrganiser = mOrganiserList.get(getAdapterPosition());

                    Intent i = new Intent(mActivity, SingleBloodBankLocation.class);

                    i.putExtra("currentOrganiserID", currentOrganiser.getOrganiserID());

                    mActivity.startActivity(i);
                }
            });
        }

    }
}

