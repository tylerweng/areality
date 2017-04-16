package areality.game;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MyPlacesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places);
        ButterKnife.bind(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int landmarkCount = pref.getInt("landmark_ids_size", 0);

        List<String> landmarkIds = new ArrayList<>();

        for (int i = 1; i <= landmarkCount; i++) {
            String numString = Integer.toString(i);
            String landmarkId = pref.getString("landmark_id_" + numString, "No Landmark Id!");
            landmarkIds.add(landmarkId);
        }

        Toast.makeText(getBaseContext(), landmarkIds.get(0), Toast.LENGTH_LONG).show();
    }

}
