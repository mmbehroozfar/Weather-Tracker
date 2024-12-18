# Weather Tracker

## Overview

This project is a weather tracking app built using **Kotlin**, **Jetpack Compose**, and **Clean
Architecture**. It demonstrates the use of **Model-View-Intent (MVI)** architecture and integrates
data from [WeatherAPI.com](https://www.weatherapi.com/docs/). The app allows users to search for a
city, display its weather on the home screen, and persist the selected city across app launches.

## Features

1. **Home Screen**:
    - Displays the weather for a saved city, including:
        - City name.
        - Temperature.
        - Weather condition (with corresponding icon).
        - Humidity (%).
        - UV index.
        - "Feels like" temperature.
    - Prompts the user to search if no city is saved.
2. **Search Functionality**:
    - Allows users to search for cities.
    - Displays a search result card for queried cities.
    - Updates the Home Screen with the selected city and persists the choice.
3. **Local Storage**:
    - Saves the selected city using **DataStore** to persist data across app launches.
      4**Unit test**:
    - Implemented unit tests for HomeViewModel to show testability of the code.

---

## Architecture

The app is implemented using **Clean Architecture** and **MVI**, ensuring modular, testable, and
scalable code. Key components include:

- **Domain Layer**:
    - Contains business logic and use cases.
- **Data Layer**:
    - Responsible for interacting with the WeatherAPI and local storage.
- **Presentation Layer**:
    - Composed of Jetpack Compose screens and ViewModels.

Dependency injection is handled using **Hilt**, and abstractions are enforced through interfaces.

---

## Requirements

To run the app, ensure the following:

1. Add your WeatherAPI key to the `local.properties` file:
   ```
   API_KEY=YOUR_API_KEY
   ```
2. The app requires an active internet connection to fetch weather data.

---

## Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Clean Architecture**
- **MVI**
- **DataStore** for local storage
- **Hilt** for dependency injection
- **WeatherAPI.com** for weather data
