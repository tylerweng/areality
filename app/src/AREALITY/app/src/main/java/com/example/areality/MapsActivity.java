package com.example.areality;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    View.OnTouchListener,
    LocationListener {

  public static final String LANDMARK_ID = "com.example.areality.MESSAGE";

  private float mAngle;
  private float mPreviousX;
  private float mPreviousY;
  private double mLat;
  private double mLong;

  private float mDownX;
  private float mDownY;
  private Date mDownTime;
  private final float TOUCH_CLICK_CUTOFF_TIME = 1000;
  private final float TOUCH_CLICK_CUTOFF_LENGTH = 10;
  private final double LAT_LONG_TOUCH_CUTOFF_DISTANCE = 0.0003;

  private GoogleMap mMap;
  private int PROXIMITY_RADIUS = 1000;
  GoogleApiClient mGoogleApiClient;
  Location mLastLocation;
  Marker mCurrLocationMarker;
  LocationRequest mLocationRequest;

  private Projection projection;
  private Circle mClickDisplay;

  private GetNearbyPlacesData getNearbyPlacesData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      checkLocationPermission();
    }

    //Check if Google Play Services Available or not
    if (!CheckGooglePlayServices()) {
      Log.d("onCreate", "Finishing test case since Google Play Services are not available");
      finish();
    } else {
      Log.d("onCreate", "Google Play Services available.");
    }

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    mAngle = 0;
    View view = findViewById(R.id.map_overlay);
    view.setOnTouchListener(this);
  }



  private boolean CheckGooglePlayServices() {
    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    int result = googleAPI.isGooglePlayServicesAvailable(this);
    if (result != ConnectionResult.SUCCESS) {
      if (googleAPI.isUserResolvableError(result)) {
        googleAPI.getErrorDialog(this, result,
            0).show();
      }
      return false;
    }
    return true;
  }


  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    //Initialize Google Play Services
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
        buildGoogleApiClient();
      }
    } else {
      buildGoogleApiClient();
    }

    mMap.getUiSettings().setCompassEnabled(false);
    mMap.getUiSettings().setMapToolbarEnabled(false);
    mMap.getUiSettings().setMyLocationButtonEnabled(false);
    mMap.getUiSettings().setAllGesturesEnabled(false);
    mMap.setBuildingsEnabled(false);
    mMap.setIndoorEnabled(false);
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    try {
      boolean success = mMap.setMapStyle(
          MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
      );

      if (!success) {
        Log.e("MapsActivity", "Style parsing failed.");
      }
    } catch (Resources.NotFoundException e) {
      Log.e("MapsActivity", "Can't find style. Error: ", e);
    }
    mLat = 0;
    mLong = 0;
    setSelfPositionMarker();
    setCameraPosition();
  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();
  }

  private void setSelfPositionMarker() {
    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
        .position(new LatLng(mLat, mLong))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
  }

  @Override
  public void onConnected(Bundle bundle) {
    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    setSelfPositionMarker();
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(1000);
    mLocationRequest.setFastestInterval(1000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
  }

  private String getUrl(double latitude, double longitude, String nearbyPlace) {
    StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
    googlePlacesUrl.append("location=" + latitude + "," + longitude);
    googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
    googlePlacesUrl.append("&type=" + nearbyPlace);
    googlePlacesUrl.append("&key=" + "AIzaSyD3FM6gEwhGLsi8ig7ebIZr4g46RgkrnQQ");
    googlePlacesUrl.append("&sensor=true");
    Log.d("getUrl", googlePlacesUrl.toString());
    return (googlePlacesUrl.toString());
  }

  @Override
  public void onConnectionSuspended(int i) {
    Toast.makeText(MapsActivity.this, "Connection Suspended", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d("onLocationChanged", "entered");

    mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
    mLat = location.getLatitude();
    mLong = location.getLongitude();

    //move map camera
    setCameraPosition();

    Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", mLat, mLong));

    //stop location updates
    if (mGoogleApiClient != null) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      Log.d("onLocationChanged", "Removing Location Updates");
    }
    Log.d("onLocationChanged", "Exit");

    setPlacesMarkers();
  }

  private void setPlacesMarkers() {
    String url = getUrl(mLat, mLong, "restaurant");
    Object[] DataTransfer = new Object[2];
    DataTransfer[0] = mMap;
    DataTransfer[1] = url;
    Log.d("onClick", url);

    getNearbyPlacesData = new GetNearbyPlacesData(mGoogleApiClient);
    getNearbyPlacesData.execute(DataTransfer);
//    Toast.makeText(MapsActivity.this, "Nearby Landmarks", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Toast.makeText(MapsActivity.this, "Connection Failed", Toast.LENGTH_LONG).show();
  }

  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

  public boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      // Asking user if explanation is needed
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            MY_PERMISSIONS_REQUEST_LOCATION);


      } else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            MY_PERMISSIONS_REQUEST_LOCATION);
      }
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          // permission was granted. Do the
          // contacts-related task you need to do.
          if (ContextCompat.checkSelfPermission(this,
              Manifest.permission.ACCESS_FINE_LOCATION)
              == PackageManager.PERMISSION_GRANTED) {

            if (mGoogleApiClient == null) {
              buildGoogleApiClient();
            }
            mMap.setMyLocationEnabled(true);
          }

        } else {

          // Permission denied, Disable the functionality that depends on this permission.
          Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
        return;
      }
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent e) {
    float x = e.getX();
    float y = e.getY();
    int width = v.getWidth();
    int height = v.getHeight();

    switch (e.getAction()) {
      case MotionEvent.ACTION_MOVE:

        int xOrigin = width / 2;
        int yOrigin = height / 2;

        double oldAngle = Math.toDegrees(Math.atan((yOrigin - mPreviousY) / (mPreviousX - xOrigin)));
        if (mPreviousX < xOrigin) {
          oldAngle += 180;
        }

        double newAngle = Math.toDegrees(Math.atan((yOrigin - y) / (x - xOrigin)));
        if (x < xOrigin) {
          newAngle += 180;
        }

        mAngle += (newAngle - oldAngle);
        setCameraPosition();
        break;
      case MotionEvent.ACTION_DOWN:
        mDownX = x;
        mDownY = y;
        mDownTime = new Date();
        break;
      case MotionEvent.ACTION_UP:
        Date now = new Date();
        long time = now.getTime() - mDownTime.getTime();

        double distance = Math.pow(x - mDownX, 2) + Math.pow(y - mDownY, 2);
        if (time < TOUCH_CLICK_CUTOFF_TIME && distance < TOUCH_CLICK_CUTOFF_LENGTH) {
          // Register Click!
          LatLng clickPos = projection.fromScreenLocation(new Point((int) x, (int) y));

          if (mClickDisplay != null) {
            removeClick();
          }

          mClickDisplay = mMap.addCircle(new CircleOptions()
              .center(clickPos)
              .radius(5)
              .strokeColor(Color.rgb(211, 103, 43))
              .fillColor(Color.rgb(105, 51, 21)));

          new android.os.Handler().postDelayed(
              new Runnable() {
                @Override
                public void run() {
                  removeClick();
                }
              }, 100
          );

          List<HashMap<String, String>> nearbyPlaces = getNearbyPlacesData.getPlacesList();

          for (int i = 0; i < nearbyPlaces.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlaces.get(i);

            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            double markerDistance = Math.pow(lat - clickPos.latitude, 2) + Math.pow(lng - clickPos.longitude, 2);
            if(Math.sqrt(markerDistance) < LAT_LONG_TOUCH_CUTOFF_DISTANCE ) {
              // For testing click distance/etc
//              Toast.makeText(MapsActivity.this, googlePlace.get("place_name"), Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(this, LandmarkPage.class);
              String landmarkId = googlePlace.get("place_id");
              intent.putExtra(LANDMARK_ID, landmarkId);
              startActivity(intent);
            }
          }
        }
    }
    mPreviousX = x;
    mPreviousY = y;
    return true;
  }

  private void removeClick() {
    if (mClickDisplay != null) {
      mClickDisplay.remove();
      mClickDisplay = null;
    }
  }

  private void setCameraPosition() {
    mCurrLocationMarker.setPosition(new LatLng(mLat, mLong));
    CameraPosition camera = new CameraPosition.Builder()
        .target(new LatLng(mLat, mLong))
        .zoom(18)
        .tilt(67.5f)
        .bearing(mAngle)
        .build();
    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    projection = mMap.getProjection();
  }
}