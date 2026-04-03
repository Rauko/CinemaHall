CREATE TABLE blocked_emails (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    reason VARCHAR(255),
    blocked_at TIMESTAMP
);