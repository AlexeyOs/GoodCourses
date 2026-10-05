# Инструкции для ИИ-агентов (GoodCourses)

GoodCourses — сайт отзывов на IT-курсы. Java 8 (компиляция), Spring MVC 5.2 + Spring Security + Spring Data JPA (Hibernate 5),
PostgreSQL, представления на JSP + JSTL + SiteMesh, сборка Maven в WAR. **Spring Boot НЕ используется.**

Читай этот файл целиком перед началом работы. Если правило здесь противоречит твоей привычке — следуй правилу.

## 1. Порядок работы над issue

1. Прочитай issue: `https://github.com/AlexeyOs/GoodCourses/issues/<номер>`. Если требование неоднозначно — спроси, не додумывай.
2. Создай ветку от свежего `master`: `git checkout master && git pull && git checkout -b codex/<короткое-имя>`.
3. Найди похожую уже реализованную функциональность (см. раздел 4) и делай **так же**: те же слои, те же имена, тот же стиль.
4. Вноси минимальные изменения, нужные для issue. Не переименовывай и не «улучшай» код вне задачи.
5. Проверь себя (раздел 6). Не пиши «готово», если сборка не прошла.
6. Закоммить (раздел 7). Не пушь и не создавай PR, пока об этом не попросили.

## 2. Команды

Все команды — из корня репозитория. Локально всегда используй профиль `-P windows` (подходит для любой ОС).

| Задача | Команда |
|---|---|
| Компиляция | `mvn -P windows clean compile` |
| Юнит-тесты (без БД) | `mvn -P windows test -Dtest=AboutMeFormTest` (перечисли нужные классы через запятую) |
| Сборка WAR | `mvn -P windows clean package -DskipTests` |
| Запуск | см. [docs/local-deployment.md](docs/local-deployment.md), шаг 4 |

Если `mvn`/`java` не найдены — смотри `AGENTS.local.md` (локальные пути, файл не в git).

## 3. Подводные камни (обязательно к прочтению)

- **Сборка перезаписывает `src/main/resources/application.properties`.** После любой команды `mvn` верни его:
  `git checkout src/main/resources/application.properties`. Этот файл никогда не должен попадать в коммит.
- **Не коммить локальные пути**: `src/main/resources/windows.properties` (`notification.config.path`, `elasticsearch.home`)
  и `MEDIA_DIR` в `TestDataGenerator.java` содержат пути конкретной машины. Не добавляй эти файлы в коммит,
  если задача не про них. Используй `git add <конкретные файлы>`, а не `git add -A` / `git add .`.
- **Тесты с Spring-контекстом не запускаются локально** (`CourseControllerTest`, `AuthControllerTest`, `CourseFilterTests`):
  `src/test/resources/test.properties` содержит путь Travis CI `/home/travis/...`. Ошибка
  `FileNotFoundException ... notifications.xml` в них — известная проблема, а не твоя ошибка. Не меняй `test.properties` ради этого.
- **Схема БД не создаётся Hibernate автоматически** (нет `hbm2ddl`). Если добавляешь/меняешь поле сущности:
  1) обнови `sql/goodcourses-db.sql`; 2) в описании изменений приложи SQL (`ALTER TABLE ...`) для уже существующей базы.
  Переименование поля в Java не требует изменения БД, если оставить старое имя в `@Column(name = "...")`.
- **Не трогай `src/main/webapp/WEB-INF/lib/`** — это копия зависимостей, её генерирует сборка.
- `src/main/webapp/media/` — загруженные картинки, в git не хранятся.
- `TestDataGenerator` **удаляет все данные** в базе. Не запускай его без явной просьбы.
- CSRF отключён (`SecurityConfig`), поэтому в формах не нужен CSRF-токен.
- Тексты интерфейса на русском, прямо в JSP. `i18n/messages.properties` используется только для сообщений валидации
  и хранится в `\uXXXX`-экранировании — при правке сохраняй этот формат.

## 4. Где что лежит

Пакет `net.os.goodcourses` в `src/main/java`:

| Слой | Где | Пример |
|---|---|---|
| Контроллеры (URL → view) | `controller/` | `FeedbackController`, `EditProfileController` |
| Сервисы: интерфейс + реализация | `service/` и `service/impl/*Impl` | `AddFeedBackService` → `AddFeedBackServiceImpl` |
| Репозитории Spring Data | `repository/storage/` | `CourseRepository` |
| JPA-сущности | `entity/` | `Course`, `FeedBack`, `Profile` |
| Формы ввода с валидацией | `form/` | `AboutMeForm` |
| Свои аннотации валидации | `annotation/constraints/`, `validator/` | `PasswordStrength` |
| Конфигурация Spring (Java-config, без XML) | `configuration/` | `SecurityConfig`, `MVCConfig` |
| Текущий пользователь | `util/SecurityUtil.getCurrentIdProfile()` | |

Веб-часть в `src/main/webapp`:

- Контроллер возвращает имя view, например `"courses"` → файл `WEB-INF/JSP/courses.jsp`. `"redirect:/path"` — редирект.
- Общий каркас страницы (шапка, меню, подвал) добавляет SiteMesh из `WEB-INF/template/page-template.jsp`.
  В JSP страницы пиши только её содержимое, без `<html>`/`<head>`.
- Переиспользуемые блоки — теги в `WEB-INF/tags/*.tag`, подключаются как `<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>`.
- Части страниц для подгрузки — `WEB-INF/JSP/fragment/`.
- Меню — `WEB-INF/tags/menu.tag` и `WEB-INF/section/nav.jsp`.
- CSS/JS/картинки — `static/` (Bootstrap 3 + jQuery); свой JS — `static/js/app.js`.

Доступ: URL `/my-profile`, `/profiles`, `/add/**`, `/edit/**`, `/remove` требуют входа (`SecurityConfig.configure(HttpSecurity)`).
Новые URL, требующие входа, добавляй туда же. В JSP проверка входа:
`<security:authorize access="hasAuthority('USER')" var="isUSer"/>`.

Образцы законченных фич (смотри их diff через `git show <hash>`):
- `c8eae34` — поле профиля «Обо мне»: сущность, форма, сервис, контроллер, JSP, тег, тест.
- `dbeda42` — CRUD навыков профиля, включая изменение `sql/goodcourses-db.sql` и JS.

## 5. Стиль кода

- Повторяй стиль окружающего файла: в одних файлах отступы табами, в других пробелами — не перемешивай внутри файла.
- Внедрение зависимостей: как в соседнем коде (`@Autowired` на поле или Lombok `@AllArgsConstructor` в конструктор).
- Синтаксис Java не новее 8: без `var`, `record`, text blocks, `List.of`.
- Логгер: `private static final Logger LOGGER = LoggerFactory.getLogger(<Класс>.class);` (slf4j).
- Бизнес-логику клади в сервис, а не в контроллер или JSP.
- Не добавляй новые зависимости в `pom.xml` без необходимости и явного упоминания этого в отчёте.

## 6. Проверка перед сдачей

1. `mvn -P windows clean compile` — должно быть `BUILD SUCCESS`.
2. Если менял формы, валидацию, утилиты или сервисы — добавь/обнови юнит-тест в `src/test/java` **без Spring-контекста**
   (образец: `AboutMeFormTest`) и запусти его: `mvn -P windows test -Dtest=<ИмяТеста>`.
3. Если менял JSP или поведение страниц — собери WAR, запусти приложение (docs/local-deployment.md)
   и открой нужную страницу на http://localhost:8080. Ошибки JSP видны только при открытии страницы.
4. `git checkout src/main/resources/application.properties`
5. `git status` и `git diff --staged` — в коммите только файлы задачи (см. раздел 3).

## 7. Коммиты и PR

- Сообщение коммита: Conventional Commits на английском — `feat: ...`, `fix: ...`, `docs: ...`, `test: ...`.
- В описании PR: `Closes #<номер issue>`, что сделано, как проверено, SQL-миграция (если менялась схема).
- В отчёте пользователю перечисли изменённые файлы и честно укажи, что не удалось проверить.
