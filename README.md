# News

News is a modern Android application built with Kotlin and Jetpack Compose that allows you to subscribe to news topics and stay updated with the latest articles. It features a clean, offline-first architecture, ensuring a smooth user experience.

## Features

*   **Topic Subscriptions:** Add and manage subscriptions to any news topic you are interested in.
*   **Personalized Feed:** View a consolidated feed of articles from all your subscribed topics, sorted by publication date.
*   **Offline Caching:** Articles and subscriptions are saved locally using Room, allowing for offline access to previously loaded news.
*   **Modern UI:** A clean and intuitive user interface built entirely with Jetpack Compose and Material 3.
*   **Manual Data Control:** Manually refresh the news feed for all subscriptions or clear the local article cache.

## Architecture

This project follows the principles of **Clean Architecture**, separating concerns into three main layers to create a scalable and maintainable codebase.

*   **Presentation Layer**: The UI layer built with Jetpack Compose. `ViewModel`s handle the UI state and user interactions, consuming data from the domain layer. State is managed reactively using Kotlin Flows.
*   **Domain Layer**: The core business logic of the application. It contains `UseCase`s for specific actions, data `Entity` objects, and `Repository` interfaces. This layer is independent of any framework, making the business logic portable and easy to test.
*   **Data Layer**: Implements the repository interfaces defined in the domain layer. It is responsible for fetching data from remote sources using `Retrofit` (from the NewsAPI) and caching it locally using the `Room` database.

## Tech Stack & Libraries

*   **Kotlin**: Primary programming language.
*   **Coroutines & Flow**: For asynchronous programming and reactive data streams.
*   **Jetpack Compose**: For building the native UI declaratively.
*   **Material 3**: For modern UI components and theming.
*   **Hilt**: For dependency injection.
*   **ViewModel**: To manage UI-related data and state.
*   **Retrofit**: For networking and consuming the REST API.
*   **Room**: For local database persistence (offline caching).
*   **Kotlinx.serialization**: For parsing JSON data from the API.
*   **Coil**: For asynchronous image loading.

## Setup

To build and run this project, you will need an API key from [newsapi.org](https://newsapi.org).

1.  Clone the repository:
    ```bash
    git clone https://github.com/r4geghost/News.git
    ```
2.  Navigate to the project directory.
3.  Create a file named `keystore.properties` in the root directory of the project.
4.  Add your News API key to the `keystore.properties` file as follows:
    ```properties
    NEWS_API_KEY="YOUR_API_KEY_HERE"
    ```
5.  Open the project in Android Studio, sync Gradle, and run the `app` module on an emulator or a physical device.
