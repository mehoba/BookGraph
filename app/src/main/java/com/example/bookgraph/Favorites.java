package com.example.bookgraph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.anychart.DataEntry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;

public class Favorites extends AppCompatActivity {

    static LinkedList<String> retVal = new LinkedList<>();
    ArrayList<DataEntry> bookCategories;
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

    public synchronized void getIsSaved(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Task<QuerySnapshot> getFavorites= db.collection("usersFavorites")
                .whereEqualTo("uid", uid)
                .get();
        Toast.makeText(this, "Please wait while list loads", Toast.LENGTH_SHORT)
                .show();
        while(!getFavorites.isComplete()){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (QueryDocumentSnapshot document : getFavorites.getResult()) {
            String bookTitle=document.getString("title");
            if(isAlredy(bookTitle)) {
                retVal.add(bookTitle);
            }
        }

        if(retVal.size()==0){
            Log.d("retVal", "EMPTY");
        }
        for(String s:retVal){
            TextView text = new TextView(this);
            text.setText(s);
            text.setTextSize(20);
            text.setGravity(Gravity.START);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mainLL.addView(text);
        }
    }

    public boolean isAlredy(String book){
        for(String s:retVal){
            if(s.equals(book)){
                return false;
            }
        }
        return true;
    }
}