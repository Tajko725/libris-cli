# Libris CLI

Konsolowa aplikacja do zarządzania biblioteką, napisana w Javie. To pierwszy z trzech projektów, w których uczę się Javy: od podstaw w konsoli, przez własną strukturę klas i zapis do plików, aż po aplikację w Spring Boot z bazą danych.

## Ścieżka nauki

| Etap | Projekt | Zakres | Status |
|------|---------|--------|--------|
| 1/3 | **libris-cli** (ten projekt) | podstawy Javy, menu w konsoli, CRUD w pamięci | ukończony (v1.0.1) |
| 2/3 | [libris-core](https://github.com/Tajko725/libris-core) | podział na klasy i pakiety, zapis danych do pliku JSON | ukończony (v1.0.0) |
| 3/3 | [libris-api](https://github.com/Tajko725/libris-api)| REST API, Spring Boot, baza danych | ukończony (v1.0.0) |

## Funkcje

- [x] menu główne z bezpiecznym odczytem wyboru
- [x] lista książek w formie tabeli, z przykładowymi danymi
- [x] dodawanie książki z walidacją (puste pola, zakres roku, duplikaty)
- [x] edycja książki (pusty Enter zostawia bieżącą wartość)
- [x] usuwanie książki z potwierdzeniem (wypożyczonej nie można usunąć)
- [x] wyszukiwanie po tytule i autorze (bez względu na wielkość liter i polskie znaki)
- [x] wypożyczanie i zwrot książek (tabela pokazuje tylko pasujące pozycje, z numerami z pełnej listy)

## Technologie

| Narzędzie | Wersja |
|-----------|--------|
| Java (JDK) | 25 (25.0.1) |
| Maven | 3 (wbudowany w IntelliJ IDEA), wtyczki: maven-compiler-plugin 3.14.0, maven-jar-plugin 3.4.2 |
| IntelliJ IDEA | 2026.2.3 |

## Uruchomienie

### Gotowy plik JAR

Gotowy plik można pobrać ze strony [Releases](https://github.com/Tajko725/libris-cli/releases). Do uruchomienia wystarczy Java 25:

```
java -jar libris-cli-1.0.1.jar
```

### Budowanie ze źródeł

Projekt otwieram w IntelliJ IDEA i w panelu Maven wybieram `Lifecycle` → `package`. Plik powstaje w `target/libris-cli-1.0.1.jar`. Program można też uruchomić bezpośrednio z IDE, uruchamiając klasę `Main`.

## Organizacja pracy

Pracuję według GitFlow: stabilne wersje trafiają do `main`, bieżący rozwój do `develop`, a każda funkcja powstaje na osobnym branchu `feature/*` i wraca do `develop` przez merge z `--no-ff`, dzięki czemu każda funkcja jest widoczna w historii jako osobna gałąź. Pull Requesty zacząłem stosować w etapie 2 ([libris-core](https://github.com/Tajko725/libris-core)), a w tym projekcie scalałem branche lokalnie. Poprawkę 1.0.1 przeprowadziłem już przez Pull Requesty. Commity piszę po polsku, z prefiksami z konwencji Conventional Commits (`feat:`, `fix:`, `docs:`, `chore:`).

## Znane ograniczenia

Dane przechowuję tylko w pamięci, więc po zamknięciu programu znikają. Zapis do pliku JSON jest już w etapie 2 ([libris-core](https://github.com/Tajko725/libris-core)).

Dane trzymam w czterech równoległych listach (tytuły, autorzy, lata, dostępność), a numer książki to jej pozycja na liście, więc po usunięciu książki numery się przesuną. To celowy punkt wyjścia, w etapie 2 zastąpiłem je klasą `Book`. Tabela zakłada tytuły do 32 znaków i autorów do 28, dłuższe teksty rozjeżdżają kolumny.

Wypożyczenie zapisuje tylko status książki, bez informacji o czytelniku i terminie zwrotu. Czytelników, historię wypożyczeń i terminy dodałem w etapie 2.