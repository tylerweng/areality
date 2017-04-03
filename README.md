# AREALITY (Flex Project Proposal)

Diane Zheng, Michael Hooton, Terry O’Shea, and Tyler Weng

## Background

A cross between Foursquare and Pokémon Go, AREALITY is an AR app that rewards users for discovering local landmarks.

[add more later]

## Functionality & MVP

With this app, users will be able to:
- [ ] Access a map (2D, 3D if we can manage) of their current location showing Landmarks in the area.
- [ ] Interact with (“clean”? “uncover”?) a Landmark by swiping when nearby
- [ ] Save their badges (e.g. “Walked 5 miles,” “Uncovered 10 (25, 50, 100) landmarks,” “Explored 5 days in a row,” “Achieved XX distance between landmarks,” “Hit XX landmarks within 24 hours”) and points/coins
- [ ] See a leaderboard of players with the most badges/coins

## Bonus Features:

- [ ] Store
- [ ] 3D map
- [ ] AR Landmark interface

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
