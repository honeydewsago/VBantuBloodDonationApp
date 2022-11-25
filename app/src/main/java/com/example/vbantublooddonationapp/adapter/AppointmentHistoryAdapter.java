package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.AppointmentDetailActivity;
import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.ViewModel.AppointmentViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.CardAppointmentBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentHistoryAdapter extends RecyclerView.Adapter<AppointmentHistoryAdapter.AppointmentHistoryHolder> {

    //initialize variable
    private Activity mActivity;
    private List<Appointment> mAppointmentList;
    private List<Organiser> mOrganiserList;
    private OrganiserViewModel mOrganiserViewModel;
    private AppointmentViewModel mAppointmentViewModel;

    //constructur
    public AppointmentHistoryAdapter(Activity activity){
        mActivity = activity;
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
    }


    //setting appointment list
    public void setAppointmentList(List<Appointment> appointmentList){
        mAppointmentList = appointmentList;
        notifyDataSetChanged();
    }

    //setting organiser list
    public void setOrganiserList(List<Organiser> organiserList){mOrganiserList = organiserList;}

    @NonNull
    @Override
    public AppointmentHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding with the layout xml file
        CardAppointmentBinding itemBinding = CardAppointmentBinding.inflate(mActivity.getLayoutInflater());
        return new AppointmentHistoryHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHistoryHolder holder, int position) {
        Appointment appointment = mAppointmentList.get(position);
        String bloodAmount;

        //setting the data from the appointment table
        bloodAmount = "Blood Amount: " + String.valueOf(appointment.getBloodAmt()) + " ml";

        holder.mTvBDC.setText(getOrganiserName(appointment.getOrganiserID()));
        holder.mTvAppointmentDate.setText(getFullDate(appointment.getAppointmentDate()));
        holder.mTvAppointmentTime.setText(appointment.getAppointmentTime());

        String status = updateAppointmentStatus(appointment);

        holder.mTvStatus.setText(status);
        //validation for status by change text and color
        if (status.equals("Ongoing")) {
            holder.mTvStatus.setBackgroundResource(R.color.orange);
            holder.mTvBloodAmount.setVisibility(View.GONE);
        }
        else if (status.equals("Completed")) {
            holder.mTvStatus.setBackgroundResource(R.color.green);
            holder.mTvBloodAmount.setVisibility(View.VISIBLE);
            holder.mTvBloodAmount.setText(bloodAmount);
        }
        else{
            holder.mTvStatus.setBackgroundResource(R.color.light_grey);
            holder.mTvBloodAmount.setVisibility(View.GONE);
        }
    }

    //get item count
    @Override
    public int getItemCount() {
        if(mAppointmentList==null){
            return 0;
        }
        return mAppointmentList.size();
    }

    public class AppointmentHistoryHolder extends RecyclerView.ViewHolder{
        private TextView mTvBDC, mTvAppointmentDate, mTvAppointmentTime, mTvBloodAmount,mTvStatus, mTvAppointmentDetails;
        public AppointmentHistoryHolder(CardAppointmentBinding itemBinding) {
            super(itemBinding.getRoot());
            //binding with the layout xml file
            mTvBDC = itemBinding.caTvBDC;
            mTvAppointmentDate = itemBinding.caTvAppointmentDate;
            mTvAppointmentTime = itemBinding.caTvAppointmentTime;
            mTvBloodAmount = itemBinding.caTvBloodAmount;
            mTvStatus = itemBinding.caTvStatus;
            mTvAppointmentDetails = itemBinding.caTvAppointmentDetails;

            //onclick listener to bring them to the appointment details
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

    //start appointment details with intent
    public void startAppointmentDetails(int position){
        Appointment currentAppointment = mAppointmentList.get(position);
        Intent i = new Intent(mActivity, AppointmentDetailActivity.class);

        i.putExtra("currentAppointmentID", currentAppointment.getAppointmentID());

        mActivity.startActivity(i);
    }

    //getting organiser name from the organiser table
    public String getOrganiserName(int id){
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
    }

    //formatting date from database into readable date format
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

    //update status if there is expire appointment
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
