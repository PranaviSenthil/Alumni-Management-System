USE alumni_db;

-- Sample Users (2 Alumni + 1 Student + 1 Admin)
INSERT INTO users (email, password_hash, first_name, last_name, user_type, is_verified, is_active)
VALUES
('alice@alumni.com',   '$2a$12$dummyhash1', 'Alice',  'Nair',   'ALUMNUS', TRUE,  TRUE),
('bob@alumni.com',     '$2a$12$dummyhash2', 'Bob',    'Menon',  'ALUMNUS', FALSE, TRUE),
('carol@student.com',  '$2a$12$dummyhash3', 'Carol',  'Das',    'STUDENT', FALSE, TRUE),
('admin@ams.com',      '$2a$12$dummyhash4', 'Admin',  'User',   'ALUMNUS', TRUE,  TRUE);

-- Alumni Profiles
INSERT INTO alumni_profiles (user_id, graduation_year, industry, work_history, skills, bio, is_mentor)
VALUES
(1, 2020, 'Software Engineering', 'TechCorp - Backend Developer 2020-2023', 'Java, Spring Boot, React', 'Experienced backend developer open to mentoring.', TRUE),
(2, 2019, 'Data Science',         'DataCo - ML Engineer 2019-2022',          'Python, TensorFlow, SQL',  'ML engineer passionate about AI.',                FALSE);

-- Student Profile
INSERT INTO student_profiles (user_id, expected_graduation_year, academic_interests, career_goals, skills, bio, is_mentee)
VALUES
(3, 2026, 'Computer Science, AI', 'Become a full-stack developer', 'Java, React, HTML, CSS', 'Final year CSE student looking for mentorship.', TRUE);

-- Sample Connections
INSERT INTO connections (sender_id, receiver_id, status)
VALUES
(1, 3, 'ACCEPTED'),
(2, 3, 'PENDING');

-- Sample Messages
INSERT INTO messages (sender_id, receiver_id, content)
VALUES
(1, 3, 'Hi Carol! Happy to mentor you in Java and Spring Boot.'),
(3, 1, 'Thank you Alice! I would love that.');