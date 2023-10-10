package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.RewardTransactionDao;
import com.example.vbantublooddonationapp.Model.RewardTransaction;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class RewardTransactionRepository {
    private RewardTransactionDao mRewardsTransactionDao;

    //initialize database and dao
    public RewardTransactionRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mRewardsTransactionDao = db.rewardTransactionDao();
    }


    //function insert rewardtransaction into rewardtransaction_table
    public void insert(RewardTransaction rewardTransaction){
        new RewardTransactionRepository.insertAsyncTask(mRewardsTransactionDao).execute(rewardTransaction);
    }


    //function get all reward transaction
    public List<RewardTransaction> getAllRewardTransactions(String status) {
        List<RewardTransaction> list = null;
        try {
            list = new RewardTransactionRepository.getAllRewardsTransactionAsyncTask(mRewardsTransactionDao).execute(status).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }


    //async task for getting all rewards transaction
    private static class getAllRewardsTransactionAsyncTask extends AsyncTask<String, Void, List<RewardTransaction>> {
        private RewardTransactionDao mSyncTaskDao;

        getAllRewardsTransactionAsyncTask(RewardTransactionDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<RewardTransaction> doInBackground(String... status) {
            List<RewardTransaction> rewardTransactionList = mSyncTaskDao.getAllRewardsTransaction(status[0]);
            return rewardTransactionList;
        }
    }

    //insert async task
    public class insertAsyncTask extends AsyncTask<RewardTransaction, Void, Void> {
        private RewardTransactionDao mSyncTaskDao;
        public insertAsyncTask(RewardTransactionDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(RewardTransaction... rewardTransactions) {
            mSyncTaskDao.insert(rewardTransactions[0]);
            return null;
        }
    }
    
    //function retrieve request by id
    public List<RewardTransaction> getRequestById(int id) {
        List<RewardTransaction> list = null;

        try {
            list = new RewardTransactionRepository.getRequestByIdAsyncTask(mRewardsTransactionDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }



    //function retrieve reward transaction with reward id
    public LiveData<List<RewardTransaction>> getRewardTransactionByRewardId(int id) {
        LiveData<List<RewardTransaction>> list = null;
        try {
            list = new RewardTransactionRepository.getRewardTransactionByIdAsyncTask(mRewardsTransactionDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    //get reward transaction with id async task
    public class getRequestByIdAsyncTask extends AsyncTask<Integer, Void, List<RewardTransaction>> {
        private RewardTransactionDao mSyncTaskDao;
        public getRequestByIdAsyncTask(RewardTransactionDao dao) {mSyncTaskDao = dao;}

        @Override
        protected List<RewardTransaction> doInBackground(Integer... id) {
            List<RewardTransaction> rewardTransactionList = mRewardsTransactionDao.getRequestById(id[0]);
            return rewardTransactionList;
        }
    }

    // async task for getting reward transaction with id
    public class getRewardTransactionByIdAsyncTask extends AsyncTask<Integer,Void,LiveData<List<RewardTransaction>>>{
        private RewardTransactionDao mSyncTaskDao;
        public getRewardTransactionByIdAsyncTask(RewardTransactionDao dao) {mSyncTaskDao=dao;}

        @Override
        protected LiveData<List<RewardTransaction>> doInBackground(Integer... id) {
            LiveData<List<RewardTransaction>> rewardTransactionList = mSyncTaskDao.getRewardsTransactionByRewardId(id[0]);
            return rewardTransactionList;
        }
    }




    //function update reward transaction data
    public void update(RewardTransaction rewardTransaction) {
        new RewardTransactionRepository.updateAsyncTask(mRewardsTransactionDao).execute(rewardTransaction);
    }

    //update async task
    private static class updateAsyncTask extends AsyncTask<RewardTransaction, Void, Void> {
        private RewardTransactionDao mSyncTaskDao;

        updateAsyncTask(RewardTransactionDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(RewardTransaction... rewardTransactions) {
            mSyncTaskDao.update(rewardTransactions[0]);
            return null;
        }
    }
}
