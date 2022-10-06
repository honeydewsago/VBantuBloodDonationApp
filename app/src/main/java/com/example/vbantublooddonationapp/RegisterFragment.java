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

import java.util.Objects;

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
                if (userType.equals("donor")) {
                    Intent ru = new Intent(getActivity(), RegisterUserActivity.class);
                    startActivity(ru);
                }
                else if (userType.equals("organiser")) {
                    Intent ro = new Intent(getActivity(), RegisterOrganiserActivity.class);
                    startActivity(ro);
                }
                else {
                    Toast.makeText(getActivity(), "Please select donor/organiser", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //return inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}