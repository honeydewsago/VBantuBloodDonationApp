package com.example.vbantublooddonationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityUpdateOrganiserProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UpdateOrganiserProfile extends AppCompatActivity {

    private ActivityUpdateOrganiserProfileBinding binding;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private Organiser mOrganiser;
    private OrganiserViewModel mOrganiserViewModel;
    private String mUserType = "user";
    private int mUserID = 1;

    private DatabaseReference database;
    private StorageReference storageReference;
    private FirebaseStorage mStorage;

    private int SELECT_PICTURE = 200;
    private Uri filepath;
    private StorageTask uploadTask;

    private Boolean validateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateOrganiserProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //setup toolbar
        Toolbar toolbar = binding.auopTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        //get shared preferences
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(mUserID);
        mOrganiser = organiserList.get(0);


        //initialise organiser details
        initProfileDetails(mOrganiser);

        mStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("OrganisationCoverPhoto").child(String.valueOf(mUserID));

        binding.auopBtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.auopBtnSubmit.setOnClickListener(new View.OnClickListener() {
            //overwrite organiser profile info
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }

    private void initProfileDetails(Organiser organiser) {
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Organiser").child(String.valueOf(mOrganiser.getOrganiserID()));

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganiserImage organiserImage = snapshot.getValue(OrganiserImage.class);
                //set image if found child for Organiser firebase
                if (organiserImage != null) {
                    setImage(organiserImage.getUrl(), mUserID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.auopEtPersonInCharge.setText(organiser.getPicName());
        binding.auopEtIcNo.setText(organiser.getPicIcNo());
        binding.auopEtContactNo.setText(organiser.getContact());
        binding.auopEtEmail.setText(organiser.getEmail());
        binding.auopEtAddress.setText(organiser.getAddress());
    }

    private void updateProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateOrganiserProfile.this);
        progressDialog.setTitle("Uploading......");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();


        //get value from each text box
        String picName = binding.auopEtPersonInCharge.getText().toString();
        String picIC = binding.auopEtIcNo.getText().toString();
        String picContact = binding.auopEtContactNo.getText().toString();
        String picEmail = binding.auopEtEmail.getText().toString();
        String organisationAddress = binding.auopEtAddress.getText().toString();


        //validate person in charge name whether is empty
        if (picName.isEmpty()){
            binding.auopEtPersonInCharge.setError(getText(R.string.nameOfPicRequired));
            binding.auopEtPersonInCharge.requestFocus();
            return;
        }

        //validate ic no length
        if (picIC.length()!= 12){
            binding.auopEtIcNo.setError(getText(R.string.icNoFormat));
            binding.auopEtIcNo.requestFocus();
            return;
        }

        //validate ic no whether is empty
        if (picIC.isEmpty()){
            binding.auopEtIcNo.setError(getText(R.string.icNumberRequired));
            binding.auopEtIcNo.requestFocus();
            return;
        }

        //validate Contact No whether is empty
        if (picContact.isEmpty()){
            binding.auopEtContactNo.setError(getText(R.string.contactNumberRequired));
            binding.auopEtContactNo.requestFocus();
            return;
        }

        //validate length of Contact No
        if (picContact.length()<9){
            binding.auopEtContactNo.setError(getText(R.string.contactNumAtLeast9Char));
            binding.auopEtContactNo.requestFocus();
            return;
        }

        //validate whether email is empty
        if (picEmail.isEmpty()){
            binding.auopEtEmail.setError(getText(R.string.emailRequired));
            binding.auopEtEmail.requestFocus();
            return;
        }

        //validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(picEmail).matches()){
            binding.auopEtEmail.setError(getText(R.string.emailInvalid));
            binding.auopEtEmail.requestFocus();
            return;
        }

        //validate company address input whether is empty
        if (organisationAddress.isEmpty()){
            binding.auopEtAddress.setError(getText(R.string.orgnAddressRequired));
            binding.auopEtAddress.requestFocus();
            return;
        }

        //update room database
        mOrganiser.setPicName(picName);
        mOrganiser.setPicIcNo(picIC);
        mOrganiser.setContact(picContact);
        mOrganiser.setEmail(picEmail);
        mOrganiser.setAddress(organisationAddress);
        mOrganiserViewModel.updateOrganiser(mOrganiser);



        //check filepath
        if (filepath != null) {
            String receiverImage = uploadImage();
            database = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Organiser").child(String.valueOf(mUserID));
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("organiserID", String.valueOf(mUserID));
                    data.put("url", receiverImage);
                    //update data to firebase
                    database.updateChildren(data);
                    progressDialog.dismiss();
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    //uploadImage to firebase storage
    private String uploadImage() {
        String imageURL = UUID.randomUUID().toString();
        if (binding.auopIvOrganisationCoverPhoto!= null){
            storageReference = mStorage.getReference().child("Organiser/"+ String.valueOf(mUserID) + "/" + imageURL);
            uploadTask = storageReference.putFile(filepath);
            Task<Uri> imageLink = storageReference.getDownloadUrl();

        }
        return imageURL;
    }


    //set image if got url
    private void setImage(String imageUrl,  int organiserID) {
        if (imageUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/"+ organiserID +"/"+imageUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        //set imageView with organiser cover photo
                        binding.auopIvOrganisationCoverPhoto.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateOrganiserProfile.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void selectImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"),SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                binding.auopIvOrganisationCoverPhoto.setImageBitmap(bitmap);
                validateImage = true;
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