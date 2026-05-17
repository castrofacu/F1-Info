# F1 Info

F1 Info is a **Kotlin Multiplatform (KMP)** app for Android and iOS that provides live and historical Formula 1 race data, including driver standings, real-time race positions, and an interactive race timeline replay feature.

## Features
- **Driver Standings**: View current F1 drivers with team information, driver numbers, and headshots
- **Race Replay**: Interactive timeline replay of race positions with play/pause controls
- **Real-time Data**: Fetch live race data from the OpenF1 API
- **Modern UI**: Material 3 on Android / native SwiftUI on iOS
- **MVI Architecture**: Unidirectional data flow with clear state management
- **Shared ViewModels**: All business logic, ViewModels and MVI state live in `shared/commonMain` — consumed by both Android and iOS

> **Note:** This project is a work in progress. More features are coming soon!

## Upcoming Features
- Local database caching (SQLDelight) for offline access
- Driver detail pages with statistics and history
- Race schedule and calendar
- Lap-by-lap telemetry data
- Dark theme support

## Architecture

The project follows **Clean Architecture** principles with **MVI (Model-View-Intent)** for the presentation layer, structured as a **Kotlin Multiplatform** project.

### Module Structure

```
F1 Info
├── app/                          # Android application module
│   ├── di/                       # Android-only DI (ViewModel registration via Koin)
│   ├── F1InfoApplication.kt      # Application entry point (Koin init)
│   ├── MainActivity.kt
│   └── presentation/
│       ├── core/
│       │   ├── components/       # Shared Compose components (ErrorComponent, LoadingComponent)
│       │   ├── navigation/       # AppDestination, NavigationState
│       │   └── theme/            # Material 3 theme (Color, Type, Shape, Spacing, Elevation)
│       ├── drivers/
│       │   ├── navigation/       # DriversNavGraph
│       │   └── ui/               # DriversScreen, DriverCard
│       └── racereplay/
│           ├── navigation/       # RaceReplayNavGraph
│           └── ui/               # RaceReplayScreen, DriverPositionCard
│
├── iosApp/iosApp/                # iOS application module (SwiftUI)
│   ├── iOSApp.swift              # App entry point — inicializa Koin
│   ├── ContentView.swift         # TabView raíz (Drivers + Race Replay)
│   ├── di/
│   │   └── KoinHelper.swift      # Llama a initKoinIos() del shared framework
│   └── presentation/
│       ├── core/
│       │   ├── components/       # LoadingView, ErrorView (equivalentes iOS de los Compose components)
│       │   └── theme/            # AppTheme.swift — colores F1, extensión Color(hex:)
│       ├── drivers/
│       │   └── ui/
│       │       ├── DriversObservableViewModel.swift  # ObservableObject wrapper de DriversViewModel
│       │       ├── DriversView.swift                 # Equivalente a DriversScreen.kt
│       │       └── DriverCard.swift                  # Equivalente a DriverCard.kt
│       └── racereplay/
│           └── ui/
│               ├── RaceReplayObservableViewModel.swift  # ObservableObject wrapper de RaceReplayViewModel
│               ├── RaceReplayView.swift                 # Equivalente a RaceReplayScreen.kt
│               └── DriverPositionCard.swift             # Equivalente a DriverPositionCard.kt
│
└── shared/                       # KMP shared module (Android + iOS)
    ├── commonMain/
    │   ├── data/
    │   │   ├── dto/              # Serializable DTOs (API response models)
    │   │   ├── mapper/           # DTO → Domain mappers
    │   │   ├── remote/           # Ktor HTTP client + safeApiCall
    │   │   ├── repository/       # Repository implementations
    │   │   └── timeline/         # RaceTimelineProcessor (BuildRaceTimelineUseCase impl)
    │   ├── domain/
    │   │   ├── model/            # Domain models (Driver, Position, DomainError, Result)
    │   │   ├── repository/       # Repository interfaces
    │   │   └── usecase/          # Use case interfaces and implementations
    │   ├── presentation/
    │   │   ├── common/           # AppConstants, BaseViewModel<State, Intent, Effect>, ErrorMessageMapper
    │   │   ├── drivers/
    │   │   │   ├── mvi/          # DriversState, DriversIntent, DriversEffect
    │   │   │   └── DriversViewModel.kt
    │   │   └── racereplay/
    │   │       ├── mvi/          # RaceReplayState, RaceReplayIntent, RaceReplayEffect
    │   │       └── RaceReplayViewModel.kt
    │   └── di/                   # sharedModule (network, repositories, use cases)
    └── iosMain/
        └── di/
            ├── IosModule.kt      # iosModule (ViewModels) + initKoinIos() entry point
            └── ViewModelHelper.kt  # KoinComponent que expone ViewModels a Swift
```

### Layer Responsibilities

- **Domain Layer** (`shared/domain/`): Pure Kotlin models, repository interfaces, and use case contracts. No framework dependencies. Shared across all platforms.
- **Data Layer** (`shared/data/`): Ktor-based networking, DTOs, mappers, and repository implementations. Isolated behind domain interfaces.
- **Presentation Layer** (`shared/presentation/`): Platform-agnostic ViewModels and MVI state/intent/effect classes. Uses `androidx.lifecycle.ViewModel` KMP support. Shared across all platforms.
- **Android UI Layer** (`app/presentation/`): Jetpack Compose screens, components, navigation, and theme. Organized by feature. Android-only.
- **iOS UI Layer** (`iosApp/presentation/`): SwiftUI screens and components, organized following the same feature folder structure as Android. iOS-only.

### Key Design Decisions

- **`safeApiCall`**: Centralized error handling for all API calls. Maps Ktor exceptions to typed `DomainError` (`ServerError`, `ParseError`, `NetworkError`, `UnknownError`).
- **`BuildRaceTimelineUseCase`**: Returns `List<Pair<Instant, List<DriverPosition>>>` (ordered by contract) instead of `Map` to guarantee chronological iteration and enable efficient binary search in the ViewModel.
- **`RaceTimelineProcessor`**: Concrete implementation of `BuildRaceTimelineUseCase` living in `data/` — the ViewModel only depends on the domain interface.
- **`expectSuccess = true`** on Ktor `HttpClient`: Ensures 4xx/5xx responses throw `ClientRequestException`/`ServerResponseException` rather than returning silently.
- **SKIE (Swift/Kotlin Interface Extensions)**: Applied to the `shared` module to transform `StateFlow<T>` into Swift `AsyncSequence` and sealed interfaces into exhaustive `switch` via `onEnum(of:)`. This enables `for await` loops in Swift instead of manually bridging Kotlin coroutines.
- **iOS Koin setup — split modules**: ViewModels are registered in `iosModule` (in `iosMain`) as `factory { }`, separate from `sharedModule` (in `commonMain`). This avoids conflicts with Android's `viewModel { }` registration in `appModule`, which integrates with the `ViewModelStore` lifecycle. `initKoinIos()` is idempotent — it checks `KoinPlatformTools.defaultContext()` before calling `startKoin` to prevent crashes in previews or tests.
- **`ViewModelHelper` (KoinComponent)**: iOS resolves ViewModels via `ViewModelHelper().driversViewModel()` — a `KoinComponent` that exposes concrete return types so SKIE can bridge them correctly to Swift. This is the standard Koin pattern for KMP iOS.
- **ObservableObject wrappers (iOS)**: Since `BaseViewModel` extends `androidx.lifecycle.ViewModel` (not directly observable by SwiftUI), each feature has a thin `@MainActor ObservableObject` class in Swift that holds a reference to the Kotlin ViewModel, observes its `StateFlow` via SKIE's `AsyncSequence`, and publishes state changes via `@Published` to drive SwiftUI re-renders.
- **`@Published alertMessage` for effects (iOS)**: One-shot effects (`ShowError`) are consumed in the ObservableObject wrapper and surfaced as `@Published var alertMessage: String?`. This avoids the race condition of a closure-based `onShowError` handler that could be nil when the effect arrives (e.g., an immediate failure on `init`).

## Tech Stack & Libraries

### Kotlin Multiplatform
- **Kotlin 2.3.0** with KMP plugin
- **SKIE 0.10.11**: Swift/Kotlin Interface Extensions — transforms `StateFlow`, `Flow`, and sealed classes into idiomatic Swift types (`AsyncSequence`, exhaustive `switch` via `onEnum(of:)`)
- **Ktor 3.1.3**: Multiplatform HTTP client
  - `ktor-client-okhttp`: Android engine
  - `ktor-client-darwin`: iOS engine (NSURLSession)
  - `ktor-client-content-negotiation` + `ktor-serialization-kotlinx-json`: JSON handling
  - `ktor-client-logging`: Request/response logging
  - `ktor-client-mock`: Mock engine for testing
- **kotlinx.serialization**: Multiplatform JSON serialization
- **kotlinx.datetime**: Multiplatform date/time
- **kotlinx-coroutines-test**: Coroutine testing utilities
- **androidx.lifecycle.viewmodel 2.10.0**: KMP ViewModel support — `BaseViewModel` lives in `shared/commonMain`

### Android
- **Android Gradle Plugin 8.9.1**
- **Min SDK 24** / **Target SDK 36** / **Java 21**
- **Jetpack Compose** (BOM 2026.01.01) + **Material 3**
- **Coil 2.7.0**: Image loading for Compose
- **Koin 4.1.1**: `appModule` — ViewModels registered with `viewModel { }` for AndroidX lifecycle integration

### iOS
- **SwiftUI** (iOS 15+)
- **Swift Concurrency** (`async/await`, `AsyncSequence`) for observing Kotlin StateFlows
- **AsyncImage** (native SwiftUI): Image loading — no third-party dependency
- **Koin** (via `shared` framework): initialized in `iOSApp.init()` via `KoinHelper.start()`

### Dependency Injection
- **Koin 4.1.1**: Lightweight DI framework
  - `sharedModule` (`commonMain`): Network, repositories, and use cases
  - `appModule` (Android, `:app`): ViewModels with `viewModel { }` for AndroidX lifecycle
  - `iosModule` (`iosMain`): ViewModels with `factory { }` for manual lifecycle management
  - `ViewModelHelper` (iOS): `KoinComponent` that resolves ViewModels with concrete return types — required for SKIE to bridge the types correctly to Swift

### Testing
- **kotlin-test**: Multiplatform test assertions
- **kotlinx-coroutines-test**: Suspend function testing with `runTest`
- **ktor-client-mock**: HTTP mocking without a real server
- **JUnit 4**: Android test runner

## API Reference

This project consumes the [OpenF1 API](https://openf1.org/) for all its data. This is a third-party API and is not affiliated with this project.

## Contributing
This is a personal portfolio project, but feedback and suggestions are always welcome!

## License
This project is for educational and portfolio purposes.

---

Built with ❤️ to demonstrate modern Android and iOS Kotlin Multiplatform development practices
