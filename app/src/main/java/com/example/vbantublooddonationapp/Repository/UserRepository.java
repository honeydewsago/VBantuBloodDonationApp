package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.UserDao;
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

    public List<User> loginUser(String email, String password) {
        loginParams params = new loginParams(email, password);

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

    private static class loginParams {
        String email;
        String password;

        public loginParams(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
    private static class loginAsyncTask extends AsyncTask<loginParams, Void, List<User>> {
        private UserDao mSyncTaskDao;

        loginAsyncTask(UserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<User> doInBackground(loginParams... loginParams) {
            List<User> userList = mSyncTaskDao.loginUser(loginParams[0].getEmail(), loginParams[0].getPassword());
            return userList;
        }
    }

}
