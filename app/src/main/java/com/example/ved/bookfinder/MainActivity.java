package com.example.ved.bookfinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final int BOOKS_LOADER_ID = 1;
    private BooksAdapter mAdapter;
    private TextView mEmptyView;
    private String mSearched;
    private boolean isConnected;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        TextView search = findViewById(R.id.search_button);
        mEmptyView = findViewById(R.id.empty_view);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        final EditText searchedTextField = findViewById(R.id.search_text);

        if (isConnected) {
            search.setClickable(true);
            search.setBackgroundColor(getResources().getColor(R.color.searchButton));
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSearched = searchedTextField.getText().toString();
                    // Toast.makeText(getBaseContext(), "Find clicked", Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "TEST : on click of the Find button done");
                    mAdapter.clear();
                    progressBar.setVisibility(view.VISIBLE);
                    startOperation();
                }
            });

        } else {

            progressBar.setVisibility(View.GONE);
            mEmptyView.setText("Not Connected to Internet");
            search.setBackgroundColor(getResources().getColor(R.color.authorName));
            search.setClickable(false);
            }

       ListView bookView = findViewById(R.id.list_view);
        bookView.setEmptyView(mEmptyView);

        mAdapter = new BooksAdapter(this,new ArrayList<Book>());

        bookView.setAdapter(mAdapter);
    }

    public void startOperation(){
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Book>> loader = loaderManager.getLoader(BOOKS_LOADER_ID);

        if(loader == null)
            loaderManager.initLoader(BOOKS_LOADER_ID,null,this);
        else
            loaderManager.restartLoader(BOOKS_LOADER_ID,null,this);
    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int i,Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        return new BooksTaskLoader(this,BOOKS_BASE_URL,mSearched);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> books) {
      progressBar.setVisibility(View.GONE);
      mEmptyView.setText("No Books Found..!");

      mAdapter.clear();

      if(books != null && !books.isEmpty()){
          mAdapter.addAll(books);
      }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
       mAdapter.clear();
    }
}
