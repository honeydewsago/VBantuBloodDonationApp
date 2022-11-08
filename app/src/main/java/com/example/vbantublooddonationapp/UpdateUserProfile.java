package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityUpdateUserProfileBinding;
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

public class UpdateUserProfile extends AppCompatActivity {

    private ActivityUpdateUserProfileBinding binding;
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private User mUser;
    private UserViewModel mUserViewModel;
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
        binding = ActivityUpdateUserProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //setup toolbar
        Toolbar toolbar = binding.auupTbToolBar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mPreferences = getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)){
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        List<User> userList = mUserViewModel.getUserById(mUserID);
        mUser = userList.get(0);

        initProfile(mUser);

        mStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("UserAvatar").child(String.valueOf(mUserID));

        binding.auupIbEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.auupBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

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
                binding.auupSivAvatar.setImageBitmap(bitmap);
                validateImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateUserProfile.this);
        progressDialog.setTitle("Uploading......");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        String userName = binding.auupEtUsername.getText().toString();
        String contact = binding.auupEtContact.getText().toString();
        String email = binding.auupEtEmail.getText().toString();

        if (userName.isEmpty()){
            binding.auupEtUsername.setError(getText(R.string.usernameRequired));
            binding.auupEtUsername.requestFocus();
            return;
        }

        if (contact.isEmpty()){
            binding.auupEtContact.setError(getText(R.string.contactNumberRequired));
            binding.auupEtContact.requestFocus();
            return;
        }

        if (contact.length()<10){
            binding.auupEtContact.setError(getText(R.string.contactNumAtLeast10Char));
            binding.auupEtContact.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.auupEtEmail.setError(getText(R.string.emailInvalid));
            binding.auupEtEmail.requestFocus();
            return;
        }

        if (email.isEmpty()){
            binding.auupEtEmail.setError(getText(R.string.emailRequired));
            binding.auupEtEmail.requestFocus();
            return;
        }

        //update room database
        mUser.setUsername(userName);
        mUser.setContact(contact);
        mUser.setEmail(email);
        mUserViewModel.updateUser(mUser);

        if (filepath != null) {
            String receiverImage = uploadImage();
            database = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(String.valueOf(mUserID));
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("userID", String.valueOf(mUserID));
                    data.put("url", receiverImage);
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

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private String uploadImage() {
        String imageURL = UUID.randomUUID().toString();
        if (binding.auupSivAvatar!= null){
            storageReference = mStorage.getReference().child("User/"+ String.valueOf(mUserID) + "/" + imageURL);
            uploadTask = storageReference.putFile(filepath);
            Task<Uri> imageLink = storageReference.getDownloadUrl();

        }
        return imageURL;
    }

    private void initProfile(User user) {
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(String.valueOf(mUser.getUserID()));

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserImage userImage = snapshot.getValue(UserImage.class);
                if (userImage != null) {
                    setImage(userImage.getUrl(), mUserID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.auupTvFullNameDisplay.setText(user.getFullName());
        binding.auupTvBloodTypeDisplay.setText(user.getBloodType());
        binding.auupEtUsername.setText(user.getUsername());
        binding.auupEtContact.setText(user.getContact());
        binding.auupEtEmail.setText(user.getEmail());
    }

    private void setImage(String imageUrl, int userID) {
        if (imageUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/"+ userID +"/"+imageUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        binding.auupSivAvatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateUserProfile.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
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