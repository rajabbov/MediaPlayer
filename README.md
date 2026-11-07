# MediaPlayer

A local audio player built on Media3 ExoPlayer, with a foreground service, a ViewModel layer and a Compose player screen alongside the View-based UI.

## Features

- Scans and plays audio stored on the device
- Background playback via a foreground `MusicService` with media notification
- `MusicViewModel` + `MusicRepository` separate playback state from the UI
- Custom `WaveformView` drawn on a Canvas
- Splash screen, Lottie animations and Coil image loading
- Player screen implemented twice — once in Views, once in Compose

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, with some XML layouts
- **SDK:** min 24, target 34
- **Package:** `com.example.mediaplayer`
- **Libraries:** Media3 / ExoPlayer, Coil, Lottie, Navigation component, ViewModel, LiveData, CircleImageView

<sub>12 source files · 593 lines of code · 4 layouts</sub>

## Getting started

```bash
git clone https://github.com/rajabbov/MediaPlayer.git
```

Open the folder in Android Studio, let Gradle sync, and run the `app` configuration on a device or emulator.

## License

MIT
