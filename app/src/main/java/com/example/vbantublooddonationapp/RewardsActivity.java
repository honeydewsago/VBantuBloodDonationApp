package com.example.vbantublooddonationapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vbantublooddonationapp.databinding.ActivityRewardsBinding;
import com.google.android.material.tabs.TabLayout;

public class RewardsActivity extends AppCompatActivity {
    private ActivityRewardsBinding binding;
    RewardViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewPagerAdapter = new RewardViewPagerAdapter(this);
        binding.arViewPager.setAdapter(mViewPagerAdapter);

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
}