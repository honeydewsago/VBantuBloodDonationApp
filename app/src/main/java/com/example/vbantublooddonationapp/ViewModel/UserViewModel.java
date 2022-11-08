package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    private LiveData<List<User>> mAllUsers;

    public UserViewModel (Application application){
        super(application);
        mRepository = new UserRepository(application);
        mAllUsers = mRepository.getAllUsers();
    }
    public LiveData<List<User>> getAllUsers(){return mAllUsers; }
    public List<String> getAllUserEmails() {
        return mRepository.getAllUserEmails();
    }
    public List<User> getUserList() {
        return mRepository.getUserList();
    }

    public void insertUser(User user){
        mRepository.insert(user);
    }

    public List<User> loginUser(String email, String password){
        return mRepository.loginUser(email,password);
    }

    public List<User> getUserById(int id){
        return mRepository.getUserById(id);
    }

    public void updateUser(User user){mRepository.update(user);}
}
