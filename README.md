Travel Planning Application
Description
This is a travel planning application developed for a midterm evaluation, featuring a mobile frontend for creating and managing trip itineraries and a backend for data persistence. The app allows users to select cities, plan activities, and save trip details.
Features

City search and selection screen with famous places in India.
Trip itinerary screen with customizable activities, meals, and notes.
Data persistence for trips, activities, and meals in a MySQL database.
Navigation between screens using Decompose.
Backend API for saving trip data, testable via Postman.

Tech Stack

Frontend: Kotlin Multiplatform with Jetpack Compose, Decompose, Ktor.
Backend: Spring Boot with JPA, MySQL.
Tools: Android Studio, Postman, MySQL Workbench.

Installation and Setup

Clone the Repository:git clone https://github.com/your-username/travel-app.git
cd travel-app


Backend Setup:
Install MySQL and create a database named travel_db.
Update src/main/resources/application.properties with your MySQL credentials.
Run the Spring Boot app: ./gradlew bootRun or use IDE Run configuration.


Frontend Setup:
Open in Android Studio and sync the project.
Update NetworkService.kt with the backend URL (e.g., http://10.0.2.2:8080/api/trips for emulator).
Build and run on an Android emulator or device.



Usage

Launch the app and search for a city (e.g., Goa, Jaipur).
Proceed to the trip itinerary screen to add activities and notes.
Save the trip via the "Proceed to Booking" button.
Test the backend API with Postman:
URL: http://localhost:8080/api/trips
Method: POST
Headers: X-User-Id: user123, Content-Type: application/json
Body: See sample JSON in the code comments.



Current Status and Challenges

Progress: Successfully implemented city search, trip itinerary, and backend data persistence.
Resolved Issues: Fixed JSON parse errors by adjusting flightId to String and resolved class resolution errors in the frontend.
Ongoing Challenges: Connection timeouts due to network configuration (e.g., emulator to backend IP). Partial data model alignment (e.g., flightId as string vs. numeric ID).

Next Steps

Resolve connection timeouts by standardizing the backend URL.
Enhance FlightDTO and AccommodationDTO with numeric id fields.
Develop the TripConfirmationScreen for post-booking feedback.
Add user authentication with Firebase.

Contributing
Contributions are welcome! Please fork the repository and submit pull requests.
License
[MIT License] (Placeholder - replace with actual license if chosen)
