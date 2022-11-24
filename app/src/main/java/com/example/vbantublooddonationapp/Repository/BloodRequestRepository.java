package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.BloodRequestDao;
import com.example.vbantublooddonationapp.Model.BloodRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BloodRequestRepository {
    private BloodRequestDao mBloodRequestDao;

    //initialize database and dao
    public BloodRequestRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mBloodRequestDao = db.bloodRequestDao();
    }

    //function insert data into blood_request_table
    public void insert(BloodRequest bloodRequest) {
        new BloodRequestRepository.insertAsyncTask(mBloodRequestDao).execute(bloodRequest);
    }


    private static class insertAsyncTask extends AsyncTask<BloodRequest, Void, Void> {
        private BloodRequestDao mSyncTaskDao;

        insertAsyncTask(BloodRequestDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(BloodRequest...bloodRequests) {
            mSyncTaskDao.insert(bloodRequests[0]);
            return null;
        }
    }

    //function retrieve all active requests
    public LiveData<List<BloodRequest>> getAllActiveRequests() {
        return mBloodRequestDao.getAllActiveRequests();
    }

    //function retrieve request using id
    public List<BloodRequest> getRequestById(int id) {
        List<BloodRequest> list = null;

        try {
            list = new BloodRequestRepository.getRequestAsyncTask(mBloodRequestDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static class getRequestAsyncTask extends AsyncTask<Integer, Void, List<BloodRequest>> {
        private BloodRequestDao mSyncTaskDao;

        getRequestAsyncTask(BloodRequestDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<BloodRequest> doInBackground(Integer...id) {
            List<BloodRequest> requestList = mSyncTaskDao.getRequestById(id[0]);
            return requestList;
        }
    }

    //function retrieve request by organiser id
    public List<BloodRequest> getRequestByOrganiserId(int id) {
        List<BloodRequest> list = null;

        try {
            list = new BloodRequestRepository.getOrganiserRequestAsyncTask(mBloodRequestDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static class getOrganiserRequestAsyncTask extends AsyncTask<Integer, Void, List<BloodRequest>> {
        private BloodRequestDao mSyncTaskDao;

        getOrganiserRequestAsyncTask(BloodRequestDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<BloodRequest> doInBackground(Integer...id) {
            List<BloodRequest> requestList = mSyncTaskDao.getRequestByOrganiserId(id[0]);
            return requestList;
        }
    }

    //function update data in blood_request table
    public void update(BloodRequest bloodRequest) {
        new BloodRequestRepository.updateAsyncTask(mBloodRequestDao).execute(bloodRequest);
    }


    private static class updateAsyncTask extends AsyncTask<BloodRequest, Void, Void> {
        private BloodRequestDao mSyncTaskDao;

        updateAsyncTask(BloodRequestDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(BloodRequest...bloodRequests) {
            mSyncTaskDao.update(bloodRequests[0]);
            return null;
        }
    }
}
