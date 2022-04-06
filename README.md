[![Build Status](https://app.travis-ci.com/Xazeq/job4j_chat.svg?branch=master)](https://app.travis-ci.com/Xazeq/job4j_chat)

![](https://img.shields.io/badge/Maven-=_3-red)
![](https://img.shields.io/badge/Java-=_14-orange)
![](https://img.shields.io/badge/Spring-=_5-darkorange)
![](https://img.shields.io/badge/PostgerSQL-=_9-blue)
![](https://img.shields.io/badge/JUnit-=_4-yellowgreen)
![](https://img.shields.io/badge/Mockito-brightgreen)
![](https://img.shields.io/badge/JaCoCo-c75a28)
![](https://img.shields.io/badge/Checkstyle-lightgrey)

## Описание
Чат на REST API, реализованный с использованием Spring Boot.
В системе используется авторизация по токену. При регистрации пользователю выдается токен и с использованием этого токена пользователь может отправлять запросы в систему.

## Технологии
* Java 14
* PostgreSQL
* Hibernate
* Spring Boot 2
* Spring Data JPA
* Spring Security
* JWT

## Использование

### Роли

`GET /roles/` - получить все роли

`GET /roles/{id}` - получить роль по её id

`POST /roles/` - создать роль

`PUT /roles/` - обновить данные роли или создать новую

`PATCH /roles/` - обновить данные существующей роли

`DELETE /roles/{id}` - удалить роль по её id

### Пользователи

`GET /persons/` - получить всех пользователей

`GET /persons/{id}` - получить пользователя по его id

`POST /persons/` - создать пользователя

`POST /login` - получить токен пользователя (в теле передает username и password созданного пользователя)

`PUT /persons/` - обновить данные пользователя или создать нового

`PATCH /persons/` - обновить данные существующего пользователя

`PUT /persons/{personId}/addRoom/{roomId}` - добавить комнату пользователю (зайти в комнату) по id пользователя

`DELETE /persons/{id}` - удалить пользователя по его id

### Комнаты

`GET /rooms/` - получить все комнаты

`GET /rooms/{id}` - получить комнату по её id

`POST /rooms/` - создать комнату

`PUT /rooms/` - обновить данные комнаты или создать новую

`PATCH /rooms/` - обновить данные существующей комнаты

`DELETE /rooms/{id}` - удалить комнату по её id

`PUT /rooms/{roomId}/addMessage` - добавить новое сообщение

### Сообщения

`GET /messages/` - получить все сообщения

`GET /messages/{id}` - получить сообщение по его id

`POST /messages/` - создать сообщение

`PUT /messages/` - обновить данные сообщения или создать новое

`PATCH /messages/` - обновить данные существующего сообщения

`DELETE /messages/{id}` - удалить сообщение по его id
