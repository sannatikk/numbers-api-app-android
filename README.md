# NerdyFactsApp 🧠✨

A simple Android app that fetches random trivia, math, and year facts from an API and allows users to save their favorite facts. Built with Jetpack Compose, Room, and Retrofit.

## 📌 Features
✅ Fetch random number, math, and year facts  
✅ Save facts to a local Room database  
✅ Filter saved facts by category  
✅ Delete saved facts  
✅ Smooth Jetpack Compose UI with navigation  
✅ Snackbar notifications for saved/deleted facts  

## 🛠️ Tech Stack
 •	Kotlin + Jetpack Compose – Modern UI framework  
 •	Room Database – Local storage  
 •	Retrofit – Fetch facts from API  
 •	StateFlow + ViewModel – Reactive state management  
 •	Navigation Component – Multi-screen navigation  

## 🚀 Setup & Installation
1.	Clone the repository:  
    `git clone https://github.com/sannatikk/numbers-api-app-android.git`  
    `cd NerdyFactsApp`  
2.	Open in Android Studio  
3.	Sync Gradle & Run the app on an Android Emulator or Device  

## 🔧 Required Configurations
•	Since the API only supports HTTP, add this to AndroidManifest.xml:  
`<application
    android:usesCleartextTraffic="true">`  
•	Ensure you have Internet permission:  
`<uses-permission android:name="android.permission.INTERNET"/>` 