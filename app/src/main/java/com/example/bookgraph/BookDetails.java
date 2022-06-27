package com.example.bookgraph;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String title;
    String subtitle;
    String publisher;
    String publishedDate;
    String description;
    String thumbnail;
    String previewLink;
    String infoLink;
    String buyLink;
    String isSaved;
    int pageCount;
    private ArrayList<String> authors;
    static String retVal;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV,isSavedTxt;
    Button previewBtn, buyBtn,saveBtn;
    private ImageView bookIV;
    boolean setAfterSave=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // initializing our views..
        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        saveBtn = findViewById(R.id.idBtnSave);
        buyBtn = findViewById(R.id.idBtnBuy);
        bookIV = findViewById(R.id.idIVbook);
        isSavedTxt = findViewById(R.id.idIsSaved);

        // getting the data which we have passed from our adapter class.
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");
        isSaved=getIntent().getStringExtra("isInFavorites");
        boolean parseBoolean=Boolean.parseBoolean(isSaved);

        // after getting the data we are setting
        // that data to our text views and image view.
        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("No Of Pages : " + pageCount);
        Picasso.get().load(thumbnail).into(bookIV);

        if(parseBoolean){
        isSavedTxt.setText("Saved!");}else { isSavedTxt.setText("Not saved!");}
        // adding on click listener for our preview button.
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // that link via an intent.
                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        // initializing on click listener for buy button.
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyLink.isEmpty()) {
                    // below toast message is displaying when buy link is empty.
                    Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // the link via an intent.
                Uri uri = Uri.parse(buyLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        //on click save this book in a JSON file
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //adding book parameters to Firebase DB
                HashMap<String, Object> user = new HashMap<>();
                HashMap<String, Object> book = new HashMap<>();

                user.put("uid",fireBaseUser.getUid());
                user.put("title",title);
                book.put("title",title);
                Database.addBook(user,book);

               // boolean isSavedNow=getIsSaved();

            }
        });
    }

    public boolean getIsSaved(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
       Task<QuerySnapshot> getFavorites= db.collection("usersFavorites")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "Query";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String bookTitle=document.getString("title");
                                Log.d(TAG, "Book Title "+bookTitle);
                                if(bookTitle.equals(title)){
                                   setAfterSave=true;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return setAfterSave;
    }
}
