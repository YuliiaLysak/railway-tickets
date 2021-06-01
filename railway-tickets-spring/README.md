# Railway tickets (Spring)

### Для запуску програми необхідно:
- запустити PostgreSQL сервер, наприклад використовуючи Docker
  ```
  docker run --name local-postgres -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres
  ```
- підключитися до сервера PostgreSQL будь-яким клієнтом та створити базу даних 
  ```
  CREATE DATABASE railwaytickets
  ```
- скомпілювати проект
  ```
  mvn clean install
  ```
- запустити проект
  ```
  java -jar ./target/railway-tickets-spring-1.0-SNAPSHOT.jar
  ```
- відкрити посилання в браузері
  ```
  http://localhost:8080
  ```
- тестові акаунти:
  ```
  Admin
  email: admin@com.ua
  password: admin
  ```
  ```
  User
  email: user@com.ua
  password: user
  ```

