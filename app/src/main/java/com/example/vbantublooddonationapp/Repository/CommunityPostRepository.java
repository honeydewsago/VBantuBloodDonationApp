package com.example.vbantublooddonationapp.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.CommunityPostDao;
import com.example.vbantublooddonationapp.Model.CommunityPost;

import java.util.List;

public class CommunityPostRepository {
    private CommunityPostDao mCommunityPostDao;
    private LiveData<List<CommunityPost>> mAllCommunityPost;

    CommunityPostRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mCommunityPostDao = db.INSTANCE.communityPostDao();
        mAllCommunityPost = mCommunityPostDao.getAllCommunityPost();
    }

    LiveData<List<CommunityPost>> getAllCommunityPost() {
        return mAllCommunityPost;
    }

}

