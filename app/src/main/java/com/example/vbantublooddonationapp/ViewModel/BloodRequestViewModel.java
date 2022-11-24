package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Repository.BloodRequestRepository;

import java.util.List;

public class BloodRequestViewModel extends AndroidViewModel {

    private BloodRequestRepository mRepository;

    public BloodRequestViewModel(Application application) {
        super(application);
        mRepository = new BloodRequestRepository(application);
    }

    //send to repo to insert blood request
    public void insertBloodRequest(BloodRequest bloodRequest){
        mRepository.insert(bloodRequest);
    }

    //send to repo to get all active request
    public LiveData<List<BloodRequest>> getAllActiveRequests() {
        return mRepository.getAllActiveRequests();
    }

    //send to repo and get request by id
    public List<BloodRequest> getRequestById(int id){
        return mRepository.getRequestById(id);
    }

    //send to repo and get reuqest by organiser id
    public List<BloodRequest> getRequestByOrganiserId(int id){
        return mRepository.getRequestByOrganiserId(id);
    }

    //send to repo and update blood request details
    public void updateBloodRequest(BloodRequest bloodRequest){
        mRepository.update(bloodRequest);
    }
}
