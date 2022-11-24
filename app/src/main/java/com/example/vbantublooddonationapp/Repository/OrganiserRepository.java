package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.Model.LoginParams;
import com.example.vbantublooddonationapp.Model.Organiser;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrganiserRepository {
    private OrganiserDao mOrganiserDao;
    private LiveData<List<Organiser>> mAllOrganisers;

    //initialize database and dao
    public OrganiserRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mOrganiserDao = db.organiserDao();

        mAllOrganisers = mOrganiserDao.getAllOrganisers();
    }

    //function retrieve all organiser
    public LiveData<List<Organiser>> getAllOrganisers() {
        return mAllOrganisers;
    }

    //function insert data into organiser_table
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

    //function login as organiser
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
        protected List<Organiser> doInBackground(LoginParams...loginParams) {
            List<Organiser> organiserList = mSyncTaskDao.loginOrganiser(loginParams[0].getEmail(), loginParams[0].getPassword());
            return organiserList;
        }
    }

    //function get all organiser email
    public List<String> getAllOrganiserEmails() {
        List<String> list = null;
        try {
            list = new getOrganiserEmailAsyncTask(mOrganiserDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getOrganiserEmailAsyncTask extends AsyncTask<Void, Void, List<String>> {
        private OrganiserDao mSyncTaskDao;

        getOrganiserEmailAsyncTask(OrganiserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<String> doInBackground(Void...params) {
            List<String> emailList = mSyncTaskDao.getAllOrganiserEmails();
            return emailList;
        }
    }

    public List<Organiser> getOrganiserById(int id) {
        List<Organiser> list = null;

        try {
            list = new getOrganiserAsyncTask(mOrganiserDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getOrganiserAsyncTask extends AsyncTask<Integer, Void, List<Organiser>> {
        private OrganiserDao mSyncTaskDao;

        getOrganiserAsyncTask(OrganiserDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<Organiser> doInBackground(Integer...id) {
            List<Organiser> organiserList = mSyncTaskDao.getOrganiserById(id[0]);
            return organiserList;
        }
    }

    //function update organiser detail into organiser table
    public void update(Organiser organiser) {
        new OrganiserRepository.updateAsyncTask(mOrganiserDao).execute(organiser);
    }

    private static class updateAsyncTask extends AsyncTask<Organiser, Void, Void>{
        private OrganiserDao mSyncTaskDao;

        updateAsyncTask(OrganiserDao dao){mSyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Organiser... organisers) {
            mSyncTaskDao.update(organisers[0]);
            return null;
        }
    }

}
