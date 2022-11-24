package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Repository.OrganiserRepository;

import java.util.List;

public class OrganiserViewModel extends AndroidViewModel {

    private OrganiserRepository mRepository;
    private LiveData<List<Organiser>> mAllOrganisers;

    public OrganiserViewModel (Application application){
        super(application);
        mRepository = new OrganiserRepository(application);
        mAllOrganisers = mRepository.getAllOrganisers();
    }

    //get all organisers
    public LiveData<List<Organiser>> getAllOrganisers() {
        return mAllOrganisers;
    }

    //send repo to insert organiser
    public void insertOrganiser(Organiser organiser){
        mRepository.insert(organiser);
    }

    //get all organiser emails
    public List<String> getAllOrganiserEmails() {
        return mRepository.getAllOrganiserEmails();
    }

    //get login organiser
    public List<Organiser> loginOrganiser(String email, String password){
        return mRepository.loginOrganiser(email,password);
    }

    //get organiser by id
    public List<Organiser> getOrganiserById(int id){
        return mRepository.getOrganiserById(id);
    }

    //send repo to update organiser details
    public void updateOrganiser(Organiser organiser){mRepository.update(organiser);}
}
