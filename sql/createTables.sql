/* FOR RESET ONLY */
DROP TABLE users CASCADE;
DROP TABLE match CASCADE;
DROP TABLE message;
DROP TABLE profile;
DROP TABLE preference;
/* FOR RESET ONLY */

CREATE TABLE users (
    -- user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE profile (
    profile_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    bio VARCHAR(500),
    photo BYTEA,
    gender VARCHAR(50),
    year VARCHAR(50),
    greek_association VARCHAR(50),
    religion VARCHAR(50),
    commitment VARCHAR(50),
    major VARCHAR(50)
);

CREATE TABLE preference (
    preference_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    gender VARCHAR(20),
    year VARCHAR(20),
    greek_preference VARCHAR(200),
    religion VARCHAR(50),
    commitment VARCHAR(50),
    major VARCHAR(100)
);

CREATE TABLE match (
    match_id SERIAL PRIMARY KEY,
    username1 VARCHAR(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    username2 VARCHAR(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE message (
    message_id SERIAL PRIMARY KEY,
    match_id INT NOT NULL REFERENCES match(match_id) ON DELETE CASCADE,
    sender_username VARCHAR(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    message_text VARCHAR(1000) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, email) VALUES ('kevin', 'pass', 'kevin@trinity.edu');
INSERT INTO users (username, password, email) VALUES ('levi', 'pass', 'levi@trinity.edu');
INSERT INTO users (username, password, email) VALUES ('choudhry', 'pass', 'choudhry@trinity.edu');
INSERT INTO users (username, password, email) VALUES ('harry', 'pass', 'harry@trinity.edu');


-- For user 'kevin'
INSERT INTO profile (username, first_name, last_name, birthdate, bio, photo_url, gender, year, greek_association, religion, commitment, major)
VALUES ('kevin', 'Kevin', 'Lastname', '1990-01-01', 'Bio of Kevin', 'http://example.com/kevin.jpg', 'Male', 'Senior', 'Alpha Beta', 'Christian', 'High', 'Computer Science');

-- For user 'levi'
INSERT INTO profile (username, first_name, last_name, birthdate, bio, photo_url, gender, year, greek_association, religion, commitment, major)
VALUES ('levi', 'Levi', 'Lastname', '1992-05-20', 'Bio of Levi', 'http://example.com/levi.jpg', 'Male', 'Junior', NULL, 'Atheist', 'Medium', 'Business Administration');

/* creating matches and send messages*/
DO $$
DECLARE
    levi_kevin_match_id INT;
BEGIN
  /* creating match */
  INSERT INTO match (username1, username2) VALUES ('levi', 'kevin');
  INSERT INTO match (username1, username2) VALUES ('levi', 'choudhry');
  INSERT INTO match (username1, username2) VALUES ('levi', 'harry');
  /* messages */
  SELECT match_id INTO levi_kevin_match_id FROM match WHERE username1 = 'levi' AND username2 = 'kevin';
  INSERT INTO message (match_id, sender_username, message_text) VALUES (levi_kevin_match_id, 'levi', 'Hi Kevin');
  INSERT INTO message (match_id, sender_username, message_text) VALUES (levi_kevin_match_id, 'kevin', 'Hi Levi');
  INSERT INTO message (match_id, sender_username, message_text) VALUES (levi_kevin_match_id, 'levi', 'Hi again Kevin');
END $$;



