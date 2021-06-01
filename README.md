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
