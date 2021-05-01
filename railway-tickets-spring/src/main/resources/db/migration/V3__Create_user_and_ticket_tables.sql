CREATE TABLE users
(
    id         BIGSERIAL    NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (phone, email)
);

CREATE TABLE users_roles
(
    user_id int8 NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE tickets
(
    id            BIGSERIAL NOT NULL,
    user_id       int8      NOT NULL,
    route_id      int8      NOT NULL,
    purchase_date TIMESTAMP NOT NULL
);

ALTER TABLE IF EXISTS users_roles
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users
            ON DELETE RESTRICT;

ALTER TABLE IF EXISTS tickets
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users
            ON DELETE RESTRICT;

ALTER TABLE IF EXISTS tickets
    ADD CONSTRAINT fk_route_id
        FOREIGN KEY (route_id)
            REFERENCES routes
            ON DELETE RESTRICT;