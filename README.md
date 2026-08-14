# MMAZone - UFC Live Results & News App

MMAZone is a native Android application developed entirely with **Kotlin** and **Jetpack Compose**. Its goal is to provide Mixed Martial Arts fans with a centralized hub featuring real-time news, detailed fighter profiles, and results for past and upcoming events.

## Key Features
* **Dynamic Dashboard:** State-driven interface displaying the next major event, top news, and quick access links.
* **Athlete Profiles (CitoAPI):** Search and display live records, weight divisions, physical statistics, and a chronological fight history with duplicate filtering.
* **Real-Time News (NewsAPI):** Consumption of external articles with secure redirection to the native browser.
* **Event History (TheSportsDB):** Complex data parsing algorithm to extract results, winners, and finish methods from past events.
* **Anti-Spoiler Mode:** Visual protection system to prevent revealing fight results until the user chooses to interact with the UI elements.

## Tech Stack & Architecture
The project follows modern Google development standards and Clean Architecture principles:
* **UI:** Jetpack Compose (100% Declarative).
* **Architecture:** MVVM (Model-View-ViewModel) coupled with `StateFlow` and `LaunchedEffect` for reactive and safe lifecycle management.
* **Networking:** `Retrofit2` and `OkHttp3` (with custom interceptors for API Key injection).
* **Asynchrony:** Kotlin Coroutines (`viewModelScope`).
* **Images:** `Coil` for asynchronous image loading and network caching.
* **Navigation:** Jetpack Navigation Compose with safe argument passing (Slugs and URLEncoding).
