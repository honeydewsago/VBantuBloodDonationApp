package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

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

    public void insert(User user) {
        new insertAsynTask(mUserDao).execute(user);
    }

    private static class insertAsynTask extends AsyncTask<User, Void, Void> {
        private UserDao mSyncTaskDao;

        insertAsynTask(UserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mSyncTaskDao.insert(users[0]);
            return null;
        }
    }
}
