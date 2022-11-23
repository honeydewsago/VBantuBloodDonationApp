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

import com.example.vbantublooddonationapp.Model.Comments;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.databinding.CardCommunityCommentsBinding;
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


public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.CommunityCommentHolder>{

    private final Activity mActivity;
    public final List<Comments> mCommentsList;

    public CommunityCommentAdapter(Activity activity, ArrayList<Comments> mCommentsList) {
        mActivity = activity;
        this.mCommentsList = mCommentsList;
    }

    @NonNull
    @Override
    public CommunityCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityCommentsBinding mCardCommunityCommentsBinding = CardCommunityCommentsBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityCommentHolder(mCardCommunityCommentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityCommentAdapter.CommunityCommentHolder holder, @SuppressLint("RecyclerView") int position) {
        Comments mComments = mCommentsList.get(position);

        String comment = mComments.getComment().toString();
        String name = mComments.getUserName().toString();
        holder.mcccTvUsername.setText(name);
        holder.mcccTvComment.setText(comment);

        //user avatar
        if (Objects.equals(mComments.getOrganiserID(), "0")) {
            String userid = mComments.getUserID();
            DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User").child(userid);

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserImage userImage = snapshot.getValue(UserImage.class);
                    if (userImage != null) {
                        setUserAvatar(userImage.getUrl(), holder.mcccIvAvatar, userid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //organiser avatar
        if (Objects.equals(mComments.getUserID(), "0")) {
            String organiserid = mComments.getOrganiserID();
            DatabaseReference mRef1 = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Organiser").child(organiserid);

            mRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    OrganiserImage organiserImage = snapshot.getValue(OrganiserImage.class);
                    if (organiserImage != null) {
                        setOrganiserAvatar(organiserImage.getUrl(), holder.mcccIvAvatar, organiserid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mCommentsList == null) {
            return 0;
        }
        return mCommentsList.size();
    }

    public static class CommunityCommentHolder extends RecyclerView.ViewHolder {

        public final TextView mcccTvComment, mcccTvUsername;
        private final ImageView mcccIvAvatar;

        public CommunityCommentHolder(@NonNull CardCommunityCommentsBinding mCardCommunityCommentsBinding) {
            super(mCardCommunityCommentsBinding.getRoot());
            mcccTvComment = mCardCommunityCommentsBinding.cccTvComment;
            mcccTvUsername = mCardCommunityCommentsBinding.cccTvUsername;
            mcccIvAvatar = mCardCommunityCommentsBinding.cccIvAvatar;
        }
    }

    private void setUserAvatar(String avatarUrl, ImageView mcccIvAvatar, String userID) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/" + userID + "/"+ avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mcccIvAvatar.setImageBitmap(bitmap);
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

    private void setOrganiserAvatar(String avatarUrl, ImageView mcccIvAvatar, String organiserid) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/" + organiserid + "/" + avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mcccIvAvatar.setImageBitmap(bitmap);
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
