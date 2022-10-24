package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodBankLocationBinding;

import java.util.Objects;

public class SingleBloodBankLocation extends AppCompatActivity {

    private ActivitySingleBloodBankLocationBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private Organiser mOrganiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingleBloodBankLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.asbblToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        Intent i = getIntent();
        int organiserID = i.getIntExtra("currentOrganiserID", 0);

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

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