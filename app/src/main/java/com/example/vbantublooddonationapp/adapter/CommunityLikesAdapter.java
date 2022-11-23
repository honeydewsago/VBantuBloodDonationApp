package com.example.vbantublooddonationapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.CommunityLikes;
import com.example.vbantublooddonationapp.Model.UserImage;
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
    public final List<CommunityLikes> mLikeList;

    public CommunityLikesAdapter(Activity activity, ArrayList<CommunityLikes> mLikesList) {
        mActivity = activity;
        this.mLikeList = mLikesList;
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

        String userName = communityLikes.getUserName().toString();
        holder.mcclTvUsername.setText(userName);

        String userid = communityLikes.getUserID().toString();
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User").child(userid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserImage userImage = snapshot.getValue(UserImage.class);
                if (userImage != null) {
                    setAvatar(userImage.getUrl(), holder.mcclIvAvatar, userid);
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

    private void setAvatar(String avatarUrl, ImageView mcclIvAvatar, String userID) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/" + userID + "/"+ avatarUrl);

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
