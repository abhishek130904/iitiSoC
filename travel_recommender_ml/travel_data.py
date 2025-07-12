import pandas as pd

def load_travel_data():
    df = pd.read_csv("data/trip_history.csv")
    return df
