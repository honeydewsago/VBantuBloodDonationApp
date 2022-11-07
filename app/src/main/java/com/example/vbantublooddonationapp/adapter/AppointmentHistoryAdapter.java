package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.AppointmentDetailActivity;
import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.CardAppointmentBinding;

import java.util.List;

public class AppointmentHistoryAdapter extends RecyclerView.Adapter<AppointmentHistoryAdapter.AppointmentHistoryHolder> {

    private Activity mActivity;
    private List<Appointment> mAppointmentHistoryList;
    private List<Organiser> mOrganiserList;
    private OrganiserViewModel mOrganiserViewModel;

    public AppointmentHistoryAdapter(Activity activity){
        mActivity = activity;
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
    }


    public void setAppointmentList(List<Appointment> appointmentHistoryList){mAppointmentHistoryList = appointmentHistoryList;}

    public void setOrganiserList(List<Organiser> organiserList){mOrganiserList = organiserList;}

    @NonNull
    @Override
    public AppointmentHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardAppointmentBinding itemBinding = CardAppointmentBinding.inflate(mActivity.getLayoutInflater());
        return new AppointmentHistoryHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHistoryHolder holder, int position) {
        Appointment appointment = mAppointmentHistoryList.get(position);
        String appointmentStatus;
        String bloodAmount;

        holder.mTvBDC.setText(getOrganiserName(appointment.getOrganiserID()));
        holder.mTvAppointmentDate.setText(getFullDate(appointment.getAppointmentDate()));
        holder.mTvAppointmentTime.setText(appointment.getAppointmentTime());

        appointmentStatus = appointment.getStatus();
        if(appointmentStatus.equals("Ongoing")){
            holder.mTvBloodAmount.setVisibility(View.GONE);
            holder.mTvStatusOngoing.setVisibility(View.VISIBLE);
            holder.mTvStatusComplete.setVisibility(View.GONE);
        }else{
            bloodAmount = "Blood amount: " + appointment.getBloodAmt();
            holder.mTvBloodAmount.setVisibility(View.VISIBLE);
            holder.mTvBloodAmount.setText(bloodAmount);
            holder.mTvStatusOngoing.setVisibility(View.GONE);
            holder.mTvStatusComplete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(mAppointmentHistoryList==null){
            return 0;
        }
        return mAppointmentHistoryList.size();
    }

    public class AppointmentHistoryHolder extends RecyclerView.ViewHolder{
        private TextView mTvBDC, mTvAppointmentDate, mTvAppointmentTime, mTvBloodAmount,mTvStatusOngoing,mTvStatusComplete, mTvAppointmentDetails;
        public AppointmentHistoryHolder(CardAppointmentBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvBDC = itemBinding.caTvBDC;
            mTvAppointmentDate = itemBinding.caTvAppointmentDate;
            mTvAppointmentTime = itemBinding.caTvAppointmentTime;
            mTvBloodAmount = itemBinding.caTvBloodAmount;
            mTvStatusOngoing = itemBinding.caTvStatusOngoing;
            mTvStatusComplete = itemBinding.caTvStatusComplete;
            mTvAppointmentDetails = itemBinding.caTvAppointmentDetails;

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startAppointmentDetails(getAdapterPosition());
                }
            });

            mTvAppointmentDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startAppointmentDetails(getAdapterPosition());
                }
            });
        }


    }

    public void startAppointmentDetails(int position){
        Appointment currentAppointment = mAppointmentHistoryList.get(position);
        Intent i = new Intent(mActivity, AppointmentDetailActivity.class);

        i.putExtra("currentAppointmentID", currentAppointment.getAppointmentID());

        mActivity.startActivity(i);
    }

    public String getOrganiserName(int id){
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
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
}
