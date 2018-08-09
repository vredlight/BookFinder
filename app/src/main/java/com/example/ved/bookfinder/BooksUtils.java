package com.example.ved.bookfinder;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class BooksUtils {

    private final static String LOG_TAG = BooksUtils.class.getSimpleName();
    private final static String QUERY_PARAM = "q";
    private final static String MAX_RESULT = "maxResults";
    private final static String PRINT_TYPE = "printType";


    private BooksUtils(){}

    private static URL createUrl(String StringUrl,String searchedText){
        URL url = null;

        try{
            Uri uri = Uri.parse(StringUrl).buildUpon()
                    .appendQueryParameter(QUERY_PARAM,searchedText)
                    .appendQueryParameter(MAX_RESULT,"15")
                    .appendQueryParameter(PRINT_TYPE,"books")
                    .build();

            url = new URL(uri.toString());
        } catch(MalformedURLException e){
            Log.e(LOG_TAG,"Error in creating URL",e);
        }
        return url;
    }

    // Making the request for the given Url and returning the data from the internet in a JSON file

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If url is null return early
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* Milliseconds */);
            urlConnection.setConnectTimeout((15000 /* MilliSeconds */));
            urlConnection.connect();

            // If the connection was successful the response code will be 200

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
                Log.e(LOG_TAG," MSG :Problem establishing network connection. Error Code" + urlConnection.getResponseCode());
        } catch (IOException e){
            Log.e(LOG_TAG,"MSG :Problem parsing data from Json file");
        }
          finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
             if (inputStream != null)
                 inputStream.close();
        }
    return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    public static List<Book> fetchDataFromUrl(String requestUrl,String searchedText){

        try{
            Thread.sleep(500);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl,searchedText);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException  e){
            Log.e(LOG_TAG,"MSG :problem making an HTTP request",e);
        }

        List<Book> books = extractDataFromJson(jsonResponse);

        return books;
    }

    private static ArrayList<Book> extractDataFromJson(String jsonResponse){

        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i=0; i<items.length() ; i++){
                   JSONObject currentObject = items.getJSONObject(i);
                   JSONObject volmInfo = currentObject.getJSONObject("volumeInfo");

                   JSONArray authors = volmInfo.getJSONArray("authors");
                   String author = authors.getString(0);

                   JSONObject imageset = volmInfo.getJSONObject("imageLinks");
                   String imageUrl = imageset.getString("thumbnail");

              /*
                   for(int j=0;j<authors.length();j++) {
                       author += "\n" + authors.getString(j);
                   }

               */
                   String title = volmInfo.getString("title");

                   Book book_item = new Book(title,author,imageUrl);
                   books.add(book_item);

            }

        } catch (JSONException e){
            Log.e(LOG_TAG,"MSG : Error retrieving data from JSON");
        }

        return books;
    }
}
