package com.example.vbantublooddonationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityRewardsBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

public class RewardsActivity extends AppCompatActivity {
    //initialize variable
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private User mUser;

    private ActivityRewardsBinding binding;
    private UserViewModel mUserViewModel;
    RewardViewPagerAdapter mViewPagerAdapter;
    Toolbar toolbar;
    String userPoints;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding the layout xml file
        binding = ActivityRewardsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        //set content view
        setContentView(v);

        //binding toolbar
        toolbar = binding.arToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));
        //initialize view model
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        //getting shared pref
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);
        //getting the value from shared pref
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        //return a user list with user id
        List<User> userList = mUserViewModel.getUserById(mUserID);
        mUser = userList.get(0);

        //store user points
        userPoints = String.valueOf(mUser.getPoints());
        //View pager for two tab layour
        mViewPagerAdapter = new RewardViewPagerAdapter(this);
        //setting the adapter to the view pager
        binding.arViewPager.setAdapter(mViewPagerAdapter);
        binding.arTvPoints.setText(userPoints);

        //set  onclick/swipe listener to transition between two fragment
        binding.arTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.arViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.arViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.arTabLayout.getTabAt(position).select();
            }
        });
    }

    //back function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}