package com.example.vbantublooddonationapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    LoginViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewPagerAdapter = new LoginViewPagerAdapter(this);
        binding.alViewPager.setAdapter(mViewPagerAdapter);

        binding.alTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.alViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.alViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.alTabLayout.getTabAt(position).select();
            }
        });
    }

}