# Prana: AI-Powered AR First-Aid Assistant

**Prana** is an AI-powered, offline-first Android first-aid assistant designed to help users respond to medical emergencies quickly, especially in low-literacy and low-connectivity environments.

## Overview

The app uses the phone's camera to identify common injuries such as cuts, burns, fractures, choking incidents, and CPR situations through on-device TensorFlow Lite models. Once an injury is recognized, Prana launches an augmented reality (AR) guidance mode that overlays animated visual instructions directly onto the real-world scene, showing users exactly where and how to perform each first-aid step. AI-powered voice guidance in both English and Hindi narrates every instruction, allowing users to follow along without relying on text.

To make the experience hands-free, Prana supports gesture-based interactions using MediaPipe and OpenCV, enabling users to move between steps, replay instructions, or pause guidance without touching the screen. All essential first-aid content is stored locally using an offline-first architecture with Room Database, while Firebase Realtime Database synchronizes user data, settings, and updated content automatically whenever an internet connection becomes available.

## Key Features

- **AI Injury Identification**: On-device TensorFlow Lite models to recognize common medical emergencies.
- **AR Guidance Mode**: Real-time visual overlays showing exactly how to perform first-aid steps.
- **Multilingual Voice Guidance**: Instructions in English and Hindi for hands-free assistance.
- **Gesture Control**: Hands-free navigation using MediaPipe and OpenCV (e.g., thumbs up for next step).
- **Offline-First Architecture**: All essential content and medical profiles stored locally using Room Database.
- **Cloud Sync**: Firebase Realtime Database for synchronizing user data and settings.
- **Learning Hub**: Downloadable first-aid videos for proactive learning.
- **Personal Medical Profile**: Secure storage for allergies, medications, blood group, and emergency contacts.
- **One-Tap Emergency**: Quick access to call ambulances or emergency contacts.

## Tech Stack

- **Platform**: Android (Kotlin)
- **UI**: Jetpack Compose
- **Database**: Room (Local), Firebase Realtime Database (Cloud Sync)
- **AI/ML**: TensorFlow Lite, MediaPipe, Gemini (Dynamic Analysis)
- **Computer Vision**: OpenCV
- **Architecture**: MVVM with Repository Pattern

## Getting Started

### Prerequisites

- [Android Studio Arctic Fox](https://developer.android.com/studio) or newer.
- Android device with ARCore support (recommended for AR features).
- Gemini API Key (for dynamic analysis features).

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/prana.git
   cd prana
   ```

2. **Set up API Keys:**
   - Create a file named `.env` in the project directory.
   - Add your Gemini API key:
     ```env
     GEMINI_API_KEY=your_actual_key_here
     ```

3. **Open in Android Studio:**
   - Open Android Studio, select **Open**, and choose the `prana` folder.
   - Let Gradle sync and download dependencies.

4. **Run the App:**
   - Connect your Android device or start an emulator.
   - Click **Run** in Android Studio.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
