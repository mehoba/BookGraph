package com.example.bookgraph;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Database {

    public static void addBook(HashMap<String,Object> user,HashMap<String,Object> book){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "TestDB";

        db.collection("usersFavorites")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        db.collection("booksCount")
                .add(book)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

    }
}

