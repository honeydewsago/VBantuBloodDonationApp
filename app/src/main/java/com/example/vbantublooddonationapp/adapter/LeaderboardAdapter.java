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

import com.example.vbantublooddonationapp.Model.LeaderboardUser;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.databinding.CardLeaderboardRowBinding;
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

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardHolder> {

    private Activity mActivity;
    private List<LeaderboardUser> mLeaderboardUserList;

    public LeaderboardAdapter(Activity activity) {
        mActivity = activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLeaderboardUserList(List<LeaderboardUser> leaderboardUserList) {
        //set the list for leaderboard Users
        mLeaderboardUserList = leaderboardUserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //set the view for each holder
        CardLeaderboardRowBinding itemBinding = CardLeaderboardRowBinding.inflate(mActivity.getLayoutInflater());
        return new LeaderboardHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaderboardHolder holder, int position) {
        //set leaderboard user info
        LeaderboardUser leaderboardUser = mLeaderboardUserList.get(position);
        holder.mTvUsername.setText(leaderboardUser.getUsername());
        holder.mTvBloodAmt.setText(leaderboardUser.getBloodAmt() + "ml");

        //add leaderboard rank position
        if (position == 0) {
            holder.mIvPlaceNoBg.setImageResource(R.color.yellow);
            holder.mIvPlaceBg.setImageResource(R.color.yellow);
            holder.mTvPlaceNo.setText("1st");
        } else if (position == 1) {
            holder.mTvPlaceNo.setText("2nd");
        } else if (position == 2) {
            holder.mTvPlaceNo.setText("3rd");
        } else {
            holder.mTvPlaceNo.setText((position + 1) + "th");
        }

        String userid = String.valueOf(leaderboardUser.getUserID());

        //get user avatar image url from firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(userid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserImage userImage = snapshot.getValue(UserImage.class);
                if (userImage != null) {
                    setAvatar(userImage.getUrl(), holder.mclrIvAvatar, userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mLeaderboardUserList == null) {
            return 0;
        }
        return mLeaderboardUserList.size();
    }

    public class LeaderboardHolder extends RecyclerView.ViewHolder {
        private ImageView mIvPlaceBg;
        private ImageView mIvPlaceNoBg;
        private TextView mTvPlaceNo;
        private TextView mTvUsername;
        private TextView mTvBloodAmt;
        private ImageView mclrIvAvatar;

        public LeaderboardHolder(CardLeaderboardRowBinding itemBinding) {
            super(itemBinding.getRoot());
            mIvPlaceBg = itemBinding.clrIvPlaceBackground;
            mIvPlaceNoBg = itemBinding.clrIvPlaceNo;
            mTvPlaceNo = itemBinding.clrTvPlaceNo;
            mTvUsername = itemBinding.clrTvPlaceUsername;
            mTvBloodAmt = itemBinding.clrTvPlaceBloodAmount;
            mclrIvAvatar = itemBinding.clrIvAvatar;
        }
    }

    //set user avatar image
    private void setAvatar(String avatarUrl, ImageView mclrIvAvatar, String userID) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/" + userID + "/" + avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mclrIvAvatar.setImageBitmap(bitmap);
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
