package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Repository.BloodRequestRepository;

import java.util.List;

public class BloodRequestViewModel extends AndroidViewModel {

    private BloodRequestRepository mRepository;

    public BloodRequestViewModel(Application application) {
        super(application);
        mRepository = new BloodRequestRepository(application);
    }

    public void insertBloodRequest(BloodRequest bloodRequest){
        mRepository.insert(bloodRequest);
    }

    public LiveData<List<BloodRequest>> getAllActiveRequests() {
        return mRepository.getAllActiveRequests();
    }

    public List<BloodRequest> getRequestById(int id){
        return mRepository.getRequestById(id);
    }

    public List<BloodRequest> getRequestByOrganiserId(int id){
        return mRepository.getRequestByOrganiserId(id);
    }

    public void updateBloodRequest(BloodRequest bloodRequest){
        mRepository.update(bloodRequest);
    }
}
