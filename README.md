# Together App - Setup Instructions

The project structure is fully set up, including the required Kotlin code, Jetpack Compose layouts, widgets, duration calculations, and alarm scheduling.

## Adding the App Icon
As requested, an `assets` folder has been created at `app/assets` and at the root `assets/`.
To add your custom minimal app icon (`🧿 + subtle heart concept`):
1. Place your vector graphics or raster icons in the appropriate `app/src/main/res/mipmap-*` and `app/src/main/res/drawable` directories.
2. If you are using Android Studio, right-click the `app` directory -> **New** -> **Image Asset** -> **Launcher Icons (Adaptive and Legacy)** and upload your image to let Android Studio generate all the correct sizes automatically.
3. The app is currently set up to use `@mipmap/ic_launcher` in the `AndroidManifest.xml`.

## Building the App
1. Open this directory (`/Users/vaibhavraj/Downloads/Codingprojects/together`) in **Android Studio**.
2. Android Studio will automatically sync the Gradle configuration and download the required dependencies.
3. Once synced, you can build and run the app on your Android device directly from Android Studio.

## Architecture Highlights
- **No Backend**: Complete local persistence using `DataStore`.
- **Jetpack Compose**: All screens and the dashboard are built in Compose matching the dark elegant aesthetic requested.
- **Glance App Widgets**: The two widgets (Duration and Elapsed Time) use modern `Glance` components, supporting Home Screen and Lock Screen (Android 15/16).
- **AlarmManager**: Offline anniversary notifications are scheduled securely and rescheduled automatically after device reboots.

Enjoy the app! 🤎
