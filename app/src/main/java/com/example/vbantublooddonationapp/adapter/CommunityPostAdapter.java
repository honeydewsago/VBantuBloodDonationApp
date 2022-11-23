package com.example.vbantublooddonationapp.adapter;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.example.vbantublooddonationapp.CommunityCommentActivity;
import com.example.vbantublooddonationapp.CommunityLikesActivity;
import com.example.vbantublooddonationapp.Model.CommunityLikes;
import com.example.vbantublooddonationapp.Model.CommunityPosts;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Model.UserImage;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.UpdateUserProfile;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.CardCommunityPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.CommunityPostHolder> {

    final Context context;
    private List<CommunityPosts> mCommunityPostsList;
    private List<CommunityLikes> mCommunityLikesList;
    private final OrganiserViewModel mOrganiserViewModel;
    private final UserViewModel mUserViewModel;
    FirebaseDatabase database;
    DatabaseReference totalComments, likes, saveLikes;
    StorageReference mStorageReference;
    CommunityCommentAdapter mCommunityCommentAdapter;

    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private int mOrganiserID = 0;
    private String mUserType = "user";
    private Organiser mOrganiser;
    private User mUser;
    private String postID = "";
    private String dateTime = "";
    private DatabaseReference mRef;

    public CommunityPostAdapter(Context context, List<CommunityPosts> communityPostsList, List<CommunityLikes> communityLikesList) {
        //initialize
        this.context = context;
        this.mCommunityPostsList = communityPostsList;
        this.mCommunityLikesList = communityLikesList;
        database = FirebaseDatabase.getInstance();
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) context).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider((FragmentActivity) context).get(UserViewModel.class);
    }

    @NonNull
    @Override
    public CommunityPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityPostBinding mCardCommunityPostBinding = CardCommunityPostBinding.inflate(LayoutInflater.from(context));
        return new CommunityPostHolder(mCardCommunityPostBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityPostAdapter.CommunityPostHolder holder, @SuppressLint("RecyclerView") int position) {
        CommunityPosts communityPosts = mCommunityPostsList.get(position);

        //get current user id and user type
        mPreferences = context.getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

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

        //show post details
        String organiserID = communityPosts.organiserID;
        int mOrganiserID = Integer.parseInt(organiserID);
        if (mOrganiserID == 0) {
            holder.mccpTvUsername.setText(communityPosts.getUserName());
        }

        String userID = String.valueOf(mUserID);
        int mUserID = Integer.parseInt(userID);
        if (mUserID == 0) {
            holder.mccpTvUsername.setText(communityPosts.getUserName());
        }

        holder.mccpTvCaption.setText(communityPosts.getPostDesc());

        //like post
        isLike(communityPosts, holder.mccpIvLike);
        likeCommunityPost(communityPosts, holder.mccpIvLike);

        holder.mccpTvLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPosts communityPosts = mCommunityPostsList.get(position);
                communityLikesPosts(communityPosts);
            }
        });

        //show posted time
        getPostDuration(communityPosts, holder.mccpTvDuration);

        //redirect to comments list
        holder.mccpIvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPosts communityPosts = mCommunityPostsList.get(position);
                communityPostComments(communityPosts);
            }
        });

        holder.mccpTvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPosts communityPosts = mCommunityPostsList.get(position);
                communityPostComments(communityPosts);
            }
        });

        //total number of likes and comments
        setTotalComments(communityPosts.getPostID(), holder.mccpTvComments);
        setTotalLikes(communityPosts.getPostID(), holder.mccpTvLikes);

        //get current date time and post id
        dateTime = communityPosts.getdateTime();
        postID = communityPosts.getPostID();

        //show post image
        String imageUrl = getImageLink(postID);
        setImage(imageUrl, holder.mccpIvPostImage, postID);

        //user avatar
        if (Objects.equals(communityPosts.organiserID, "0")) {
            String userid = communityPosts.getUserID();
            DatabaseReference mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User").child(userid);

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserImage userImage = snapshot.getValue(UserImage.class);
                    if (userImage != null) {
                        setUserAvatar(userImage.getUrl(), holder.mccpIvAvatar, userid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //organiser avatar
        if (Objects.equals(communityPosts.userID, "0")) {
            String organiserid = String.valueOf(mOrganiser.getOrganiserID());
            DatabaseReference mRef1 = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Organiser").child(organiserid);

            mRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    OrganiserImage organiserImage = snapshot.getValue(OrganiserImage.class);
                    if (organiserImage != null) {
                        setOrganiserAvatar(organiserImage.getUrl(), holder.mccpIvAvatar, organiserid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setUserAvatar(String avatarUrl, ImageView mccpIvAvatar, String userID) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("User/" + userID + "/" + avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mccpIvAvatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setOrganiserAvatar(String avatarUrl, ImageView mccpIvAvatar, String organiserid) {
        if (avatarUrl != null) {
            StorageReference mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/" + organiserid + "/" + avatarUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mccpIvAvatar.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImage(String imageUrl, ImageView mccpIvPostImage, String postID) {
        if (imageUrl != null) {
            mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("CommunityPost/" + postID);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mccpIvPostImage.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getImageLink(String postID) {
        for (CommunityPosts communityPosts : mCommunityPostsList) {
            if (Objects.equals(communityPosts.getPostID(), postID)) {
                return communityPosts.getUrl();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mCommunityPostsList == null) {
            return 0;
        }
        return mCommunityPostsList.size();
    }

    public static class CommunityPostHolder extends RecyclerView.ViewHolder {
        private final TextView mccpTvUsername;
        private final TextView mccpTvCaption;
        private final TextView mccpTvDuration;
        private TextView mccpTvLikes;
        private final TextView mccpTvComments;
        private ImageView mccpIvAvatar;
        private ImageView mccpIvPostImage;
        private final ImageView mccpIvComment;
        private final ImageView mccpIvLike;

        public CommunityPostHolder(@NonNull CardCommunityPostBinding mCardCommunityPostBinding) {
            super(mCardCommunityPostBinding.getRoot());
            mccpTvUsername = mCardCommunityPostBinding.ccpTvUsername;
            mccpTvCaption = mCardCommunityPostBinding.ccpTvCaption;
            mccpTvDuration = mCardCommunityPostBinding.ccpTvDuration;
            mccpTvLikes = mCardCommunityPostBinding.ccpTvLikes;
            mccpIvAvatar = mCardCommunityPostBinding.ccpIvAvatar;
            mccpIvPostImage = mCardCommunityPostBinding.ccpIvPostImage;
            mccpTvComments = mCardCommunityPostBinding.ccpTvComments;
            mccpIvComment = mCardCommunityPostBinding.ccpIvComment;
            mccpIvLike = mCardCommunityPostBinding.ccpIvLike;
        }
    }

    public void communityLikesPosts(CommunityPosts communityPosts) {
        String pstID = communityPosts.getPostID();
        String userID = String.valueOf(mUserID);
        Intent likeIntent = new Intent(context, CommunityLikesActivity.class);
        likeIntent.putExtra("likePostID", pstID);
        likeIntent.putExtra("userPostID", userID);
        likeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(likeIntent);
    }

    public void communityPostComments(CommunityPosts mCommunityPost) {
        Intent i = new Intent(context, CommunityCommentActivity.class);
        i.putExtra("currentPostID", mCommunityPost.getPostID());
        context.startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    public void getPostDuration(CommunityPosts post, TextView mccpTvDuration) {

        String dateT = post.getdateTime();
        //get current date time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        try {
            Date postTime = sdf.parse(dateT);
            Date end = sdf.parse(currentTime);
            long diff = Objects.requireNonNull(end).getTime() - Objects.requireNonNull(postTime).getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            int hours = (int) (diff / (1000 * 60 * 60));
            int minutes = (int) (diff / (1000 * 60));
            int months = days / 31;

            if (months > 1) {
                mccpTvDuration.setText(months + " months ago");
            } else if (months == 1) {
                mccpTvDuration.setText(months + " month ago");
            } else if (days > 1) {
                mccpTvDuration.setText(days + " days ago");
            } else if (days == 1) {
                mccpTvDuration.setText(days + " day ago");
            } else if (hours > 1) {
                mccpTvDuration.setText(hours + " hours ago");
            } else if (hours == 1) {
                mccpTvDuration.setText(hours + " hour ago");
            } else if (minutes > 1) {
                mccpTvDuration.setText(minutes + " minutes ago");
            } else if (minutes == 1) {
                mccpTvDuration.setText(minutes + " minute ago");
            } else {
                mccpTvDuration.setText("just now");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void isLike(CommunityPosts post, ImageView mccpIvLike) {

        String postID = post.getPostID();
        String userID = String.valueOf(mUserID);
        likes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Likes").child(postID).child(userID);

        likes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_red);
                } else {
                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_grey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void likeCommunityPost(CommunityPosts post, ImageView mccpIvLike) {

        mccpIvLike.setOnTouchListener((v, event) -> {

            // show interest in events resulting from ACTION_DOWN
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            }

            // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
            if (event.getAction() != MotionEvent.ACTION_UP) return false;

            String userID = String.valueOf(mUserID);
            String postID = post.getPostID();

            saveLikes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
            saveLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.child("Likes").child(postID).child(userID).exists()) {
                        HashMap<String, Object> likes = new HashMap<>();
                        likes.put("userLikes", "true");
                        likes.put("userID", userID);
                        likes.put("userType", mUserType);
                        likes.put("userName", mUser.getUsername());
                        likes.put("postID", postID);
                        saveLikes.child("Likes").child(postID).child(userID).updateChildren(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_red);

                                } else {
                                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_grey);
                                }
                            }
                        });
                    } else {
                        saveLikes.child("Likes").child(postID).child(String.valueOf(mUserID)).removeValue();
                        mccpIvLike.setImageResource(R.drawable.ic_thumb_up_grey);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return true;
        });
    }

    private void setTotalComments(String postID, final TextView mccpTvComments) {

        totalComments = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Comment").child(postID);

        totalComments.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalComments = snapshot.getChildrenCount();
                if (totalComments == 0) {
                    mccpTvComments.setText(totalComments + " Comment");
                } else {
                    mccpTvComments.setText(totalComments + " Comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTotalLikes(String postID, final TextView mccpTvLikes) {
        DatabaseReference totalLikes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID);

        totalLikes.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mccpTvLikes.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
