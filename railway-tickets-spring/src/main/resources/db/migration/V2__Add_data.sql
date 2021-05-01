INSERT INTO stations(city, name)
VALUES ('Kyiv', 'Kyiv-Pasazhyrsky'),
       ('Lviv', 'Lviv'),
       ('Odesa', 'Odesa-Holovna'),
       ('Kharkiv', 'Kharkiv-Pas'),
       ('Ivano-Frankivsk', 'Ivano-Frankivsk');


INSERT INTO routes(departure_station_id, arrival_station_id,
                   departure_time, arrival_time,
                   train_name, total_seats, price_per_seat)
VALUES (1, 2, '2021-06-01 06:50', '2021-06-01 13:45', '715 K IC+', 550, 685.54),
       (2, 1, '2021-06-02 16:12', '2021-06-02 23:08', '716 L IC+', 550, 685.54),
       (1, 3, '2021-06-01 15:52', '2021-06-02 05:23', '148 K', 250, 392.62),
       (3, 1, '2021-06-02 22:05', '2021-06-03 07:10', '106 SH', 250, 392.62),
       (1, 4, '2021-06-01 06:45', '2021-06-01 11:40', '722 K IC+', 550, 685.54),
       (4, 1, '2021-06-02 05:54', '2021-06-02 11:47', '719 D IC+', 550, 685.54),
       (1, 5, '2021-06-01 19:06', '2021-06-02 05:43', '043 K', 300, 450.00),
       (5, 1, '2021-06-02 21:55', '2021-06-03 08:48', '043 L', 300, 450.00);
