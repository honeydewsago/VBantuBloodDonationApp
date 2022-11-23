package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.CommunityPosts;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.Model.User;
//import com.example.vbantublooddonationapp.ViewModel.CommunityPostViewModel;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityCommunityNewPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class CommunityNewPostActivity extends AppCompatActivity {

    private ActivityCommunityNewPostBinding mCommunityNewPostBinding;

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mOrganiserID = 0;
    private String mUserType = "user";

    private Organiser mOrganiser;
    private User mUser;
    private CommunityPosts mCommunityPosts;
    private OrganiserViewModel mOrganiserViewModel;
    private UserViewModel mUserViewModel;

    //Firebase
    private StorageTask uploadTask;
    private DatabaseReference database;
    private StorageReference storageReference;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;

    private int SELECT_PICTURE = 200;
    private Uri filepath;
    String myUrl = "";

    private Boolean validateImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //implementing binding
        mCommunityNewPostBinding = ActivityCommunityNewPostBinding.inflate(getLayoutInflater());
        View v = mCommunityNewPostBinding.getRoot();
        setContentView(v);

        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //storageReference = FirebaseStorage.getInstance().getReference("CommunityPost").child(mCommunityPosts.postID);

        mCommunityNewPostBinding.acnpIvPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE);
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
        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

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

        String userid = String.valueOf(mUserID);
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User").child(userid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserImage userImage = snapshot.getValue(UserImage.class);
                if (userImage != null) {
                    setAvatar(userImage.getUrl(), mCommunityNewPostBinding.acnpIvAvatar, userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAvatar(String avatarUrl, ImageView acnpIvAvatar, String userID) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/" + userID + "/"+ avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        acnpIvAvatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CommunityNewPostActivity.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPost() {

        final ProgressDialog progressDialog = new ProgressDialog(CommunityNewPostActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        //get post description
        String postDesc = mCommunityNewPostBinding.acnpEtCaption.getText().toString();

        if (postDesc.isEmpty()) {
            progressDialog.dismiss();
            mCommunityNewPostBinding.acnpEtCaption.setError("Write something here.");
            mCommunityNewPostBinding.acnpEtCaption.requestFocus();
            return;
        }

        if (!validateImage){
            progressDialog.dismiss();
            mCommunityNewPostBinding.acnpIvPostImage.requestFocus();
            Toast.makeText(CommunityNewPostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        //get current date time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        //get image
        if (filepath != null) {
            String postID = UUID.randomUUID().toString();

            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("CommunityPost/" + postID);
            uploadTask = fileRef.putFile(filepath);
            uploadTask.continueWithTask(task -> {
                if (!task.isComplete()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //get the download Uri of the image
                    Uri downloadUri = (Uri) task.getResult();
                    myUrl = downloadUri.toString();

                    database = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!(snapshot.child("CommunityPost").child(postID).exists())) {
                                HashMap<String, Object> data = new HashMap<>();

                                List<User> mUserList = mUserViewModel.getUserById(mUserID);
                                mUser = mUserList.get(0);

                                List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mUserID);
                                mOrganiser = mOrganiserList.get(0);

                                data.put("postDesc", postDesc);
                                data.put("dateTime", currentDateTime);
                                data.put("url", myUrl);
                                data.put("postID", postID);
                                if (mUserType.equals("user")) {
                                    data.put("userID", String.valueOf(mUserID));
                                    data.put("userName", mUser.getUsername());
                                    data.put("organiserID", "0");
                                }
                                if (mUserType.equals("organiser")) {
                                    data.put("userID", "0");
                                    data.put("userName", mOrganiser.getCompanyName());
                                    data.put("organiserID", String.valueOf(mUserID));
                                }

                                database.child("CommunityPost").child(postID).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CommunityNewPostActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Network Error. Please Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Network Error. Please try Again", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CommunityNewPostActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }).addOnFailureListener(e -> Toast.makeText(CommunityNewPostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            progressDialog.dismiss();
            Toast.makeText(CommunityNewPostActivity.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                mCommunityNewPostBinding.acnpIvPostImage.setImageBitmap(bitmap);
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