package com.example.bookgraph;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Favorites extends AppCompatActivity {

    static LinkedList<String> retVal = new LinkedList<>();
    ArrayList<String> bookCategories = new ArrayList<>();
    LinearLayout mainLL;
    Button chartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites2);
        chartBtn = findViewById(R.id.chartBtn);
        mainLL = findViewById(R.id.myLayoutId);
        getIsSaved();


        chartBtn.setOnClickListener(view -> {
            Intent i = new Intent(Favorites.this, BookPieChart.class);
            i.putExtra("data", bookCategories);
            startActivity(i);
        });
    }

    public synchronized void getIsSaved() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> gatherAllBooksInFavorites = new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Task<QuerySnapshot> getFavorites = db.collection("usersFavorites")
                    .whereEqualTo("uid", uid)
                    .get();
            Toast.makeText(this, "Please wait while list loads", Toast.LENGTH_SHORT)
                    .show();
            while (!getFavorites.isComplete()) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (QueryDocumentSnapshot document : getFavorites.getResult()) {
                String bookTitle = document.getString("title");
                String bookCategory = document.getString("category");

                if (!isInFavorites(bookTitle)) {
                    retVal.add(bookTitle);
                }
                if (bookCategory != null) {
                    gatherAllBooksInFavorites.put(bookTitle, bookCategory);
                }
            }
        }

        if (retVal.size() == 0) {
            Log.d("retVal", "EMPTY");
        }
        for (String s : retVal) {
            TextView text = new TextView(this);
            text.setText(s);
            text.setTextSize(20);
            text.setGravity(Gravity.START);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mainLL.addView(text);
        }
        setAllCategories(gatherAllBooksInFavorites);
    }

    public boolean isInFavorites(String book) {
        for (String s : retVal) {
            if (s.equals(book)) {
                return true;
            }
        }
        return false;
    }

    public void setAllCategories(Map<String, String> allBooks) {
        allBooks.forEach((title, category) -> bookCategories.add(category));
    }
}