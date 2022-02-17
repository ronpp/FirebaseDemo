package com.example.firebasetemplate.model;

import java.util.HashMap;

public class Post {

    public String postid;
    public String content;
    public String authorName;
    public String date;
    public String imageUrl;
    public String authorPhoto;


    //String será el ID del usuario y Boolean será true si ha dado like
    public HashMap<String, Boolean> likes = new HashMap<>();


}
