# Dokumentacja Projektu Aleksandria

## 1. Opis projektu
**Aleksandria** to system backendowy do zarządzania biblioteką, zaimplementowany w architekturze REST. Aplikacja umożliwia zarządzanie cyklem życia książek, użytkowników oraz procesem wypożyczania. Kluczowym elementem systemu jest zaawansowany moduł bezpieczeństwa oparty na tokenach JWT oraz dynamicznej kontroli dostępu (RBAC) z uwzględnieniem hierarchii ról.

## 2. Stos technologiczny

Projekt wykorzystuje następujące technologie i biblioteki:

* **Język:** Java 25
* **Framework:** Spring Boot 4.0.0
* **Bezpieczeństwo:** Spring Security
* **Baza danych:** H2 Database (tryb in-memory `jdbc:h2:./library`) - *tymczasowo*
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

### Model bazy danych

<img width="1159" height="653" alt="Zrzut ekranu 2025-12-9 o 13 20 59" src="https://github.com/user-attachments/assets/ae569fe7-2dcf-4349-a97c-e0353bf45387" />


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

## 5. Punkty końcowe (endpointy)

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

## 6. Konfiguracja

Plik: `src/main/resources/application.properties`

```properties
spring.application.name=Aleksandria
# Baza danych H2 zapisywana w pliku lokalnym ./library
spring.datasource.url=jdbc:h2:./library;AUTO_SERVER=TRUE
# Schemat jest niszczony po zamknięciu aplikacji
spring.jpa.hibernate.ddl-auto=create-drop

# Konfiguracja JWT
# Wymaga ustawienia zmiennej środowiskowej lub podmiany wartości
application.security.jwt.secret-key=${INSERT_HERE}
application.security.jwt.expiration=864000000
```

## 7. Dane testowe

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
