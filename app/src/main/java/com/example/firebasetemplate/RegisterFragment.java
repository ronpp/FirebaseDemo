package com.example.firebasetemplate;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterFragment extends AppFragment {
    private FragmentRegisterBinding binding;
    private Uri uriImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.registerImg.setOnClickListener(v -> galeria.launch("image/*"));
        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                Glide.with(this).load(uri).into(binding.registerImg);
                uriImage = uri;
            }

        });

        binding.createAccountButton.setOnClickListener(v -> {
            if (binding.passwordEditText.getText().toString().isEmpty()) {
                binding.passwordEditText.setError("Required");
                return;
            }
            if (binding.emailEditText.getText().toString().isEmpty()) {
                binding.emailEditText.setError("Required");
                return;
            }

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                            binding.emailEditText.getText().toString(),
                            binding.passwordEditText.getText().toString()
                    ).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(binding.userEditText.getText().toString())
                                    .setPhotoUri(uriImage)
                                    .build();
                            user.updateProfile(profileUpdates);

                            navController.navigate(R.id.action_registerFragment_to_postHomeFragment);

                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w("FAIL", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(requireContext(), task.getException().getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();

                        }

                    });
        });
    }
}