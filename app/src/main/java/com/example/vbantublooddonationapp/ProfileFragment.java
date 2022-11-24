package com.example.vbantublooddonationapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.FragmentProfileBinding;
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
import java.util.List;

public class ProfileFragment extends Fragment {
    //Initialize Variable and Data
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "user";
    private List<User> mUserList;
    private List<Organiser> mOrganiserList;

    //Getting view model needed
    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;
    private FragmentProfileBinding profileBinding;
    Toolbar toolbar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setting the binding to link it to the respective layout xml
        profileBinding = FragmentProfileBinding.inflate(getLayoutInflater());

        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //shared pref
        mPreferences = getActivity().getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);
        initViewModel();
        //setting the toolbar
        toolbar = (Toolbar)getView().findViewById(R.id.fp_toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        //new intent to the setting page
        profileBinding.fpBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        //getting values form shared pref
        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY,1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }


        //if its organiser
        if (mUserType.equals("organiser")) {
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mUserID);
            Organiser organiser = mOrganiserList.get(0);
            // locating all elements in xml layout and setting text
            profileBinding.fpTvUsername.setText(organiser.getPicName());
            profileBinding.fpTvUserType.setText(R.string.user_type_o);
            profileBinding.fpTvInfoType.setText(R.string.info_type_o);
            profileBinding.fpTvCompany.setText(organiser.getCompanyName());
            profileBinding.fpTvContactNo.setText(organiser.getContact());
            profileBinding.fpTvAddress.setText(organiser.getAddress());
            // setting visibility to text view or layout that are not needed for organiser
            profileBinding.fpClPersonal.setVisibility(View.GONE);
            profileBinding.fpClOrganiser.setVisibility(View.VISIBLE);
            profileBinding.fpIvQrCode.setVisibility(View.GONE);
            profileBinding.fpTvAppointmentHistory.setVisibility(View.GONE);
            profileBinding.fpTvRewards.setVisibility(View.GONE);
            profileBinding.fpTvUserAppointments.setVisibility(View.VISIBLE);
        }
        //if it is user
        else {
            //getting user list with user id
            List<User> mUserList = mUserViewModel.getUserById(mUserID);
            User user = mUserList.get(0);
            DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(String.valueOf(user.getUserID()));
            //Event listener to get data from the firebase / realtime database
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserImage userImage = snapshot.getValue(UserImage.class);
                    if (userImage != null) {
                        setUserImage(userImage.getUrl(), mUserID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            // locating all elements in xml layout and setting text for each responding data
            profileBinding.fpTvUserType.setText(R.string.user_type_bd);
            profileBinding.fpTvUserAppointments.setVisibility(View.GONE);
            profileBinding.fpTvUsername.setText(user.username);
            profileBinding.fpTvInfoType.setText(R.string.info_type_p);
            profileBinding.fpTvEmail.setText(user.email);
            profileBinding.fpTvIC.setText(user.icNo);
            profileBinding.fpTvDOB.setText(user.dateOfBirth);
            profileBinding.fpTvBloodGroup.setText(user.bloodType);
            // setting visibility to text view or layout that are not needed for user
            profileBinding.fpClPersonal.setVisibility(View.VISIBLE);
            profileBinding.fpClOrganiser.setVisibility(View.GONE);
        }

        //New intent to the qr code page
        profileBinding.fpIvQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileQrActivity.class));
            }
        });

        //New intent to the appointment history page
        profileBinding.fpTvAppointmentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AppointmentHistoryActivity.class));
            }
        });

        //New intent to my rewards page
        profileBinding.fpTvRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RewardsActivity.class));
            }
        });
        //Intent to user appointment history (for organiser only)
        profileBinding.fpTvUserAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserAppointmentsActivity.class));
            }
        });
    }

    //Used to display user avatar
    private void setUserImage(String imageUrl, int userID) {
        if (imageUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/"+ userID +"/"+imageUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        //setting the avatar if success
                        profileBinding.fpIvUserAvatar.setImageBitmap(bitmap);
                    }
                    //checking for error
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    //initialize the live data with observer
    private void initViewModel(){
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        final Observer<List<User>> userListObserver = new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mUserList = users;
            }
        };
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(),userListObserver);

        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        final Observer<List<Organiser>> organiserListObserver = new Observer<List<Organiser>>() {
            @Override
            public void onChanged(List<Organiser> organisers) {
                mOrganiserList = organisers;
            }
        };
        mOrganiserViewModel.getAllOrganisers().observe(getViewLifecycleOwner(),organiserListObserver);
    }

}