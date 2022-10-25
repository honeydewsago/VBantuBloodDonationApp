package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.ColumnInfo;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vbantublooddonationapp.ViewModel.OrganiserViewModel;
import com.example.vbantublooddonationapp.ViewModel.UserViewModel;
import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.example.vbantublooddonationapp.databinding.FragmentRegisterBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private String userType="";
    private OrganiserViewModel mOrganiserViewModel;
    private UserViewModel mUserViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOrganiserViewModel = new ViewModelProvider(this).get(OrganiserViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.frToggleBtnGrp.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    if (checkedId == R.id.fr_btnDonor) {
                        binding.frEtFullName.setHint(R.string.fullname);
                        userType = "donor";
                    }
                    else if (checkedId == R.id.fr_btnOrganiser){
                        binding.frEtFullName.setHint(R.string.companyName);
                        userType = "organiser";
                    }
                }
                else {
                    if (group.getCheckedButtonId() == View.NO_ID) {
                        userType = "";
                        Toast.makeText(getActivity(), "Please select donor/organiser", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.frBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount();
            }
        });
    }

    private void registerAccount(){
        //get user or organiser data
        String email = binding.frEtEmail.getText().toString();
        String fullName = binding.frEtFullName.getText().toString();

        //validate to check if email is empty
        if (email.isEmpty()) {
            binding.frEtEmail.setError(getText(R.string.emailRequired));
            binding.frEtEmail.requestFocus();
            return;
        }
        //validate to check if email format is invalid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.frEtEmail.setError(getText(R.string.emailInvalid));
            binding.frEtEmail.requestFocus();
            return;
        }

        //validate to check if full name or company name is empty
        if (fullName.isEmpty()) {
            binding.frEtFullName.setError(getText(R.string.nameRequired));
            binding.frEtFullName.requestFocus();
            return;
        }

        List<String> organiserEmailsList= mOrganiserViewModel.getAllOrganiserEmails();
        List<String> userEmailsList= mUserViewModel.getAllUserEmails();

        for (String organiserEmail : organiserEmailsList) {
            if (organiserEmail.equals(email)) {
                Toast.makeText(getActivity(), R.string.emailAddressAlreadyExist, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for (String userEmail : userEmailsList) {
            if (userEmail.equals(email)) {
                Toast.makeText(getActivity(), R.string.emailAddressAlreadyExist, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (userType.equals("donor")) {
            Intent ru = new Intent(getActivity(), RegisterUserActivity.class);
            ru.putExtra("user_email", email);
            ru.putExtra("user_fullName", fullName);
            startActivity(ru);
        }
        else if (userType.equals("organiser")) {
            Intent ro = new Intent(getActivity(), RegisterOrganiserActivity.class);
            ro.putExtra("organiser_email", email);
            ro.putExtra("company_name", fullName);
            startActivity(ro);
        }
        else {
            Toast.makeText(getActivity(), "Please select donor/organiser", Toast.LENGTH_SHORT).show();
        }
    }
}