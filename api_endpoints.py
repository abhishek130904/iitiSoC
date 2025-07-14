from flask import Flask, request, jsonify
from flask_cors import CORS
import mysql.connector
import json
import pandas as pd
from datetime import datetime, timedelta
from one import TripRecommendationEngine
import os
import traceback

app = Flask(__name__)
CORS(app)

# Initialize recommendation engine
engine = TripRecommendationEngine()

@app.route('/api/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({'status': 'healthy', 'message': 'Recommendation API is running'})

@app.route('/api/trip-completed', methods=['POST'])
def trip_completed():
    """Trigger when a trip is completed"""
    try:
        data = request.get_json()
        user_id = data.get('user_id')
        trip_data = data.get('trip_data')
        
        # Store trip data in database
        if engine.connect_database():
            cursor = engine.conn.cursor()
            
            # Insert into trips
            query = """
            INSERT INTO trips 
            (user_id, city_name, hotel_name, created_at, notes, flight_id)
            VALUES (%s, %s, %s, %s, %s, %s)
            """
            
            cursor.execute(query, (
                user_id,
                trip_data.get('city_name'),
                trip_data.get('hotel_name'),
                trip_data.get('created_at') or datetime.now(),
                trip_data.get('notes'),
                trip_data.get('flight_id')
            ))
            
            engine.conn.commit()
            cursor.close()
            
            # Generate recommendations
            recommendations = engine.generate_recommendations(user_id)
            
            return jsonify({
                'success': True,
                'message': 'Trip completed and recommendations generated',
                'recommendations': recommendations
            })
            
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/api/recommendations/<user_id>', methods=['GET'])
def get_recommendations(user_id):
    """Get recommendations for a user"""
    try:
        if engine.connect_database():
            # Get user's trip history
            cursor = engine.conn.cursor(dictionary=True)
            cursor.execute("SELECT * FROM trips WHERE user_id = %s ORDER BY created_at DESC", (user_id,))
            trip_history = cursor.fetchall()
            
            if not trip_history:
                return jsonify({'success': False, 'message': 'No trip history found for user'})
            
            # Generate recommendations
            recommendations = engine.generate_recommendations(user_id)
            
            # Store recommendations in database
            store_recommendations(user_id, recommendations)
            
            return jsonify({
                'success': True,
                'recommendations': recommendations
            })
            
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/api/recommendations/<user_id>/feedback', methods=['POST'])
def submit_recommendation_feedback(user_id):
    """Submit feedback on recommendations"""
    try:
        data = request.get_json()
        recommendation_id = data.get('recommendation_id')
        feedback = data.get('feedback')  # 'like', 'dislike', 'neutral'
        
        if engine.connect_database():
            cursor = engine.conn.cursor()
            cursor.execute(
                "UPDATE user_recommendations SET feedback = %s WHERE id = %s AND user_id = %s",
                (feedback, recommendation_id, user_id)
            )
            engine.conn.commit()
            cursor.close()
            
            return jsonify({'success': True, 'message': 'Feedback submitted successfully'})
            
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/api/deals/<user_id>', methods=['GET'])
def get_personalized_deals(user_id):
    """Get personalized deals for a user"""
    try:
        if engine.connect_database():
            cursor = engine.conn.cursor(dictionary=True)
            
            # Get user's preferred cities from trip history
            cursor.execute("""
                SELECT DISTINCT city_name 
                FROM trips 
                WHERE user_id = %s
            """, (user_id,))
            
            user_cities = cursor.fetchall()
            preferred_cities = []
            for row in user_cities:
                if row['city_name']:
                    preferred_cities.append(row['city_name'])
            
            # Get active deals for preferred cities
            deals = []
            for city in set(preferred_cities):
                cursor.execute("""
                    SELECT * FROM deals_offers 
                    WHERE is_active = TRUE 
                    AND (applicable_cities LIKE %s OR applicable_cities IS NULL)
                    AND valid_until >= CURDATE()
                """, (f'%{city}%',))
                
                city_deals = cursor.fetchall()
                deals.extend(city_deals)
            
            cursor.close()
            
            return jsonify({
                'success': True,
                'deals': deals
            })
            
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/api/packing-tips/<user_id>', methods=['GET'])
def get_packing_tips(user_id):
    """Get personalized packing tips"""
    try:
        if engine.connect_database():
            cursor = engine.conn.cursor(dictionary=True)
            
            # Get user's recent trip
            cursor.execute("""
                SELECT city_name, hotel_name, created_at 
                FROM trips 
                WHERE user_id = %s 
                ORDER BY created_at DESC 
                LIMIT 1
            """, (user_id,))
            
            recent_trip = cursor.fetchone()
            cursor.close()
            
            if recent_trip:
                # You may want to update this to use only available fields
                tips = [
                    f"Pack for your trip to {recent_trip['city_name']}!",
                    "Don't forget your essentials."
                ]
                return jsonify({
                    'success': True,
                    'packing_tips': tips
                })
            else:
                return jsonify({
                    'success': False,
                    'message': 'No recent trips found'
                })
                
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

def generate_post_trip_recommendations(user_id, trip_data):
    """Generate recommendations after trip completion"""
    try:
        # Load data if not already loaded
        if engine.user_trip_history is None:
            engine.load_data()
        
        # Generate recommendations
        recommendations = engine.generate_recommendations(user_id, [trip_data])
        
        return recommendations
        
    except Exception as e:
        print(f"Error generating recommendations: {e}")
        return {}

def store_recommendations(user_id, recommendations):
    """Store recommendations in database"""
    try:
        cursor = engine.conn.cursor()
        
        for rec_type, rec_data in recommendations.items():
            cursor.execute("""
                INSERT INTO user_recommendations 
                (user_id, recommendation_type, recommendation_data)
                VALUES (%s, %s, %s)
            """, (user_id, rec_type, json.dumps(rec_data)))
        
        engine.conn.commit()
        cursor.close()
        
    except Exception as e:
        print(f"Error storing recommendations: {e}")

@app.route('/api/train-models', methods=['POST'])
def train_models():
    """Train/re-train the ML models"""
    try:
        if engine.connect_database() and engine.load_data():
            # Preprocess user data
            user_data = engine.preprocess_user_data()
            
            # Create feature matrix
            X, encoders = engine.create_feature_matrix(user_data)
            
            # Build recommendation systems
            user_profiles = engine.build_content_based_recommender()
            svd_model = engine.build_collaborative_filtering(X)
            kmeans_model, user_clusters = engine.cluster_users(X)
            
            # Save models
            models = {
                'svd': svd_model,
                'kmeans': kmeans_model,
                'encoders': encoders,
                'user_profiles': user_profiles
            }
            engine.save_models(models)
            
            return jsonify({
                'success': True,
                'message': 'Models trained and saved successfully'
            })
            
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/api/similar-users/<user_id>', methods=['GET'])
def get_similar_users(user_id):
    """Find users with similar travel preferences"""
    try:
        if engine.connect_database():
            # Load models if not already loaded
            models = engine.load_models()
            if not models:
                return jsonify({'success': False, 'message': 'Models not found. Please train models first.'})
            
            # Get user's trip history
            cursor = engine.conn.cursor(dictionary=True)
            cursor.execute("SELECT * FROM trips WHERE user_id = %s", (user_id,))
            user_trips = cursor.fetchall()
            
            if not user_trips:
                return jsonify({'success': False, 'message': 'No trip history found'})
            
            # Find similar users based on travel style and preferences
            user_travel_style = user_trips[0]['travel_style']
            user_budget = user_trips[0]['budget_range']
            
            cursor.execute("""
                SELECT DISTINCT user_id, COUNT(*) as trip_count
                FROM trips 
                WHERE travel_style = %s AND budget_range = %s AND user_id != %s
                GROUP BY user_id
                ORDER BY trip_count DESC
                LIMIT 5
            """, (user_travel_style, user_budget, user_id))
            
            similar_users = cursor.fetchall()
            cursor.close()
            
            return jsonify({
                'success': True,
                'similar_users': similar_users
            })
            
    except Exception as e:
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000) 