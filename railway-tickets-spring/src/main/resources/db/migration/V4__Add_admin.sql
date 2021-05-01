INSERT INTO users(first_name, last_name, phone, email, password)
VALUES ('admin', 'admin', '380630636363', 'admin@com.ua', 'admin');

INSERT INTO users_roles(user_id, roles)
VALUES (1, 'ADMIN'),
       (1, 'USER');