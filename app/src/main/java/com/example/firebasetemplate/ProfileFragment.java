package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentProfileBinding;


public class ProfileFragment extends AppFragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentProfileBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.txtUserEmail.setText((auth.getCurrentUser().getEmail()));
        binding.txtUsername.setText((auth.getCurrentUser().getDisplayName()));
        Glide.with(requireContext()).load(auth.getCurrentUser().getPhotoUrl()).into(binding.profileImg);


//        NavGraphDirections.ActionProfileFragmentToProfileUpdateFragment action = NavGraphDirections.actionProfileFragmentToProfileUpdateFragment();
//        action.setUserid("userId");
//        navController.navigate(action);

        binding.btnUpdateProfile.setOnClickListener(v ->{
        navController.navigate(R.id.action_profileFragment_to_profileUpdateFragment);
        //navController.popBackStack();
        });

    }
}