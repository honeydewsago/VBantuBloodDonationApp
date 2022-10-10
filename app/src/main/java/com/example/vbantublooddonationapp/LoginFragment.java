package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vbantublooddonationapp.Model.User;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.FragmentLoginBinding;
import com.example.vbantublooddonationapp.databinding.FragmentRegisterBinding;

import java.util.List;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserViewModel mUserViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //initialize the view model from the UserViewModel
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
                    binding.flEtEmail.setError("Email is required!");
                    binding.flEtEmail.requestFocus();
                    return;
                }

                //validate to check if password is empty
                if (password.isEmpty()) {
                    binding.flEtPassword.setError("Password is required!");
                    binding.flEtPassword.requestFocus();
                    return;
                }

                //return a list of user with the same email and password
                List<User> mUserList = mUserViewModel.loginUser(email,password);

                if (mUserList!= null && mUserList.isEmpty()) {

                }
                else {
                    //get the user object from the list
                    User user = mUserList.get(0);

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