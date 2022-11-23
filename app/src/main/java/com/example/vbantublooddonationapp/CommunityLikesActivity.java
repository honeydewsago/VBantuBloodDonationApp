package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.Model.CommunityLikes;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.adapter.CommunityCommentAdapter;
import com.example.vbantublooddonationapp.adapter.CommunityLikesAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityCommentBinding;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityLikesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommunityLikesActivity extends AppCompatActivity {

    private ActivityCommunityLikesBinding mActivityCommunityLikesBinding;
    private CommunityLikesAdapter mCommunityLikesAdapter;

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mOrganiserID = 0;
    private String mUserType = "user";
    private String username = "";
    private String organiserUsername = "";
    String currentDateTime = "";

    private Organiser mOrganiser;
    private User mUser;
    private OrganiserViewModel mOrganiserViewModel;
    private UserViewModel mUserViewModel;

    RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mRef;

    ArrayList<CommunityLikes> mLikesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_likes);

        mActivityCommunityLikesBinding = ActivityCommunityLikesBinding.inflate(getLayoutInflater());
        View v = mActivityCommunityLikesBinding.getRoot();
        setContentView(v);

        Toolbar toolbar = mActivityCommunityLikesBinding.aclToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        currentDateTime = sdf.format(new Date());

        //connect db
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //initialise view model
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        if (mUserType.equals("organiser")) {
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mUserID);
            mOrganiser = mOrganiserList.get(0);
            organiserUsername = mOrganiser.getCompanyName();

        } else {
            List<User> mUserList = mUserViewModel.getUserById(mUserID);
            mUser = mUserList.get(0);
            username = mUser.getUsername();
        }

        //get intent and current post id
        Intent mIntent = getIntent();
        String postID = mIntent.getStringExtra("likePostID");
        String userID = mIntent.getStringExtra("userPostID");
        mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID);
        mLikesList = new ArrayList<>();
        mCommunityLikesAdapter = new CommunityLikesAdapter(CommunityLikesActivity.this, mLikesList);
        mActivityCommunityLikesBinding.aclRvLikes.setAdapter(mCommunityLikesAdapter);
        mActivityCommunityLikesBinding.aclRvLikes.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mActivityCommunityLikesBinding.aclRvLikes.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //read data from database
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CommunityLikes likes = dataSnapshot.getValue(CommunityLikes.class);
                    //add the data to the array list
                    mLikesList.add(likes);
                }
                //update the adapter
                mCommunityLikesAdapter.notifyDataSetChanged();
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