package com.sspm.quickride.util;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sspm.quickride.ui.interfaces.Callback_v2;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Siddhesh on 16-04-2017.
 */

public class DownloadTask extends AsyncTask<String, Void, PolylineOptions> {

    Callback_v2 callback;

    public DownloadTask(Callback_v2 callback){
        this.callback = callback;
    }

    // Downloading data in non-ui thread
    @Override
    protected PolylineOptions doInBackground(String... url) {
        return process(url[0]);
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(PolylineOptions result) {
        super.onPostExecute(result);
        callback.invoke(result);
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private PolylineOptions process(String url) {

        // For storing data from web service
        String data = "";
        try {
            // Fetching the data from web service
            data = downloadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        try {
            jObject = new JSONObject(data);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            // Starts parsing data
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        // Traversing through all the routes
        if (routes != null && routes.size() > 0) {
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = routes.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLACK);
            }
        }
        return lineOptions;
    }


}