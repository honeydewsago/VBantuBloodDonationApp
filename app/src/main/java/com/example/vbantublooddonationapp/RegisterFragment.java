package com.example.vbantublooddonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vbantublooddonationapp.databinding.ActivityLoginBinding;
import com.example.vbantublooddonationapp.databinding.FragmentRegisterBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private String userType="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.frToggleBtnGrp.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    if (checkedId == R.id.fr_btnDonor) {
                        binding.frEtFullName.setHint(R.string.Fullname);
                        userType = "donor";
                    }
                    else if (checkedId == R.id.fr_btnOrganiser){
                        binding.frEtFullName.setHint(R.string.CompanyName);
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
                if (userType == "donor") {
                    Intent i = new Intent(getActivity(), RegisterUserActivity.class);
                    startActivity(i);
                }
            }
        });
        //return inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}