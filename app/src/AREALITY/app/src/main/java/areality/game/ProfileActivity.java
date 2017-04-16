package areality.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends Activity {

    private static final int REQUEST_MY_PLACES = 0;
    private static final int REQUEST_MAP = 0;
    private static final int REQUEST_LOGIN = 0;

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

        TextView usernameView = (TextView) findViewById(R.id.profileName);
        usernameView.setText(username);

        TextView pointsView = (TextView) findViewById(R.id.profilePoints);
        pointsView.setText("Points: " + Integer.toString(points));

        TextView landmarkCountView = (TextView) findViewById(R.id.profileLandmarkCount);
        landmarkCountView.setText("Landmarks: " + Integer.toString(landmarkCount));
    }
}
