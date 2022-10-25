package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Repository.CommunityPostRepository;

import java.util.List;

public class CommunityPostViewModel extends AndroidViewModel {
    private CommunityPostRepository mCommunityPostRepository;
    private LiveData<List<CommunityPost>> mAllCommunityPost;

    public CommunityPostViewModel (Application application) {
        super(application);
        mCommunityPostRepository = new CommunityPostRepository(application);
        mAllCommunityPost = mCommunityPostRepository.getAllCommunityPost();
    }

    LiveData<List<CommunityPost>> getAllCommunityPost(){
        return mAllCommunityPost;
    }

    public void insertCommunityPost(CommunityPost communityPost) { mCommunityPostRepository.insertCommunityPost(communityPost); }
    public void deleteCommunityPost(CommunityPost communityPost) { mCommunityPostRepository.deleteCommunityPost(communityPost); }
    public void updateCommunityPost(CommunityPost communityPost) { mCommunityPostRepository.updateCommunityPost(communityPost); }
}