package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityNewPostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class CommunityNewPostActivity extends AppCompatActivity {

    private ActivityCommunityNewPostBinding mCommunityNewPostBinding;

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";

    private Organiser mOrganiser;
    private User mUser;
    private OrganiserViewModel mOrganiserViewModel;
    private UserViewModel mUserViewModel;

    private ImageView btn_done, community_ImageView;
    private EditText et_communityNewPost;

    //Firebase
    private StorageTask uploadTask;
    private DatabaseReference database;
    private StorageReference storageReference;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private String userUUID;

    private int SELECT_PICTURE = 200;
    private Uri filepath;
    String myUrl = "";

    private Boolean validateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //implementing binding
        mCommunityNewPostBinding = ActivityCommunityNewPostBinding.inflate(getLayoutInflater());
        View v = mCommunityNewPostBinding.getRoot();
        setContentView(v);

        mStorage = FirebaseStorage.getInstance();
        community_ImageView = (ImageView) findViewById(R.id.acnp_ivPostImage);
        mAuth = FirebaseAuth.getInstance();
//        userUUID = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference("CommunityPost").child(String.valueOf(mUserID));

        community_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),SELECT_PICTURE);

            }
        });

        mCommunityNewPostBinding.acnpIvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

        //initialise view model
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //set toolbar and display icon
        Toolbar toolbar = mCommunityNewPostBinding.acnpToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        //get data in sp file
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        if (mUserType.equals("organiser")) {
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mUserID);
            mOrganiser = mOrganiserList.get(0);
            mCommunityNewPostBinding.acnpTvUsername.setText(mOrganiser.getCompanyName());

        } else {
            List<User> mUserList = mUserViewModel.getUserById(mUserID);
            mUser = mUserList.get(0);
            mCommunityNewPostBinding.acnpTvUsername.setText(mUser.getUsername());
        }
    }

    public void uploadPost(){
        //get post description
        final ProgressDialog progressDialog = new ProgressDialog(CommunityNewPostActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        String postDesc = mCommunityNewPostBinding.acnpEtCaption.getText().toString();

        if (postDesc.isEmpty()) {
            progressDialog.dismiss();
            mCommunityNewPostBinding.acnpEtCaption.setError("Write something here.");
            mCommunityNewPostBinding.acnpEtCaption.requestFocus();
            return;
        }

        //get current date time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        Random r = new Random();
        int postID = r.nextInt();
        String pstID = String.valueOf(postID);

//        //get image
        if(filepath != null){
            String receiverImage = uploadImage();

            database = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("CommunityPost1").child(String.valueOf(mUserID)).child(currentDateTime);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("userID", String.valueOf(mUserID));
                    data.put("PostID", pstID);
                    data.put("url", receiverImage);
                    data.put("data", currentDateTime);
                    database.updateChildren(data);
                    progressDialog.dismiss();
                    Intent intent = new Intent(CommunityNewPostActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            progressDialog.dismiss();
            Toast.makeText(CommunityNewPostActivity.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private String uploadImage(){
        String imageURL = UUID.randomUUID().toString();
        if (community_ImageView != null) {
            storageReference = mStorage.getReference().child("Community/" + mUserID + "/" + imageURL);

            uploadTask = storageReference.putFile(filepath);
            Task<Uri> imageLink = storageReference.getDownloadUrl();
        }

        return imageURL;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                community_ImageView.setImageBitmap(bitmap);
                validateImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //close activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}