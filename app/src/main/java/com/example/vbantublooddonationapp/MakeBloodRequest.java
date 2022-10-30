package com.example.vbantublooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityMakeBloodRequestBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MakeBloodRequest extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivityMakeBloodRequestBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private BloodRequestViewModel mBloodRequestViewModel;
    private Organiser mOrganiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeBloodRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mBloodRequestViewModel = new ViewModelProvider(this).get(BloodRequestViewModel.class);

        Toolbar toolbar = binding.ambrToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(mUserID);
        mOrganiser = organiserList.get(0);

        binding.ambrTvDonationCenter.setText(mOrganiser.getCompanyName());
        binding.ambrTvContactNo.setText(mOrganiser.getContact());
        binding.ambrTvAddress.setText(mOrganiser.getAddress());

        binding.ambrEtRequestInfo.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence != null) {
                    String s = charSequence.toString();
                    if (s.contains("\n")) {
                        return s.replaceAll("\n", "");
                    }
                }
                return null;
            }
        }});

        binding.ambrBtnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest();
            }
        });
    }

    private void submitRequest() {
        String bloodTypeAB="", bloodTypeA="", bloodTypeB="", bloodTypeO="", combinedBloodType="";
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

        if (requestInfo.contains("\n")) {
            requestInfo.replaceAll("\n", "");
        }

        bloodTypeAB = checkBloodType(binding.ambrChbABPov, binding.ambrChbABNeg);
        bloodTypeA = checkBloodType(binding.ambrChbAPov, binding.ambrChbANeg);
        bloodTypeB = checkBloodType(binding.ambrChbBPov, binding.ambrChbBNeg);
        bloodTypeO = checkBloodType(binding.ambrChbOPov, binding.ambrChbONeg);

        combinedBloodType = bloodTypeAB + bloodTypeA + bloodTypeB + bloodTypeO;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        BloodRequest bloodRequest = new BloodRequest(mUserID,requestInfo,combinedBloodType,currentDateTime,1);

        mBloodRequestViewModel.insertBloodRequest(bloodRequest);

        Toast.makeText(this, R.string.requestSubmittedSuccessfully, Toast.LENGTH_SHORT).show();
        finish();
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