# Railway tickets (Залізнична каса)

### Project for searching and purchasing railway tickets:


### [with Spring](https://github.com/YuliiaLysak/railway-tickets/tree/main/railway-tickets-spring)


### [with Java EE (Servlet)](https://github.com/YuliiaLysak/railway-tickets/tree/main/railway-tickets-servlet)

### Database UML diagram
![Database UML diagram](https://github.com/YuliiaLysak/railway-tickets/blob/main/DATABASE:railwaytickets.png?raw=true)


### Опис проекту:
Адміністратор може додавати/видаляти/редагувати список Станцій і залізничних Маршрутів між ними.

Маршрут містить інформацію:
- початкова станція і час відправлення;
- кінцева станція і час прибуття.

Користувач може здійснювати пошук маршрутів між станціями, які його цікавлять.
Результатом пошуку є список потягів, кожна стрічка якого містить:
- номер потягу;
- час/дату і станцію відправлення;
- час в дорозі;
- час/дату і станцію прибуття;
- кількість вільних місць;
- вартість проїзду;
- посилання на сторінку перегляду відповідного маршруту (користувач може переглянути інформацію про маршрут).

Якщо користувач зареєстрований в системі, то він повинен мати можливість купити білет на вибраний маршрут на вказану дату (за умови наявності вільних місць).

### Main page
![Main page](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/main-page-unauthorized.png?raw=true)

### Search result
![Search result](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/search-result.png?raw=true)

### Route information - for unauthorized users
![Route info unauthorized](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/route-info-unauthorized.png?raw=true)

### Route information with opportunity to buy a ticket - for authorized users
![Route info authorized](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/buying-ticket.png?raw=true)

### Sign in page
![Sign in](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/sign-in.png?raw=true)

### Stations page for admin
![Stations for admin](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/admin-page-stations.png?raw=true)

### Routes page for admin
![Routes for admin](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/admin-page-routes.png?raw=true)

### Access forbidden for authorized user
![403 forbidden](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/403-forbidden-error-page.png?raw=true)

### Page not found
![404 not found](https://github.com/YuliiaLysak/railway-tickets/tree/main/doc/404-not-found-error-page.png?raw=true)
