package com.example.ved.bookfinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class BooksAdapter  extends ArrayAdapter<Book> {

    public BooksAdapter(Context context , List<Book> books){
        super(context,0,books);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.books_list_view,parent,false);
            }

            Book currentBook = getItem(position);

         TextView bookTitle = listItemView.findViewById(R.id.book_title);
         bookTitle.setText(currentBook.getTitle());

         TextView bookAuthor = listItemView.findViewById(R.id.book_author);
         bookAuthor.setText(currentBook.getAuthor());


        ImageView bookImage = listItemView.findViewById(R.id.book_image);
        String imageUrl = currentBook.getImageUrl();

        Picasso.get()
                .load(imageUrl)
                .resize(80,100)
                .centerCrop()
                .into(bookImage);

        return listItemView;
    }
}
