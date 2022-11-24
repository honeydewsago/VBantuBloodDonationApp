package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.ViewModel.BloodRequestViewModel;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.adapter.BloodTypeAdapter;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodRequestBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SingleBloodRequestActivity extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivitySingleBloodRequestBinding binding;
    private BloodRequestViewModel mBloodRequestViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private BloodRequest mBloodRequest;
    private Organiser mOrganiser;
    private List<String> mBloodTypeList;
    private BloodTypeAdapter mBloodTypeAdapter;

    private DatabaseReference mRef;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleBloodRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize view model
        mBloodRequestViewModel = new ViewModelProvider(this).get(BloodRequestViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mBloodTypeAdapter = new BloodTypeAdapter(this);

        Toolbar toolbar = binding.asbrToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        //get current logged in user from shared preferences
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        //get current requestID from adapter view model
        Intent i = getIntent();
        int requestID = i.getIntExtra("currentRequestID", 1);

        //get blood request object
        List<BloodRequest> mBloodRequestList = mBloodRequestViewModel.getRequestById(requestID);
        mBloodRequest = mBloodRequestList.get(0);

        //set the requet information
        binding.asbrTvRequestInfo.setText(getString(R.string.fullRequestInformation,mBloodRequest.getRequestInfo()));

        //Date format in YYYYMMDD_HHMMSS
        String dateTime = mBloodRequest.getDateTime();

        //set the date and time
        binding.asbrTvDate.setText(getFullDate(dateTime));
        binding.asbrTvTime.setText(convertTimeTo12HFormat(dateTime));

        //get the organiser of the blood request
        List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mBloodRequest.getOrganiserID());
        mOrganiser = mOrganiserList.get(0);

        //get the organiser cover photo from firebase
        mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Organiser").child(String.valueOf(mOrganiser.getOrganiserID()));

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganiserImage organiserImage = snapshot.getValue(OrganiserImage.class);
                if (organiserImage != null) {
                    setImage(organiserImage.getUrl(), mOrganiser.getOrganiserID());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //set organiser information
        binding.asbrTvOrganiser.setText(mOrganiser.getCompanyName());
        binding.asbrTvAddress.setText(mOrganiser.getAddress());
        binding.asbrTvContact.setText(mOrganiser.getContact());

        //set the blood type adapter for blood shortage list
        mBloodTypeList = Arrays.asList(mBloodRequest.getShortageType().split(","));
        mBloodTypeAdapter.setBloodTypeList(mBloodTypeList);
        binding.asbrRvBloodType.setAdapter(mBloodTypeAdapter);

        //set the layout manager
        binding.asbrRvBloodType.setLayoutManager(new GridLayoutManager(this, 4));

        //disable make appointment button for organisers
        if (mUserType.equals("organiser")){
            binding.asbrBtnMakeAppointment.setEnabled(false);
            binding.asbrBtnMakeAppointment.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.medium_grey));
            binding.asbrBtnMakeAppointment.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            binding.asbrBtnMakeAppointment.setEnabled(true);
        }

        //button to make appointment
        binding.asbrBtnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleBloodRequestActivity.this, MakeAppointment.class);
                intent.putExtra("currentOrganiserID", mBloodRequest.getOrganiserID());
                startActivity(intent);
            }
        });
    }

    //set the organiser cover photo from image url
    private void setImage(String imageUrl, int organiserID) {
        if (imageUrl != null) {
            mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/"+ organiserID +"/"+imageUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        binding.asbrIvOrganiser.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SingleBloodRequestActivity.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //convert the numerical date to full date with month labelled
    public String getFullDate(String dateTime) {
        String year = dateTime.substring(0,4);
        int month = Integer.parseInt(dateTime.substring(4,6));
        String day = dateTime.substring(6,8);

        return day + " "+ getMonthName(month) + " " + year;
    }

    //get the month name
    public String getMonthName(int month_value){
        switch (month_value) {
            case 1:
                return getResources().getString(R.string.january);
            case 2:
                return getResources().getString(R.string.february);
            case 3:
                return getResources().getString(R.string.march);
            case 4:
                return getResources().getString(R.string.april);
            case 5:
                return getResources().getString(R.string.may);
            case 6:
                return getResources().getString(R.string.june);
            case 7:
                return getResources().getString(R.string.july);
            case 8:
                return getResources().getString(R.string.august);
            case 9:
                return getResources().getString(R.string.september);
            case 10:
                return getResources().getString(R.string.october);
            case 11:
                return getResources().getString(R.string.november);
            case 12:
                return getResources().getString(R.string.december);
            default:
                return getResources().getString(R.string.month);
        }
    }

    //convert 24H time to 12H format
    public String convertTimeTo12HFormat(String dateTime) {
        String time24H = dateTime.substring(9,15);
        int hour = Integer.parseInt(time24H.substring(0,2));
        String minute = time24H.substring(2,4);
        String period="";

        if (hour > 13) {
            hour = hour - 12;
            period = "PM";
        }
        else {
            period = "AM";
        }

        return String.valueOf(hour) + ":" + minute + " " + period;
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