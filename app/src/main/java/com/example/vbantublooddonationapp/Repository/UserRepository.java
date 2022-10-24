package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.DAO.UserDao;
import com.example.vbantublooddonationapp.Model.LoginParams;
import com.example.vbantublooddonationapp.Model.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {

    private UserDao mUserDao;

    public UserRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mUserDao = db.userDao();
    }

    public void insert(User user) {
        new insertAsyncTask(mUserDao).execute(user);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao mSyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mSyncTaskDao.insert(users[0]);
            return null;
        }
    }

    public List<String> getAllUserEmails() {
        List<String> list = null;
        try {
            list = new getUserEmailAsyncTask(mUserDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getUserEmailAsyncTask extends AsyncTask<Void, Void, List<String>> {
        private UserDao mSyncTaskDao;

        getUserEmailAsyncTask(UserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<String> doInBackground(Void...params) {
            List<String> emailList = mSyncTaskDao.getAllUserEmails();
            return emailList;
        }
    }

    public List<User> loginUser(String email, String password) {
        LoginParams params = new LoginParams(email, password);

        List<User> list = null;
        try {
            list = new loginAsyncTask(mUserDao).execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static class loginAsyncTask extends AsyncTask<LoginParams, Void, List<User>> {
        private UserDao mSyncTaskDao;

        loginAsyncTask(UserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<User> doInBackground(LoginParams... loginParams) {
            List<User> userList = mSyncTaskDao.loginUser(loginParams[0].getEmail(), loginParams[0].getPassword());
            return userList;
        }
    }

}
