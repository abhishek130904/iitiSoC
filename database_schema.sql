-- Database Schema for Trip Recommendation System

-- User Trip History Table
CREATE TABLE user_trip_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    trip_name VARCHAR(255),
    cities_visited TEXT,
    activities_done TEXT,
    hotels_stayed TEXT,
    budget_range ENUM('low', 'medium', 'high', 'luxury') DEFAULT 'medium',
    travel_style ENUM('leisure', 'adventure', 'business', 'luxury', 'solo', 'couple', 'family') DEFAULT 'leisure',
    rating DECIMAL(2,1) CHECK (rating >= 1.0 AND rating <= 5.0),
    trip_duration INT, -- in days
    season ENUM('spring', 'summer', 'autumn', 'winter'),
    trip_date DATE,
    feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Destination Cities Table
CREATE TABLE destination_cities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    travel_style ENUM('leisure', 'adventure', 'business', 'luxury', 'cultural', 'nature') DEFAULT 'leisure',
    budget_range ENUM('low', 'medium', 'high', 'luxury') DEFAULT 'medium',
    rating DECIMAL(2,1) DEFAULT 4.0,
    popularity_score DECIMAL(3,2) DEFAULT 0.5,
    weather_info JSON,
    best_season ENUM('spring', 'summer', 'autumn', 'winter'),
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotels Table
CREATE TABLE hotels (
    id INT PRIMARY KEY AUTO_INCREMENT,
    hotel_name VARCHAR(255) NOT NULL,
    city VARCHAR(255),
    country VARCHAR(255),
    rating DECIMAL(2,1) DEFAULT 4.0,
    price_range ENUM('low', 'medium', 'high', 'luxury') DEFAULT 'medium',
    hotel_type ENUM('hotel', 'resort', 'hostel', 'apartment', 'boutique') DEFAULT 'hotel',
    amenities JSON,
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Activities Table
CREATE TABLE activities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    activity_name VARCHAR(255) NOT NULL,
    city VARCHAR(255),
    country VARCHAR(255),
    activity_type ENUM('cultural', 'adventure', 'leisure', 'food', 'shopping', 'nature') DEFAULT 'leisure',
    popularity DECIMAL(3,2) DEFAULT 0.5,
    price_range ENUM('low', 'medium', 'high') DEFAULT 'medium',
    duration_hours INT,
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Recommendations Table
CREATE TABLE user_recommendations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    recommendation_type ENUM('similar_destinations', 'better_hotels', 'missed_activities', 'packing_tips', 'deals_offers') NOT NULL,
    recommendation_data JSON,
    is_viewed BOOLEAN DEFAULT FALSE,
    is_clicked BOOLEAN DEFAULT FALSE,
    feedback ENUM('like', 'dislike', 'neutral') DEFAULT 'neutral',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Deals and Offers Table
CREATE TABLE deals_offers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    deal_title VARCHAR(255) NOT NULL,
    deal_description TEXT,
    discount_percentage INT,
    valid_from DATE,
    valid_until DATE,
    applicable_cities TEXT,
    applicable_hotels TEXT,
    deal_type ENUM('hotel', 'flight', 'package', 'activity') DEFAULT 'hotel',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Preferences Table
CREATE TABLE user_preferences (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    preferred_travel_style ENUM('leisure', 'adventure', 'business', 'luxury', 'solo', 'couple', 'family'),
    preferred_budget ENUM('low', 'medium', 'high', 'luxury'),
    preferred_seasons JSON,
    preferred_activities JSON,
    preferred_cities JSON,
    notification_preferences JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Sample Data for Destination Cities
INSERT INTO destination_cities (city, country, travel_style, budget_range, rating, popularity_score, best_season, description) VALUES
('Paris', 'France', 'leisure', 'medium', 4.5, 0.9, 'spring', 'City of love and culture'),
('Tokyo', 'Japan', 'cultural', 'high', 4.7, 0.8, 'spring', 'Modern city with rich traditions'),
('New York', 'USA', 'business', 'high', 4.3, 0.85, 'autumn', 'The city that never sleeps'),
('Bali', 'Indonesia', 'leisure', 'medium', 4.6, 0.75, 'summer', 'Tropical paradise'),
('Barcelona', 'Spain', 'leisure', 'medium', 4.4, 0.7, 'spring', 'Coastal city with rich history'),
('Sydney', 'Australia', 'adventure', 'high', 4.2, 0.65, 'summer', 'Harbor city with outdoor activities'),
('Marrakech', 'Morocco', 'cultural', 'medium', 4.1, 0.6, 'spring', 'Exotic city with vibrant markets'),
('Reykjavik', 'Iceland', 'adventure', 'high', 4.8, 0.55, 'summer', 'Land of fire and ice');

-- Sample Data for Hotels
INSERT INTO hotels (hotel_name, city, country, rating, price_range, hotel_type, description) VALUES
('Hotel Ritz Paris', 'Paris', 'France', 4.8, 'luxury', 'hotel', 'Luxury hotel in the heart of Paris'),
('Tokyo Grand Hotel', 'Tokyo', 'Japan', 4.6, 'high', 'hotel', 'Modern luxury in Tokyo'),
('Central Park Hotel', 'New York', 'USA', 4.4, 'high', 'hotel', 'Overlooking Central Park'),
('Bali Beach Resort', 'Bali', 'Indonesia', 4.5, 'medium', 'resort', 'Beachfront resort paradise'),
('Barcelona Boutique', 'Barcelona', 'Spain', 4.3, 'medium', 'boutique', 'Charming boutique hotel');

-- Sample Data for Activities
INSERT INTO activities (activity_name, city, country, activity_type, popularity, price_range, duration_hours, description) VALUES
('Eiffel Tower Visit', 'Paris', 'France', 'cultural', 0.95, 'medium', 3, 'Iconic tower with city views'),
('Louvre Museum', 'Paris', 'France', 'cultural', 0.9, 'low', 4, 'World-famous art museum'),
('Sushi Making Class', 'Tokyo', 'Japan', 'food', 0.8, 'high', 2, 'Learn to make authentic sushi'),
('Times Square Walk', 'New York', 'USA', 'leisure', 0.85, 'low', 2, 'Experience the bright lights'),
('Beach Yoga', 'Bali', 'Indonesia', 'leisure', 0.7, 'medium', 1, 'Sunrise yoga on the beach'),
('Sagrada Familia Tour', 'Barcelona', 'Spain', 'cultural', 0.8, 'medium', 2, 'Gaudi\'s masterpiece');

-- Sample User Trip History
INSERT INTO user_trip_history (user_id, trip_name, cities_visited, activities_done, hotels_stayed, budget_range, travel_style, rating, trip_duration, season) VALUES
(1, 'Paris Adventure', 'Paris', 'Eiffel Tower Visit,Louvre Museum,Shopping', 'Hotel Ritz Paris', 'medium', 'leisure', 4.5, 5, 'spring'),
(1, 'Tokyo Business Trip', 'Tokyo', 'Sushi Making Class,Temple Visit', 'Tokyo Grand Hotel', 'high', 'business', 4.7, 3, 'autumn'),
(2, 'Bali Relaxation', 'Bali', 'Beach Yoga,Spa Treatment', 'Bali Beach Resort', 'medium', 'leisure', 4.6, 7, 'summer'),
(3, 'New York City Break', 'New York', 'Times Square Walk,Broadway Show', 'Central Park Hotel', 'high', 'leisure', 4.3, 4, 'autumn');

-- Indexes for better performance
CREATE INDEX idx_user_trip_history_user_id ON user_trip_history(user_id);
CREATE INDEX idx_user_trip_history_travel_style ON user_trip_history(travel_style);
CREATE INDEX idx_destination_cities_travel_style ON destination_cities(travel_style);
CREATE INDEX idx_hotels_city ON hotels(city);
CREATE INDEX idx_activities_city ON activities(city);
CREATE INDEX idx_user_recommendations_user_id ON user_recommendations(user_id); 