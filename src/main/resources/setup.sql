CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS credits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_amount DOUBLE NOT NULL DEFAULT 0.0,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    amount DOUBLE NOT NULL,
    main_category VARCHAR(100) NOT NULL,
    sub_category VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS budgets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    monthly_limit DOUBLE NOT NULL DEFAULT 0.0,
    user_id INT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert a default user for testing
INSERT IGNORE INTO users (id, name, email, password) VALUES (1, 'Default User', 'user@example.com', 'password123');
-- Insert a default credit entry for testing
INSERT IGNORE INTO credits (user_id, total_amount) VALUES (1, 10000.0);
