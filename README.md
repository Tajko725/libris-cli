# Libris CLI

Konsolowa aplikacja do zarządzania biblioteką, napisana w Javie. To pierwszy z trzech projektów, w których uczę się Javy: od podstaw w konsoli, przez własną strukturę klas i zapis do plików, aż po aplikację w Spring Boot z bazą danych.

## Ścieżka nauki

| Etap | Projekt | Zakres | Status |
|------|---------|--------|--------|
| 1/3 | **libris-cli** (ten projekt) | podstawy Javy, menu w konsoli, CRUD w pamięci | w trakcie |
| 2/3 | libris-core | podział na klasy i pakiety, zapis danych do pliku JSON | planowany |
| 3/3 | libris-api | REST API, Spring Boot, baza danych | planowany |

Linki do kolejnych etapów dodam, gdy powstaną repozytoria.

## Funkcje

- [x] menu główne z bezpiecznym odczytem wyboru
- [ ] lista książek
- [ ] dodawanie książki
- [ ] edycja i usuwanie książki
- [ ] wyszukiwanie po tytule i autorze
- [ ] wypożyczanie i zwrot książek

## Technologie

| Narzędzie | Wersja |
|-----------|--------|
| Java (JDK) | 25 (25.0.1) |
| Maven | wtyczki: maven-compiler-plugin 3.14.0, maven-jar-plugin 3.4.2 |
| IntelliJ IDEA | 2026.2.3 |

## Uruchomienie

Wymagane są JDK 25 i Maven.

```
mvn package
java -jar target/libris-cli-0.1.0-SNAPSHOT.jar
```

Program można też uruchomić z IntelliJ IDEA, uruchamiając klasę `Main`.

## Organizacja pracy

Pracuję według GitFlow: stabilne wersje trafiają do `main`, bieżący rozwój do `develop`, a każda funkcja powstaje na osobnym branchu `feature/*` i wraca do `develop` przez Pull Request. Commity piszę po polsku, z prefiksami z konwencji Conventional Commits (`feat:`, `fix:`, `docs:`, `chore:`).

## Znane ograniczenia

Dane przechowuję tylko w pamięci, więc po zamknięciu programu znikają. Zapis do pliku pojawi się w etapie 2.