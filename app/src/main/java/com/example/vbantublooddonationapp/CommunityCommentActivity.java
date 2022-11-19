package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.adapter.CommunityCommentAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CommunityCommentActivity extends AppCompatActivity {

    private ActivityCommunityCommentBinding mActivityCommunityCommentBinding;
    private CommunityCommentAdapter mCommunityCommentAdapter;
    private TextView mcccTvComment, mcccTvUsername;

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

        //connect db
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //get intent and current post id
        Intent i = getIntent();
        String currentPostID = i.getStringExtra("currentPostID");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Comment").child("ID");
        ArrayList<Comments> mCommentsList = new ArrayList<>();


        mCommunityCommentAdapter = new CommunityCommentAdapter(this, mCommentsList);
        mActivityCommunityCommentBinding.accRvComments.setAdapter(mCommunityCommentAdapter);

        mRef.child("ID").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Comments mComments = dataSnapshot.getValue(Comments.class);
//                    mCommentsList.add(mComments);
//                }
//                mCommunityCommentAdapter.notifyDataSetChanged();
                if (snapshot.exists()){
                    Comments comment = snapshot.getValue(Comments.class);
                    mcccTvComment.setText(comment.getComment());
                    mcccTvUsername.setText(comment.getUserID());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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