DELETE FROM users;

INSERT INTO users (email, password_hash, created_at, last_login, is_active) VALUES
                                                                                ('user1@example.com', 'hash1', '2021-01-01 12:00:00', '2021-01-02 12:00:00', TRUE),
                                                                                ('user2@example.com', 'hash2', '2021-02-01 12:00:00', '2021-02-02 12:00:00', TRUE),
                                                                                ('user3@example.com', 'hash3', '2021-03-01 12:00:00', '2021-03-02 12:00:00', TRUE);