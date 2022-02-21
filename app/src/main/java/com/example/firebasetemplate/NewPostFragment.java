package com.example.firebasetemplate;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentNewPostBinding;
import com.example.firebasetemplate.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDate;
import java.util.UUID;

public class NewPostFragment extends AppFragment {

    private FragmentNewPostBinding binding;
    private Uri uriImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNewPostBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.previsualizacion.setOnClickListener(v -> galeria.launch("image/*"));

        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                Glide.with(this).load(uri).into(binding.previsualizacion);
                uriImage = uri;
            }

        });
        binding.publicar.setOnClickListener(v ->{
            binding.publicar.setEnabled(false);

            // Subir la imagen antes del post para luego obtener su URl y guardarla
            FirebaseStorage .getInstance()
                    .getReference("/images/" + UUID.randomUUID()+".jpg")
                    .putFile(uriImage)
                    .continueWithTask(task ->
                            task.getResult().getStorage().getDownloadUrl())
                    .addOnSuccessListener(urlDescarga ->{

                        Post post = new Post();
                        post.content = binding.contenido.getText().toString();
                        post.authorName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        post.date = LocalDate.now().toString();
                        post.imageUrl = urlDescarga.toString();
                        post.authorPhoto = auth.getCurrentUser().getPhotoUrl().toString();
                        db.collection("posts")
                                .add(post)
                                .addOnCompleteListener(task ->{
                                    appViewModel.setUriImagenSeleccionada(null);
                                    binding.publicar.setEnabled(true);
                                    navController.popBackStack();
                                });
                    });

        });
    }


}