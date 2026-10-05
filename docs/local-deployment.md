# Локальный запуск GoodCourses

Инструкция описывает запуск приложения на своём компьютере без Docker:
PostgreSQL + сборка Maven + запуск WAR через `webapp-runner` (так же, как на Heroku).

## 1. Что нужно установить

| Инструмент | Версия | Комментарий |
|---|---|---|
| JDK | 11 | Проект компилируется под Java 8 (`source/target 1.8`), проверено на JDK 11 |
| Maven | 3.6+ | Проверено на 3.8.8 |
| PostgreSQL | 11 | Дамп схемы снят с 11.1 |

Убедитесь, что команды доступны:

```bash
java -version
mvn -v
psql --version
```

## 2. База данных

Приложение ожидает базу `goodcourses` на `localhost:5432`
с пользователем `goodcourses` / паролем `1234` (см. `src/main/resources/windows.properties`).

Создайте пользователя и базу (от имени суперпользователя `postgres`):

```bash
psql -U postgres -h localhost -c "CREATE ROLE goodcourses LOGIN PASSWORD '1234';"
psql -U postgres -h localhost -c "CREATE DATABASE goodcourses OWNER goodcourses;"
```

Загрузите схему:

```bash
psql -U goodcourses -h localhost -d goodcourses -f sql/goodcourses-db.sql
```

`sql/goodcourses-db.sql` создаёт таблицы и заполняет только справочник `skill_category`.
Курсы, профили и отзывы создаются генератором тестовых данных (шаг 5).

## 3. Сборка

Профиль конфигурации выбирается Maven-профилем. По умолчанию активен `heroku`,
он ищет базу на хосте `db_postgre`, поэтому для локального запуска используйте профиль `windows`
(несмотря на название, он подходит для любой ОС — в нём `localhost`):

```bash
mvn -P windows clean package -DskipTests
```

Результат: `target/GoodCourses.war` и `target/dependency/webapp-runner.jar`.

> **Важно.** Сборка перезаписывает `src/main/resources/application.properties`
> (туда записывается выбранный профиль). Не коммитьте этот файл, верните его так:
> `git checkout src/main/resources/application.properties`

### Путь к шаблонам уведомлений

При старте приложение читает файл шаблонов писем по **абсолютному** пути из
`notification.config.path` в `src/main/resources/windows.properties`.
Если путь не существует, приложение не запустится. Укажите путь к своей копии репозитория, например:

```properties
notification.config.path=C:/work/GoodCourses/src/main/webapp/WEB-INF/notifications/notifications.xml
```

После изменения пересоберите проект. Это локальная настройка — не коммитьте её.

## 4. Запуск

Приложению нужны переменные окружения с секретами. Для локального запуска подойдут любые значения —
сайт заработает, но отправка писем и вход через Facebook работать не будут.

| Переменная | Назначение |
|---|---|
| `SMTP_USER_NAME`, `SMTP_PASSWORD` | Учётная запись SMTP для отправки писем |
| `FACEBOOK_SECRET` | Секрет приложения Facebook для входа через соцсеть |

Bash (Linux, macOS, Git Bash):

```bash
export SMTP_USER_NAME=local SMTP_PASSWORD=local FACEBOOK_SECRET=local
java -jar target/dependency/webapp-runner.jar --port 8080 target/GoodCourses.war
```

PowerShell:

```powershell
$env:SMTP_USER_NAME="local"; $env:SMTP_PASSWORD="local"; $env:FACEBOOK_SECRET="local"
java -jar target/dependency/webapp-runner.jar --port 8080 target/GoodCourses.war
```

Приложение готово, когда в логе появится `Starting ProtocolHandler ["http-nio-8080"]`.
Откройте http://localhost:8080 — должна открыться страница со списком курсов.

## 5. Тестовые данные (необязательно)

Без данных список курсов будет пустым. Заполнить базу можно генератором
`src/test/java/net/os/goodcourses/testenv/TestDataGenerator.java`.

> **Внимание.** Генератор **удаляет** все профили, курсы, отзывы и навыки из базы
> и очищает папку `src/main/webapp/media`, затем создаёт данные заново.

1. В `TestDataGenerator.java` укажите в `MEDIA_DIR` абсолютный путь к папке
   `src/main/webapp/media` своей копии репозитория (локальная правка, не коммитьте её).
2. Запустите генератор из корня репозитория (база должна быть запущена):

```bash
mvn -P windows test-compile exec:java -Dexec.mainClass="net.os.goodcourses.testenv.TestDataGenerator" -Dexec.classpathScope="test"
```

## Частые ошибки

| Симптом в логе | Причина | Решение |
|---|---|---|
| `Could not resolve placeholder 'SMTP_USER_NAME'` | Не заданы переменные окружения | Шаг 4 |
| Ошибка подключения к БД, в тексте `db_postgre` | Сборка без `-P windows` | Шаг 3 |
| `Connection refused` к `localhost:5432` | Не запущен PostgreSQL | Шаг 2 |
| Ошибка загрузки, в тексте `notifications.xml` | Неверный `notification.config.path` | Шаг 3 |
| `Address already in use` | Порт 8080 занят | Запустите с другим `--port` и откройте соответствующий адрес |
