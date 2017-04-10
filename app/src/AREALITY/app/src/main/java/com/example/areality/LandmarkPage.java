package com.example.areality;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Hashtable;
import java.util.List;


public class LandmarkPage extends Activity {

    private MyGLSurfaceView glView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landmark_page);

        String testPlaceId = "ChIJN1t_tDeuEmsRUsoyG83frY4";
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            testPlaceId= extras.getString("com.example.areality.MESSAGE");
        }

        String urlString = getDetailUrl(testPlaceId);
        String result = "";
        JSONObject jsonObject = null;
        List<String> photoUrls = new ArrayList<>();
        List<Hashtable> reviewList = new ArrayList<>();

        String name = "";
        String rating = "0.0";
        String schedule = "";

        try {
            result = makeHTTPRequest(urlString);
            jsonObject = new JSONObject(result);
            name = jsonObject.getJSONObject("result").getString("name");
            rating = jsonObject.getJSONObject("result").getString("rating");
            schedule = getSchedule(jsonObject);
            reviewList = getReviewList(jsonObject);
            photoUrls = getPhotoUrlsList(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String photos[] = new String[photoUrls.size()];
        photos = photoUrls.toArray(photos);
        setInfo(name,schedule,reviewList, photos, Float.valueOf(rating));
    }

    private void setInfo(String name, String schedule, List<Hashtable> reviews, String[] photos, float rating){
        glView = (MyGLSurfaceView) findViewById(R.id.glsurfaceview);
        glView.setPhotos(photos);
        TableLayout layout = (TableLayout) findViewById(R.id.tableView);

        TextView title = (TextView) findViewById(R.id.name);
        title.setText(name);
        //first child is schedule
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(schedule);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(rating);

        for (int i = 1; i < 6; i++) {
            Hashtable review = reviews.get(i-1);
            View child = layout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {
                    TextView view = (TextView) row.getChildAt(x);
                    String entry = "<b>"+review.get("authorName").toString() + "</b>" + "<br></br>"
                                 + review.get("reviewText").toString();
                    view.setText((Html.fromHtml(entry)));
                }
            }
        }
    }

    public List<Hashtable> getReviewList(JSONObject jsonObject) throws JSONException {
        List<Hashtable> reviewList = new ArrayList<>();
        JSONArray reviewArray = null;
        reviewArray = jsonObject.getJSONObject("result").getJSONArray("reviews");
        for (int i = 0; i < 5; i++) {
            Hashtable<String, String> review = new Hashtable<>();
            JSONObject reviewDetail = reviewArray.getJSONObject(i);
            String authorName = reviewDetail.getString("author_name");
            String reviewText = reviewDetail.getString("text");

            review.put("authorName", authorName);
            review.put("reviewText", reviewText);
            reviewList.add(review);
        }

        return reviewList;
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

    private String getSchedule(JSONObject jsonObject) throws JSONException{
        JSONArray scheduleArr = jsonObject.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text");
        String schedule = "Opening Times";

        for (int i =0; i < scheduleArr.length(); i++){
            String time = scheduleArr.getString(i);
            schedule += "\n" + time;
        }
        return schedule;
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
        return (googlePlacesDetailUrl.toString());
    }

    private String getPhotoUrl(String photoReference) {
        StringBuilder photoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
        photoUrl.append("maxheight=" + "600");
        photoUrl.append("&maxwidth=" + "600");
        photoUrl.append("&photoreference=" + photoReference);
        photoUrl.append("&key=" + "AIzaSyD3FM6gEwhGLsi8ig7ebIZr4g46RgkrnQQ");
        return photoUrl.toString();
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
