# Lumi

Lumi is a modern, offline-first to-do list application for Android, built to demonstrate modern Android development practices.

The app is built entirely with **Kotlin** and follows the official architecture guidelines, using key components from **Jetpack**.

## Features

- **Create & Manage Tasks:** Easily add, edit, check off, and delete tasks.
- **Task Filtering:** Quickly filter your tasks to see what's `All`, `Active`, or `Completed`.
- **Profile Page:** Personalize your experience by setting your name and track your task completion stats.
- **Offline Support:** All data is stored locally in a Room database, making the app fully functional without an internet connection.
- **Modern & Dynamic UI:**
  - A 100% Jetpack Compose-based UI.
  - Implements Material 3 design principles.
  - Supports dynamic color theming, adapting to your device's wallpaper on Android 12+.
- **Clean Architecture:** Follows the MVVM (Model-View-ViewModel) pattern for a scalable and maintainable codebase.

## Tech Stack & Key Libraries

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/compose) for a declarative and modern UI.
- **Database**: [Room](https://developer.android.com/jetpack/androidx/releases/room) persistence library for a robust local data storage.
- **Navigation**: Navigation Compose for handling in-app navigation.

## Architecture

This project follows the official **MVVM (Model-View-ViewModel)** architecture recommended by Google, ensuring a clean separation of concerns.

- **UI Layer (View):** `MainActivity`, `LumiHomeScreen`, `LumiProfileScreen` and other `@Composable` functions that observe state changes from the ViewModel.
- **ViewModel Layer:** `LumiViewModel` holds and processes UI-related data. It interacts with the Data layer and exposes state to the UI via `StateFlow`.
- **Data Layer:** `TaskDao`, `UserDao`, and `AppDatabase` (Room) which act as the single source of truth for the app's data.

## Getting Started

To build and run this project locally, you will need:

- Android Studio Iguana | 2023.2.1 or higher
- Android SDK 34

Follow these steps:

1. **Clone the repository:**
   ```
   git clone [https://github.com/ausathdzil/Lumi.git](https://github.com/ausathdzil/Lumi.git)
   ```
3. **Open the project in Android Studio:**
   - Go to `File > Open...`
   - Navigate to the cloned repository folder and select it.
5. **Sync Gradle:**
   - Let Android Studio download all the required Gradle dependencies.
7. **Run the app**:
   - Click the `Run 'app'` button or use the shortcut `Shift + F10`.
