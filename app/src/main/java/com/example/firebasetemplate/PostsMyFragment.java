package com.example.firebasetemplate;

import com.google.firebase.firestore.Query;


public class PostsMyFragment extends PostsHomeFragment{

    Query setQuery(){
        return db.collection("posts").whereEqualTo("authorName", auth.getCurrentUser().getEmail());
    }
}
