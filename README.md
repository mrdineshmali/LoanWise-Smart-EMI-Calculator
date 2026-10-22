# 📊 LoanWise: Smart EMI Calculator

[![Android](https://img.shields.io/badge/Platform-Android-green.svg?logo=android)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Design-Material%203-indigo.svg)](https://m3.material.io)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**LoanWise** is a modern, high-performance Android financial calculator built with **Kotlin**, **Jetpack Compose**, **Room DB**, and **Material 3**. It provides instant reducing-balance EMI calculations, complete amortization schedules, side-by-side loan offer comparisons, and offline calculation persistence.

---

## 📥 Download Release APK

You can download the latest pre-built APK directly from GitHub:

👉 **[Download LoanWise v1.0.0 APK](https://github.com/mrdineshmali/LoanWise-Smart-EMI-Calculator/raw/main/release/LoanWise-v1.0.0.apk)**

---

## ✨ Key Features

- 💡 **Instant Reducing-Balance EMI Calculation**: Accurate monthly EMI, total interest, and total payout metrics calculated in real time.
- ⚡ **120Hz Ultra-Smooth Sliders & Quick Chips**: Drag sliders or tap 1-tap quick preset chips (`₹1L`, `₹5L`, `₹10L`, `1 Yr`, `3 Yrs`, `5 Yrs`) for lightning-fast inputs.
- 📈 **Interactive Donut Breakdown Chart**: Visual representation of Principal vs. Interest percentage ratios.
- 📅 **Complete Amortization Schedule**: Detailed month-by-month principal, interest, and remaining balance breakdown table.
- 🏷️ **Side-by-Side Loan Comparison**: Compare two loan offers simultaneously with automatic total savings calculation.
- 📂 **Offline Room Database**: Save, rename, share, and delete loan estimates offline.
- 🌗 **Light & Dark Mode**: Modern adaptive theme powered by Jetpack Compose Styles & Material 3.
- 📱 **Native Sharing & Copying**: Export professional EMI summaries to WhatsApp, Email, or Clipboard with 1 tap.

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org) (100%)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) + [Material Design 3](https://m3.material.io)
- **Architecture**: Modern Android Architecture (MVVM + Clean Domain Layer + Repository Pattern)
- **State Management**: Kotlin `StateFlow` & `viewModelScope`
- **Database**: [Room DB](https://developer.android.com/training/data-storage/room) with KSP annotation processing
- **Async & Reactive**: Kotlin Coroutines & Flow
- **Navigation**: Jetpack Navigation Compose with safe backstack handling
- **Monetization**: Google Mobile Ads (AdMob) SDK with adaptive banner layout support

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2.1+) or newer
- JDK 17
- Android SDK 35 (Min SDK 24 / Android 7.0+)

### Building from Source

1. **Clone the repository**:
   ```bash
   git clone https://github.com/mrdineshmali/LoanWise-Smart-EMI-Calculator.git
   cd LoanWise-Smart-EMI-Calculator
   ```

2. **Open in Android Studio**:
   Open Android Studio, select **Open**, and navigate to the cloned folder.

3. **Build & Run**:
   Run the app on an Android device or emulator (`Shift + F10`) or via CLI:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

## 📁 Project Structure

```
LoanWise/
├── app/
│   ├── src/main/java/com/dineshmali/smartemicalculator/
│   │   ├── data/            # Local Room DB, Entities, DAOs & Repository
│   │   ├── domain/          # EMI Calculation Logic & Models
│   │   ├── navigation/      # Jetpack Navigation Graph & Screen Routes
│   │   ├── ui/
│   │   │   ├── components/  # DonutChart, EmiSummaryCard, InputFields, AmortizationTable
│   │   │   ├── screens/     # Splash, Home, Calculator, Compare, Saved, Settings, DeveloperDetails
│   │   │   └── theme/       # Material 3 Color, Shape, Theme & Type definitions
│   │   ├── utils/           # Currency Formatter, Share Utilities, AdMob Config
│   │   └── viewmodel/       # MVVM StateHolders & ViewModel Factories
```

---

## 👨‍💻 Developer & Credits

Designed and developed by **Dinesh Mali**.

- 🌐 **Portfolio**: [dineshmali.in](https://dineshmali.in)
- 🐙 **GitHub**: [@mrdineshmali](https://github.com/mrdineshmali)

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
