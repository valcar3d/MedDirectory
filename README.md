# MedDirectory

An Android application that displays a directory of medical professionals with detailed information and salary statistics.

## Overview

MedDirectory is a modern Android app built with Jetpack Compose that fetches and displays a feed of medical professionals from a remote API. The app features a clean, responsive UI with detailed information cards for each professional, including dynamic avatar generation.

## Features

- **Feed Screen**: Browse a list of medical professionals with key information
- **Detail Screen**: View detailed information about each professional
- **Salary Statistics**: Visual indicators showing salary ranges compared to average
- **Dynamic Avatars**: Generated avatars for each professional using DiceBear API
- **Error Handling**: Graceful handling of network and server errors with retry functionality
- **Responsive Design**: Material Design 3 with adaptive layouts

## Tech Stack

### Core Technologies

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Coroutines & Flow** - Asynchronous programming

### Architecture & DI

- **Clean Architecture** - Separation of concerns with data, domain, and presentation layers
- **MVVM Pattern** - Model-View-ViewModel for UI layer
- **Hilt** - Dependency injection
- **Repository Pattern** - Abstraction for data sources

### Networking & Data

- **Retrofit** - HTTP client for API calls
- **Kotlinx Serialization** - JSON serialization/deserialization
- **OkHttp** - HTTP client with logging interceptor
- **Coil 3** - Image loading and caching

### Navigation

- **Navigation Compose** - Type-safe navigation with Kotlin Serialization

### Testing

- **JUnit 4** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Turbine** - Testing Kotlin Flow
- **Coroutines Test** - Testing coroutines

## Architecture

The project follows **Clean Architecture** principles with three main layers:

### 1. Data Layer

```
data/
├── constants/          # API constants and endpoints
├── remote/            # API service interfaces and DTOs
└── repository/        # Repository implementations
```

- **FeedApiService**: Retrofit interface for fetching feed data
- **FeedItemDto**: Data transfer object for API responses
- **FeedRepositoryImpl**: Repository implementation that handles data operations
- **ErrorMapper**: Maps HTTP exceptions to domain errors

### 2. Domain Layer

```
domain/
├── model/             # Domain models (FeedItem, etc.)
├── repository/        # Repository interfaces
└── usecases/          # Use cases (business logic)
```

- **FeedItem**: Domain model representing a medical professional
- **FeedRepository**: Repository interface
- **GetFeedUseCase**: Use case for fetching the feed

### 3. Presentation Layer

```
presentation/
├── navigation/        # Navigation setup
├── screens/          # UI screens (Feed, Detail)
├── common/           # Shared UI utilities and extensions
└── PreviewData.kt    # Preview data for Compose previews
```

- **FeedScreen**: Main screen displaying the list of professionals
- **DetailScreen**: Detailed view of a selected professional
- **FeedViewModel**: Manages UI state and business logic for FeedScreen
- **AppNavigation**: Navigation graph configuration

## Data Flow

```
UI (Compose) → ViewModel → UseCase → Repository → API Service → Remote API
                                              ↓
UI ← ViewModel ← UseCase ← Repository ← DTO Mapping
```

1. **UI** triggers actions (e.g., initial load, retry)
2. **ViewModel** processes actions and calls use cases
3. **UseCase** contains business logic and calls repository
4. **Repository** fetches data from remote API
5. **DTOs** are mapped to domain models
6. **UI State** is updated and observed by Compose UI

## API Integration

The app uses [Mocki.io](https://mocki.io/) for mock data:

- **Base URL**: `https://mocki.io/v1/`
- **Endpoint**: `5bb09ab0-8d6d-4d85-8284-b6a467299353`

### Data Model

```kotlin
data class FeedItem(
    val id: String,
    val firstName: String,
    val lastName: String,
    val suffix: String?,           // MD, DO, etc.
    val specialty: String,          // Medical specialty
    val npi: String?,              // National Provider Identifier
    val location: String,          // Practice location
    val salaryRange: String?,      // Salary range (e.g., "$150k-$200k")
    val acceptingNewPatients: Boolean
)
```

### Avatar Generation

Avatars are dynamically generated using [DiceBear API](https://dicebear.com/):

- **Style**: `notionists`
- **Format**: PNG
- **Based on**: Professional's ID for consistent generation

## Project Structure

```
app/src/main/java/com/example/meddirectory/
├── common/
│   └── AppError.kt              # Sealed class for error handling
├── data/
│   ├── constants/
│   │   └── ApiConstants.kt      # API URLs and endpoints
│   ├── remote/
│   │   ├── FeedApiService.kt    # Retrofit service
│   │   └── dto/
│   │       └── FeedItemDto.kt   # API data model
│   └── repository/
│       ├── ErrorMapper.kt       # Exception to error mapping
│       └── FeedRepositoryImpl.kt # Repository implementation
├── di/
│   ├── ImageLoaderModule.kt     # Coil configuration
│   ├── NetworkModule.kt         # Retrofit & OkHttp setup
│   └── RepositoryModule.kt      # Repository bindings
├── domain/
│   ├── model/
│   │   └── FeedItem.kt          # Domain model
│   ├── repository/
│   │   └── FeedRepository.kt    # Repository interface
│   └── usecases/
│       └── GetFeedUseCase.kt    # Business logic
├── presentation/
│   ├── common/
│   │   └── FeedItemExtensions.kt # UI extensions and utilities
│   ├── navigation/
│   │   └── AppNavigation.kt     # Navigation setup
│   ├── screens/
│   │   ├── detail/
│   │   │   └── DetailScreen.kt  # Detail UI
│   │   └── feed/
│   │       ├── FeedScreen.kt    # Feed UI
│   │       ├── FeedUiState.kt   # UI state sealed class
│   │       ├── FeedViewModel.kt # ViewModel
│   │       └── components/
│   │           └── FeedItemCard.kt # Individual item card
│   ├── PreviewData.kt           # Mock data for previews
│   └── theme/                   # Material 3 theme
├── MainActivity.kt              # Entry point
└── MedDirectoryApplication.kt   # Application class
```

## UI Components

### Feed Screen

- **TopAppBar**: Title with refresh action
- **FeedItemCard**: Card displaying professional info with:
  - Avatar (generated)
  - Name with suffix
  - Specialty badge
  - Location
  - Salary range with visual indicator
  - "Accepting new patients" status
- **Loading State**: Centered progress indicator
- **Error State**: Error message with retry button

### Detail Screen

- Detailed view of selected professional
- Larger avatar display
- Complete information layout
- Salary statistics comparison
- Back navigation

## Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35
- Minimum Android version: API 24 (Android 7.0)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/valcar3d/MedDirectory.git
   ```

2. Open in Android Studio

3. Sync Gradle and build the project

4. Run on an emulator or device

## Configuration

### API Configuration

API constants are defined in `ApiConstants.kt`:

```kotlin
object ApiConstants {
    const val BASE_URL = "https://mocki.io/v1/"
    const val FEED_ENDPOINT = "5bb09ab0-8d6d-4d85-8284-b6a467299353"
}
```

To use a different API endpoint, update the `FEED_ENDPOINT` constant.

### Theme Customization

Colors and typography are defined in `ui/theme/`:

- **Color.kt**: Define color schemes
- **Type.kt**: Typography settings
- **Theme.kt**: Light/dark theme configuration

## Error Handling

The app handles various error scenarios:

- **NetworkError**: No internet connection
- **NotFoundError**: Resource not found (404)
- **ServerError**: Server-side errors (5xx)
- **UnknownError**: Unexpected errors

Each error displays an appropriate message with a retry option.

## Testing

### Unit Tests

Located in `app/src/test/`:

- **FeedRepositoryImplTest**: Tests repository logic
- **FeedViewModelTest**: Tests ViewModel state management

Run tests with:
```bash
./gradlew test
```

### UI Testing

Instrumented tests located in `app/src/androidTest/`:

- Basic instrumentation test setup

Run with:
```bash
./gradlew connectedAndroidTest
```

## Dependencies

Key dependencies (managed via `libs.versions.toml`):

```kotlin
// Compose BOM - latest stable versions
implementation(platform(libs.androidx.compose.bom))

// Navigation
implementation(libs.androidx.navigation.compose)

// Networking
implementation(libs.retrofit)
implementation(libs.retrofit.converter.kotlinx)
implementation(libs.okhttp.logging)

// DI
implementation(libs.hilt.android)
ksp(libs.hilt.compiler)

// Image Loading
implementation(libs.coil.compose)
implementation(libs.coil.network)

// Serialization
implementation(libs.kotlinx.serialization.json)
```

## Build Configuration

- **Compile SDK**: 35
- **Min SDK**: 24
- **Target SDK**: 35
- **Java/Kotlin Target**: 17

## Future Enhancements

Potential improvements:

- [ ] Local caching with Room database
- [ ] Search functionality
- [ ] Filter by specialty or location
- [ ] Favorite/bookmark professionals
- [ ] Share professional profile
- [ ] Dark mode optimization
- [ ] Accessibility improvements
- [ ] Pull-to-refresh
- [ ] Pagination for large datasets

## License

This project is for educational purposes.

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

---

Built with ❤️ using Jetpack Compose and modern Android architecture patterns.
