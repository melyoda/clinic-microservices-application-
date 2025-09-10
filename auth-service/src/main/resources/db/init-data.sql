-- init-data.sql
-- Target table name is '_users', not 'users'
-- Insert a default admin user. The plain text password is "admin123", hashed with BCrypt.
INSERT INTO _users (username, password, role, created_at)
VALUES (
           'admin',
           '$2a$10$92LXCEt4s4U47XrQc2aHHu4hC6MvqzQq6Zz.5VdOPQ5TxVlTjL5uW', -- This is the hash for 'admin123'
           'ADMIN', -- This assumes your 'role' column is of type String (or an Enum stored as a String)
           NOW()
       ) ON CONFLICT (username) DO NOTHING; -- This conflicts on the 'username' unique constraint