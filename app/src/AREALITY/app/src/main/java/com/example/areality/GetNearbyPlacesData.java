package com.example.areality;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by tyler on 4/5/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
    private String googlePlacesData;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private List<HashMap<String, String>> nearbyPlacesList;


    public GetNearbyPlacesData(GoogleApiClient googleApiClient){
        mGoogleApiClient = googleApiClient;
    }

    public List<HashMap<String, String>> getPlacesList() {
        return nearbyPlacesList;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            String url;
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
//        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        Log.d("onPostExecute", "Entered into showing locations");
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            final String placeId = googlePlace.get("place_id");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + ": " + placeId);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

    }



}

