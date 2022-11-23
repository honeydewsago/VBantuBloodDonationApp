package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.RewardDao;
import com.example.vbantublooddonationapp.Model.Reward;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RewardRepository {
    private RewardDao mRewardDao;

    public RewardRepository(Application application){
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mRewardDao = db.rewardDao();
    }

    public void insert(Reward reward){
        new RewardRepository.insertAsyncTask(mRewardDao).execute(reward);
    }

    public class insertAsyncTask extends AsyncTask<Reward, Void, Void> {
        private RewardDao mSyncTaskDao;
        public insertAsyncTask(RewardDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Reward... rewards) {
            mSyncTaskDao.insert(rewards[0]);
            return null;
        }
    }

    public List<Reward> getRequestById(int id){
        List<Reward> list = null;

        try {
            list = new RewardRepository.getRequestByIdAsyncTask(mRewardDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public class getRequestByIdAsyncTask extends AsyncTask<Integer,Void,List<Reward>> {
        private RewardDao mSyncTaskDao;

        public getRequestByIdAsyncTask(RewardDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<Reward> doInBackground(Integer... id) {
            List<Reward> rewardList = mRewardDao.getRequestById(id[0]);
            return rewardList;
        }
    }

    public LiveData<List<Reward>> getRewardByUserId(int id){
        LiveData<List<Reward>> list = null;
        try {
            list = new RewardRepository.getRewardByIdAsyncTask(mRewardDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static class getRewardByIdAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Reward>>> {
        private RewardDao mSyncTaskDao;

        public getRewardByIdAsyncTask(RewardDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Reward>> doInBackground(Integer... id) {
            LiveData<List<Reward>> rewardList = mSyncTaskDao.getRewardByUserId(id[0]);
            return rewardList;
        }
    }
    public void update(Reward reward) {
        new RewardRepository.updateAsyncTask(mRewardDao).execute(reward);
    }

    private static class updateAsyncTask extends AsyncTask<Reward, Void, Void> {
        private RewardDao mSyncTaskDao;

        updateAsyncTask(RewardDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Reward... reward) {
            mSyncTaskDao.update(reward[0]);
            return null;
        }
    }
}
