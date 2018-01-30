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
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<knyga> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<knyga> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }

}