package com.example.android.booklisting;

/**
 * Created by Justas on 1/14/2018.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.booklisting.QueryUtils;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<knyga>> {
    private static final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.i("","BookLoader has been called");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i("","onStartLoading has been called");
    }

    @Override
    public List<knyga> loadInBackground() {
        if (mUrl == null) {
            Log.i("","loadInBackground if statement url=0 ");
            return null;
        }
        Log.i("","loadInBackground has been called url!=0");
        Log.i("url= ",""+mUrl);
        List<knyga> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }

}