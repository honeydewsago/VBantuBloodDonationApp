package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RewardViewPagerAdapter extends FragmentStateAdapter {
    public RewardViewPagerAdapter(@NonNull FragmentActivity fragmentActivity){super(fragmentActivity);}

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new StoreFragment();
            case 1:
                return new MyRewardFragment();
            default:
                return new StoreFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
