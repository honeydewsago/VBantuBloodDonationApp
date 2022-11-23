package com.example.vbantublooddonationapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.CommunityCommentActivity;
import com.example.vbantublooddonationapp.Model.CommunityImage;
import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.ViewModel.CommunityPostViewModel;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.CommunityPostHolder> {

    private final Activity mActivity;
    private List<CommunityPost> mCommunityPostList;
    private List<CommunityImage> mCommunityImageList;
    private final CommunityPostViewModel mCommunityPostViewModel;
    private final OrganiserViewModel mOrganiserViewModel;
    private final UserViewModel mUserViewModel;
    CommunityLikesAdapter mCommunityLikesAdapter;
    FirebaseDatabase database;
    DatabaseReference totalComments, likes, saveLikes;
    StorageReference mStorageReference;

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

    public CommunityPostAdapter(Activity activity, List<CommunityImage> communityImageList) {
        mActivity = activity;
        mCommunityPostViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(CommunityPostViewModel.class);
        mOrganiserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider((FragmentActivity) mActivity).get(UserViewModel.class);
        mCommunityImageList = communityImageList;
    }

    public void setCommunityPostList(List<CommunityPost> communityPostList) {
        mCommunityPostList = communityPostList;
    }

    @NonNull
    @Override
    public CommunityPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCommunityPostBinding mCardCommunityPostBinding = CardCommunityPostBinding.inflate(mActivity.getLayoutInflater());
        return new CommunityPostHolder(mCardCommunityPostBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityPostAdapter.CommunityPostHolder holder, @SuppressLint("RecyclerView") int position) {
        CommunityPost communityPost = mCommunityPostList.get(position);

        mPreferences = mActivity.getSharedPreferences("com.example.vbantublooddonationapp", MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            mUserID = mPreferences.getInt(USERID_KEY, 1);
            mUserType = mPreferences.getString(USERTYPE_KEY, "user");
        }

        //show post details
        if (communityPost.organiserID == 0) {
            holder.mccpTvUsername.setText(getUserName(communityPost.getUserID()));
        }
        if (communityPost.userID == 0) {
            holder.mccpTvUsername.setText(getOrganiserName(communityPost.getOrganiserID()));
        }
        holder.mccpTvCaption.setText(communityPost.getPostDesc());

        //like post
        isLike(communityPost, holder.mccpIvLike);
        likeCommunityPost(communityPost, holder.mccpIvLike);
        holder.mccpTvLikes.setOnClickListener(v -> viewLikesDialog(postID));

        //show posted time
        getPostDuration(position, holder.mccpTvDuration);

        //redirect to comments list
        holder.mccpIvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPost communityPost = mCommunityPostList.get(position);
                communityPostComments(communityPost);
            }
        });

        holder.mccpTvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityPost communityPost = mCommunityPostList.get(position);
                communityPostComments(communityPost);
            }
        });

        //total number of likes and comments
        setTotalComments(communityPost.getPostID(), holder.mccpTvComments);
        setTotalLikes(communityPost.getPostID(), holder.mccpTvLikes);

        dateTime = communityPost.getPostDateTime();
        postID = String.valueOf(communityPost.getPostID());

        mRef = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("CommunityPost").child(postID).child(dateTime);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    CommunityImage communityImage = data.getValue(CommunityImage.class);
                    mCommunityImageList.add(communityImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //show post image
        String imageUrl = getImageLink(Integer.parseInt(postID));
        setImage(imageUrl, holder.mccpIvPostImage, postID);

        //holder.mccpIvAvatar
    }

    private void viewLikesDialog(String postID) {

        //initialize the alert dialog and set the dialog view from borrow_book_layout.xml
        AlertDialog dialogBuilder = new AlertDialog.Builder(mActivity).create();
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_community_likeslist, null);

        TextView mTvNoLikes = dialogView.findViewById(R.id.dcl_tvNolikes);
        RecyclerView mRvLikes = dialogView.findViewById(R.id.dcl_rvLikesList);
        List<String> mLikesList;
        CommunityLikesAdapter mLikesAdapter;

        DatabaseReference ref = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Likes").child(postID);

        mRvLikes.setLayoutManager(new LinearLayoutManager(mActivity));

        mLikesList = new ArrayList<>();
        mLikesAdapter = new CommunityLikesAdapter(mActivity, mLikesList);
        mRvLikes.setAdapter(mLikesAdapter);

        System.out.println("ABC" + postID);

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String userID = dataSnapshot.getKey();
                    mLikesList.add(userID);
                }
                if (mLikesList.size() > 0) {
                    mTvNoLikes.setVisibility(View.INVISIBLE);

                } else {
                    mTvNoLikes.setVisibility(View.VISIBLE);
                }
                mLikesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //set the view and show the alert dialog
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
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
                        Toast.makeText(mActivity, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getImageLink(int postID) {
        for (CommunityImage communityImage : mCommunityImageList) {
            if (communityImage.getPostID() == postID) {
                return communityImage.getUrl();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mCommunityPostList == null) {
            return 0;
        }
        return mCommunityPostList.size();
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

    public void communityPostComments(CommunityPost mCommunityPost) {
        Intent i = new Intent(mActivity, CommunityCommentActivity.class);
        i.putExtra("currentPostID", mCommunityPost.getPostID());
        System.out.println(mCommunityPost.getPostID());
        mActivity.startActivity(i);
    }

    public String getUserName(int id) {
        List<User> userList = mUserViewModel.getUserById(id);
        User user = userList.get(0);
        return user.getUsername();
    }

    public String getOrganiserName(int id) {
        List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(id);
        Organiser organiser = organiserList.get(0);
        return organiser.getCompanyName();
    }

    @SuppressLint("SetTextI18n")
    public void getPostDuration(int position, TextView mccpTvDuration) {
        CommunityPost currentPost = mCommunityPostList.get(position);
        List<CommunityPost> communityPostList = mCommunityPostViewModel.getCommunityPostByID(currentPost.getPostID());
        CommunityPost communityPost = communityPostList.get(0);

        //get current date time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        try {
            Date postTime = sdf.parse(communityPost.postDateTime);
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

    private void isLike(CommunityPost post, ImageView mccpIvLike) {

        postID = String.valueOf(post.getPostID());
        likes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Likes").child(postID).child(String.valueOf(mUserID));

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
    private void likeCommunityPost(CommunityPost post, ImageView mccpIvLike) {

        mccpIvLike.setOnTouchListener((v, event) -> {

            // show interest in events resulting from ACTION_DOWN
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            }

            // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
            if (event.getAction() != MotionEvent.ACTION_UP) return false;



            postID = String.valueOf(post.getPostID());
            saveLikes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
            saveLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.child("Likes").child(postID).child(String.valueOf(mUserID)).exists()) {
                        HashMap<String, Object> likes = new HashMap<>();
                        likes.put(String.valueOf(mUserID), true);
                        likes.put("userID", mUserID);
                        likes.put("userType", mUserType);
                        saveLikes.child("Likes").child(postID).child(String.valueOf(mUserID)).updateChildren(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
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


//            likes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID).child(String.valueOf(mUserID));
//            likes.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        mccpIvLike.setImageResource(R.drawable.ic_thumb_up_red);
//                    } else {
//                        mccpIvLike.setImageResource(R.drawable.ic_thumb_up_grey);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

//            if (mccpIvLike.isPressed()) {
//                if (mUserType.equals("organiser")) {
//                    int organiserID = mUserID;
//                    String postID = String.valueOf(post.getPostID());
//                    FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID)
//                            .child(String.valueOf(organiserID)).removeValue();
//                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_red);
//                } else {
//                    int userID = mUserID;
//                    String postID = String.valueOf(post.getPostID());
//                    FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID)
//                            .child(String.valueOf(userID)).removeValue();
//                    mccpIvLike.setImageResource(ContextCompat.getDrawable(R.drawable.ic_thumb_up_red));
//                }
//            } else {
//                if (mUserType.equals("organiser")) {
//                    int organiserID = mUserID;
//                    String postID = String.valueOf(post.getPostID());
//                    FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID)
//                            .child(String.valueOf(organiserID)).setValue(true);
//                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_grey);
//                } else {
//                    int userID = mUserID;
//                    String postID = String.valueOf(post.getPostID());
//                    FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(postID)
//                            .child(String.valueOf(userID)).setValue(true);
//                    mccpIvLike.setImageResource(R.drawable.ic_thumb_up_grey);
//                }
//            }
            return true;
        });
    }

    private void setTotalComments(int postID, final TextView mccpTvComments) {

        totalComments = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Comment").child(String.valueOf(postID));

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

    private void setTotalLikes(int postID, final TextView mccpTvLikes) {
        DatabaseReference totalLikes = FirebaseDatabase.getInstance("https://vbantu-blood-donation-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Likes").child(String.valueOf(postID));

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
