#!/usr/bin/env python3
"""
Test script for the Trip Recommendation System
"""

import requests
import json
import time

# API base URL
BASE_URL = "http://localhost:5000/api"

def test_health_check():
    """Test health check endpoint"""
    print("🏥 Testing health check...")
    try:
        response = requests.get(f"{BASE_URL}/health")
        if response.status_code == 200:
            print("✅ Health check passed")
            return True
        else:
            print(f"❌ Health check failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Health check error: {e}")
        return False

def test_trip_completion():
    """Test trip completion endpoint"""
    print("\n🎯 Testing trip completion...")
    
    trip_data = {
        "user_id": 1,
        "trip_data": {
            "trip_name": "Paris Adventure",
            "cities_visited": "Paris",
            "activities_done": "Eiffel Tower Visit,Louvre Museum,Shopping",
            "hotels_stayed": "Hotel Ritz Paris",
            "budget_range": "medium",
            "travel_style": "leisure",
            "rating": 4.5,
            "trip_duration": 5,
            "season": "spring"
        }
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/trip-completed",
            json=trip_data,
            headers={'Content-Type': 'application/json'}
        )
        
        if response.status_code == 200:
            result = response.json()
            print("✅ Trip completion successful")
            print(f"📊 Generated recommendations: {len(result.get('recommendations', {}))} types")
            return True
        else:
            print(f"❌ Trip completion failed: {response.status_code}")
            print(f"Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Trip completion error: {e}")
        return False

def test_get_recommendations():
    """Test getting recommendations"""
    print("\n🎁 Testing get recommendations...")
    
    try:
        response = requests.get(f"{BASE_URL}/recommendations/1")
        
        if response.status_code == 200:
            result = response.json()
            recommendations = result.get('recommendations', {})
            
            print("✅ Get recommendations successful")
            print(f"📋 Similar destinations: {len(recommendations.get('similar_destinations', []))}")
            print(f"🏨 Better hotels: {len(recommendations.get('better_hotels', []))}")
            print(f"🎯 Missed activities: {len(recommendations.get('missed_activities', []))}")
            print(f"🧳 Packing tips: {len(recommendations.get('packing_tips', []))}")
            print(f"💰 Deals: {len(recommendations.get('deals_offers', []))}")
            
            return True
        else:
            print(f"❌ Get recommendations failed: {response.status_code}")
            print(f"Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Get recommendations error: {e}")
        return False

def test_packing_tips():
    """Test packing tips endpoint"""
    print("\n🧳 Testing packing tips...")
    
    try:
        response = requests.get(f"{BASE_URL}/packing-tips/1")
        
        if response.status_code == 200:
            result = response.json()
            tips = result.get('packing_tips', [])
            
            print("✅ Packing tips successful")
            print(f"💡 Generated {len(tips)} packing tips")
            for tip in tips:
                print(f"   • {tip}")
            
            return True
        else:
            print(f"❌ Packing tips failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Packing tips error: {e}")
        return False

def test_deals():
    """Test deals endpoint"""
    print("\n💰 Testing deals...")
    
    try:
        response = requests.get(f"{BASE_URL}/deals/1")
        
        if response.status_code == 200:
            result = response.json()
            deals = result.get('deals', [])
            
            print("✅ Deals successful")
            print(f"🎁 Found {len(deals)} personalized deals")
            
            return True
        else:
            print(f"❌ Deals failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Deals error: {e}")
        return False

def test_model_training():
    """Test model training endpoint"""
    print("\n🤖 Testing model training...")
    
    try:
        response = requests.post(f"{BASE_URL}/train-models")
        
        if response.status_code == 200:
            result = response.json()
            print("✅ Model training successful")
            print(f"📝 Message: {result.get('message', '')}")
            return True
        else:
            print(f"❌ Model training failed: {response.status_code}")
            print(f"Response: {response.text}")
            return False
    except Exception as e:
        print(f"❌ Model training error: {e}")
        return False

def test_similar_users():
    """Test similar users endpoint"""
    print("\n👥 Testing similar users...")
    
    try:
        response = requests.get(f"{BASE_URL}/similar-users/1")
        
        if response.status_code == 200:
            result = response.json()
            similar_users = result.get('similar_users', [])
            
            print("✅ Similar users successful")
            print(f"👤 Found {len(similar_users)} similar users")
            
            return True
        else:
            print(f"❌ Similar users failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Similar users error: {e}")
        return False

def run_all_tests():
    """Run all tests"""
    print("🚀 Starting Trip Recommendation System Tests")
    print("=" * 50)
    
    tests = [
        test_health_check,
        test_trip_completion,
        test_get_recommendations,
        test_packing_tips,
        test_deals,
        test_model_training,
        test_similar_users
    ]
    
    passed = 0
    total = len(tests)
    
    for test in tests:
        try:
            if test():
                passed += 1
        except Exception as e:
            print(f"❌ Test {test.__name__} crashed: {e}")
    
    print("\n" + "=" * 50)
    print(f"📊 Test Results: {passed}/{total} tests passed")
    
    if passed == total:
        print("🎉 All tests passed! System is working correctly.")
    else:
        print("⚠️  Some tests failed. Please check the system setup.")
    
    return passed == total

if __name__ == "__main__":
    # Wait a bit for the server to start
    print("⏳ Waiting 3 seconds for server to start...")
    time.sleep(3)
    
    success = run_all_tests()
    
    if success:
        print("\n🎯 System is ready for production!")
        print("📱 You can now integrate with your mobile app.")
    else:
        print("\n🔧 Please fix the failing tests before proceeding.") 