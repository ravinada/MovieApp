# MovieApp
The purpose of this project was to built an app, to help users discover popular and highly rated movies on the web. It displays a scrolling grid of movie trailers, launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews. This app utilizes core Android user interface components and fetches movie information using themoviedb.org web API.
 
 <img width="33%" src="https://user-images.githubusercontent.com/49580276/83109726-83f14600-a0df-11ea-842b-866e6b9c30b3.png" />          <img width="33%" src="https://user-images.githubusercontent.com/49580276/83109752-90759e80-a0df-11ea-9c74-e279a9d17b97.png" />            <img width="33%" src="https://user-images.githubusercontent.com/49580276/83109765-97041600-a0df-11ea-82f2-f302c58c5aaa.png" />
 
 Developer setup
---------------

### Getting Started

App uses The Movie Database API. You have to enter your API key in order to run the app. You can create your own one very easy! https://www.themoviedb.org/account/signup?language=en-EN. When you get it, just set it here:  "popular-movies-app/gradle.properties"

### Requirements

- Java 8
- Latest version of Android SDK and Android Build Tools

### API Key

The app uses themoviedb.org API to get movie information and posters. You must provide your own [API key][1] in order to build the app.

Just put your API key into `~/.gradle/gradle.properties` file (create the file if it does not exist already):

```gradle
MY_MOVIE_DB_API_KEY="ravinada"
```

### Building

You can build the app with Android Studio or with `./gradlew assembleDebug` command.
