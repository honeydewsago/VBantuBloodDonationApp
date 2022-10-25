package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityMakeBloodRequestBinding;

import java.util.List;
import java.util.Objects;

public class MakeBloodRequest extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 0;
    private String mUserType = "user";

    private ActivityMakeBloodRequestBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private Organiser mOrganiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeBloodRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        Toolbar toolbar = binding.ambrToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,0);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(mUserID);
        mOrganiser = organiserList.get(0);

        binding.ambrTvDonationCenter.setText(mOrganiser.getCompanyName());
        binding.ambrTvContactNo.setText(mOrganiser.getContact());
        binding.ambrTvAddress.setText(mOrganiser.getAddress());

        binding.ambrBtnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest();
            }
        });
    }

    private void submitRequest() {
        String bloodTypeAB="", bloodTypeA="", bloodTypeB="", bloodTypeO="", allBloodType="";
        String requestInfo = binding.ambrEtRequestInfo.getText().toString().trim();

        //validate to check if email is empty
        if (requestInfo.isEmpty()) {
            binding.ambrEtRequestInfo.setError(getText(R.string.requestInfoRequired));
            binding.ambrEtRequestInfo.requestFocus();
            return;
        }

        if (!binding.ambrChbABPov.isChecked() && !binding.ambrChbAPov.isChecked() && !binding.ambrChbBPov.isChecked() && !binding.ambrChbOPov.isChecked() &&
                !binding.ambrChbABNeg.isChecked() && !binding.ambrChbANeg.isChecked() && !binding.ambrChbBNeg.isChecked() && !binding.ambrChbONeg.isChecked()) {
            Toast.makeText(this, R.string.bloodTypeNeeded, Toast.LENGTH_SHORT).show();
            binding.ambrChbABPov.requestFocus();
            return;
        }

        bloodTypeAB = checkBloodType(binding.ambrChbABPov, binding.ambrChbABNeg);
        bloodTypeA = checkBloodType(binding.ambrChbAPov, binding.ambrChbANeg);
        bloodTypeB = checkBloodType(binding.ambrChbBPov, binding.ambrChbBNeg);
        bloodTypeO = checkBloodType(binding.ambrChbOPov, binding.ambrChbONeg);

        allBloodType = bloodTypeAB + bloodTypeA + bloodTypeB + bloodTypeO;


    }

    private String checkBloodType(CheckBox bloodPositive, CheckBox bloodNegative) {
        String bloodType="";

        if (bloodPositive.isChecked()) {
            bloodType = bloodType + bloodPositive.getText().toString() + ",";
        }
        else {
            bloodType = bloodType.replace(bloodPositive.getText().toString() + ",", "");
        }

        if (bloodNegative.isChecked()) {
            bloodType = bloodType + bloodNegative.getText().toString() + ",";
        }
        else {
            bloodType = bloodType.replace(bloodNegative.getText().toString()+",", "");
        }

        return bloodType;
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