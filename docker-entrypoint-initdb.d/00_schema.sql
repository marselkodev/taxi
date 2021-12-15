CREATE TABLE drivers
(
    id           BIGSERIAL PRIMARY KEY,
    name         TEXT             NOT NULL,
    phone_number TEXT             NOT NULL,
    photo_url    TEXT             NOT NULL,
    rating       FLOAT            NOT NULL CHECK ( rating <= 5 AND rating > 0 ) DEFAULT 4.7,
    license      BOOLEAN          NOT NULL                                      DEFAULT FALSE,
    car_name     TEXT             NOT NULL,
    car_number   TEXT             NOT NULL,
    car_color    TEXT             NOT NULL,
    position_x   DOUBLE PRECISION NOT NULL                                      DEFAULT 55.47,
    position_y   DOUBLE PRECISION NOT NULL                                      DEFAULT 49.6,
    removed      BOOLEAN          NOT NULL                                      DEFAULT FALSE,
    created      timestamptz      NOT NULL                                      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders
(
    id                  BIGSERIAL PRIMARY KEY,
    address_from_where  TEXT        NOT NULL,
    address_where       TEXT        NOT NULL,
    driver_comment      TEXT,
    baby_chair          BOOLEAN     NOT NULL                                                               DEFAULT FALSE,
    choice_of_car_class INT         NOT NULL CHECK ( choice_of_car_class > 0 AND choice_of_car_class < 4 ) DEFAULT 1,
    created             timestamptz NOT NULL                                                               DEFAULT CURRENT_TIMESTAMP
);

-- CREATE TABLE completed_trips
-- (
--     id               BIGSERIAL PRIMARY KEY,
--     addressFromWhere TEXT        NOT NULL,
--     addressWhere     TEXT        NOT NULL,
--     distance         FLOAT       NOT NULL,
--     price            INT,
--     created          timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
-- );