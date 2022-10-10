package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    public UserViewModel (Application application){
        super(application);
        mRepository = new UserRepository(application);
    }

    public void insertUser(User user){
        mRepository.insert(user);
    }
}
