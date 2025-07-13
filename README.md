# 🎯 Trip Recommendation System

A comprehensive ML-based recommendation system that provides personalized post-trip recommendations including similar destinations, better hotels, missed activities, packing tips, and personalized deals.

## 🚀 Features

### ✅ Core Recommendation Types
- **Similar Destinations**: Based on travel style and preferences
- **Better Hotels**: Higher-rated accommodations in user's budget range
- **Missed Activities**: Popular activities user hasn't tried yet
- **Packing Tips**: Personalized based on season and travel style
- **Deals & Offers**: Personalized discounts and loyalty rewards

### 🤖 ML Algorithms Used
- **Content-Based Filtering**: User profile matching
- **Collaborative Filtering**: SVD-based similarity
- **Clustering**: KMeans for user segmentation
- **Feature Engineering**: One-hot encoding and scaling

## 📋 Prerequisites

- Python 3.8+
- MySQL 8.0+
- pip (Python package manager)

## 🛠️ Installation

### 1. Clone and Setup
```bash
# Install dependencies
pip install -r requirements.txt

# Create models directory
mkdir models
```

### 2. Database Setup
```bash
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE travel_db;
USE travel_db;

# Run the schema
source database_schema.sql;
```

### 3. Environment Configuration
Create a `.env` file:
```env
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=your_password
DB_NAME=travel_db
API_PORT=5000
```

## 🎯 Usage

### 1. Train Models
```bash
# Train the ML models
python one.py
```

### 2. Start API Server
```bash
# Start the Flask API
python api_endpoints.py
```

### 3. API Endpoints

#### Health Check
```bash
GET /api/health
```

#### Trip Completion
```bash
POST /api/trip-completed
Content-Type: application/json

{
  "user_id": 1,
  "trip_data": {
    "trip_name": "Paris Adventure",
    "cities_visited": "Paris",
    "activities_done": "Eiffel Tower Visit,Louvre Museum",
    "hotels_stayed": "Hotel Ritz Paris",
    "budget_range": "medium",
    "travel_style": "leisure",
    "rating": 4.5,
    "trip_duration": 5,
    "season": "spring"
  }
}
```

#### Get Recommendations
```bash
GET /api/recommendations/1
```

#### Submit Feedback
```bash
POST /api/recommendations/1/feedback
Content-Type: application/json

{
  "recommendation_id": 1,
  "feedback": "like"
}
```

#### Get Personalized Deals
```bash
GET /api/deals/1
```

#### Get Packing Tips
```bash
GET /api/packing-tips/1
```

## 🧠 ML Architecture

### 1. Data Preprocessing
- **Feature Engineering**: Convert categorical data to numerical
- **Scaling**: Standardize numerical features
- **Encoding**: One-hot encode categorical variables

### 2. Recommendation Algorithms

#### Content-Based Filtering
```python
# User profile creation
user_profile = {
    'preferred_cities': ['Paris', 'London'],
    'preferred_activities': ['Museum', 'Shopping'],
    'travel_style': 'leisure',
    'budget_range': 'medium'
}
```

#### Collaborative Filtering
```python
# SVD for dimensionality reduction
svd = TruncatedSVD(n_components=50)
user_matrix_reduced = svd.fit_transform(user_data_matrix)
similarity_matrix = cosine_similarity(user_matrix_reduced)
```

#### Clustering
```python
# KMeans clustering
kmeans = KMeans(n_clusters=5)
user_clusters = kmeans.fit_predict(user_data_matrix)
```

### 3. Recommendation Generation

#### Similar Destinations
```python
# Find cities with similar characteristics
similar_cities = destination_cities[
    (destination_cities['travel_style'] == user_travel_style) &
    (~destination_cities['city'].isin(user_visited_cities))
]
```

#### Better Hotels
```python
# Find higher-rated hotels in budget range
better_hotels = hotels_data[
    (hotels_data['rating'] > 4.0) &
    (hotels_data['price_range'] == user_budget)
]
```

## 📊 Database Schema

### Core Tables
- `user_trip_history`: Store completed trips
- `destination_cities`: Available destinations
- `hotels`: Hotel information
- `activities`: Activity catalog
- `user_recommendations`: Generated recommendations
- `deals_offers`: Available deals
- `user_preferences`: User preferences

### Sample Data
The system comes with sample data for:
- 8 destination cities (Paris, Tokyo, New York, etc.)
- 5 hotels with ratings
- 6 popular activities
- 4 sample user trips

## 🔄 Integration with Mobile App

### 1. Trip Completion Flow
```kotlin
// When trip is completed
val tripData = TripData(
    tripName = "Paris Adventure",
    citiesVisited = "Paris",
    activitiesDone = "Eiffel Tower Visit,Louvre Museum",
    hotelsStayed = "Hotel Ritz Paris",
    budgetRange = "medium",
    travelStyle = "leisure",
    rating = 4.5f,
    tripDuration = 5,
    season = "spring"
)

apiService.completeTrip(userId, tripData)
```

### 2. Recommendation Display
```kotlin
// Get recommendations
apiService.getRecommendations(userId) { recommendations ->
    // Display in Jetpack Compose
    LazyColumn {
        item { SimilarDestinationsCard(recommendations.similarDestinations) }
        item { BetterHotelsCard(recommendations.betterHotels) }
        item { MissedActivitiesCard(recommendations.missedActivities) }
        item { PackingTipsCard(recommendations.packingTips) }
        item { DealsCard(recommendations.dealsOffers) }
    }
}
```

## 🎨 UI Components (Jetpack Compose)

### Recommendation Cards
```kotlin
@Composable
fun SimilarDestinationsCard(destinations: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Similar Destinations",
                style = MaterialTheme.typography.h6
            )
            destinations.forEach { destination ->
                Text(text = "• $destination")
            }
        }
    }
}
```

## 🔧 Configuration

### Model Parameters
```python
# In one.py
KMEANS_CLUSTERS = 5
SVD_COMPONENTS = 50
SIMILARITY_THRESHOLD = 0.7
```

### API Configuration
```python
# In api_endpoints.py
app.run(debug=True, host='0.0.0.0', port=5000)
```

## 📈 Performance Optimization

### 1. Database Indexing
```sql
CREATE INDEX idx_user_trip_history_user_id ON user_trip_history(user_id);
CREATE INDEX idx_destination_cities_travel_style ON destination_cities(travel_style);
```

### 2. Model Caching
```python
# Save trained models
joblib.dump(model, 'models/kmeans.pkl')

# Load models for inference
model = joblib.load('models/kmeans.pkl')
```

### 3. API Response Caching
```python
# Cache recommendations for 1 hour
@cache.memoize(timeout=3600)
def get_recommendations(user_id):
    # Generate recommendations
    pass
```

## 🧪 Testing

### Unit Tests
```bash
# Run tests
python -m pytest tests/
```

### API Testing
```bash
# Test endpoints
curl -X GET http://localhost:5000/api/health
curl -X POST http://localhost:5000/api/trip-completed \
  -H "Content-Type: application/json" \
  -d '{"user_id": 1, "trip_data": {...}}'
```

## 🚀 Deployment

### 1. Production Setup
```bash
# Install gunicorn
pip install gunicorn

# Run with gunicorn
gunicorn -w 4 -b 0.0.0.0:5000 api_endpoints:app
```

### 2. Docker Deployment
```dockerfile
FROM python:3.9-slim
COPY . /app
WORKDIR /app
RUN pip install -r requirements.txt
EXPOSE 5000
CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:5000", "api_endpoints:app"]
```

## 📝 Future Enhancements

### 1. Advanced ML Features
- **Deep Learning**: Neural networks for better recommendations
- **Real-time Learning**: Update models based on user feedback
- **A/B Testing**: Test different recommendation algorithms

### 2. Additional Features
- **Weather Integration**: Real-time weather-based recommendations
- **Social Features**: Share trips with friends
- **Voice Integration**: Voice-based trip planning
- **AR Features**: Augmented reality city exploration

### 3. Performance Improvements
- **Redis Caching**: Fast recommendation retrieval
- **Microservices**: Split into recommendation, user, and deal services
- **CDN**: Fast image and content delivery

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support, email: support@travelapp.com

---

**🎉 Happy Traveling with AI-Powered Recommendations!** 