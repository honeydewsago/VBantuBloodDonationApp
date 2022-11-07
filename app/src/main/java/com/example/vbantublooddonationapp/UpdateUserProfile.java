package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vbantublooddonationapp.databinding.ActivityUpdateOrganiserProfileBinding;
import com.example.vbantublooddonationapp.databinding.ActivityUpdateUserProfileBinding;

import java.util.Objects;

public class UpdateUserProfile extends AppCompatActivity {

    private ActivityUpdateUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //setup toolbar
        Toolbar toolbar = binding.auupTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));
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