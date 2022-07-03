package com.example.bookgraph;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private LinkedList<String> bookFavLinkedList;
    private static final LinkedList<String> retVal=new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.idLoadingPB);
        searchEdt = findViewById(R.id.idEdtSearchBooks);
        ImageButton searchBtn = findViewById(R.id.idBtnSearch);
        ImageButton favBtn = findViewById(R.id.idFavoritesButton);

        searchBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            if (searchEdt.getText().toString().isEmpty()) {
                searchEdt.setError("Please enter search query");
                return;
            }
            getBooksInfo(searchEdt.getText().toString());
        });

        favBtn.setOnClickListener(v -> {
            Log.d("favBTN","...");
            Intent i = new Intent(MainActivity.this, Favorites.class);
            startActivity(i);
        });
    }

    private void getBooksInfo(String query) {
        bookInfoArrayList = new ArrayList<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        mRequestQueue.getCache().clear();

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            boolean isInFavoritesb=false;
            progressBar.setVisibility(View.GONE);
            try {
                JSONArray itemsArray = response.getJSONArray("items");
                bookFavLinkedList=getIsSaved();

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemsObj = itemsArray.getJSONObject(i);
                    JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                    String title = volumeObj.optString("title");
                    String subtitle = volumeObj.optString("subtitle");
                    JSONArray authorsArray = volumeObj.getJSONArray("authors");
                    String publisher = volumeObj.optString("publisher");
                    String publishedDate = volumeObj.optString("publishedDate");
                    String description = volumeObj.optString("description");
                    int pageCount = volumeObj.optInt("pageCount");
                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                    String thumbnail = imageLinks.optString("thumbnail");
                    thumbnail = "https://bookstoreromanceday.org/wp-content/uploads/2020/08/book-cover-placeholder.png";
                    String previewLink = volumeObj.optString("previewLink");
                    String infoLink = volumeObj.optString("infoLink");
                    JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                    String buyLink = saleInfoObj.optString("buyLink");
                    ArrayList<String> authorsArrayList = new ArrayList<>();

                    if (authorsArray.length() != 0) {
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authorsArrayList.add(authorsArray.optString(i));
                        }
                    }

                    for(String s:bookFavLinkedList){
                        if(s.equals(title)){
                            Log.d("FoundFav", "Book Title "+s+" found in Favorite List");
                            isInFavoritesb=true;
                        }else{
                            Log.d("FoundFav", "Book Title "+s+"  is not the same as: "+title);
                        }
                    }
                    String isInFavorites="false";
                    if(isInFavoritesb){isInFavorites="true";}
                    Book bookInfo = new Book(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink,isInFavorites);
                    isInFavoritesb=false;

                    bookInfoArrayList.add(bookInfo);

                    BookAdapter adapter = new BookAdapter(bookInfoArrayList, MainActivity.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                    RecyclerView mRecyclerView = findViewById(R.id.idRVBooks);

                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show());
        queue.add(booksObjrequest);
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
                                retVal.add(bookTitle);
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

}
