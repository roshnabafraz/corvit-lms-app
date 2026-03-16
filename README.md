<br/>
<div align="center">
  <a href="#">
    <!-- Replace this with your actual logo -->
    <img src="https://via.placeholder.com/150x150?text=Corvit+Logo" alt="Logo" width="120" height="120">
  </a>

  <h3 align="center">Corvit LMS</h3>

  <p align="center">
    A comprehensive Learning Management System developed for Android using Jetpack Compose.
    <br/>
    <br/>
    <!-- Add useful badges here -->
    <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
    <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white"/>
    <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black"/>
  </p>
</div>

---

## 📖 About The Project

**Corvit LMS** is a modern Android application designed to provide a seamless digital learning experience. Developed entirely in **Kotlin** with a fully reactive UI built on **Jetpack Compose**, the app empowers students and educators with an intuitive, efficient, and interactive platform. 

It covers all primary aspects of a student's learning lifecycle: from browsing courses and enrolling, tracking attendance and assignments, processing fees, and even streaming relevant video content directly via YouTube integration.

### ✨ Key Features

* **🔐 Authentication:** Secure Login and Sign-up flow powered by Firebase Authentication, including Google Sign-in integration.
* **📚 Course Catalog:** Browse available courses, view detailed curriculum, and check available batches fetched dynamically via REST APIs.
* **🎓 User Dashboard:** A centralized hub to manage:
  * My Courses (Active Enrollments)
  * Daily Attendance & Track Record
  * Assignments & Submissions
  * Certifications Portfolio
  * Term Results & Exam Schedules (Date Sheet)
  * Weekly Timetable
* **💳 Fee Management:** Digital fee challan generation, viewing pending dues, and payment tracking.
* **🔔 Notifications:** Real-time updates and alerts for students.
* **🎥 Video Learning:** Integrated YouTube Player to stream course resources natively inside the app without switching contexts.
* **🌓 Theme Support:** Built-in dynamic Light and Dark mode toggling.

---

## 📱 Screenshots

> *Note: I will upload the actual UI screens later. Below are the functional views of the app.*

| Login Screen | Home Dashboard | Course Catalog | Course Details |
| :---: | :---: | :---: | :---: |
| <img src="https://via.placeholder.com/250x500?text=Login+Screen" width="200" alt="Login"/> | <img src="https://via.placeholder.com/250x500?text=Home+Dashboard" width="200" alt="Home"/> | <img src="https://via.placeholder.com/250x500?text=Course+Catalog" width="200" alt="Catalog"/> | <img src="https://via.placeholder.com/250x500?text=Course+Detail" width="200" alt="Detail"/> |

| Timetable View | Attendance Tracker | Payments & Challans | Profile / Settings |
| :---: | :---: | :---: | :---: |
| <img src="https://via.placeholder.com/250x500?text=Timetable" width="200" alt="Timetable"/> | <img src="https://via.placeholder.com/250x500?text=Attendance" width="200" alt="Attendance"/> | <img src="https://via.placeholder.com/250x500?text=Fee+Challan" width="200" alt="Challan"/> | <img src="https://via.placeholder.com/250x500?text=Profile" width="200" alt="Profile"/> |

---

## 🛠 Tech Stack & Libraries

This application is built leveraging modern Android development standards.

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Architecture:** MVVM (Model-View-ViewModel) logic flow and `MainNavGraph` for Compose Navigation
* **Network & API:** 
  * [Retrofit2](https://square.github.io/retrofit/) for REST API communication
  * Coroutines for robust asynchronous programming
* **Backend & Backend-as-a-Service:**
  * Firebase Authentication (Google Services Auth)
  * Firebase Firestore (NoSQL remote data sync)
* **Image Loading:** [Coil-kt](https://coil-kt.github.io/coil/) (Compose integration)
* **Local Storage:** AndroidX DataStore (Preferences) for local state and settings management
* **Media / Third-Party Integration:** [Android YouTube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player)

---

## ⚙️ Setup & Installation Instructions

Follow these instructions to run the application on your local machine.

### Prerequisites

* Android Studio (Latest Stable Version recommended)
* JDK 11 or higher
* Minimum Android SDK: API 24 (Nougat)
* Target Android SDK: API 36
* An active Firebase Project

### Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/corvit-lms.git
   ```

2. **Open the project:**
   Launch Android Studio, click on `Open` and navigate to the cloned project's root folder (`Corvit_LMS`).

3. **Configure Firebase:**
   * Go to the [Firebase Console](https://console.firebase.google.com/) and create/select your project.
   * Register a new Android App with the package name `com.corvit.corvit_lms`.
   * Download the `google-services.json` file.
   * Place the `google-services.json` file inside your `app/` directory.

4. **Sync Project:**
   Click on the **"Sync Project with Gradle Files"** button in Android Studio to download dependencies.

5. **Run the Application:**
   Select an Emulator or a physical device via USB Debugging, and push the "Run" (▶) button.

---

## 📂 Project Structure Snapshot

```text
com.corvit.corvit_lms
 ┣ data/             # Models and Repositories (Auth, Course Repos)
 ┣ navigation/       # Manage complete app routing via Compose NavGraph
 ┣ network/          # Retrofit Client and CorvitApi endpoints
 ┣ screens/          # All Jetpack Compose Views (Login, Dashboard, Catalog, etc.)
 ┣ ui.theme/         # Custom color, typography, and theme definitions
 ┣ viewmodel/        # MVVM implementations (AuthViewModel, CatalogViewModel)
 ┗ MainActivity.kt   # App Entry Point & Global State Setup
```

---

<div align="center">
  <i>If you find this project interesting, please give it a ⭐️!</i>
</div>
