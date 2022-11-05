package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.databinding.CardBloodRequestHistoryBinding;
import com.example.vbantublooddonationapp.databinding.CardUserAppointmentsBinding;

import java.util.List;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.AppointmentHolder>{

    private Activity mActivity;
    private List<Appointment> mAppointmentList;

    public UserAppointmentAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        mAppointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardUserAppointmentsBinding itemBinding = CardUserAppointmentsBinding.inflate(mActivity.getLayoutInflater());
        return new AppointmentHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder holder, int position) {
        Appointment appointment = mAppointmentList.get(position);
        holder.mTvName.setText("Halo");
        holder.mTvDate.setText("12345656");
        holder.mTvTime.setText("weeee");
    }

    @Override
    public int getItemCount() {
        if (mAppointmentList == null) {
            return 0;
        }
        return mAppointmentList.size();
    }

    public class AppointmentHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private TextView mTvBloodType;
        private TextView mTvDate;
        private TextView mTvTime;

        public AppointmentHolder(CardUserAppointmentsBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvName = itemBinding.cuaTvName;
            mTvBloodType = itemBinding.cuaTvBloodGroup;
            mTvDate = itemBinding.cuaTvAppointmentDate;
            mTvTime = itemBinding.cuaTvAppointmentTime;
        }
    }
}
