package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Repository.BloodRequestRepository;

public class BloodRequestViewModel extends AndroidViewModel {

    private BloodRequestRepository mRepository;

    public BloodRequestViewModel(Application application) {
        super(application);
        mRepository = new BloodRequestRepository(application);
    }

    public void insertBloodRequest(BloodRequest bloodRequest){
        mRepository.insert(bloodRequest);
    }
}