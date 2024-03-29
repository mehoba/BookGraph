package com.example.bookgraph;

import static java.util.Objects.requireNonNull;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class BookDetails extends AppCompatActivity {
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
    String category;
    int pageCount;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, categoryTV, publishDateTV, isSavedTxt;
    Button previewBtn, buyBtn, saveBtn;

    boolean setAfterSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        categoryTV = findViewById(R.id.idIVCategory);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        saveBtn = findViewById(R.id.idBtnSave);
        buyBtn = findViewById(R.id.idBtnBuy);
        ImageView bookIV = findViewById(R.id.idIVbook);
        isSavedTxt = findViewById(R.id.idIsSaved);

        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        category = getIntent().getStringExtra("category");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");
        isSaved = getIntent().getStringExtra("isInFavorites");
        boolean parseBoolean = Boolean.parseBoolean(isSaved);

        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("Number of pages : " + pageCount);
        categoryTV.setText("Category : " + category);
        Picasso.get().load(thumbnail).into(bookIV);

        if (parseBoolean) {
            isSavedTxt.setText("Is in your Favorites: Yes");
        } else {
            isSavedTxt.setText("Is in your Favorites: No");
        }
        previewBtn.setOnClickListener(v -> {
            if (previewLink.isEmpty()) {
                Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = Uri.parse(previewLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        buyBtn.setOnClickListener(v -> {
            if (buyLink.isEmpty()) {
                Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = Uri.parse(buyLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        saveBtn.setOnClickListener(v -> {

            FirebaseUser fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
            HashMap<String, Object> user = new HashMap<>();
            HashMap<String, Object> book = new HashMap<>();

            if(fireBaseUser!= null) {
                user.put("uid", fireBaseUser.getUid());
                user.put("title", title);
                user.put("category", category);
                book.put("title", title);
                book.put("category", category);
                Database.addBook(user, book);
            }

            boolean isSavedInFavorites = getIsSaved();

            if (isSavedInFavorites) {
                isSavedTxt.setText("Is in your Favorites: Yes");
            }

        });
    }

    public boolean getIsSaved() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            db.collection("usersFavorites")
                    .whereEqualTo("uid", uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        private static final String TAG = "Query";

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String bookTitle = requireNonNull(document.getString("title"), "bookTitle");
                                    if (bookTitle.equals(title)) {
                                        setAfterSave = true;
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            return setAfterSave;
        }
        return false;
    }
}
