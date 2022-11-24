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
            case 0://go storefragment if selected
                return new StoreFragment();
            case 1://go myrewardfragment if selected
                return new MyRewardFragment();
            default://default page
                return new StoreFragment();
        }
    }

    //number of fragment
    @Override
    public int getItemCount() {
        return 2;
    }
}
