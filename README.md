# MatchKMP

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue.svg?style=flat-square)](https://kotlinlang.org/docs/reference/multiplatform.html)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-brightgreen.svg?style=flat-square)](https://www.jetbrains.com/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Overview

MatchKMP is a cross-platform application built with Kotlin Multiplatform and Compose Multiplatform that brings the classic M.A.S.H. (Mansion, Apartment, Shack, House) fortune-telling game to your Android and iOS devices. The app provides a modern and engaging user experience with customizable game templates, different visual themes, and fun animations.

## Features

* **Cross-Platform:** Enjoy the same experience on both Android and iOS.
* **Classic & Modern Gameplay:** Choose between the traditional M.A.S.H. gameplay or a new, modern version.
* **Customizable Templates:** Create, edit, and save your own M.A.S.H. templates for endless fun.
* **Game History:** Keep track of all your past M.A.S.H. results.
* **Theming:** Personalize your app experience with different color themes.
* **Engaging Animations:** Fun and interactive animations that bring the game to life.

## Architecture

MatchKMP is built using the latest in Kotlin Multiplatform development:

* **Kotlin Multiplatform:** The core logic is written in Kotlin and shared between Android and iOS, reducing redundancy and ensuring consistency.
* **Compose Multiplatform:** The UI is built with Compose Multiplatform, allowing for a shared, declarative UI across both platforms.
* **ViewModel:** A `MashViewModel` handles the application's state and business logic, interacting with the UI through Kotlin Flows.
* **Platform-Specific Implementations:** Utilizes `expect`/`actual` for platform-specific features like settings management.

## Getting Started

To get started with MatchKMP, you'll need the following:

* **Android Studio:** For running the Android version of the app.
* **Xcode:** For running the iOS version of the app.
* **Kotlin Multiplatform Mobile Plugin:** To build and run the project.

### Building the Project

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/MatchKMP.git](https://github.com/your-username/MatchKMP.git)
    ```
2.  **Open in Android Studio:**
    Open the project in Android Studio. The IDE will automatically sync the Gradle files and download the necessary dependencies.

3.  **Run on Android:**
    Select the `composeApp` run configuration and choose an Android emulator or a connected device.

4.  **Run on iOS:**
    Open the `iosApp` directory in Xcode and run the app on an iOS simulator or a connected device.

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, please feel free to open an issue or submit a pull request.

## License

MatchKMP is licensed under the Apache 2.0 License. See the [LICENSE](LICENSE) file for more details.
