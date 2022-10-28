package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.adapter.BloodTypeAdapter;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodRequestBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SingleBloodRequestActivity extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivitySingleBloodRequestBinding binding;
    private BloodRequestViewModel mBloodRequestViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private BloodRequest mBloodRequest;
    private Organiser mOrganiser;
    private List<String> mBloodTypeList;
    private BloodTypeAdapter mBloodTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleBloodRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBloodRequestViewModel = new ViewModelProvider(this).get(BloodRequestViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mBloodTypeAdapter = new BloodTypeAdapter(this);

        Toolbar toolbar = binding.asbrToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        Intent i = getIntent();
        int requestID = i.getIntExtra("currentRequestID", 1);

        List<BloodRequest> mBloodRequestList = mBloodRequestViewModel.getRequestById(requestID);
        mBloodRequest = mBloodRequestList.get(0);

        binding.asbrTvRequestInfo.setText(getString(R.string.fullRequestInformation,mBloodRequest.getRequestInfo()));

        //Date format in YYYYMMDD_HHMMSS
        String dateTime = mBloodRequest.getDateTime();
        String day = dateTime.substring(6,8);
        String year = dateTime.substring(0,4);
        int month = Integer.parseInt(dateTime.substring(4,6));
        String time = dateTime.substring(9,15);

        binding.asbrTvDate.setText(day + " "+ getMonthName(month) + " " + year);
        binding.asbrTvTime.setText(convertTimeTo12HFormat(time));

        List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mBloodRequest.getOrganiserID());
        mOrganiser = mOrganiserList.get(0);

        binding.asbrTvOrganiser.setText(mOrganiser.getCompanyName());
        binding.asbrTvAddress.setText(mOrganiser.getAddress());
        binding.asbrTvContact.setText(mOrganiser.getContact());

        mBloodTypeList = Arrays.asList(mBloodRequest.getShortageType().split(","));
        mBloodTypeAdapter.setBloodTypeList(mBloodTypeList);
        binding.asbrRvBloodType.setAdapter(mBloodTypeAdapter);

        //set the layout manager
        binding.asbrRvBloodType.setLayoutManager(new GridLayoutManager(this, 4));

        if (mUserType.equals("organiser")){
            binding.asbrBtnMakeAppointment.setEnabled(false);
            binding.asbrBtnMakeAppointment.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.medium_grey));
            binding.asbrBtnMakeAppointment.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            binding.asbrBtnMakeAppointment.setEnabled(true);
        }

        binding.asbrBtnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleBloodRequestActivity.this, MakeAppointment.class);
                intent.putExtra("currentOrganiserID", mBloodRequest.getOrganiserID());
                startActivity(intent);
            }
        });
    }

    public String getMonthName(int month_value){
        switch (month_value) {
            case 1:
                return getResources().getString(R.string.january);
            case 2:
                return getResources().getString(R.string.february);
            case 3:
                return getResources().getString(R.string.march);
            case 4:
                return getResources().getString(R.string.april);
            case 5:
                return getResources().getString(R.string.may);
            case 6:
                return getResources().getString(R.string.june);
            case 7:
                return getResources().getString(R.string.july);
            case 8:
                return getResources().getString(R.string.august);
            case 9:
                return getResources().getString(R.string.september);
            case 10:
                return getResources().getString(R.string.october);
            case 11:
                return getResources().getString(R.string.november);
            case 12:
                return getResources().getString(R.string.december);
            default:
                return getResources().getString(R.string.month);
        }
    }

    public String convertTimeTo12HFormat(String time24H) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //close the current activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}