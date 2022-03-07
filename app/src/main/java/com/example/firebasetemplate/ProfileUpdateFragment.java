package com.example.firebasetemplate;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentProfileUpdateBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class ProfileUpdateFragment extends AppFragment {

    private FragmentProfileUpdateBinding binding;
    private Uri uriImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return (binding = FragmentProfileUpdateBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.editUsername.setText(auth.getCurrentUser().getDisplayName());
        binding.txtEmail.setText(auth.getCurrentUser().getEmail());
        Glide.with(requireContext()).load(auth.getCurrentUser().getPhotoUrl()).into(binding.updateProfileImg);

        binding.updateProfileImg.setOnClickListener(v -> galeria.launch("image/*"));
        // Open Gallery
        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                Glide.with(this).load(uri).into(binding.updateProfileImg);
                uriImage = uri;
            }
        });

        binding.btnUpateProfSave.setOnClickListener(v ->{
            FirebaseUser user = auth.getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.editUsername.getText().toString())
                    .setPhotoUri(uriImage)
                    .build();
            user.updateProfile(profileUpdates);

            navController.popBackStack();
        });
       // String userid = ProfileUpdateFragmentArgs.fromBundle(getArguments()).getUserid();


    }
}