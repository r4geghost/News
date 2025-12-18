# News Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API_24%2B-blue.svg?logo=android)](https://developer.android.com/studio/releases/platforms)

A modern Android news aggregator built with Kotlin and Jetpack Compose, implementing Clean Architecture to deliver personalized news feeds with offline-first capabilities.

## Key Features

- **Reactive State Management**: ViewModel with StateFlow and sealed interfaces for type-safe UI commands
- **Offline-First Architecture**: Room database with background synchronization for cached articles access
- **Dependency Injection with Hilt**: Modular DI for testable code and repository pattern implementation
- **Asynchronous Data Processing**: Kotlin Coroutines and Flow for reactive streams with error handling
- **Material 3 UI Components**: Jetpack Compose with custom theming and responsive layouts

## Architecture

The app follows Clean Architecture principles with three main layers:

- **Presentation Layer**: Jetpack Compose UI components with ViewModels implementing MVVM pattern
- **Domain Layer**: Business logic with Use Cases and Repository interfaces
- **Data Layer**: Room database implementation with Repository concrete classes

```
app/
├── data/          # Data layer (Room, DAO, Repository impl)
├── domain/        # Domain layer (Use Cases, Repository interfaces)
└── presentation/  # Presentation layer (Compose UI, ViewModels)
```

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