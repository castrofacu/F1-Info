# F1 Info

F1 Info is an Android application project designed to showcase modern mobile development skills, including **Kotlin Multiplatform (KMP)** architecture. The app provides live and historical Formula 1 race data, including driver standings, real-time race positions, and an interactive race timeline replay feature.

## Features
- **Driver Standings**: View current F1 drivers with team information, driver numbers, and headshots
- **Race Replay**: Interactive timeline replay of race positions with play/pause controls
- **Real-time Data**: Fetch live race data from the OpenF1 API
- **Modern UI**: Clean, adaptive Material 3 design with support for different screen sizes
- **MVI Architecture**: Unidirectional data flow with clear state management
- **Kotlin Multiplatform**: Shared data, domain, and **presentation layers** (ViewModels + MVI) targeting Android and iOS

> **Note:** This project is a work in progress. More features are coming soon!

## Upcoming Features
- iOS SwiftUI UI layer consuming the shared KMP module
- Local database caching (SQLDelight) for offline access
- Driver detail pages with statistics and history
- Race schedule and calendar
- Lap-by-lap telemetry data
- Dark theme support

## Architecture

The project follows **Clean Architecture** principles with **MVI (Model-View-Intent)** for the presentation layer, and is structured as a **Kotlin Multiplatform** project.

### Module Structure

```
F1 Info
├── app/                        # Android application module
│   ├── di/                     # Android-only DI (ViewModel registration via Koin)
│   ├── F1InfoApplication.kt    # Application entry point (Koin init)
│   ├── MainActivity.kt
│   └── presentation/
│       ├── core/
│       │   ├── components/     # Shared Compose components (ErrorComponent, LoadingComponent)
│       │   ├── navigation/     # AppDestination, NavigationState
│       │   └── theme/          # Material 3 theme (Color, Type, Shape, Spacing, Elevation)
│       ├── drivers/
│       │   ├── navigation/     # DriversNavGraph
│       │   └── ui/             # DriversScreen, DriverCard
│       └── racereplay/
│           ├── navigation/     # RaceReplayNavGraph
│           └── ui/             # RaceReplayScreen, DriverPositionCard
│
└── shared/                     # KMP shared module (Android + iOS)
    └── commonMain/
        ├── data/
        │   ├── dto/            # Serializable DTOs (API response models)
        │   ├── mapper/         # DTO → Domain mappers
        │   ├── remote/         # Ktor HTTP client + safeApiCall
        │   ├── repository/     # Repository implementations
        │   └── timeline/       # RaceTimelineProcessor (BuildRaceTimelineUseCase impl)
        ├── domain/
        │   ├── model/          # Domain models (Driver, Position, DomainError, Result)
        │   ├── repository/     # Repository interfaces
        │   └── usecase/        # Use case interfaces and implementations
        ├── presentation/
        │   ├── mvi/            # BaseViewModel<State, Intent, Effect>
        │   ├── common/         # AppConstants (session keys, etc.)
        │   ├── util/           # ErrorMessageMapper
        │   ├── drivers/
        │   │   ├── mvi/        # DriversState, DriversIntent, DriversEffect
        │   │   └── DriversViewModel.kt
        │   └── racereplay/
        │       ├── mvi/        # RaceReplayState, RaceReplayIntent, RaceReplayEffect
        │       └── RaceReplayViewModel.kt
        └── di/                 # Koin shared module (network, repositories, use cases)
```

### Layer Responsibilities

- **Domain Layer** (`shared/domain/`): Pure Kotlin models, repository interfaces, and use case contracts. No framework dependencies. Shared across all platforms.
- **Data Layer** (`shared/data/`): Ktor-based networking, DTOs, mappers, and repository implementations. Isolated behind domain interfaces.
- **Presentation Layer** (`shared/presentation/`): Platform-agnostic ViewModels and MVI state/intent/effect classes. Uses `androidx.lifecycle.ViewModel` KMP support. Shared across all platforms.
- **UI Layer** (`app/presentation/`): Jetpack Compose screens, components, navigation, and theme. Organized by feature under a single flat `presentation/` package — no `features/` nesting. Android-only.

### Key Design Decisions

- **`safeApiCall`**: Centralized error handling for all API calls. Maps Ktor exceptions to typed `DomainError` (`ServerError`, `ParseError`, `NetworkError`, `UnknownError`).
- **`BuildRaceTimelineUseCase`**: Returns `List<Pair<Instant, List<DriverPosition>>>` (ordered by contract) instead of `Map` to guarantee chronological iteration and enable efficient binary search in the ViewModel.
- **`RaceTimelineProcessor`**: Concrete implementation of `BuildRaceTimelineUseCase` living in `data/` — the ViewModel only depends on the domain interface.
- **`expectSuccess = true`** on Ktor `HttpClient`: Ensures 4xx/5xx responses throw `ClientRequestException`/`ServerResponseException` rather than returning silently.

## Tech Stack & Libraries

### Kotlin Multiplatform
- **Kotlin 2.3.0** with KMP plugin
- **Ktor 3.1.3**: Multiplatform HTTP client (replaces Retrofit)
  - `ktor-client-android`: Android engine
  - `ktor-client-content-negotiation` + `ktor-serialization-kotlinx-json`: JSON handling
  - `ktor-client-logging`: Request/response logging
  - `ktor-client-mock`: Mock engine for testing
- **kotlinx.serialization**: Multiplatform JSON serialization (replaces Gson)
- **kotlinx.datetime**: Multiplatform date/time (replaces `java.time`)
- **kotlinx-coroutines-test**: Coroutine testing utilities
- **androidx.lifecycle.viewmodel 2.10.0**: KMP ViewModel support — `BaseViewModel` lives in `shared/commonMain`

### Android
- **Kotlin 2.3.0**
- **Android Gradle Plugin 8.9.1**
- **Min SDK 24** / **Target SDK 36**
- **Java 21**

### UI & Design
- **Jetpack Compose** (BOM 2026.01.01)
- **Material 3** with Adaptive Navigation Suite
- **Coil 2.7.0**: Image loading for Compose

### Dependency Injection
- **Koin 4.1.1**: Lightweight DI framework
  - `sharedModule`: Network, repositories, and use cases (in `:shared`)
  - `appModule`: ViewModels (in `:app`)

### Testing
- **kotlin-test**: Multiplatform test assertions
- **kotlinx-coroutines-test**: Suspend function testing with `runTest`
- **ktor-client-mock**: HTTP mocking without a real server (equivalent to MockWebServer, but KMP)
- **JUnit 4**: Android test runner

## KMP Migration Status

| Layer | Status |
|---|---|
| Domain models | Migrated to `shared/commonMain` |
| Repository interfaces | Migrated to `shared/commonMain` |
| DTOs + Mappers | Migrated to `shared/commonMain` |
| Ktor HTTP client | Migrated (replaced Retrofit) |
| Repository implementations | Migrated to `shared/commonMain` |
| Use cases | Migrated to `shared/commonMain` |
| DI (Koin shared module) | Migrated to `shared/commonMain` |
| Android ViewModel + MVI | Migrated to `shared/commonMain/presentation/` |
| Compose UI | Remains in `:app` (platform-specific) |
| iOS SwiftUI UI | Pending |

## API Reference

This project consumes the [OpenF1 API](https://openf1.org/) for all its data. This is a third-party API and is not affiliated with this project.

## Contributing
This is a personal portfolio project, but feedback and suggestions are always welcome!

## License
This project is for educational and portfolio purposes.

---

Built with ❤️ to demonstrate modern Android and Kotlin Multiplatform development practices
