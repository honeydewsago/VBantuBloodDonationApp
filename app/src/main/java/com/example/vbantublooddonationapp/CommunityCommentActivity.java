package com.example.vbantublooddonationapp;

/*
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
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.CommunityPostViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.adapter.CommunityCommentAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityCommentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommunityCommentActivity extends AppCompatActivity {

    private ActivityCommunityCommentBinding mActivityCommunityCommentBinding;
    private CommunityCommentAdapter mCommunityCommentAdapter;

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mOrganiserID = 0;
    private String mUserType = "user";
    private String username = "";
    private int currentPostID = 0;
    String currentDateTime = "";

    private Organiser mOrganiser;
    private User mUser;
    private OrganiserViewModel mOrganiserViewModel;
    private UserViewModel mUserViewModel;
    private CommunityPostViewModel mCommunityPostViewModel;

    RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mRef;

    ArrayList<Comments> mCommentsList;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        currentDateTime = sdf.format(new Date());

        //connect db
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //initialise view model
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mCommunityPostViewModel = new ViewModelProvider(this).get(CommunityPostViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        if (mUserType.equals("organiser")) {
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mUserID);
            mOrganiser = mOrganiserList.get(0);
            String organiserUsername = mOrganiser.getCompanyName();

        } else {
            List<User> mUserList = mUserViewModel.getUserById(mUserID);
            mUser = mUserList.get(0);
            username = mUser.getUsername();
        }

        //get intent and current post id
        Intent mIntent = getIntent();
        currentPostID = mIntent.getIntExtra("currentPostID", 0);
        mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Comment").child(String.valueOf(currentPostID));
        mCommentsList = new ArrayList<>();
        mCommunityCommentAdapter = new CommunityCommentAdapter(CommunityCommentActivity.this, mCommentsList);
        mActivityCommunityCommentBinding.accRvComments.setAdapter(mCommunityCommentAdapter);
        mActivityCommunityCommentBinding.accRvComments.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mActivityCommunityCommentBinding.accRvComments.setLayoutManager(mLayoutManager);

        //insert new comment
        mActivityCommunityCommentBinding.accIvCommentsSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String communityComment = mActivityCommunityCommentBinding.accEtComment.getText().toString();


                if (communityComment.isEmpty()) {
                    mActivityCommunityCommentBinding.accEtComment.setError("Please don't leave blank.");
                    mActivityCommunityCommentBinding.accEtComment.requestFocus();
                } else {
                    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!(snapshot.child("Comment").child(String.valueOf(currentPostID)).child(currentDateTime).exists())) {
                                HashMap<String, Object> comment = new HashMap<>();
                                comment.put("comment", communityComment);
                                if (mUserType.equals("user")) {
                                    comment.put("userID", mUserID);
                                    comment.put("organiserID", 0);
                                    comment.put("userName", username);
                                }
                                if (mUserType.equals("organiser")) {
                                    comment.put("userID", 0);
                                    comment.put("organiserID", mUserID);
                                    comment.put("userName", username);
                                }
                                comment.put("postID", currentPostID);
                                comment.put("date", currentDateTime);
                                rootRef.child("Comment").child(String.valueOf(currentPostID)).child(currentDateTime).updateChildren(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            System.out.println(currentPostID);
                                            Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                            finish();

                                        } else {
                                            System.out.println(currentPostID);
                                            Toast.makeText(getApplicationContext(), "Network Error. Please Try Again", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(CommunityCommentActivity.this, "Network Error. Please Try Again", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

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
                    Comments cmt = dataSnapshot.getValue(Comments.class);
                    //add the data to the array list
                    mCommentsList.add(cmt);
                    System.out.println(cmt);
                }
                //update the adapter
                mCommunityCommentAdapter.notifyDataSetChanged();


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

 */