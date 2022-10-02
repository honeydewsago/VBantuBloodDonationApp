package com.example.vbantublooddonationapp.Repository;

import android.app.Application;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.UserDao;
import com.example.vbantublooddonationapp.Model.User;

import java.util.List;

public class UserRepository {

    private UserDao mUserDao;

    public UserRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mUserDao = db.userDao();
    }

}
