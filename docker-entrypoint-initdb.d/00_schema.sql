CREATE TABLE drivers
(
    id           BIGSERIAL PRIMARY KEY,
    name         TEXT        NOT NULL,
    phone_number TEXT        NOT NULL,
    photo_url    TEXT        NOT NULL,
    rating       FLOAT       NOT NULL CHECK ( rating <= 5 AND rating > 0 ) DEFAULT 4.7,
    car_name     TEXT        NOT NULL,
    car_number   TEXT        NOT NULL,
    car_color    TEXT        NOT NULL,
    removed      BOOLEAN     NOT NULL                                      DEFAULT FALSE,
    created      timestamptz NOT NULL                                      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders
(
    id                  BIGSERIAL PRIMARY KEY,
    address_from        TEXT             NOT NULL,
    address_to          TEXT             NOT NULL,
    driver_comment      TEXT             NOT NULL                                                               DEFAULT 'No comments',
    baby_chair          BOOLEAN          NOT NULL                                                               DEFAULT FALSE,
    choice_of_car_class INT              NOT NULL CHECK ( choice_of_car_class > 0 AND choice_of_car_class < 4 ) DEFAULT 1,
    distance            DOUBLE PRECISION NOT NULL,
    duration            DOUBLE PRECISION NOT NULL,
    price               DOUBLE PRECISION NOT NULL,
    accept              BOOLEAN          NOT NULL                                                               DEFAULT FALSE,
    accept_driver       BOOLEAN          NOT NULL                                                               DEFAULT FALSE,
    driver_id           INT              NOT NULL                                                               DEFAULT 0,
    complete_order      BOOLEAN          NOT NULL                                                               DEFAULT FALSE,
    created             timestamptz      NOT NULL                                                               DEFAULT CURRENT_TIMESTAMP
);