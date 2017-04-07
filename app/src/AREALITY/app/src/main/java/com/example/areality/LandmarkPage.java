package com.example.areality;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class LandmarkPage extends Activity {

    private MyGLSurfaceView glView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gl);

        String testPlaceId = "ChIJIQBpAG2ahYAR_6128GcTUEo";
        String urlString = getDetailUrl(testPlaceId);
        String result = "didn't work";
        List<HashMap<String, String>> results;
        try {
            result = makeHTTPRequest(urlString);
//            results = parse(result);
        } catch (IOException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_landmark_page);
        String info = "Information about landmarks. Notes: " +
                "1. center image" +
                "2. Does line breaks work" +
                "3";
        String photos[] = {"https://static.pexels.com/photos/104827/cat-pet-animal-domestic-104827.jpeg"};
        setInfo(info, photos);


//        Toast.makeText(LandmarkPage.this,result.toString(), Toast.LENGTH_LONG).show();

    }
    private void setInfo(String message, String[] photos){
        glView = (MyGLSurfaceView) findViewById(R.id.glsurfaceview);
        glView.setPhotos(photos);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);

    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
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
