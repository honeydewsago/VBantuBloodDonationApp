package com.example.vbantublooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.vbantublooddonationapp.databinding.ActivityOrganiserSingleAppointmentDetailsBinding;
import com.example.vbantublooddonationapp.databinding.ActivityScanQrcodeBinding;
import com.google.zxing.Result;

import java.util.Objects;

public class ScanQRCode extends AppCompatActivity {

    //declare variables
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;

    private ActivityScanQrcodeBinding binding;
    private int appointmentID, appointmentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set toolbar
        Toolbar toolbar = binding.asqToolbar;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_ios));

        //get current appointment and appointment user
        Intent i = getIntent();
        appointmentID = i.getIntExtra("currentAppointmentID", 1);
        appointmentUserID = i.getIntExtra("appointmentUserID", 1);

        //setup camera and enable scan function
        setupCameraPermissions();
        scanCode();
    }

    private void scanCode() {
        //enable scan
        CodeScannerView scannerView = binding.asqScannerView;
        mCodeScanner = new CodeScanner(this, scannerView);

        //decode the QR code
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanQRCode.this);

                        builder.setCancelable(false);

                        //when user ID from QR code is correct and verified, launch next activity to confirm appointment
                        if (Integer.parseInt(result.getText().trim()) == appointmentUserID) {
                            builder.setTitle("User Identity Verified!")
                                    .setMessage("Press OK to continue")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(ScanQRCode.this, ConfirmAppointmentActivity.class);
                                            i.putExtra("currentAppointmentID", appointmentID);
                                            i.putExtra("appointmentUserID", appointmentUserID);
                                            startActivity(i);
                                        }
                                    });
                        }
                        else {
                            //when user ID from QR code is invalid, prompt organiser to try again
                            builder.setTitle("Invalid user identity!")
                                    .setMessage("Press try again")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                        }

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    //make sure camera function is ongoing
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    //prompt user to allow camera permissions
    private void setupCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.cameraPermissionGrantedToast, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.cameraPermissionRequiredToast, Toast.LENGTH_LONG).show();
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