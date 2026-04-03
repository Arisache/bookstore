INSERT INTO users (username, email, created_at)
SELECT 'admin', 'admin@bookstore.com', NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);