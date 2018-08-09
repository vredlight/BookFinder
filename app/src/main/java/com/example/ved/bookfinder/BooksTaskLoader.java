package com.example.ved.bookfinder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class BooksTaskLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private String mSearchedString;
    private static final String LOG_TAG = BooksTaskLoader.class.getSimpleName();

    public BooksTaskLoader(Context context,String url,String searched){
        super(context);
        mUrl = url;
        mSearchedString = searched;
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

       Log.i(LOG_TAG,"TEST : murl :" + mUrl);
       Log.i(LOG_TAG,"TEST : searched text " + mSearchedString);

        if(mUrl == null){
            return null;
        }

        List<Book> result = BooksUtils.fetchDataFromUrl(mUrl,mSearchedString);

        return result;
    }
}
