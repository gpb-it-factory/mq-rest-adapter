# MQ Rest Adapter

## Обзор

MQ Rest Adapter - это микросервис, который принимает запросы через RabbitMQ, валидирует их и вызывает внешний сервис
wallet для получения баланса кошелька. Этот сервис построен с использованием Java 17, Spring Boot, Spring OpenFeign и
RabbitMQ.

## Функции

- Прием запросов через RabbitMQ.
- Валидация входящих запросов.
- Вызов внешнего сервиса wallet для получения баланса кошелька.

## Требования

- Java 17
- Docker (для запуска Testcontainers)
- Gradle

## Сборка и запуск

Соберите проект:

```bash
./gradlew clean build
```
Запустите приложение:

```bash
./gradlew bootRun
```

## Тестирование

Для запуска интеграционных тестов, убедитесь, что у вас установлен Docker. Тесты используют Testcontainers для запуска
RabbitMQ и WireMock.

```bash
./gradlew test
```