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
    //get all users
    public LiveData<List<User>> getAllUsers(){return mAllUsers; }
    //get all user emails
    public List<String> getAllUserEmails() {
        return mRepository.getAllUserEmails();
    }
    //get user List
    public List<User> getUserList() {
        return mRepository.getUserList();
    }

    //send to repo to insert user
    public void insertUser(User user){
        mRepository.insert(user);
    }

    //login user
    public List<User> loginUser(String email, String password){
        return mRepository.loginUser(email,password);
    }

    //get user by id
    public List<User> getUserById(int id){
        return mRepository.getUserById(id);
    }

    //send repo to update user details
    public void updateUser(User user){mRepository.update(user);}
}
