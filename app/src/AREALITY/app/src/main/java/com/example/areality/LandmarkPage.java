package com.example.areality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class LandmarkPage extends Activity {

    private MyGLSurfaceView glView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_landmark_page);

        String testPlaceId = "ChIJIQBpAG2ahYAR_6128GcTUEo";

        testPlaceId = intent.getStringExtra(EXTRA_MESSAGE);

        String urlString = getDetailUrl(testPlaceId);
        String result = "didn't work";
        JSONObject jsonObject = null;
        List<String> photoUrls = new ArrayList<String>();



        try {
            result = makeHTTPRequest(urlString);
            jsonObject = new JSONObject(result);
            photoUrls = getPhotoUrlsList(jsonObject);
            Log.d("photoUrls", "photoUrls");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




        String schedule = "Schedules. " + "\n"+
                "1. center image" + "\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "2. Does line breaks work" +"\n"+
                "3";
        String photos[] = new String[photoUrls.size()];
        photos = photoUrls.toArray(photos);
        setInfo(schedule, photos);



    }



    private void setInfo(String schedule, String[] photos){
        glView = (MyGLSurfaceView) findViewById(R.id.glsurfaceview);
        glView.setPhotos(photos);
        TableLayout layout = (TableLayout) findViewById(R.id.tableView);

        //first child is schedule
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(schedule);


        for (int i = 1; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {
                    View view = row.getChildAt(x);
                    //add reviews to view
                }
            }
        }
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


    public List<String> getPhotoUrlsList(JSONObject jsonObject) throws JSONException {
        List<String> photoUrls = new ArrayList<>();
        JSONArray photoArray = null;
        photoArray = jsonObject.getJSONObject("result").getJSONArray("photos");

        for (int i = 0; i < photoArray.length(); i++) {
            JSONObject photo = photoArray.getJSONObject(i);
            String photoReference = photo.getString("photo_reference");
            String photoUrl = getPhotoUrl(photoReference);
            photoUrls.add(photoUrl);
        }
        return photoUrls;
    }
    private String getPhotoUrl(String photoReference) {
        StringBuilder photoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
        photoUrl.append("maxheight=" + "600");
        photoUrl.append("&maxwidth=" + "600");
        photoUrl.append("&photoreference=" + photoReference);
        photoUrl.append("&key=" + "AIzaSyD3FM6gEwhGLsi8ig7ebIZr4g46RgkrnQQ");
        return photoUrl.toString();
    }
}
