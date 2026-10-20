-- =======================================================
-- FashionStore Database Schema & Updates
-- Database: fashion_store
-- =======================================================

USE fashion_store;

-- 1. Ensure 'role' column exists in users table for Admin authorization
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER';

-- 2. Index on email for fast authentication lookups
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 3. Index on category_id and is_active for fast catalog filtering
CREATE INDEX IF NOT EXISTS idx_products_category_active ON products(category_id, is_active);

-- 4. Index on orders user_id
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);

-- 5. Seed / Ensure Default Admin user (Password: Admin@123)
-- Role: 'ADMIN'
INSERT INTO users (full_name, email, phone, password, gender, address, role)
SELECT 'Store Admin', 'admin@fashionstore.com', '9876543210', 
       '8v3r5sE21Vq...:PBKDF2WithHmacSHA256', 'Other', 'FashionStore HQ', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@fashionstore.com');

-- 6. Pending Registrations table for secure Email OTP verification
CREATE TABLE IF NOT EXISTS pending_registrations (
    email VARCHAR(150) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    gender VARCHAR(20),
    address VARCHAR(255) NOT NULL,
    otp_hash VARCHAR(255) NOT NULL,
    attempts INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    last_sent_at TIMESTAMP NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

