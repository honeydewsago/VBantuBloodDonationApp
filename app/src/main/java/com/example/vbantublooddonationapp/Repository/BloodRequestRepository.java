package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.BloodRequestDao;
import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BloodRequestRepository {
    private BloodRequestDao mBloodRequestDao;

    public BloodRequestRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mBloodRequestDao = db.bloodRequestDao();
    }

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

    public LiveData<List<BloodRequest>> getAllActiveRequests() {
        return mBloodRequestDao.getAllActiveRequests();
    }

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
}
