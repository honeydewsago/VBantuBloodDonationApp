package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.CommunityPostDao;
import com.example.vbantublooddonationapp.Model.CommunityPost;

import java.util.List;

public class CommunityPostRepository {
    private CommunityPostDao mCommunityPostDao;
    private LiveData<List<CommunityPost>> mAllCommunityPost;

    public CommunityPostRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mCommunityPostDao = db.INSTANCE.communityPostDao();
        mAllCommunityPost = mCommunityPostDao.getAllCommunityPost();
    }

    public LiveData<List<CommunityPost>> getAllCommunityPost() {
        return mAllCommunityPost;
    }

    public void insertCommunityPost (CommunityPost communityPost) {
        new insertAsyncTask(mCommunityPostDao).execute(communityPost);
    }

    public void deleteCommunityPost(CommunityPost communityPost){
        new deleteAsyncTask(mCommunityPostDao).execute(communityPost);
    }

    public void updateCommunityPost(CommunityPost communityPost){
        new updateAsyncTask(mCommunityPostDao).execute(communityPost);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<CommunityPost, Void, Void> {
        private CommunityPostDao mAsyncTaskDao;
        insertAsyncTask(CommunityPostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CommunityPost... params) {
            mAsyncTaskDao.insertCommunityPost(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<CommunityPost, Void, Void> {
        private CommunityPostDao mAsyncTaskDao;
        deleteAsyncTask(CommunityPostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CommunityPost... params) {
            mAsyncTaskDao.deleteCommunityPost(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<CommunityPost, Void, Void> {
        private CommunityPostDao mAsyncTaskDao;
        updateAsyncTask(CommunityPostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CommunityPost... params) {
            mAsyncTaskDao.updateCommunityPost(params[0]);
            return null;
        }
    }


}

