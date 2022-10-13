package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Repository.OrganiserRepository;
import com.example.vbantublooddonationapp.Repository.UserRepository;

import java.util.List;

public class OrganiserViewModel extends AndroidViewModel {

    private OrganiserRepository mRepository;

    public OrganiserViewModel (Application application){
        super(application);
        mRepository = new OrganiserRepository(application);
    }

    public void insertOrganiser(Organiser organiser){
        mRepository.insert(organiser);
    }

    public List<Organiser> loginOrganiser(String email, String password){
        return mRepository.loginOrganiser(email,password);
    }
}
