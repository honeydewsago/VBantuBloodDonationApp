package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.adapter.BloodRequestHistoryAdapter;
import com.example.vbantublooddonationapp.adapter.BloodTypeAdapter;
import com.example.vbantublooddonationapp.adapter.UrgentRequestAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityBloodRequestHistoryBinding;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodBankLocationBinding;

import java.util.List;
import java.util.Objects;

public class BloodRequestHistory extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivityBloodRequestHistoryBinding binding;
    private BloodRequestViewModel mBloodRequestViewModel;
    private BloodRequestHistoryAdapter mBloodRequestHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBloodRequestHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBloodRequestViewModel = new ViewModelProvider(this).get(BloodRequestViewModel.class);
        mBloodRequestHistoryAdapter = new BloodRequestHistoryAdapter(this);

        Toolbar toolbar = binding.abrhToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        binding.abrhBtnMakeBloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BloodRequestHistory.this,MakeBloodRequest.class);
                startActivity(i);
            }
        });

        List<BloodRequest> mBloodRequestList = mBloodRequestViewModel.getRequestByOrganiserId(mUserID);
        mBloodRequestHistoryAdapter.setRequestList(mBloodRequestList);
        binding.abrhRvBloodRequest.setAdapter(mBloodRequestHistoryAdapter);

        //set the layout manager
        binding.abrhRvBloodRequest.setLayoutManager(new GridLayoutManager(this,getResources().getInteger(R.integer.grid_column_count)));
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