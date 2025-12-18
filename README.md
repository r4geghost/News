# News Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API_24%2B-blue.svg?logo=android)](https://developer.android.com/studio/releases/platforms)

A modern Android news aggregator built with Kotlin and Jetpack Compose, implementing Clean Architecture to deliver personalized news feeds with offline-first capabilities.

## Key Features

- **Reactive State Management**: Implemented ViewModel with StateFlow and sealed interfaces for type-safe UI commands, ensuring unidirectional data flow and predictable state updates
- **Offline-First Architecture**: Utilizes Room database with structured concurrency for background data synchronization, allowing users to access cached articles without network dependency
- **Dependency Injection with Hilt**: Configured modular dependency injection for testable code structure, enabling seamless repository pattern implementation across domain and data layers
- **Asynchronous Data Processing**: Leverages Kotlin Coroutines and Flow for reactive streams, handling API calls with proper error boundaries and cancellation support
- **Material 3 UI Components**: Built entirely with Jetpack Compose and Material 3, featuring custom theming and responsive layouts for modern Android design standards

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │    │     Domain      │    │      Data       │
│   Layer         │◄──►│   Layer         │◄──►│   Layer         │
│                 │    │                 │    │                 │
│ • ViewModels    │    │ • Entities      │    │ • Repositories  │
│ • UI Screens    │    │ • Use Cases     │    │ • API Services  │
│ • State Mgmt    │    │ • Repositories  │    │ • Local DB      │
│                 │    │                 │    │ • Mappers       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

This Clean Architecture implementation ensures separation of concerns with the Presentation layer handling UI logic via ViewModels and Compose screens, the Domain layer containing business rules through Use Cases and Repository interfaces, and the Data layer managing external data sources with Room for local persistence and Retrofit for remote API integration.

## Technical Stack

- **Kotlin 2.2.21**: Chosen for its null safety, concise syntax, and first-class coroutine support, enabling robust asynchronous programming
- **Jetpack Compose 2025.12.00**: Selected for declarative UI development, providing modern Android UI patterns with better testability and performance over traditional View system
- **Hilt 2.57.2**: Implemented for compile-time dependency injection, reducing boilerplate and ensuring proper object lifecycle management across the application
- **Room 2.8.4**: Used for local data persistence with type-safe SQL queries, supporting offline caching and complex relationships between news articles and subscriptions
- **Retrofit 3.0.0 with Kotlinx Serialization**: Employed for type-safe REST API communication, chosen for its annotation-based request configuration and seamless JSON parsing
- **Kotlin Coroutines & Flow**: Utilized for reactive programming, enabling efficient background processing and real-time UI updates with proper error handling

## How to Build and Run

1. **Prerequisites**: Android Studio Iguana or later, JDK 11, and a NewsAPI.org account for API access

2. **Clone the Repository**:

   ```bash
   git clone https://github.com/meekieD/News.git
   cd News
   ```

3. **Configure API Key**: Create `keystore.properties` in the project root:
   ```
   NEWS_API_KEY="your_api_key_here"
   ```

4. **Build the Project**:

   ```bash
   ./gradlew build
   ```

5. **Run Unit Tests**:

   ```bash
   ./gradlew test
   ```

6. **Run on Device/Emulator**: Open in Android Studio and select Run > Run 'app' or use:

   ```bash
   ./gradlew installDebug
   ```

7. **Assemble Release APK** (optional):

   ```bash
   ./gradlew assembleRelease
   ```
