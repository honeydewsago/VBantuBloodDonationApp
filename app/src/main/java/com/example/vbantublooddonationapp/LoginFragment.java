package com.example.vbantublooddonationapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.FragmentLoginBinding;
import com.example.vbantublooddonationapp.databinding.FragmentRegisterBinding;

import java.util.List;

public class LoginFragment extends Fragment {

    //Member variables
    private final String USERID_KEY = "userid", USERTYPE_KEY = "usertype";
    private SharedPreferences mPreferences;
    private int mUserID = 1;
    private String mUserType = "";

    private FragmentLoginBinding binding;
    private UserViewModel mUserViewModel;
    private OrganiserViewModel mOrganiserViewModel;

    private boolean status = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //initialize the view model from the UserViewModel
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);

        //get the shared preferences file
        mPreferences = getActivity().getSharedPreferences("com.example.vbantublooddonationapp",MODE_PRIVATE);

        if (mPreferences.contains(USERID_KEY) && mPreferences.contains(USERTYPE_KEY)) {
            Intent i = new Intent(getActivity(), HomeActivity.class);
            startActivity(i);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.flBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.flEtEmail.getText().toString().trim();
                String password = binding.flEtPassword.getText().toString().trim();

                //validate to check if email is empty
                if (email.isEmpty()) {
                    binding.flEtEmail.setError(getText(R.string.emailRequired));
                    binding.flEtEmail.requestFocus();
                    return;
                }

                //validate to check if password is empty
                if (password.isEmpty()) {
                    binding.flEtPassword.setError(getText(R.string.passwordRequired));
                    binding.flEtPassword.requestFocus();
                    return;
                }

                //return a list of user with the same email and password
                List<User> mUserList = mUserViewModel.loginUser(email,password);

                if (mUserList.isEmpty()) {
                    //return a list of organiser with the same email and password
                    List<Organiser> mOrganiserList = mOrganiserViewModel.loginOrganiser(email,password);

                    if (mOrganiserList.isEmpty()) {
                        //toast error message when no account is found
                        Toast.makeText(getActivity(), "Invalid Credentials! Please try again", Toast.LENGTH_SHORT).show();
                        status = false;
                    }
                    else {
                        Organiser organiser = mOrganiserList.get(0);
                        mUserID = organiser.organiserID;
                        mUserType = "organiser";
                        status = true;
                    }
                }
                else {
                    //get the user object from the list
                    User user = mUserList.get(0);
                    mUserID = user.userID;
                    mUserType = "user";
                    status = true;
                }

                //user or organiser account exists
                if (status) {
                    //save the userID in the shared preferences file
                    SharedPreferences.Editor spEditor = mPreferences.edit();
                    spEditor.putInt(USERID_KEY, mUserID);
                    spEditor.putString(USERTYPE_KEY, mUserType);
                    spEditor.apply();

                    Toast.makeText(getActivity(), R.string.userLoginSuccessfully, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(),HomeActivity.class);
                    startActivity(i);
                }
            }
        });

        binding.flTvForgotPassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPass = new Intent(getActivity(),ForgotPasswordActivity.class);
                startActivity(forgotPass);
            }
        });
    }
}