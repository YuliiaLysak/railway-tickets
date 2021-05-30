CREATE TABLE session_analytic
(
    analytic_id                BIGSERIAL    NOT NULL,
    session_id                 VARCHAR(255) NOT NULL,
    user_id                    int8,
    creation_time              TIMESTAMP    NOT NULL,
    last_accessed_time         TIMESTAMP    NOT NULL,
    search_route_request_count int4,
    buy_ticket_request_count   int4,
    PRIMARY KEY (analytic_id)
);