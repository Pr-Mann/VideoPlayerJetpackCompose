# VideoPlayerJetpackCompose
Jetpack Compose Video Player is an android app that lets you stream videos from the provided API. The app has a simple and intuitive UI with a video player at the top and a scrollable details section at the bottom. You can easily control the playback with custom media controls that let you play/pause, and skip to the next/previous video. You can also access a list of videos sorted by date, fetched from the API. The app also provides a rich and informative details section that shows the title, author, and description of the current video. When you change the screen orientation, the app automatically adjusts to show only the video player for an immersive viewing experience.

This project utilize Model-View-ViewModel (MVVM) pattern, Jetpack Compose, Retrofit, Kotlin, Live Data, etc.

## SCREENSHOTS
<img src="https://github.com/Pr-Mann/VideoPlayerJetpackCompose/assets/66731540/0f70b4f3-e997-4db5-982b-9f458174aa72" width="270" height="570" />  <img src="https://github.com/Pr-Mann/VideoPlayerJetpackCompose/assets/66731540/246617a3-56a6-4a3b-b964-d321c675fe0c" width="270" height="570" />

<img src="https://github.com/Pr-Mann/VideoPlayerJetpackCompose/assets/66731540/22e248c1-1881-4a33-beec-2fd33816be62" width="570" height="270" />

## Tasks

 1. Display a screen similar to the provided wireframe. The screen should
    contain a video player at the top and a scrollable details section at the
    bottom.
 2. Import and use the provided image assets in `assets/` for the media
    controls. For Android, use the provided SVG files. For iOS, use the provided PDF files.
 3. Fetch a list of videos from the provided API (see instructions below for
    running the API).
 4. Sort the received list of videos by date.
 5. Load the first video into the UI by default.
 6. Implement the play/pause button for the video player. The app should be
    paused on startup.
 7. Implement next/previous buttons for the video player. Clicking next should
    update the UI with the next video and video details. Buttons should be
    insensitive when at the start/end of the list.
 8. In the details section, show the returned description for the current video
    as rendered Markdown.
 9. In the details section, also display the title and author of the current
    video.
    
Getting Started With the Server Backend (/server)
-----------------------------------------------
For this exercise a pre-built server application is provided. The application runs by default on `localhost:4000` and has the following endpoints:

 - `http://localhost:4000/videos` - returns a JSON-encoded array of videos.

### Running the Server

The provided API server is needed as a data source for your project. To run the server you will need NodeJS and Yarn.

![](docs/apple.svg) On macOS you can install the requirements using Homebrew ([installation instructions](https://brew.sh/)) with:

```sh
brew install node yarn
```

![](docs/linux.svg) On Linux, use your distribution’s package manager to install Node JS and Yarn. Node 10 or greater is required. You may need to add repositories:

 - https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions
 - https://classic.yarnpkg.com/en/docs/install/#debian-stable

![](docs/windows.svg) On Windows, the best option is to use package installers from:

 - https://nodejs.org/en/download/, and
 - https://classic.yarnpkg.com/en/docs/install/#windows-stable

With dependencies installed, you can run the server with:

```sh
cd server
yarn install
yarn start
```

You can verify the API is working by visiting http://localhost:4000/videos in your browser or another HTTP client.
