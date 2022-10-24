package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.DAO.UserDao;
import com.example.vbantublooddonationapp.Model.LoginParams;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrganiserRepository {
    private OrganiserDao mOrganiserDao;
    private LiveData<List<Organiser>> mAllOrganisers;

    public OrganiserRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mOrganiserDao = db.organiserDao();

        mAllOrganisers = mOrganiserDao.getAllOrganisers();
    }

    public LiveData<List<Organiser>> getAllOrganisers() {
        return mAllOrganisers;
    }

    public void insert(Organiser organiser) {
        new insertAsyncTask(mOrganiserDao).execute(organiser);
    }

    private static class insertAsyncTask extends AsyncTask<Organiser, Void, Void> {
        private OrganiserDao mSyncTaskDao;

        insertAsyncTask(OrganiserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Organiser...organisers) {
            mSyncTaskDao.insert(organisers[0]);
            return null;
        }
    }

    public List<Organiser> loginOrganiser(String email, String password) {
        LoginParams params = new LoginParams(email, password);

        List<Organiser> list = null;
        try {
            list = new loginAsyncTask(mOrganiserDao).execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static class loginAsyncTask extends AsyncTask<LoginParams, Void, List<Organiser>> {
        private OrganiserDao mSyncTaskDao;

        loginAsyncTask(OrganiserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<Organiser> doInBackground(LoginParams... loginParams) {
            List<Organiser> organiserList = mSyncTaskDao.loginOrganiser(loginParams[0].getEmail(), loginParams[0].getPassword());
            return organiserList;
        }
    }
}
