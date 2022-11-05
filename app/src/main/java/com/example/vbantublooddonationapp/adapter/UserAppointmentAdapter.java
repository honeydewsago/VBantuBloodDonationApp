package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.OrganiserSingleAppointmentDetails;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.SingleBloodBankLocation;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.CardBloodRequestHistoryBinding;
import com.example.vbantublooddonationapp.databinding.CardUserAppointmentsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.AppointmentHolder>{

    private Activity mActivity;
    private List<Appointment> mAppointmentList;
    private UserViewModel mUserViewModel;
    private AppointmentViewModel mAppointmentViewModel;

    public UserAppointmentAdapter(Activity activity) {
        mActivity = activity;
        mUserViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(UserViewModel.class);
        mAppointmentViewModel = new ViewModelProvider((FragmentActivity)mActivity).get(AppointmentViewModel.class);
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

        String status = updateAppointmentStatus(appointment);

        holder.mTvStatus.setText(status);
        if (status.equals("Ongoing")) {
            holder.mTvStatus.setBackgroundResource(R.color.orange);
        }
        else if (status.equals("Completed")) {
            holder.mTvStatus.setBackgroundResource(R.color.green);
        }
        else{
            holder.mTvStatus.setBackgroundResource(R.color.light_grey);
        }

        holder.mTvName.setText(getUserFullname(appointment.getUserID()));
        holder.mTvDate.setText(getFullDate(appointment.getAppointmentDate()));
        holder.mTvTime.setText(appointment.appointmentTime);
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
        private TextView mTvStatus;
        private TextView mTvDetails;

        public AppointmentHolder(CardUserAppointmentsBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvName = itemBinding.cuaTvName;
            mTvBloodType = itemBinding.cuaTvBloodGroup;
            mTvDate = itemBinding.cuaTvAppointmentDate;
            mTvTime = itemBinding.cuaTvAppointmentTime;
            mTvStatus = itemBinding.cuaTvStatus;
            mTvDetails = itemBinding.cuaTvAppointmentDetails;

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSingleAppointmentActivity(getAdapterPosition());
                }
            });
            mTvDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSingleAppointmentActivity(getAdapterPosition());
                }
            });
        }
    }

    public void startSingleAppointmentActivity(int position){
        Appointment currentAppointment = mAppointmentList.get(position);

        Intent i = new Intent(mActivity, OrganiserSingleAppointmentDetails.class);

        i.putExtra("currentAppointmentID", currentAppointment.getAppointmentID());

        mActivity.startActivity(i);
    }

    private String getUserFullname(int id) {
        List<User> userList = mUserViewModel.getUserById(id);
        User user = userList.get(0);
        return user.getFullName();
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

    private String updateAppointmentStatus(Appointment appointment) {
        String status = appointment.getStatus();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        int currentDate = Integer.parseInt(sdf.format(new Date()));
        int date = Integer.parseInt(appointment.getAppointmentDate());

        if (status.equals("Ongoing") && currentDate > date) {
            appointment.setStatus("Expired");
            mAppointmentViewModel.updateAppointment(appointment);
            status = "Expired";
        }

        return status;
    }
}
