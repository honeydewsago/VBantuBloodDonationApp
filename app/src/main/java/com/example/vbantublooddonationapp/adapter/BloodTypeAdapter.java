package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.databinding.CardBloodTypeBinding;

import java.util.List;

public class BloodTypeAdapter extends RecyclerView.Adapter<BloodTypeAdapter.BloodTypeHolder> {

    private Activity mActivity;
    private List<String> mBloodTypeList;

    public BloodTypeAdapter(Activity activity) {
        mActivity = activity;
    }

    //set the list for blood type
    public void setBloodTypeList(List<String> bloodTypeList) {
        mBloodTypeList = bloodTypeList;
    }

    @NonNull
    @Override
    public BloodTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //set the view for each holder
        CardBloodTypeBinding itemBinding = CardBloodTypeBinding.inflate(mActivity.getLayoutInflater());
        return new BloodTypeHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodTypeHolder holder, int position) {
        //set the blood type name
        String bloodType = mBloodTypeList.get(position);
        holder.mBloodType.setText(bloodType);
    }

    @Override
    public int getItemCount() {
        if (mBloodTypeList == null) {
            return 0;
        }
        return mBloodTypeList.size();
    }

    public class BloodTypeHolder extends RecyclerView.ViewHolder{
        private TextView mBloodType;

        public  BloodTypeHolder(CardBloodTypeBinding itemBinding) {
            super(itemBinding.getRoot());
            mBloodType = itemBinding.cbtTvBloodType;
        }
    }
}
