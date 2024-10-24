import json
import sqlite3
import argparse

# Function to read phrases from the JSON file
def read_phrases_from_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        return json.load(file)

# Function to create database and tables
def create_database(db_name):
    conn = sqlite3.connect(db_name)
    cursor = conn.cursor()

    # Create Categories Table
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS categories (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        description TEXT
    )
    ''')

    # Create Phrases Table
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS phrases (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        category_id INTEGER,
        arabic TEXT NOT NULL,
        meaning TEXT NOT NULL,
        FOREIGN KEY (category_id) REFERENCES categories (id)
    )
    ''')

    # Create Words Table
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS words (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        word TEXT NOT NULL,
        meaning TEXT NOT NULL
    )
    ''')

    # Create Sarfs Table
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS sarfs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        fi_l TEXT NOT NULL,
        sarf TEXT NOT NULL,
        meaning TEXT NOT NULL
    )
    ''')

    return conn

# Function to populate categories and phrases into the database
def populate_database(conn, phrases_data):
    cursor = conn.cursor()

    # Define categories with descriptions
    categories = [
        ('Greetings', 'Common Islamic greetings and introductions'),
        ('Phrases', 'Common phrases used in everyday conversations'),
        ('Vocabulary', 'Words and their meanings'),
        ('Sarfs', 'Conjugation forms of verbs')
    ]

    # Insert categories into the database
    cursor.executemany('INSERT INTO categories (name, description) VALUES (?, ?)', categories)

    # Retrieve category IDs
    cursor.execute('SELECT id, name FROM categories')
    category_map = {name: id for id, name in cursor.fetchall()}

    # Insert phrases into the database
    for phrase in phrases_data.get("greetings_introductions", []):
        cursor.execute('''
        INSERT INTO phrases (category_id, arabic, meaning) VALUES (?, ?, ?)
        ''', (category_map['Greetings'], phrase['arabic'], phrase['meaning']))

    # Commit the changes
    conn.commit()

# Main function to handle command-line arguments
def main():
    parser = argparse.ArgumentParser(description="Populate the database from a JSON file")
    parser.add_argument('--json-path', type=str, required=True, help="Path to the JSON file containing the phrases")
    parser.add_argument('--db-path', type=str, default='phrases_database.db', help="Path to the SQLite database file")
    
    args = parser.parse_args()

    # Read phrases from JSON file
    phrases_data = read_phrases_from_file(args.json_path)

    # Create database and tables
    conn = create_database(args.db_path)

    # Populate the database with data
    populate_database(conn, phrases_data)

    # Close the database connection
    conn.close()
    print("Database populated successfully.")

if __name__ == "__main__":
    main()
