package com.example.vbantublooddonationapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.CommunityLikes;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.CardCommunityLikesBinding;
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

public class CommunityLikesAdapter extends RecyclerView.Adapter<CommunityLikesAdapter.CommunityLikesHolder> {

    private final Activity mActivity;
    public List<CommunityLikes> mLikeList;
    private final OrganiserViewModel mOrganiserViewModel;
    private final UserViewModel mUserViewModel;

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mOrganiserID = 0;
    private String mUserType = "user";
    private Organiser mOrganiser;
    private User mUser;
    private String postID = "";
    private String dateTime = "";

    public CommunityLikesAdapter(Activity activity) {
        mActivity = activity;
        //ArrayList<CommunityLikes> mLikesList
        //this.mLikeList = mLikesList;
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(UserViewModel.class);

        //get the share preferences data, user id and user type
        mPreferences = mActivity.getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        if (mUserType.equals("organiser")) {
            List<Organiser> mOrganiserList = mOrganiserViewModel.getOrganiserById(mUserID);
            mOrganiser = mOrganiserList.get(0);
        } else {
            List<User> mUserList = mUserViewModel.getUserById(mUserID);
            mUser = mUserList.get(0);
        }
    }

    public void setLikesList(List<CommunityLikes> mLikesList){
        this.mLikeList = mLikesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommunityLikesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityLikesBinding mCardCommunityLikesBinding = CardCommunityLikesBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityLikesHolder(mCardCommunityLikesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityLikesAdapter.CommunityLikesHolder holder, @SuppressLint("RecyclerView") int position) {
        CommunityLikes communityLikes = mLikeList.get(position);

        String userName = communityLikes.getUserName();
        holder.mcclTvUsername.setText(userName);

        String userid = communityLikes.getUserID();

        //firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        if (communityLikes.getUserType().equals("organiser")) {
            mRef = mRef.child("Organiser").child(userid);
        } else {
            mRef = mRef.child("User").child(userid);
        }

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserImage userImage = snapshot.getValue(UserImage.class);
                if (userImage != null) {

                    if (communityLikes.getUserType().equals("organiser")) {
                        setOrganiserAvatar(userImage.getUrl(), holder.mcclIvAvatar, userid);
                    } else {
                        setAvatar(userImage.getUrl(), holder.mcclIvAvatar, userid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mLikeList == null) {
            return 0;
        }
        return mLikeList.size();
    }

    public static class CommunityLikesHolder extends RecyclerView.ViewHolder {

        public final TextView mcclTvUsername;
        public final ImageView mcclIvAvatar;

        public CommunityLikesHolder(@NonNull CardCommunityLikesBinding mCardCommunityLikesBinding) {
            super(mCardCommunityLikesBinding.getRoot());
            mcclTvUsername = mCardCommunityLikesBinding.cclTvUsername;
            mcclIvAvatar = mCardCommunityLikesBinding.cclIvAvatar;
        }
    }

    //set user avatar
    private void setAvatar(String avatarUrl, ImageView mcclIvAvatar, String userID) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/" + userID + "/" + avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mcclIvAvatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mActivity.getApplicationContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //set organiser avatar
    private void setOrganiserAvatar(String avatarUrl, ImageView mcclIvAvatar, String organiserid) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/" + organiserid + "/" + avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mcclIvAvatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mActivity.getApplicationContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
