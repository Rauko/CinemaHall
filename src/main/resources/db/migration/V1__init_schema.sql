-- =========================
-- 1. BASE TABLES
-- =========================

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    status VARCHAR(50) NOT NULL
);

CREATE TABLE directors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE actors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE,
    biography TEXT
);

-- =========================
-- 2. MOVIES
-- =========================

CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration INT,
    release_year INT,
    director_id BIGINT,
    poster_url VARCHAR(500),
    FOREIGN KEY (director_id) REFERENCES directors(id)
);

CREATE TABLE movie_genres (
    movie_id BIGINT NOT NULL,
    genre VARCHAR(50),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE movie_actors (
    movie_id BIGINT,
    actor_id BIGINT,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (actor_id) REFERENCES actors(id)
);

-- =========================
-- 3. HALLS + SEATS
-- =========================

CREATE TABLE halls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    total_rows INT,
    seats_per_row INT
);

CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hall_id BIGINT NOT NULL,
    row_number INT NOT NULL,
    seat_number INT NOT NULL,
    type VARCHAR(50) NOT NULL,

    CONSTRAINT uq_seat UNIQUE (hall_id, row_number, seat_number),
    FOREIGN KEY (hall_id) REFERENCES halls(id)
);

-- =========================
-- 4. SCREENINGS
-- =========================

CREATE TABLE screenings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    duration INT NOT NULL,
    hall_name VARCHAR(255) NOT NULL,
    base_price DOUBLE NOT NULL,
    imax BOOLEAN,

    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (hall_id) REFERENCES halls(id)
);

-- =========================
-- 5. TICKETS
-- =========================

CREATE TABLE tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    screening_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    purchase_time TIMESTAMP NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(50),
    reserved_at TIMESTAMP,
    purchase_date TIMESTAMP,
    glasses_option VARCHAR(50),

    CONSTRAINT uq_ticket UNIQUE (screening_id, seat_id),

    FOREIGN KEY (screening_id) REFERENCES screenings(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- =========================
-- 6. PAYMENT METHODS
-- =========================

CREATE TABLE payment_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    type VARCHAR(50),
    masked_details VARCHAR(255),
    provider_token VARCHAR(255),
    is_default BOOLEAN,

    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =========================
-- 7. PURCHASE HISTORY (LAST!)
-- =========================

CREATE TABLE purchase_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    ticket_id BIGINT NOT NULL,
    amount DOUBLE,
    action VARCHAR(50),
    purchase_time TIMESTAMP,
    payment_status VARCHAR(50),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);