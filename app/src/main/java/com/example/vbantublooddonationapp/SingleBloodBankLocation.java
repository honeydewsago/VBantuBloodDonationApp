package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.adapter.BloodTypeAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodBankLocationBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleBloodBankLocation extends AppCompatActivity {

    private ActivitySingleBloodBankLocationBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private Organiser mOrganiser;
    private ArrayList<String> mBloodTypeList;
    private BloodTypeAdapter mBloodTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingleBloodBankLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mBloodTypeAdapter = new BloodTypeAdapter(this);
        mBloodTypeList = new ArrayList<String>();

        Toolbar toolbar = binding.asbblToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        Intent i = getIntent();
        int organiserID = i.getIntExtra("currentOrganiserID", 0);

        List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(organiserID);
        mOrganiser = mOrganiserList.get(0);

        binding.asbblTvOrganiser.setText(mOrganiser.getCompanyName());
        binding.asbblTvAddress.setText(mOrganiser.getAddress());
        binding.asbblTvContact.setText(mOrganiser.getContact());

        mBloodTypeList.add("AB");
        mBloodTypeList.add("A");
        mBloodTypeList.add("B");
        mBloodTypeList.add("O");
        mBloodTypeAdapter.setBloodTypeList(mBloodTypeList);
        binding.asbblRvBloodType.setAdapter(mBloodTypeAdapter);

        //set the layout manager
        binding.asbblRvBloodType.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //close the current activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}