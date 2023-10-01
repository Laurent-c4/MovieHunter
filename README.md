# Movie Hunter

## Description
This is a movie search app that allows users to search for movies and view the movie details. The app also allows users to watch movie trailers where available.

## Table of Contents
* [Installation](#installation)
* [Features](#features)
* [Screenshots](#screenshots)
* [Technologies](#technologies)
* [Contributing](#contributing)

## APK
[Download](https://github.com/laurentjuma/moviehunter/releases)

## Installation
Clone the repo and open in Android Studio.

Add your API keys to your `local.properties` file. You can use the following API keys:

```
TMDB_API_KEY=09ad8ace66eec34302943272db0e8d2c
```

The following link contains additional API keys for TMDB. [Click to view API Keys](https://github.com/rickylawson/freekeys/blob/master/index.js)

You also need a google-services.json file from Firebase. Click [Google Services](https://firebase.google.com/docs/android/setup)  for more information.


I used `JAVA_VERSION=17`. You may need to change  to this version of Java in your Android Studio settings.

## Features
* Authentication with Firebase - Google and Email/Password
* Fingerprint Lock
* Search for movies
* View movies in grid or list view
* View movie details
* Watch movie trailers of a movie
* Offline support

## Future Features
* Support for multiple screen sizes and orientations
* Add favorite movies to a list
* Implement movie recommendations (ML/AI)

## Technologies
* Jetpack Compose
* Hilt Dependency Injection
* Clean Architecture
* Room DB

## License
This project is licensed under the MIT license.
