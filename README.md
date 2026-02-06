# F1 Info

F1 Info is an Android application project designed to showcase my Android development skills. The app provides live and historical Formula 1 race data, including driver standings, real-time race positions, and an interactive race timeline replay feature. It is built using modern Android development practices and libraries.

## Features
- **Driver Standings**: View current F1 drivers with team information, driver numbers, and headshots
- **Race Replay**: Interactive timeline replay of race positions with play/pause controls
- **Real-time Data**: Fetch live race data from Formula 1 API
- **Modern UI**: Clean, adaptive Material 3 design with support for different screen sizes
- **MVI Architecture**: Unidirectional data flow with clear state management
- **Modular Design**: Feature-based module organization with clean architecture principles

> **Note:** This project is a work in progress. More features are coming soon!

## Upcoming Features
- Local database caching (Room) for offline access
- Driver detail pages with statistics and history
- Race schedule and calendar
- Lap-by-lap telemetry data
- Push notifications for race updates
- Dark theme support
- More race statistics and analytics

## Tech Stack & Libraries

### Core Android
- **Kotlin 2.3.0**: Modern programming language for Android
- **Android Gradle Plugin 9.0.0**: Build system
- **Min SDK 24** / **Target SDK 36**: Android 7.0+ support
- **Java 21**: Latest JVM features with desugaring for backward compatibility

### UI & Design
- **Jetpack Compose**: Modern declarative UI toolkit
  - Compose BOM 2026.01.01
  - Material 3: Latest Material Design components
  - Material 3 Adaptive Navigation Suite: Responsive navigation for different screen sizes
  - Material Icons Extended: Comprehensive icon library
  - Compose UI Tooling: Preview and debugging tools
- **Coil 2.7.0**: Efficient image loading library for Compose

### Architecture & Dependencies
- **Koin 4.1.1**: Lightweight dependency injection framework
  - koin-android: Core Android support
  - koin-androidx-compose: Compose integration
- **AndroidX Core KTX 1.17.0**: Kotlin extensions for Android APIs
- **Lifecycle Runtime KTX 2.10.0**: Lifecycle-aware components
- **Activity Compose 1.12.3**: Compose integration for activities

### Networking
- **Retrofit 3.0.0**: Type-safe HTTP client
- **Gson Converter**: JSON serialization/deserialization

### Testing
- **JUnit 4.13.2**: Unit testing framework
- **AndroidX JUnit 1.3.0**: Android unit testing extensions

### Other
- **Desugar JDK Libs 2.1.5**: Backport of Java APIs for older Android versions

## Architecture

The project follows **Clean Architecture** principles with **MVI (Model-View-Intent)** pattern for presentation layer:

- **Domain Layer**: Use cases and business logic
- **Data Layer**: Repository implementations and data sources
- **Presentation Layer**: ViewModels, UI states, intents, and effects
- **UI Layer**: Composable screens and components

## Screenshots
(Coming soon)

## Contributing
This is a personal portfolio project, but feedback and suggestions are always welcome!

## License
This project is for educational and portfolio purposes.

---

Built with ❤️ to demonstrate modern Android development practices