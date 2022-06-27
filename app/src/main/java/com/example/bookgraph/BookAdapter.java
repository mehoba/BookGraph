package com.example.bookgraph;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    // creating variables for arraylist and context.
    private ArrayList<Book> bookInfoArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public BookAdapter(ArrayList<Book> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        // inside on bind view holder method we are
        // setting ou data to each UI component.
        Book book = bookInfoArrayList.get(position);
        holder.nameTV.setText(book.getTitle());
        holder.publisherTV.setText(book.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + book.getPageCount());
        holder.dateTV.setText(book.getPublishedDate());

        // below line is use to set image from URL in our image view.
        Picasso.get().load(book.getThumbnail()).into(holder.bookIV);

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                Intent i = new Intent(mcontext, BookDetails.class);
                i.putExtra("title", book.getTitle());
                i.putExtra("subtitle", book.getSubtitle());
                i.putExtra("authors", book.getAuthors());
                i.putExtra("publisher", book.getPublisher());
                i.putExtra("publishedDate", book.getPublishedDate());
                i.putExtra("description", book.getDescription());
                i.putExtra("pageCount", book.getPageCount());
                i.putExtra("thumbnail", book.getThumbnail());
                i.putExtra("previewLink", book.getPreviewLink());
                i.putExtra("infoLink", book.getInfoLink());
                i.putExtra("buyLink", book.getBuyLink());
                i.putExtra("isInFavorites",book.getInFavorites());
                // after passing that data we are
                // starting our new intent.
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // inside get item count method we
        // are returning the size of our array list.
        return bookInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize
        // our text view and image views.
        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);
        }
    }
}
