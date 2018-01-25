package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public final class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<knyga>> {
    TextView mEmptyStateTextView;
    View loadingIndicator;
    ImageView pirmas,antras,trecias,ketvirtas,penktas;
    AdaptorEditor mAdapter;
    LinearLayout listlayout;
    int i=0;
    private int LoaderID = 1;
    Button search;
    EditText ed;
    String URL="https://www.googleapis.com/books/v1/volumes?q=",url1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pirmas = findViewById(R.id.pirmas);
        antras = findViewById(R.id.antras);
        trecias = findViewById(R.id.trecias);
        ketvirtas = findViewById(R.id.ketvirtas);
        penktas = findViewById(R.id.penktas);
        listlayout = findViewById(R.id.linearlistlayout);
        pirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daugiau = "https://en.wikipedia.org/wiki/Don_Quixote";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(daugiau));
                startActivity(i);
            }
        });
        antras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daugiau = "https://en.wikipedia.org/wiki/A_Tale_of_Two_Cities";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(daugiau));
                startActivity(i);
            }
        });
        trecias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daugiau = "https://en.wikipedia.org/wiki/The_Lord_of_the_Rings";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(daugiau));
                startActivity(i);
            }
        });
        ketvirtas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daugiau = "https://en.wikipedia.org/wiki/The_Little_Prince";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(daugiau));
                startActivity(i);
            }
        });
        penktas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daugiau = "https://en.wikipedia.org/wiki/Harry_Potter_and_the_Philosopher%27s_Stone";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(daugiau));
                startActivity(i);
            }
        });
        ListView earthquakeListView = (ListView) findViewById(R.id.listview);
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyview);
        loadingIndicator = findViewById(R.id.progress);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new AdaptorEditor(this, new ArrayList<knyga>());
        earthquakeListView.setAdapter(mAdapter);
       ed =findViewById(R.id.rinkiklis);
        search =findViewById(R.id.searchButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                mEmptyStateTextView.setText("");

                URL = "https://www.googleapis.com/books/v1/volumes?q=" + ed.getText().toString();
                Log.i("", "1 url "+URL);
               if(!"https://www.googleapis.com/books/v1/volumes?q=".equals(URL) && networkInfo != null && networkInfo.isConnected() && !url1.equals(URL))
               {

                   url1=URL;
                   RelativeLayout mostpopular = findViewById(R.id.mostpopular);
                   mostpopular.setVisibility(View.GONE);
                   listlayout.setVisibility(View.VISIBLE);
                   Log.i("", "caller");
                   Log.i("", "2url "+URL);
                   loadingIndicator.setVisibility(View.VISIBLE);
                caller();
               }
               else if ("https://www.googleapis.com/books/v1/volumes?q=".equals(URL)){
                   Toast.makeText(MainActivity.this, "Įrašykite tekstą į paiešką", Toast.LENGTH_SHORT).show();
               }

               else {
                   mEmptyStateTextView.setText("No internet connection");
               }
                LoaderID++;
            }
        });
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                knyga currentBook = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentBook.getMurl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("LoaderID",LoaderID);
        super.onSaveInstanceState(savedInstanceState);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LoaderID = savedInstanceState.getInt("LoaderID");
    }

    @Override
    public Loader<List<knyga>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<knyga>> loader, List<knyga> earthquakes) {

        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText("Knygų nerasta");
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        // If there is a valid list of {@link word}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<knyga>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
    void caller()
    {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LoaderID, null, this);
    }
}
