CREATE TYPE event_type AS ENUM ('MOVIE', 'CONCERT', 'PLAY', 'OTHER');

CREATE TYPE seat_type AS ENUM ('VIP', 'REGULAR', 'OTHER');

CREATE TABLE users
(
    id    BIGSERIAL PRIMARY KEY,
    name  TEXT        NOT NULL,
    email TEXT UNIQUE NOT NULL
);

CREATE TABLE events
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT       NOT NULL,
    start_time TIMESTAMP  NOT NULL,
    type       event_type NOT NULL
);

CREATE TABLE seats
(
    id       BIGSERIAL PRIMARY KEY,
    event_id BIGINT    NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    label    TEXT      NOT NULL, -- Supports labels like "A1", "B5"
    type     seat_type NOT NULL,
    UNIQUE (event_id, label)
);

CREATE TABLE reservations
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    -- UNIQUE constraint ensures that a seat can only be reserved by one user at a time, preventing race conditions
    seat_id     BIGINT UNIQUE NOT NULL REFERENCES seats (id) ON DELETE CASCADE,
    reserved_at TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- indexes for faster lookups
CREATE INDEX idx_seats_event_id ON seats (event_id);
CREATE INDEX idx_reservations_user_id ON reservations (user_id);