package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.Repository.JavaMailAPI;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityForgotPasswordBinding;

import java.util.List;


public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private OrganiserViewModel mOrganiserViewModel;
    private UserViewModel mUserViewModel;
    private String userType;
    private int userID;
    private Organiser mOrganiser;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);



        binding.frBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = binding.afpEtEmail.getText().toString();
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        //set otp
        String otp = getAlphaNumericString(6);
        String userName ="";


        //validate to check if email is empty
        if (email.isEmpty()) {
            binding.afpEtEmail.setError(getText(R.string.emailRequired));
            binding.afpEtEmail.requestFocus();
            return;
        }
        //validate to check if email format is invalid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.afpEtEmail.setError(getText(R.string.emailInvalid));
            binding.afpEtEmail.requestFocus();
            return;
        }

        List<String> organiserEmailsList= mOrganiserViewModel.getAllOrganiserEmails();
        List<String> userEmailsList= mUserViewModel.getAllUserEmails();

        //check whether email inputted is in registered organiser email list
        for (String organiserEmail : organiserEmailsList) {
            if (organiserEmail.equals(email)) {
                userType = "organiser";
                userID = organiserEmailsList.indexOf(organiserEmail) + 1;
                List<Organiser> organiserList = mOrganiserViewModel.getOrganiserById(userID);
                mOrganiser = organiserList.get(0);
                userName = mOrganiser.getPicName();
            }
        }

        //check whether email inputted is in registered user email list
        for (String userEmail : userEmailsList) {
            if (userEmail.equals(email)) {
                userType = "user";
                userID = userEmailsList.indexOf(userEmail) + 1;
                List<User> userList = mUserViewModel.getUserById(userID);
                mUser = userList.get(0);
                userName = mUser.getFullName();
            }
        }


        Intent otpa = new Intent(ForgotPasswordActivity.this, OTPActivity.class );
        otpa.putExtra("userType", userType);
        otpa.putExtra("userID", userID);
        otpa.putExtra("otp", otp);
        //send user email the otp to reset password
        sendEmail(userName, email, otp);
        startActivity(otpa);
        finish();





    }

    //send email function
    private void sendEmail(String userName, String email, String otp) {
        String receiver = email;
        String subject = "Reset your password";
        String message = getText(R.string.greet) + " " + userName + ",\n" +
                getText(R.string.forgotPasswordEmailStarting) + "\n\n" +
                otp + "\n\n" +
                getText(R.string.forgotPasswordInfo) + "\n\n" +
                getText(R.string.emailEnding);
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, receiver, subject, message);
        javaMailAPI.execute();
    }


    //generate otp
    private String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of stringBuilder
            stringBuilder.append(AlphaNumericString
                    .charAt(index));
        }
        return stringBuilder.toString();
    }
}