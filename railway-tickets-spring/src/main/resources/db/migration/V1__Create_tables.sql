create table routes
(
    id                   bigserial    not null,
    departure_station_id int8         not null,
    arrival_station_id   int8         not null,
    departure_time       timestamp    not null,
    arrival_time         timestamp    not null,
    train_name           varchar(255) not null,
    total_seats          int4         not null,
    price_per_seat       float8       not null,
    primary key (id)
);

create table stations
(
    id   bigserial    not null,
    city varchar(255) not null,
    name varchar(255) not null,
    primary key (id),
    unique (city, name)
);

alter table routes
    add constraint fk_arrival_station
        foreign key (arrival_station_id)
            references stations
            on delete restrict;

alter table routes
    add constraint fk_departure_station
        foreign key (departure_station_id)
            references stations
            on delete restrict;