package com.example.areality;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tyler on 4/5/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
    private String googlePlacesData;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;


    public GetNearbyPlacesData(GoogleApiClient googleApiClient){
        mGoogleApiClient = googleApiClient;
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
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

//        DownloadUrl downloadUrl = new DownloadUrl();
//        downloadUrl.readUrl(getDetailUrl("ChIJIQBpAG2ahYAR_6128GcTUEo"));

//        String testPlaceId = "ChIJIQBpAG2ahYAR_6128GcTUEo";
//        String urlString = getDetailUrl(testPlaceId);
//
//        try {
//            makeHTTPRequest(urlString);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        PlacesDetail placesDetail = new PlacesDetail(mGoogleApiClient);
//        placesDetail.fetchDetail(mGoogleApiClient, testPlaceId);


        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String placeId = googlePlace.get("place_id");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

    }

    private String getDetailUrl(String placeId) {

        StringBuilder googlePlacesDetailUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesDetailUrl.append("placeid=" + placeId);
        googlePlacesDetailUrl.append("&key=" + "AIzaSyD3FM6gEwhGLsi8ig7ebIZr4g46RgkrnQQ");
        Log.d("getUrl", googlePlacesDetailUrl.toString());
        return (googlePlacesDetailUrl.toString());
    }

    protected String makeHTTPRequest(String urlString) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection connection;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            return content;
        } catch (IOException e) {
            Log.d("error", e.toString());
            return e.toString();
        }
    }

}

