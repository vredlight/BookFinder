package com.example.ved.bookfinder;

public class Book {
    private String title;
    private String author;
    private String imageUrl;

    public Book(String t,String a,String u){
        title = t;
        author= a;
        imageUrl = u;
    }

    public void setTitle(String t){
        title = t;
    }

    public void setAuthor(String a){
        author = a;
    }

    public void setImageUrl(String u){imageUrl = u;}

    public String getImageUrl(){return imageUrl;}

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }
}
