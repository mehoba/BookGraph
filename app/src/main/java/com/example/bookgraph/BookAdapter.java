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

    private final ArrayList<Book> bookInfoArrayList;
    private final Context mcontext;

    public BookAdapter(ArrayList<Book> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookInfoArrayList.get(position);
        holder.nameTV.setText(book.getTitle());
        holder.publisherTV.setText(book.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + book.getPageCount());
        holder.dateTV.setText(book.getPublishedDate());

        Picasso.get().load(book.getThumbnail()).into(holder.bookIV);

        holder.itemView.setOnClickListener(v -> {
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
            mcontext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
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
