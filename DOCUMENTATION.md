# Dokumentacja Projektu Aleksandria

## 1. Opis projektu
**Aleksandria** to system backendowy do zarządzania biblioteką, zaimplementowany w architekturze REST. Aplikacja umożliwia zarządzanie cyklem życia książek, użytkowników oraz procesem wypożyczania. Kluczowym elementem systemu jest zaawansowany moduł bezpieczeństwa oparty na tokenach JWT oraz dynamicznej kontroli dostępu (RBAC) z uwzględnieniem hierarchii ról.

## 2. Wykorzystane technologie

* **Język:** Java 25
* **Framework:** Spring Boot 4.0.0
* **Bezpieczeństwo:** Spring Security
* **Baza danych:** PostgreSQL
* **Tokeny:** JJWT (Java JWT) - moduły `api`, `impl`, `jackson` w wersji 0.11.5
* **Redukcja kodu:** Project Lombok
* **Testy:** JUnit 5

## 3. Architektura i model danych

### Struktura klas
System opiera się na relacyjnym modelu danych z następującymi kluczowymi klasami:

1.  **User (`users`)**:
    * Przechowuje dane osobowe, adresowe oraz poświadczenia.
    * Relacja: *Many-to-One* z rolą (`Role`).
    * Unikalny adres email (login).
2.  **Role (`roles`)**:
    * Słownik ról systemowych: `READER`, `LIBRARIAN`, `ADMIN`.
3.  **Title (`title`)**:
    * Reprezentuje abstrakcyjny tytuł książki.
    * Zawiera autora i listę gatunków (`Genre`).
4.  **Book (`book`)**:
    * Fizyczny egzemplarz danego tytułu (`itemId`).
    * Posiada flagę dostępności (`available`).
5.  **Genre (`genre`)**:
    * Kategorie książek.
6.  **Rental (`rental`)** i **Queue (`queue`)**:
    * Obsługa procesów wypożyczania i kolejkowania.
7.  **Review (`review`)**:
    * Oceny (1-5 gwiazdek) i komentarze użytkowników do Tytułów.
8.  **Queue (`queue_entry`)**:
    * Kolejka oczekujących na niedostępne tytuły.

### Model bazy danych

<img width="1640" height="1007" alt="Aleksandria" src="https://github.com/user-attachments/assets/3fd4af19-1a37-40a0-bed9-690c81f541ea" />


## 4. Bezpieczeństwo

### Autentykacja
Proces logowania jest bezstanowy.
1.  Klient wysyła POST na `/auth/login`.
2.  Serwer weryfikuje hasło (BCrypt) i generuje token JWT ważny przez określony czas (konfigurowalne w `application.properties`).
3.  Token musi być przesyłany w nagłówku: `Authorization: Bearer <token>`.

### Autoryzacja 
System wykorzystuje dwa poziomy zabezpieczeń:

1.  **Role-Based Access Control (RBAC):**
    * Zabezpieczenie URL w `SecurityConfig`.
    * Adnotacje `@PreAuthorize("hasRole('...')")`.

2.  **Access Control Logic (ACL) - `RoleSecurity`:**
    * W projekcie zaimplementowano niestandardowy bean `RoleSecurity` do obsługi złożonych reguł biznesowych.
    * **Zasada hierarchii modyfikacji:**
        * **ADMIN**: Może modyfikować/usuwać każdego użytkownika.
        * **LIBRARIAN**: Może modyfikować/usuwać tylko użytkowników z rolą `READER`. Nie może modyfikować Administratorów ani innych Bibliotekarzy.
        * **READER**: Może modyfikować tylko własne dane (sprawdzane przez `#id == principal.id`).

## 5. Funkcjonalności Biznesowe

### System Powiadomień (MailService)
Aplikacja automatycznie wysyła wiadomości e-mail w następujących scenariuszach:
* **Rejestracja:** Powitanie nowego użytkownika.
* **Wypożyczenie:** Potwierdzenie wypożyczenia z terminem zwrotu.
* **Zwrot:** Potwierdzenie oddania książki.
* **Przypomnienia (Scheduler):** Automatyczne sprawdzanie opóźnień (codziennie o północy) i wysyłanie ponagleń do dłużników.

### Zarządzanie Zasobami
* Rozdział na **Tytuły** (logiczne pozycje) i **Książki** (fizyczne egzemplarze).
* Możliwość zarządzania gatunkami literackimi.

### Interakcje Użytkownika
* **Recenzje:** Czytelnicy mogą oceniać i komentować przeczytane tytuły.
* **Kolejkowanie:** Możliwość zapisu do kolejki, jeśli wszystkie egzemplarze danego tytułu są wypożyczone.

## 6. Punkty końcowe (endpointy)

Poniższa tabela przedstawia dostępne endpointy oraz wymagane uprawnienia.

### Autentykacja (`/auth`)
| Metoda | Ścieżka | Opis | Wymagane Uprawnienia |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | Rejestracja nowego użytkownika (domyślnie rola READER) | **Publiczny** |
| `POST` | `/auth/login` | Logowanie i pobranie tokena JWT | **Publiczny** |

### Użytkownicy (`/users`)
| Metoda | Ścieżka | Parametry | Opis | Wymagane Uprawnienia |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/users/all` | - | Lista wszystkich użytkowników | `LIBRARIAN`, `ADMIN` |
| `GET` | `/users/search/by_id` | `?id=X` | Pobranie użytkownika po ID | `LIBRARIAN`, `ADMIN` lub Właściciel konta |
| `GET` | `/users/search/by_email` | `?email=X` | Pobranie użytkownika po Emailu | `LIBRARIAN`, `ADMIN` lub Właściciel konta |
| `GET` | `/users/search/by_role` | `?role=X` | Pobranie listy wg roli | `LIBRARIAN`, `ADMIN` |
| `GET` | `/users/search/by_fullname`| `?firstName=X&lastName=Y` | Szukanie po imieniu i nazwisku | `LIBRARIAN`, `ADMIN` |
| `POST` | `/users/create` | `CreateUserRequest` | Tworzenie użytkownika z konkretną rolą | `ADMIN` (wszystkie role) <br> `LIBRARIAN` (nie może utworzyć Admina) |
| `PUT` | `/users/update` | `UpdateUserRequest` | Aktualizacja danych | Właściciel konta LUB hierarchia `RoleSecurity` |
| `DELETE`| `/users/delete` | `?id=X` | Usunięcie użytkownika | Zgodnie z hierarchią `RoleSecurity` |

### Tytuły (`/titles`)
| Metoda | Ścieżka | Parametry | Opis | Uprawnienia |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/titles/all` | - | Lista wszystkich tytułów | Zalogowany |
| GET | `/titles/search/by_id` | `?id=` | Pobranie tytułu po ID | Zalogowany |
| GET | `/titles/search/by_name` | `?name=` | Szukanie po nazwie | Zalogowany |
| GET | `/titles/search/by_author` | `?author=` | Szukanie po autorze | Zalogowany |
| GET | `/titles/search/by_genre` | `?genre=` | Szukanie po gatunku | Zalogowany |
| POST | `/titles/create` | Body: `CreateTitleRequest` | Dodanie nowego tytułu | LIBRARIAN, ADMIN |
| PUT | `/titles/update` | Body: `UpdateTitleRequest` | Edycja tytułu | LIBRARIAN, ADMIN |
| DELETE | `/titles/delete` | `?id=` | Usunięcie tytułu | LIBRARIAN, ADMIN |

### Książki / Egzemplarze (`/books`)
| Metoda | Ścieżka | Parametry | Opis | Uprawnienia |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/books/all` | - | Lista wszystkich egzemplarzy | Zalogowany |
| GET | `/books/search/by_id` | `?id=` | Pobranie egzemplarza po ID | Zalogowany |
| GET | `/books/search/by_name` | `?title=` | Dostępne egzemplarze danego tytułu | Zalogowany |
| GET | `/books/search/by_availability`| `?availability=` | Szukanie wg dostępności (true/false)| Zalogowany |
| POST | `/books/create` | Body: `Integer title_id` | Dodanie egzemplarza do tytułu | LIBRARIAN, ADMIN |
| PUT | `/books/update_availability` | `?id=&availability=` | Zmiana statusu dostępności | LIBRARIAN, ADMIN |
| DELETE | `/books/delete` | `?id=` | Usunięcie egzemplarza | LIBRARIAN, ADMIN |

### Wypożyczenia (`/rentals`)
| Metoda | Ścieżka | Parametry | Opis | Uprawnienia |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/rentals/all` | - | Wszystkie wypożyczenia | LIBRARIAN, ADMIN |
| GET | `/rentals/search/by_id` | `?id=` | Szczegóły wypożyczenia | LIBRARIAN, ADMIN |
| GET | `/rentals/search/by_user` | `?user_id=` | Historia wypożyczeń użytkownika | LIBRARIAN, ADMIN |
| GET | `/rentals/search/by_book` | `?book_id=` | Historia wypożyczeń egzemplarza | LIBRARIAN, ADMIN |
| GET | `/rentals/search/by_title` | `?title_id=` | Historia wypożyczeń tytułu | LIBRARIAN, ADMIN |
| GET | `/rentals/search/by_date` | `?date=` | Wypożyczenia z danego dnia | LIBRARIAN, ADMIN |
| GET | `/rentals/search/by_date_range`| `?start_date=&end_date=` | Raport z zakresu dat | LIBRARIAN, ADMIN |
| POST | `/rentals/create` | Body: `CreateRentalRequest` | Nowe wypożyczenie | LIBRARIAN, ADMIN |
| POST | `/rentals/return` | `?rental_id=` | Zwrot książki | LIBRARIAN, ADMIN |
| DELETE | `/rentals/delete` | `?id=` | Usunięcie rekordu wypożyczenia | LIBRARIAN, ADMIN |

### Recenzje (`/reviews`)
| Metoda | Ścieżka                      | Parametry | Opis | Uprawnienia |
| :--- |:-----------------------------| :--- | :--- | :--- |
| GET | `/reviews/byTitle`           | `?titleId=` | Pobranie recenzji dla tytułu | Zalogowany |
| POST | `/reviews/create`            | Body: `CreateReviewRequest` | Dodanie recenzji | READER |
| PUT | `/reviews/update/{reviewId}` | Body: `UpdateReviewRequest`| Zmiana recenzji | ADMIN, Właściciel |
| DELETE | `/reviews/delete`            | `?reviewId=` | Usunięcie recenzji | ADMIN |

### Kolejka (`/queue`)
| Metoda | Ścieżka | Parametry | Opis | Uprawnienia |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/queue/search` | `?title_id=` | Kto czeka na dany tytuł | LIBRARIAN, ADMIN |
| GET | `/queue/search` | `?user_id=` | Na co czeka dany użytkownik | LIBRARIAN, ADMIN, Właściciel |
| GET | `/queue/position` | `?title_id=&user_id=` | Pozycja w kolejce | LIBRARIAN, ADMIN, Właściciel |
| POST | `/queue/join` | Body: `QueueRequest` | Dołączenie do kolejki | LIBRARIAN, ADMIN, Właściciel |
| POST | `/queue/leave` | Body: `QueueRequest` | Opuszczenie kolejki | LIBRARIAN, ADMIN, Właściciel |

### Gatunki (`/genres`)
| Metoda | Ścieżka | Parametry | Opis | Uprawnienia |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/genres/all` | - | Lista gatunków | Zalogowany |
| GET | `/genres/search/by_id` | `?id=` | Pobranie po ID | Zalogowany |
| GET | `/genres/search/by_name` | `?name=` | Pobranie po nazwie | Zalogowany |
| POST | `/genres/create` | `?name=` | Dodanie gatunku | LIBRARIAN, ADMIN |
| PUT | `/genres/update` | Body: `UpdateGenreRequest` | Edycja gatunku | LIBRARIAN, ADMIN |
| DELETE | `/genres/delete` | `?id=` | Usunięcie gatunku | LIBRARIAN, ADMIN |

## 7. Konfiguracja

Plik: `src/main/resources/application.properties`

```properties
# W polach oznaczonych ${...} należy wstawić odpowiednie wartości konfiguracyjne.
# Nazwa aplikacji
spring.application.name=Aleksandria
# Konfiguracja bazy danych PostgreSQL
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update

# Konfiguracja JWT
application.security.jwt.secret-key=${SECRET_KEY}
application.security.jwt.expiration=864000000

# Konfiguracja MailService (wymagana do wysyłki emaili)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL}
spring.mail.password=${EMAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 8. Dane testowe

Przy uruchomieniu aplikacji (w profilu domyślnym) klasa `TestConfiguration` oraz `RoleConfiguration` automatycznie zasilają bazę danych H2 przykładowymi danymi, co pozwala na natychmiastowe testowanie API bez konieczności ręcznego tworzenia kont.

### Zainicjalizowane konta użytkowników

| Rola w Systemie | Imię i Nazwisko | Email (Login) | Hasło | Opis |
| :--- | :--- | :--- | :--- | :--- |
| **READER** | Jan Testowy | `jan.testowy@poczta.pl` | `test1234` | Standardowe konto czytelnika. Może wypożyczać książki. |
| **LIBRARIAN** | Adam Bibliotekarz | `adam.biliot@gmail.com` | `test1234` | Konto pracownika. Może zarządzać zasobami i czytelnikami. |
| **ADMIN** | Marta Admin | `admin@aleksandria.pl` | `adminpass` | Pełny dostęp administracyjny do systemu. |

> Hasła są przechowywane w bazie danych w formie zaszyfrowanej (BCrypt).

### Przykładowe zasoby biblioteczne
Do bazy dodawana jest również przykładowa pozycja:
* **Tytuł:** *Rok 1984*
* **Autor:** George Orwell
* **Gatunki:** Dystopia, Political
* **Egzemplarz:** Dostępny do wypożyczenia
