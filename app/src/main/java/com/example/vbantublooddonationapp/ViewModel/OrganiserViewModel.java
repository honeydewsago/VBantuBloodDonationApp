package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Repository.OrganiserRepository;
import com.example.vbantublooddonationapp.Repository.UserRepository;

import java.util.List;

public class OrganiserViewModel extends AndroidViewModel {

    private OrganiserRepository mRepository;
    private LiveData<List<Organiser>> mAllOrganisers;

    public OrganiserViewModel (Application application){
        super(application);
        mRepository = new OrganiserRepository(application);
        mAllOrganisers = mRepository.getAllOrganisers();
    }

    public LiveData<List<Organiser>> getAllOrganisers() {
        return mAllOrganisers;
    }

    public void insertOrganiser(Organiser organiser){
        mRepository.insert(organiser);
    }

    public List<String> getAllOrganiserEmails() {
        return mRepository.getAllOrganiserEmails();
    }

    public List<Organiser> loginOrganiser(String email, String password){
        return mRepository.loginOrganiser(email,password);
    }

    public List<Organiser> getOrganiserById(int id){
        return mRepository.getOrganiserById(id);
    }

}
