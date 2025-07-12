import pandas as pd
from sklearn.neighbors import NearestNeighbors
from travel_data import load_travel_data

# Load data
df = load_travel_data()

# Step 1: Get unique users and places
users = df['user_id'].unique()
places = df['place'].unique()

# Step 2: Create a simple user-place matrix manually
data = []
for user in users:
    visited = df[df['user_id'] == user]['place'].tolist()
    row = [1 if place in visited else 0 for place in places]
    data.append(row)

# Step 3: Train the KNN model
model = NearestNeighbors(metric='cosine', algorithm='brute')
model.fit(data)

# Step 4: Recommend function
def recommend_places_knn_simple(user_id, n_neighbors=2):
    if user_id not in users:
        return ["User not found"]

    index = list(users).index(user_id)
    distances, indices = model.kneighbors([data[index]], n_neighbors=n_neighbors+1)

    similar_user_indices = indices[0][1:]  # Skip self
    user_visited = set(df[df['user_id'] == user_id]['place'])

    recommendations = set()
    for i in similar_user_indices:
        similar_user_id = users[i]
        similar_places = set(df[df['user_id'] == similar_user_id]['place'])
        recommendations.update(similar_places - user_visited)

    return list(recommendations)

# Test
print("Recommendations for user 1:", recommend_places_knn_simple(1))
print("Recommendations for user 2:", recommend_places_knn_simple(2))
print("Recommendations for user 3:", recommend_places_knn_simple(3))
