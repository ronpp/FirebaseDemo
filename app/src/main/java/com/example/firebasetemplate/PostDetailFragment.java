package com.example.firebasetemplate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentPostDetailBinding;
import com.example.firebasetemplate.databinding.ViewholderCommentBinding;
import com.example.firebasetemplate.model.Comment;
import com.google.firebase.firestore.DocumentSnapshot;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends AppFragment {
    private FragmentPostDetailBinding binding;
    private List<Comment> commentsList = new ArrayList<>();
    private CommentsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPostDetailBinding.inflate(inflater, container, false)).getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            PostDetailFragmentArgs args = PostDetailFragmentArgs.fromBundle(getArguments());
            binding.author.setText(args.getAuthorName());
            Glide.with(requireContext()).load(args.getAuthorPhoto()).into(binding.authorPhoto);
            binding.commentContent.setText(args.getContent());
            Glide.with(requireContext()).load(args.getImageUrl()).into(binding.imagen);
            Glide.with(getContext()).load(auth.getCurrentUser().getPhotoUrl()).circleCrop().into(binding.userPhotoDetailPost);

            binding.recyclerComments.setAdapter(adapter = new CommentsAdapter());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            binding.recyclerComments.setLayoutManager(linearLayoutManager);

            db.collection("posts").document(args.getPostId()).collection("comments").orderBy("date")
                    .addSnapshotListener((collectionSnapshot, e) -> {
                        commentsList.clear();
                        for (DocumentSnapshot documentSnapshot: collectionSnapshot) {
                            Comment comment = documentSnapshot.toObject(Comment.class);
                            comment.commentId = documentSnapshot.getId();
                            commentsList.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    });

            binding.add.setOnClickListener(v -> {
                Comment comment = new Comment();
                comment.comment = binding.edtAddComment.getText().toString().trim();
                comment.userComment = auth.getCurrentUser().getPhotoUrl().toString();
                comment.date = LocalDateTime.now().toString();
                if (binding.edtAddComment.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("posts").document(args.getPostId())
                            .collection("comments").add(comment);
                }
            });
        }
    }

    //
    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(ViewholderCommentBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Comment comment = commentsList.get(position);
            holder.binding.commentDetail.setText(comment.comment);
            Glide.with(getContext()).load(comment.userComment).circleCrop().into(holder.binding.userPhotoComment);

        }

        @Override
        public int getItemCount() {
            return commentsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewholderCommentBinding binding;
            public ViewHolder(ViewholderCommentBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}