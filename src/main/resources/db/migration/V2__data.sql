INSERT INTO users (name, email)
VALUES ('Alice Johnson', 'alice.johnson@example.com'),
       ('Bob Smith', 'bob.smith@example.com'),
       ('Charlie Brown', 'charlie.brown@example.com'),
       ('Diana Prince', 'diana.prince@example.com'),
       ('Ethan Hunt', 'ethan.hunt@example.com'),
       ('Fiona Gallagher', 'fiona.gallagher@example.com'),
       ('George Washington', 'george.washington@example.com'),
       ('Hannah Baker', 'hannah.baker@example.com'),
       ('Ian McGregor', 'ian.mcgregor@example.com'),
       ('Jessica Davis', 'jessica.davis@example.com');

INSERT INTO events (name, start_time, type)
VALUES ('Inception Screening', '2025-03-10 18:30:00', 'MOVIE'),
       ('Rock Concert Night', '2025-03-15 20:00:00', 'CONCERT'),
       ('Shakespeare Play', '2025-03-20 19:00:00', 'PLAY'),
       ('Jazz Festival', '2025-03-25 21:00:00', 'CONCERT'),
       ('Classic Movie Marathon', '2025-03-28 17:00:00', 'MOVIE');

INSERT INTO seats (event_id, label, type)
VALUES (1, 'A1', 'REGULAR'),
       (1, 'A2', 'REGULAR'),
       (1, 'B1', 'VIP'),
       (1, 'B2', 'VIP'),

       (2, 'A1', 'REGULAR'),
       (2, 'A2', 'REGULAR'),
       (2, 'B1', 'REGULAR'),
       (2, 'B2', 'REGULAR'),
       (2, 'C1', 'VIP'),

       (3, 'A1', 'REGULAR'),
       (3, 'A2', 'REGULAR'),
       (3, 'B1', 'REGULAR'),
       (3, 'C1', 'VIP'),

       (4, 'A1', 'REGULAR'),
       (4, 'A2', 'REGULAR'),
       (4, 'B1', 'REGULAR'),
       (4, 'C1', 'VIP'),
       (4, 'C2', 'VIP'),

       (5, 'A1', 'REGULAR'),
       (5, 'A2', 'REGULAR'),
       (5, 'B1', 'VIP'),
       (5, 'B2', 'VIP'),
       (5, 'C1', 'REGULAR');

INSERT INTO reservations (user_id, seat_id)
VALUES (1, 1),
       (1, 9),
       (2, 3);