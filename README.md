# Movie Guru

## Description
This is a movie search app that allows users to search for movies and view the movie details. The app also allows users to watch movie trailers where available.

## Table of Contents
* [Installation](#installation)
* [Features](#features)
* [Technologies](#technologies)
* [Screenshots](#screenshots)
* [Technologies](#technologies)
* [Contributing](#contributing)

## APK
[Download](https://drive.google.com/uc?export=download&id=1EczhvfKy_h-o7iqac4AJrGGwTEn2bl2i)

## Installation
Clone the repo and open in Android Studio.

Add your API keys to the gradle.properties file. You can use the following API keys: 
\`\`\`
OMDB_API_KEY=a95b5205
TMDB_API_KEY=09ad8ace66eec34302943272db0e8d2c
\`\`\`

The link below contains additional API keys for OMDB and TMDB
[Keys](https://github.com/rickylawson/freekeys/blob/master/index.js)

You also need a google-services.json file from Firebase. Click the link below for more information.
[Google Services](https://firebase.google.com/docs/android/setup)

I used JAVA_VERSION=17. You may need to change  to this version of Java in your Android Studio settings.

## Features
* Authentication with Firebase - Google and Email/Password
* Fingerprint Lock
* Search for movies
* Sort movies by date in ascending order (More implementations to come) _Note: This feature is not reliable since OMDB API does not have sorting capabilities. Sorting is therefore done in the Room DB_
* View movies in grid or list view
* View movie details
* Watch movie trailers of a movie
* Offline support

## Future Features
* Support for multiple screen sizes and orientations
* Add favorite movies to a list
* Implement movie recommendations (ML/AI)
* Connectivity Monitor

## Technologies
* Jetpack Compose
* Hilt Dependency Injection
* Clean Architecture
* Room DB

## ScreenShots
![Home(Grid)](https://github.com/Laurent-c4/MovieGuru/assets/43743544/1599fdfe-a6e4-427c-b017-9f2fbc27b167)
![Home(List)](https://github.com/Laurent-c4/MovieGuru/assets/43743544/0dc66cbb-afb3-49d2-bb8a-9a6095fbcd82)
![Movie Details](https://github.com/Laurent-c4/MovieGuru/assets/43743544/07fd45de-6cfc-4310-858c-c54ab51db21d)

## License
This project is licensed under the MIT license.