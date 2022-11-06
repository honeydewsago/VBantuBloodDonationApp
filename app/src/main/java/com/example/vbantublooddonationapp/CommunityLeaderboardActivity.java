package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;

import com.example.vbantublooddonationapp.databinding.ActivityCommunityLeaderboardBinding;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityNewPostBinding;

import java.util.Objects;

public class CommunityLeaderboardActivity extends AppCompatActivity {

    private ActivityCommunityLeaderboardBinding mCommunityLeaderboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //implementing binding
        mCommunityLeaderboardBinding = ActivityCommunityLeaderboardBinding.inflate(getLayoutInflater());
        View v = mCommunityLeaderboardBinding.getRoot();
        setContentView(v);

        //set toolbar and display icon
        Toolbar toolbar = mCommunityLeaderboardBinding.aclToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));
    }
}