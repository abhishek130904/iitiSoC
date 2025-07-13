import pandas as pd
import mysql.connector
import numpy as np
from sklearn.preprocessing import OneHotEncoder
from sklearn.cluster import KMeans
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.decomposition import TruncatedSVD
import joblib
import json

class TripRecommendationEngine:
    def __init__(self):
        self.conn = None
        self.trips = None
        self.user_clusters = None
        self.similarity_matrix = None

    def connect_database(self):
        try:
            self.conn = mysql.connector.connect(
                host="localhost",
                user="root",
                password="",
                database="travel_db"
            )
            print("✅ Database connected successfully")
            return True
        except mysql.connector.Error as e:
            print(f"❌ Database connection error: {e}")
            return False

    def load_data(self):
        try:
            self.trips = pd.read_sql("SELECT * FROM trips", self.conn)
            print(f"📊 Loaded {len(self.trips)} trips")
            return True
        except Exception as e:
            print(f"❌ Error loading data: {e}")
            return False

    def preprocess_user_data(self):
        print("🔄 Preprocessing user data...")
        # We'll use user_id, city_name, hotel_name for recommendations
        user_features = self.trips[['user_id', 'city_name', 'hotel_name']].copy()
        return user_features

    def create_feature_matrix(self, user_data):
        print("🔧 Creating feature matrix...")
        # Encode categorical features
        categorical_features = ['city_name', 'hotel_name']
        encoders = {}
        encoded_features = []
        for feature in categorical_features:
            encoder = OneHotEncoder(sparse_output=False, handle_unknown='ignore')
            encoded = encoder.fit_transform(user_data[[feature]])
            encoders[feature] = encoder
            encoded_features.append(encoded)
        # Combine all features
        X = np.hstack(encoded_features)
        return X, encoders

    def build_content_based_recommender(self):
        print("🎯 Building content-based recommender...")
        user_profiles = {}
        for user_id in self.trips['user_id'].unique():
            user_trips = self.trips[self.trips['user_id'] == user_id]
            profile = {
                'cities_visited': user_trips['city_name'].tolist(),
                'hotels_stayed': user_trips['hotel_name'].tolist(),
                'trip_count': len(user_trips)
            }
            user_profiles[user_id] = profile
        return user_profiles

    def build_collaborative_filtering(self, user_data_matrix):
        print("👥 Building collaborative filtering...")
        svd = TruncatedSVD(n_components=5, random_state=42)
        user_matrix_reduced = svd.fit_transform(user_data_matrix)
        self.similarity_matrix = cosine_similarity(user_matrix_reduced)
        return svd

    def cluster_users(self, user_data_matrix, n_clusters=3):
        print("🎪 Clustering users...")
        kmeans = KMeans(n_clusters=n_clusters, random_state=42)
        user_clusters = kmeans.fit_predict(user_data_matrix)
        return kmeans, user_clusters

    def generate_recommendations(self, user_id):
        print(f"🎁 Generating recommendations for user {user_id}...")
        recommendations = {
            'similar_destinations': [],
            'other_hotels': [],
            'generic_packing_tips': [
                "Pack according to the weather of your destination city.",
                "Keep your travel documents and essentials handy."
            ],
            'generic_deals': [
                "Check for last-minute hotel deals in your destination city.",
                "Look for bundled offers with flights and hotels."
            ]
        }
        # Get user's cities and hotels
        user_trips = self.trips[self.trips['user_id'] == user_id]
        user_cities = set(user_trips['city_name'])
        user_hotels = set(user_trips['hotel_name'])
        # Recommend cities visited by other users but not by this user
        other_cities = set(self.trips['city_name']) - user_cities
        recommendations['similar_destinations'] = list(other_cities)[:5]
        # Recommend hotels in user's cities that the user hasn't stayed at
        hotels_in_user_cities = self.trips[
            self.trips['city_name'].isin(user_cities) & ~self.trips['hotel_name'].isin(user_hotels)
        ]['hotel_name'].unique().tolist()
        recommendations['other_hotels'] = hotels_in_user_cities[:5]
        return recommendations

    def close_connection(self):
        if self.conn and self.conn.is_connected():
            self.conn.close()
            print("🔌 Database connection closed")

if __name__ == "__main__":
    engine = TripRecommendationEngine()
    if engine.connect_database():
        if engine.load_data():
            user_data = engine.preprocess_user_data()
            X, encoders = engine.create_feature_matrix(user_data)
            user_profiles = engine.build_content_based_recommender()
            svd_model = engine.build_collaborative_filtering(X)
            kmeans_model, user_clusters = engine.cluster_users(X)
            # Example: Generate recommendations for a user
            sample_user_id = engine.trips['user_id'].iloc[0] if not engine.trips.empty else 1
            recommendations = engine.generate_recommendations(sample_user_id)
            print("\n🎉 Recommendation System Ready!")
            print("Sample recommendations:", json.dumps(recommendations, indent=2))
        engine.close_connection()
