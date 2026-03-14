# MedDirectory
An Android application that displays a mock directory of medical professionals with detailed information and salary statistics.

## Overview
MedDirectory is a test application to show a solid MVVM architecture and patterns in a professional environment

## Features
- **Feed Screen**: Browse a list of medical professionals with key information
- **Detail Screen**: View detailed information about each professional
- **Salary Statistics**: Visual indicators showing salary ranges compared to average
- **Dynamic Avatars**: Generated avatars for each professional using DiceBear API
- **Error Handling**: Graceful handling of network and server errors with retry functionality
- **Responsive Design**: Material Design 3 with adaptive layouts

## Tech Stack

### Core Technologies
- **Kotlin**
- **Jetpack Compose** 
- **Coroutines & Flow**

### Architecture & DI
- **Clean Architecture**
- **MVVM Pattern**
- **Hilt**
- **Repository Pattern**

### Networking & Data
- **Retrofit**
- **Kotlinx Serialization**
- **OkHttp**
- **Coil 3**

### Navigation
- **Navigation Compose**

### Testing
- **JUnit 4**
- **MockK**
- **Turbine**
- **Coroutines Test**

## Architecture

The project follows **Clean Architecture** principles with three main layers:

### 1. Data Layer
- **FeedApiService**: Retrofit interface for fetching feed data
- **FeedItemDto**: Data transfer object for API responses
- **FeedRepositoryImpl**: Repository implementation that handles data operations
- **ErrorMapper**: Maps HTTP exceptions to domain errors

### 2. Domain Layer
- **FeedItem**: Domain model representing a medical professional
- **FeedRepository**: Repository interface
- **GetFeedUseCase**: Use case for fetching the feed

### 3. Presentation Layer
- **FeedScreen**: Main screen displaying the list of professionals
- **DetailScreen**: Detailed view of a selected professional
- **FeedViewModel**: Manages UI state and business logic for FeedScreen
- **AppNavigation**: Navigation graph configuration

## Data Flow
This is the important thing in UDF (recommended by Google)

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

The app uses the next API for mock data:

- **Base URL**: `https://mocki.io/v1/`
- **Endpoint**: `5bb09ab0-8d6d-4d85-8284-b6a467299353`


### Avatar Generation

Avatars are dynamically generated using [DiceBear API](https://dicebear.com/):

- **Style**: `notionists`
- **Format**: PNG
- **Based on**: Professional's ID for consistent generation

## Project Structure


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

## Error Handling

The app handles various error scenarios:

- **NetworkError**: No internet connection
- **NotFoundError**: Resource not found (404)
- **ServerError**: Server-side errors (5xx)
- **UnknownError**: Unexpected errors

Each error displays an appropriate message with a retry option.
