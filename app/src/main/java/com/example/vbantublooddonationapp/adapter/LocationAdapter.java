package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Activity mActivity;
    private List<Organiser> mOrganiserList;

    public LocationAdapter(Activity activity) {
        mActivity = activity;
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

        public LocationHolder(CardLocationBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvOrganiser = itemBinding.clTvOrganiser;
        }
    }
}

