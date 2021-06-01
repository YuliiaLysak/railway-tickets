# Railway tickets (Java EE Servlet)

### Для запуску програми необхідно:
- запустити PostgreSQL сервер, наприклад використовуючи Docker
  ```
  docker run --name local-postgres -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres
  ```
- підключитися до сервера PostgreSQL будь-яким клієнтом та створити базу даних 
  ```
  CREATE DATABASE railwaytickets
  ```
- наразі в проекті на базі Java EE не імплементовано міграцію бази даних - для її запуску та наповнення див. 
  [railway-tickets with Spring](https://github.com/YuliiaLysak/railway-tickets/tree/main/railway-tickets-spring/README.md)
  
- запустити Tomcat 9.0.45

- скомпілювати проект
  ```
  mvn clean install
  ```
- задеплоїти отриманий артефакт в Tomcat ./target/railway-tickets-servlet-1.0-SNAPSHOT.war

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

