package com.example.bookgraph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class Favorites extends AppCompatActivity {
    static LinkedList<String> retVal = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites2);

        LinearLayout MainLL= (LinearLayout) findViewById(R.id.myLayoutId);

        retVal=getIsSaved();
       for(String s:retVal){
            TextView text = new TextView(this);
            text.setText(s); // <-- does it really compile without the + sign?
            text.setTextSize(20);
            text.setGravity(Gravity.LEFT);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            MainLL.addView(text);
        }
    }

    public synchronized LinkedList<String> getIsSaved(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Task<QuerySnapshot> getFavorites= db.collection("usersFavorites")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "Query";

                    @Override
                    public synchronized void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String bookTitle=document.getString("title");

                                while (bookTitle == null) {
                                    try {
                                        wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Log.d(TAG, "Book Title "+bookTitle);
                                if(isAlredy(bookTitle)){
                                retVal.add(bookTitle);}
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        notifyAll();
        if(retVal.size()==0){
            Log.d("retVal", "EMPTY");
        }
        for(String s:retVal){
            Log.d("retVal", "Book in Favorites: "+s);
        }
        return retVal;
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