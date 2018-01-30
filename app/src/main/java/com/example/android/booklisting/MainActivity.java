package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

        final ListView booksListView =findViewById(R.id.listview);
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

        mEmptyStateTextView =findViewById(R.id.emptyview);
        loadingIndicator = findViewById(R.id.progress);
        booksListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new AdaptorEditor(this, new ArrayList<knyga>());
        booksListView.setAdapter(mAdapter);
        ed =findViewById(R.id.rinkiklis);
        search =findViewById(R.id.searchButton);
        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buttonclciked();
                    handled = true;
                }
                return handled;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              buttonclciked();
            }
        });
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                knyga currentBook = mAdapter.getItem(position);
                Uri BookseUri = Uri.parse(currentBook.getMurl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, BookseUri);
                startActivity(websiteIntent);
            }
        });
        if (savedInstanceState != null) {
            url1=URL;
            RelativeLayout mostpopular = findViewById(R.id.mostpopular);
            mostpopular.setVisibility(View.GONE);
            listlayout.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);
            caller();
            LoaderID++;
        }
    }
    @Override
    public Loader<List<knyga>> onCreateLoader(int i, Bundle bundle) {
        Log.i("",URL);
        return new BookLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<knyga>> loader, List<knyga> books) {

        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText("Knygų nerasta");
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<knyga>> loader) {
        mAdapter.clear();
    }
    void caller()
    {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LoaderID, null, this);
    }
    public void buttonclciked()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        mEmptyStateTextView.setText("");

        URL = "https://www.googleapis.com/books/v1/volumes?q=" + ed.getText().toString();
        if(networkInfo != null && networkInfo.isConnected() && !url1.equals(URL))
        {
            url1=URL;
            RelativeLayout mostpopular = findViewById(R.id.mostpopular);
            mostpopular.setVisibility(View.GONE);
            listlayout.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);

            caller();
            LoaderID++;
            Log.i("","1 if");
        }
        else if ("https://www.googleapis.com/books/v1/volumes?q=".equals(URL)){
            Toast.makeText(MainActivity.this, "Įrašykite tekstą į paiešką", Toast.LENGTH_SHORT).show();
            Log.i("","2 if");
        }

        else {
            mEmptyStateTextView.setText("No internet connection");
            Log.i("","3 if");
        }

    }
}
