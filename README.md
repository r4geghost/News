# News Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API_24%2B-blue.svg?logo=android)](https://developer.android.com/studio/releases/platforms)

A modern Android news aggregator built with Kotlin and Jetpack Compose, implementing Clean Architecture to deliver personalized news feeds with offline-first capabilities.

## Key Features

- **Topic-Based News Subscriptions**: Subscribe to specific news topics with real-time filtering
- **Offline-First Architecture**: Room database caching with background WorkManager synchronization
- **Modern Jetpack Compose UI**: Material 3 design with card layouts, pull-to-refresh, search functionality, and fluid transitions
- **Reactive State Management**: ViewModel with StateFlow and sealed interfaces for type-safe UI commands
- **Configurable Background Refresh**: Customizable update intervals (15 minutes to 24 hours) with Wi-Fi-only options
- **Multi-Language Support**: News search in English, Russian, French, and German
- **Push Notifications**: Optional notifications for new articles (with permission handling)
- **Article Sharing & Reading**: Direct links to full articles with native sharing functionality
- **Image Loading & Caching**: Coil integration for efficient image loading with network caching
- **Dependency Injection**: Hilt for modular, testable code with repository pattern implementation

## Architecture

The app follows Clean Architecture principles with three main layers:

- **Presentation Layer**: Jetpack Compose UI components with ViewModels implementing MVVM pattern
- **Domain Layer**: Business logic with Use Cases and Repository interfaces
- **Data Layer**: Room database implementation with Repository concrete classes

### Layer Structure
```
app/src/main/java/com/dyusov/news/
├── data/              # Data layer (Room DAO, Repositories, API clients, Workers)
├── domain/            # Domain layer (Business logic)
└── presentation/      # Presentation layer (UI)
```

## Tech Stack

- **Programming Language**: Kotlin 2.2.21
- **Architecture Pattern**: Clean Architecture with MVVM
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room (SQLite)
- **Dependency Injection**: Hilt
- **Async Programming**: Kotlin Coroutines and Flow
- **Networking**: Retrofit with Kotlinx Serialization
- **Image Loading**: Coil
- **Background Tasks**: Work Manager
- **Preferences Storage**: DataStore
- **Build Tool**: Gradle with Kotlin DSL
- **Minimum Android SDK**: API 24 (Android 7.0)

## How to Build and Run

### Prerequisites
- **Android Studio**: Iguana or later
- **JDK**: 11 or later
- **NewsAPI Account**: Get API key from [NewsAPI.org](https://newsapi.org/)

### Setup Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/meekieD/News.git
   cd News
   ```

2. **Configure API Key**: Create `keystore.properties` in the project root:
   ```
   NEWS_API_KEY="your_api_key_here"
   ```

3. **Open the project in Android Studio and sync Gradle files**

4. **Build the Project**:
   ```bash
   ./gradlew build
   ```

5. **Install Debug Build**:
   ```bash
   ./gradlew installDebug
   ```

6. **Assemble Release APK** (optional):
   ```bash
   ./gradlew assembleRelease
   ```

### Running on Device/Emulator
- Open in Android Studio and select "Run" → "Run 'app'"
- Or use the install command above
- The app will request internet and notification permissions on first run

## Additional Information

### Permissions Required
- `android.permission.INTERNET`: For fetching news articles
- `android.permission.POST_NOTIFICATIONS`: For push notifications (Android 13+)

### Background Processing
- **WorkManager** handles periodic article updates
- **Constraints**: Battery optimization and network type checking
- **Notifications**: Only shown when new articles are found and notifications enabled

### Data Persistence
- **Room Database**: SQLite with entities for articles and subscriptions
- **DataStore**: Preferences for user settings
- **Foreign Keys**: Cascade deletion ensures data integrity

### Performance Optimizations
- **Distinct Flow**: Prevents unnecessary UI updates
- **Async Operations**: Parallel article fetching for multiple topics
- **Image Caching**: Coil handles memory and disk caching
- **Lazy Loading**: Compose LazyColumn for efficient list rendering