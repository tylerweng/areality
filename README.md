# AREALITY

Diane Zheng, Michael Hooton, Terry O’Shea, and Tyler Weng
A cross between Foursquare and Pokémon Go, AREALITY is an Android app that rewards users for discovering local landmarks.

## Features & Implementation

<!-- - [ ] User accounts with secure authentication
- [ ] 3D map of user's current location displaying Landmarks in the
vicinity
- [ ] Interact with a Landmark by tapping when nearby
- [ ] Fetch user reviews
- [ ] User profile points and badges (e.g. "Walked 5 miles", "Explored 5 days in a row", "Achieved XX distance between landmarks") -->

### 3-D Map with Intelligent Camera

Upon signing in, the user is immersed into the world of AREALITY, where they are presented with a 3-D map of their surroundings. The camera is centered around our protagonist, and follows them to wherever their desires may lead them.

#### Camera Position

`MapsActivity.java`
```java
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
```

### Landmark Page with Photo Cube Rotation

Users navigate to a landmark's page upon tapping its marker in the Maps Page.
In the landmark page, the user is presented with that landmark's overall rating, its hours of availability and user reviews. The user may also swipe the photo cube to rotate it.

#### Photo Cube Rotation

`MYGLSurfaceView.java`
```java

public boolean onTouchEvent(MotionEvent e) {

  float x = e.getX();
  float y = e.getY();

  switch (e.getAction()) {
    case MotionEvent.ACTION_MOVE:

      float dx = x - mPreviousX;
      float dy = y - mPreviousY;

      mRenderer.setRot(dy/100, dx/10);
      mRenderer.setAngle(
        mRenderer.getAngle() +
        ((dx + dy) * TOUCH_SCALE_FACTOR));
      requestRender();
  }

  mPreviousX = x;
  mPreviousY = y;
  return true;
}
```

## Overall Structure

AREALITY was built using a Java/Android front-end client and a Node.js backend following the Express.js application framework with a MongoDB database.

### Technologies & Frameworks

- [ ] Android
- [ ] Node.js
- [ ] Express.js
- [ ] MongoDB


## Wireframes

### Loading Page

[![areality][loadingpage]][areality]

[areality]: https://github.com/tylerweng/areality
[loadingpage]: docs/wireframes/loading_page.png

### Map Page

[![areality][mappage]][areality]

[mappage]: docs/wireframes/map_portion.png

### Landmark Page

[![areality][landmarkpage]][areality]

[landmarkpage]: docs/wireframes/landmark_page.png

### Profile Page

[![areality][profilepage]][areality]

[profilepage]: docs/wireframes/profile_page.png

## Technologies & Technical Challenges

This app will be built with Java through the Android SDK. The app will be split
into the following script files:

- [ ] `MapActivity.java`: Users can navigate across a map
- [ ] `ProfileActivity.java`: User may view their points / accomplishments
- [ ] `LandmarkActivity.java`: User may swipe on landmark and reveal interesting information
and collect coins
- [ ] `LoadingScreenActivity.java`: Loading page to deliver a smooth UI/UX

We will use the Google Places API (built in Android API), Google Static Maps API (http request with image response), and OpenGL (built in Android) to build our 3D map.
On the backend, we will use Node/Express and a PostgreSQL database.
We will style the app using XML.
We will use Gradle as the build tool.

## Group Members & Work Breakdown

Our group consists of Diane Zheng, Michael Hooton, Terry O’Shea, and Tyler Weng.

### Terry’s Primary Responsibilities:

- [ ] Backend
- [ ] Loading Page

### Tyler’s Primary Responsibilities:

- [ ] Places

### Michael’s Primary Responsibilities:

- [ ] Map

### Diane’s Primary Responsibilities:

- [ ] Landmark Page

## Implementation Timeline

### Day 1:

- [ ] Terry: relearn Java/Android development
- [ ] Tyler: learn Java/Android development
- [ ] Michael: relearn Java/Android development
- [ ] Diane: relearn Java/Android development

### Day 2:

- [ ] Terry: write backend (users table; badges table; badging (join table)) and user email signup; API endpoints to add points/badges
- [ ] Tyler: Google places API connection
- [ ] Michael: Display map in view using OpenGL ES and display a map as its texture. Texture will come from the Google Maps Static API that returns a static image for a set of coordinates that we will get from gps eventually
- [ ] Diane: Connect front end/backend; check API endpoints; finish setting up new computer

### Day 3:

- [ ] Terry: users can use Facebook (https://developers.facebook.com/docs/facebook-login/android) or Google+ (https://developers.google.com/+/mobile/android/getting-started) to sign up
- [ ] Tyler: Places filtering and saving
- [ ] Michael: Display landmarks on map by instancing the objects at their corresponding coordinates
- [ ] Diane: Spinning/Cleaning/Interacting with landmark; display info and increment coins

### Day 4:

- [ ] Terry: loading screen (http://www.41post.com/4588/programming/android-coding-a-loading-screen-part-1)
- [ ] Tyler: User Profile Page
- [ ] Michael: Limit tapping on landmarks based on distance
- [ ] Diane: Linking landmarks to their landmark pages

### Day 5:

- [ ] Terry: production README
- [ ] Tyler: emulator
- [ ] Michael: emulator (and Youtube video)
- [ ] Diane: demo page

## Userbase Plan

Terry, Tyler, Diane, and Michael will each share with at least 10 friends and family and ask for good reviews.
Tyler will find an appropriate subreddit and make a post there to show off the app.
