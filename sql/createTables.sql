/* FOR RESET ONLY */
DROP TABLE users CASCADE;
DROP TABLE match CASCADE;
DROP TABLE message;
DROP TABLE profile;
DROP TABLE preference;
/* FOR RESET ONLY */

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE profile (
    profile_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    birthdate DATE,
    bio VARCHAR(500),
    photo_url VARCHAR(200),
    gender VARCHAR(50),
    year VARCHAR(50),
    greek_association VARCHAR(50),
    religion VARCHAR(50),
    commitment VARCHAR(50),
    major VARCHAR(50)
);

CREATE TABLE preference (
    preference_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    gender VARCHAR(20),
    year VARCHAR(20),
    greek_preference VARCHAR(200),
    religion VARCHAR(50),
    commitment VARCHAR(50),
    major VARCHAR(100)
);

CREATE TABLE match (
    match_id SERIAL PRIMARY KEY,
    user_id1 INT REFERENCES users(user_id) ON DELETE CASCADE,
    user_id2 INT REFERENCES users(user_id) ON DELETE CASCADE,
    match_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE message (
    message_id SERIAL PRIMARY KEY,
    match_id INT REFERENCES match(match_id) ON DELETE CASCADE,
    sender_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    message_text VARCHAR(1000) NOT NULL,
);

INSERT INTO users (username, password, email) VALUES ('kevin', 'pass', 'kevin@trinity.edu');
INSERT INTO users (username, password, email) VALUES ('levi', 'pass', 'levi@trinity.edu');

/* declaring Levi's and Kevin's user id into a variable */
DO $$
DECLARE
    levi_user_id INT;
    kevin_user_id INT;
BEGIN
    SELECT user_id INTO levi_user_id FROM users WHERE username = 'levi';
    SELECT user_id INTO kevin_user_id FROM users WHERE username = 'kevin';
    INSERT INTO match (user_id1, user_id2) VALUES (levi_user_id, kevin_user_id);
END $$;


