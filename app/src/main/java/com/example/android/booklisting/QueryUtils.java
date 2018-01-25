package com.example.android.booklisting;

/**
 * Created by Justas on 1/14/2018.
 */

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

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<knyga> fetchEarthquakeData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        }

        List<knyga> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {

            url = new URL(stringUrl);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Problem building the URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {

                output.append(line);

                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<knyga> extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        List<knyga> earthquakes = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray items = baseJsonResponse.getJSONArray("items");

            for (int i=0; i<items.length(); i++)
            {
                String author = "no author";
                String thumbnail="no thumbnail";
                JSONObject object = items.getJSONObject(i);
                JSONObject volumeInfo = object.getJSONObject("volumeInfo");
                if(volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    thumbnail = imageLinks.getString("smallThumbnail");
                }
                JSONObject saleInfo = object.getJSONObject("saleInfo");
                String saleability = saleInfo.getString("saleability");
                String title = volumeInfo.getString("title");
                if(volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                }
                String url = volumeInfo.getString("infoLink");

                if(saleability.equals("FOR_SALE")) {
                    JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");
                    double amount = retailPrice.getDouble("amount");
                    String currencyCode = retailPrice.getString("currencyCode");
                    knyga earthquake = new knyga(author, title, url,thumbnail,currencyCode,amount );
                    earthquakes.add(earthquake);
                }
                else {
                    knyga earthquake = new knyga(author, title, url,thumbnail);
                    earthquakes.add(earthquake);
                }
                }
        }
        catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }
}
