package com.example.vbantublooddonationapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.vbantublooddonationapp.databinding.ActivityCustomerSupportBinding;

import java.util.Objects;

public class CustomerSupport extends AppCompatActivity {

    private ActivityCustomerSupportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSupportBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //setup toolbar
        Toolbar toolbar = binding.acsTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        binding.acsBtnEmailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize support email
                String[] email = {"vbantumalaysia@gmail.com"};

                //create intent to send email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));

                //pass in the support email
                intent.putExtra(Intent.EXTRA_EMAIL, email);

                try {
                    //launch the intent
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //error message if process failed
                    Log.d("ImplicitIntent", "Can't handle this action");
                }
            }
        });


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