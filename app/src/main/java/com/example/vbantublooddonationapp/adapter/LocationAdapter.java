package com.example.vbantublooddonationapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.OrganiserImage;
import com.example.vbantublooddonationapp.R;
import com.example.vbantublooddonationapp.SingleBloodBankLocation;
import com.example.vbantublooddonationapp.databinding.CardLocationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.intellij.lang.annotations.JdkConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Activity mActivity;
    private List<Organiser> mOrganiserList;
    private List<String> mBloodTypeList;
    private List<OrganiserImage> mOrganiserImageList;
    private BloodTypeAdapter mBloodTypeAdapter;
    private StorageReference mStorageReference;

    public LocationAdapter(Activity activity, List<OrganiserImage> organiserImageList) {
        mActivity = activity;
        mOrganiserImageList = organiserImageList;

        mBloodTypeAdapter = new BloodTypeAdapter(mActivity);

        mBloodTypeList = new ArrayList<String>();
        mBloodTypeList.add("AB");
        mBloodTypeList.add("A");
        mBloodTypeList.add("B");
        mBloodTypeList.add("O");
    }

    public void setOrganiserList(List<Organiser> organiserList) {
        mOrganiserList = organiserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardLocationBinding itemBinding = CardLocationBinding.inflate(mActivity.getLayoutInflater());
        return new LocationHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Organiser organiser = mOrganiserList.get(position);
        holder.mTvOrganiser.setText(organiser.getCompanyName());

        String imageUrl = getImageLink(organiser.getOrganiserID());
        setImage(imageUrl, holder.mIvOrganiserImage, organiser.getOrganiserID());

        String address = organiser.getAddress();
        if (address.length() > 50) {
            address = address.substring(0,47);
            address = address + "...";
        }
        holder.mTvAddress.setText(address);

        mBloodTypeAdapter.setBloodTypeList(mBloodTypeList);
        holder.mRvBloodType.setAdapter(mBloodTypeAdapter);

        //set the layout manager
        holder.mRvBloodType.setLayoutManager(new GridLayoutManager(mActivity.getApplicationContext(), 4));
    }

    @Override
    public int getItemCount() {
        if (mOrganiserList == null) {
            return 0;
        }
        return mOrganiserList.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder{
        private ImageView mIvOrganiserImage;
        private TextView mTvOrganiser;
        private TextView mTvAddress;
        private RecyclerView mRvBloodType;
        private Button mBtnMoreInfo;

        public LocationHolder(CardLocationBinding itemBinding) {
            super(itemBinding.getRoot());
            mIvOrganiserImage = itemBinding.clIvOrganiser;
            mTvOrganiser = itemBinding.clTvOrganiser;
            mTvAddress = itemBinding.clTvAddress;
            mRvBloodType = itemBinding.clRvBloodType;
            mBtnMoreInfo = itemBinding.clBtnOrganiserMoreInfo;

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSingleLocationActivity(getAdapterPosition());
                }
            });
            mBtnMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSingleLocationActivity(getAdapterPosition());
                }
            });
        }
    }

    public void startSingleLocationActivity(int position){
        Organiser currentOrganiser = mOrganiserList.get(position);

        Intent i = new Intent(mActivity, SingleBloodBankLocation.class);

        i.putExtra("currentOrganiserID", currentOrganiser.getOrganiserID());

        mActivity.startActivity(i);
    }

    private void setImage(String imageUrl, ImageView imageView, int organiserID) {
        if (imageUrl != null) {
            mStorageReference = FirebaseStorage.getInstance("gs://vbantu-blood-donation-app.appspot.com/").getReference("Organiser/"+ organiserID +"/"+imageUrl);

            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
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

    private String getImageLink(int id) {
        for (OrganiserImage organiserImage : mOrganiserImageList) {
            if (Integer.parseInt(organiserImage.getOrganiserID()) == id) {
                return organiserImage.getUrl();
            }
        }
        return null;
    }
}