package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.adapter.BloodTypeAdapter;
import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.example.vbantublooddonationapp.databinding.ActivitySingleBloodBankLocationBinding;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleBloodBankLocation extends AppCompatActivity {

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private ActivitySingleBloodBankLocationBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private Organiser mOrganiser;
    private List<String> mBloodTypeList;
    private BloodTypeAdapter mBloodTypeAdapter;

    private DatabaseReference mRef;
    private StorageReference mStorageReference;

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

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        Intent i = getIntent();
        int organiserID = i.getIntExtra("currentOrganiserID", 1);

        List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(organiserID);
        mOrganiser = mOrganiserList.get(0);

        mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Organiser").child(String.valueOf(organiserID));

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganiserImage organiserImage = snapshot.getValue(OrganiserImage.class);
                if (organiserImage != null) {
                    setImage(organiserImage.getUrl(), organiserID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        if (mUserType.equals("organiser")){
            binding.asbblBtnMakeAppointment.setEnabled(false);
            binding.asbblBtnMakeAppointment.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.medium_grey));
            binding.asbblBtnMakeAppointment.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            binding.asbblBtnMakeAppointment.setEnabled(true);
        }

        binding.asbblBtnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleBloodBankLocation.this, MakeAppointment.class);
                intent.putExtra("currentOrganiserID", mOrganiser.getOrganiserID());
                startActivity(intent);
            }
        });
    }

    private void setImage(String imageUrl, int organiserID) {
        if (imageUrl != null) {
            mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/"+ organiserID +"/"+imageUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        binding.asbblIvOrganiser.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SingleBloodBankLocation.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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