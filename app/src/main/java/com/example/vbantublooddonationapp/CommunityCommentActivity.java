package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.databinding.ActivityCommunityCommentBinding;

import java.util.Objects;

public class CommunityCommentActivity extends AppCompatActivity {

    private ActivityCommunityCommentBinding mActivityCommunityCommentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //implementing binding
        mActivityCommunityCommentBinding = ActivityCommunityCommentBinding.inflate(getLayoutInflater());
        View v = mActivityCommunityCommentBinding.getRoot();
        setContentView(v);

        Toolbar toolbar = mActivityCommunityCommentBinding.accToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //close activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}