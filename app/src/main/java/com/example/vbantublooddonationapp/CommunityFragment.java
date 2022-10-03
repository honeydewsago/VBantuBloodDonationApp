package com.example.vbantublooddonationapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.vbantublooddonationapp.databinding.FragmentCommunityBinding;


public class CommunityFragment extends Fragment {
    FragmentCommunityBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCommunityBinding.inflate(inflater,container,false);

        return binding.getRoot();

    }



}