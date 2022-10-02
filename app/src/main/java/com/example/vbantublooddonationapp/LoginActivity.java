package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.example.vbantublooddonationapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    LoginViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        mViewPagerAdapter = new LoginViewPagerAdapter(this);
        binding.viewPager.setAdapter(mViewPagerAdapter);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.getTabAt(position).select();
            }
        });
    }

}