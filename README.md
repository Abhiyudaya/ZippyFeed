# ZippyFeed 🍔

**Your favorite foods delivered fast at your door.**

ZippyFeed is a modern Android food delivery application built with Jetpack Compose and Kotlin. The app provides a seamless experience for users to browse restaurants, explore food items, and place orders with fast delivery.

## ✨ Features

- **User Authentication**: Sign up and sign in with email, Google, or Facebook
- **Restaurant Browse**: Explore various restaurants and their offerings
- **Food Discovery**: Browse food items by categories
- **Restaurant Details**: View detailed information about restaurants
- **Modern UI**: Built with Jetpack Compose for a smooth, native Android experience
- **Social Login**: Integrated Google and Facebook authentication
- **Fast Performance**: Optimized for quick loading and responsive interactions

## 🏗️ Architecture

- **MVVM Pattern**: Clean architecture with ViewModels and UI separation
- **Jetpack Compose**: Modern declarative UI toolkit
- **Hilt Dependency Injection**: For clean dependency management
- **Retrofit**: REST API communication
- **Navigation Compose**: Type-safe navigation between screens
- **Coroutines**: Asynchronous operations and state management

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Authentication**: Google Sign-In, Facebook Login
- **Image Loading**: Coil
- **Navigation**: Navigation Compose
- **State Management**: Compose State + ViewModels

## 📱 Screens

- **Authentication Flow**: Welcome, Sign Up, Sign In screens
- **Home Screen**: Main dashboard with restaurant listings
- **Restaurant Details**: Detailed view of restaurant information
- **Food Item Details**: Individual food item information

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 8 or higher
- Android SDK 24+ (Android 7.0)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ZippyFeed.git
   ```

2. Open the project in Android Studio

3. Configure your API keys:
   - Add your Google OAuth client ID
   - Configure Facebook App ID and Client Token in `strings.xml`

4. Build and run the project:
   ```bash
   ./gradlew assembleDebug
   ```

## 📦 Build Configuration

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 35

## 🔧 Dependencies

### Core Dependencies
- AndroidX Core KTX
- Jetpack Compose BOM
- Material 3
- Activity Compose
- Lifecycle Runtime KTX

### Architecture & DI
- Hilt Android
- Navigation Compose
- Lifecycle Runtime Compose

### Networking
- Retrofit
- Gson Converter
- OkHttp Logging Interceptor

### Authentication
- Google Identity
- Facebook Login SDK
- AndroidX Credentials

### UI & Image Loading
- Coil (Image loading)
- Accompanist System UI Controller
- Core Splash Screen

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

For any questions or support, please reach out to the development team.

---

**ZippyFeed** - Fast food delivery at your fingertips! 🚀