package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.AppointmentDao;
import com.example.vbantublooddonationapp.DAO.CommunityPostDao;
import com.example.vbantublooddonationapp.Model.CommunityPost;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CommunityPostRepository {
    private CommunityPostDao mCommunityPostDao;

    public CommunityPostRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mCommunityPostDao = db.communityPostDao();
    }

    public void insertCommunityPost(CommunityPost communityPost) {
        new insertAsyncTask(mCommunityPostDao).execute(communityPost);
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

    public void updateCommunityPost(CommunityPost communityPost) {
        new updateAsyncTask(mCommunityPostDao).execute(communityPost);
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

    public List<CommunityPost> getAllCommunityPost() {
        List<CommunityPost> list = null;
        try {
            list = new getAllCommunityPostAsyncTask(mCommunityPostDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getAllCommunityPostAsyncTask extends AsyncTask<Void, Void, List<CommunityPost>> {
        private CommunityPostDao mSyncTaskDao;

        getAllCommunityPostAsyncTask(CommunityPostDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<CommunityPost> doInBackground(Void... params) {
            List<CommunityPost> communityPostList = mSyncTaskDao.getAllCommunityPost();
            return communityPostList;
        }
    }

    public List<CommunityPost> getCommunityPostByID(int id) {
        List<CommunityPost> list = null;

        try {
            list = new CommunityPostRepository.getCommunityPostByIDAsyncTask(mCommunityPostDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getCommunityPostByIDAsyncTask extends AsyncTask<Integer, Void, List<CommunityPost>> {
        private CommunityPostDao mSyncTaskDao;

        getCommunityPostByIDAsyncTask(CommunityPostDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<CommunityPost> doInBackground(Integer... id) {
            List<CommunityPost> communityPostList = mSyncTaskDao.getCommunityPostByID(id[0]);
            return communityPostList;
        }
    }
}

