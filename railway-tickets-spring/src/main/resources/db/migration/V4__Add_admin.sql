INSERT INTO users(first_name, last_name, phone, email, password)
VALUES ('admin',
        'admin',
        '380630636363',
        'admin@com.ua',
        '8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918');

INSERT INTO users_roles(user_id, roles)
VALUES (1, 'ADMIN'),
       (1, 'USER');