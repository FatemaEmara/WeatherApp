# 🌤️ Weather Forecast App

An Android mobile application that displays real-time weather status and temperature based on your current location or any location you choose.

---


## 📋 Features

- 🌍 **Current Location Weather** – Automatically fetch weather based on GPS
- 🗺️ **Map Location Picker** – Drop a pin on any location to get its forecast
- 🔍 **City Search** – Auto-complete search to find any city instantly
- ⭐ **Favorites** – Save and manage multiple favorite locations
- 🔔 **Weather Alerts** – Set custom alerts for rain, wind, fog, snow, extreme temperatures, and more
- 🌐 **Multi-language** – Supports Arabic and English
- 🌡️ **Unit Customization** – Choose between Kelvin, Celsius, and Fahrenheit

---

## 🖼️ Screens

### 🏠 Home Screen
Displays full current weather details:
- Current temperature, date & time
- Humidity, wind speed, pressure, clouds
- City name & weather icon
- Weather description (e.g. clear sky, light rain)
- Hourly forecast for the current day
- 5-day forecast

### ⚙️ Settings Screen
- **Location**: GPS auto-detection or manual map selection
- **Temperature Units**: Kelvin / Celsius / Fahrenheit
- **Wind Speed Units**: meter/sec or miles/hour
- **Language**: Arabic / English

### ⭐ Favorites Screen
- List of all saved favorite locations
- Tap any location to view its full forecast
- FAB button to add a new favorite via map or search
- Swipe to remove a saved location

### 🔔 Weather Alerts Screen
- Add weather alerts with:
  - Active duration (start & end time)
  - Alert type: notification or alarm sound
  - Option to dismiss/stop the alert

---

## 🛠️ Technologies & Tools

- Kotlin
- Jetpack Compose
- MVVM Architecture
- Retrofit
- Room Database
- Coroutines
- WorkManager
- AlarmManager
- OSM for map
- Location Services (GPS)
- Unit Testing


---

## 🌐 API

This app uses the **OpenWeatherMap API**:
```
https://api.openweathermap.org/data/2.5/forecast
```

📄 [OpenWeatherMap Documentation](https://openweathermap.org/api)

