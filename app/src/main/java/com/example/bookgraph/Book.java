package com.example.bookgraph;

import java.util.ArrayList;

public class Book {
    private final String title;
    private final String subtitle;
    private final ArrayList<String> authors;
    private final String publisher;
    private final String publishedDate;
    private final String description;
    private final int pageCount;
    private final String thumbnail;
    private final String previewLink;
    private final String infoLink;
    private final String buyLink;
    private final String isInFavorites;
    private final String category;

    public Book(String title, String subtitle, ArrayList<String> authors, String publisher,
                String publishedDate, String description, int pageCount, String thumbnail,
                String previewLink, String infoLink, String buyLink, String isInFavorites, String category) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
        this.previewLink = previewLink;
        this.infoLink = infoLink;
        this.buyLink = buyLink;
        this.isInFavorites = isInFavorites;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public String getInFavorites() {
        return isInFavorites;
    }

    public String getCategory() { return category; }
}
