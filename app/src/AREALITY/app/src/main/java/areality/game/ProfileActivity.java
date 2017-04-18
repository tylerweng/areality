package areality.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends Activity {

    private static final int REQUEST_MY_PLACES = 0;
    private static final int REQUEST_MAP = 0;
    private static final int REQUEST_LOGIN = 0;
    private static final String TAG = "ProfileActivity";


    @OnClick(R.id.myPlacesButton) void myPlaces() {
        Intent intent = new Intent(getApplicationContext(), MyPlacesActivity.class);
        startActivityForResult(intent, REQUEST_MY_PLACES);
    }

    @OnClick(R.id.toMapButton) void switchToMap() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(intent, REQUEST_MAP);
    }

    @OnClick(R.id.logoutButton) void logout() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        pref.edit().clear().apply();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Log.d("MapActivity", "username here on map: " + pref.getString("username", null));

        String username = pref.getString("username", null);
        int points = pref.getInt("points", 0);
        int landmarkCount = pref.getInt("landmark_ids_size", 0);

        int size = pref.getInt("landmark_ids_size", 0);

        // array of JSON objects with lat, lon, and id attributes for use on map
        JSONObject[] seenCoords = new JSONObject[size];

        for (int i = 0; i < size; i++) {
            try {
                seenCoords[i] = new JSONObject(pref.getString("landmark_id_" + (i + 1), null));
            } catch (JSONException e) {
                Log.e("ProfileActivity", "JSON error: ", e);
            }
        }

        TextView usernameView = (TextView) findViewById(R.id.profileName);
        usernameView.setText(username);

        TextView pointsView = (TextView) findViewById(R.id.profilePoints);
        pointsView.setText("Points: " + Integer.toString(points));

        TextView landmarkCountView = (TextView) findViewById(R.id.profileLandmarkCount);
        landmarkCountView.setText("Total landmarks: " + Integer.toString(landmarkCount));

        TextView coordsListView = (TextView) findViewById(R.id.profileCoordsList);
        coordsListView.setText("Visited landmark coordinates: " + Arrays.toString(seenCoords));
    }
}
