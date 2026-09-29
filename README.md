<div align="center">

# 🥊 MMAZone
### UFC Live Results & News for Android

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Declarative%20UI-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-orange?style=for-the-badge)](https://developer.android.com/topic/architecture)
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)

A modern, native Android application built with **Kotlin** and **Jetpack Compose** designed for Mixed Martial Arts fans. Centralizes live news, detailed athlete records, and fight event schedules with built-in spoiler protection.

</div>

---

## 📱 App Preview

| Dynamic Dashboard | Athlete Profile | Real-Time News Feed |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/b96e58ae-5243-4921-88a9-d086b9c8bf66" width="260" alt="Dashboard Screen"/> | <img src="https://github.com/user-attachments/assets/ee0db7cc-8494-4a1a-b86a-61515f18bc0f" width="260" alt="Fighter Profile Screen"/> | <img src="https://github.com/user-attachments/assets/40974184-d2cb-4496-a185-45ae2d5086a5" width="260" alt="News Preview Card"/> |

---

## ⚡ Key Features

* **Dynamic Event Dashboard:** State-driven interface presenting upcoming major cards, breaking stories, and quick-access shortcuts.
* **Athlete Records & Profiles:** Searchable fighter database with win/loss/draw records, weight classes, physical attributes, and deduplicated chronological fight logs.
* **Curated News Feed:** Live aggregation of global MMA headlines with deep links to original articles via custom browser tabs.
* **Historical Event Data:** Robust parser mapping card results, method of finish (KO/TKO, Sub, Dec), and winning rounds.
* **🛡️ Anti-Spoiler Mode:** Interactive obfuscation layer concealing event outcomes until explicitly revealed by the user.

---

## 🏗️ Architecture & Tech Stack

This project follows Google's recommended **Clean Architecture** patterns alongside an **MVVM (Model-View-ViewModel)** unidirectional data flow (UDF).

| Category | Technologies / Libraries |
|---|---|
| **Language & UI** | Kotlin, Jetpack Compose, Material 3 Design |
| **Architecture** | MVVM, Clean Architecture, StateFlow, Coroutines |
| **Networking** | Retrofit2, OkHttp3, Interceptors (Auth / API Key injection) |
| **Image Loading** | Coil (Network caching & memory management) |
| **Navigation** | Navigation Compose with type-safe arguments and URL encoding |
| **Data Serialization** | Kotlinx Serialization / Gson |

---

## 🌐 External APIs & Data Sources

| Provider | Purpose | Integration Details |
|---|---|---|
| **CitoAPI** | Fighter Profiles & Records | Physical stats, divisions, fight history |
| **NewsAPI** | Real-Time MMA News | Article headlines, summaries, sources |
| **TheSportsDB** | Event Schedules & Results | Historic cards, match outcomes, finishes |

---

## 🚀 Getting Started

### Prerequisites
* Android Studio Ladybug (or newer)
* JDK 17+
* Android SDK with Min SDK 24+

### Setup & Run
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/your-username/MMAZone.git](https://github.com/your-username/MMAZone.git)
   cd MMAZone
   ```

2. **Add API Keys:**
   Create a `secrets.properties` or `local.properties` file in your root folder and add your credentials:
   ```properties
   NEWS_API_KEY="your_news_api_key_here"
   CITO_API_KEY="your_cito_api_key_here"
   ```

3. **Build & Install:**
   Open the project in Android Studio, sync Gradle, and deploy to an emulator or physical device via the `Run` button (`Shift + F10`).

---
