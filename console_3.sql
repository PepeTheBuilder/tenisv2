CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE tennis_players (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    country VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE referees (
    user_id SERIAL PRIMARY KEY,
    certification_level VARCHAR(50),
    experience INT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE administrators (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100),
    contact_info VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE tournaments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_date VARCHAR(100) NOT NULL,
    end_date VARCHAR(100) NOT NULL
);

CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    tournament_id INT,
    player1_id INT,
    player2_id INT,
    referee_id INT,
    match_date TIMESTAMP,
    score VARCHAR(20),
    FOREIGN KEY (tournament_id) REFERENCES tournaments (id),
    FOREIGN KEY (player1_id) REFERENCES tennis_players (user_id),
    FOREIGN KEY (player2_id) REFERENCES tennis_players (user_id),
    FOREIGN KEY (referee_id) REFERENCES referees (user_id)
);

ALTER TABLE matches
ALTER COLUMN match_date TYPE VARCHAR(255)
USING match_date::text;

ALTER TABLE matches
ADD CONSTRAINT chk_score_format CHECK (score ~ '^\d+-\d+ \d+-\d+$');

CREATE TABLE user_enrollment (
    enrollment_id SERIAL PRIMARY KEY,
    tournament_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


INSERT INTO users (username, password, email, role)
VALUES
('player1', 'password1', 'player1@example.com', 'tennis_player'),
('player2', 'password2', 'player2@example.com', 'tennis_player'),
('referee1', 'password3', 'referee1@example.com', 'referee'),
('admin1', 'password4', 'admin1@example.com', 'administrator');


INSERT INTO users (username, password, email, role)
VALUES
('referee2', 'password3', 'referee2@example.com', 'referee');


INSERT INTO tennis_players (user_id, name, age, country)
VALUES
(1, 'John Doe', 25, 'USA'),
(2, 'Jane Smith', 28, 'UK');


INSERT INTO referees (user_id, certification_level, experience)
VALUES
-- (3, 'Level 1', 3),
(5, 'Level 2', 2);


INSERT INTO administrators (user_id, full_name, contact_info)
VALUES
(4, 'Admin Name', 'admin@example.com');


INSERT INTO tournaments (name, start_date, end_date)
VALUES
('Grand Slam 2024', '2024-07-01', '2024-07-15'),
('US Open 2024', '2024-08-25', '2024-09-08');


INSERT INTO matches (tournament_id, player1_id, player2_id, referee_id, match_date, score)
VALUES
(1, 1, 2, 3, '2024-07-05 10:00:00', '6-4, 7-6'),
(2, 2, 1, 3, '2024-08-30 14:00:00', '7-5, 6-3');



SELECT * FROM matches WHERE referee_id = :refereeId
