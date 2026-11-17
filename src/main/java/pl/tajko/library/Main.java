package pl.tajko.library;

import java.text.Normalizer;
import java.util.ArrayList;
import java.time.Year;
import java.util.List;
import java.util.Scanner;

/**
 * Główna klasa aplikacji Libris CLI.
 * Przechowuje dane biblioteki w pamięci, wyświetla menu główne
 * i obsługuje wybór opcji użytkownika.
 */
public class Main {

    /**
     * Wspólny obiekt do odczytu danych z konsoli.
     * Nie zamykam go celowo, bo zamknięcie zamknęłoby też System.in.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /** Najwcześniejszy dopuszczalny rok wydania książki (początki druku w Europie). */
    private static final int MIN_YEAR = 1450;

    /** Wartość zwracana, gdy użytkownik anulował wybór albo nie ma z czego wybierać. */
    private static final int NO_SELECTION = -1;

    /**
     * Tytuły książek. Jedną książkę opisują elementy o tym samym indeksie
     * we wszystkich czterech listach (titles, authors, years, available).
     */
    private static final List<String> titles = new ArrayList<>();

    /** Autorzy książek. */
    private static final List<String> authors = new ArrayList<>();

    /** Lata wydania książek. */
    private static final List<Integer> years = new ArrayList<>();

    /** Dostępność książek: true oznacza dostępną, false wypożyczoną. */
    private static final List<Boolean> available = new ArrayList<>();

    /**
     * Punkt wejścia programu. Wczytuje dane startowe i pokazuje menu w pętli,
     * aż użytkownik wybierze opcję wyjścia.
     *
     * @param args argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {
        loadSampleData();

        System.out.println("=== Libris - biblioteka w konsoli ===");
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Wybierz opcję: ");

            switch (choice) {
                case 1 -> printBooks();
                case 2 -> addBook();
                case 3 -> editBook();
                case 4 -> deleteBook();
                case 5 -> searchBooks();
                case 6 -> borrowBook();
                case 7 -> returnBook();
                case 0 -> running = false;
                default -> System.out.println("Nie ma takiej opcji, wybierz numer z menu.");
            }
            System.out.println();
        }

        System.out.println("Do zobaczenia!");
    }

    /**
     * Wypisuje w konsoli listę opcji menu głównego.
     */
    private static void printMenu() {
        System.out.println("1. Wyświetl wszystkie książki");
        System.out.println("2. Dodaj książkę");
        System.out.println("3. Edytuj książkę");
        System.out.println("4. Usuń książkę");
        System.out.println("5. Szukaj książek");
        System.out.println("6. Wypożycz książkę");
        System.out.println("7. Zwróć książkę");
        System.out.println("0. Wyjście");
    }

    /**
     * Wypisuje wszystkie książki w formie tabeli z równymi kolumnami.
     * Gdy biblioteka jest pusta, wyświetla odpowiedni komunikat.
     * Numer w pierwszej kolumnie to pozycja książki na liście, licząc od 1.
     */
    private static void printBooks() {
        if (titles.isEmpty()) {
            System.out.println("Biblioteka jest pusta.");
            return;
        }

        printTableHeader();
        for (int i = 0; i < titles.size(); i++) {
            printBookRow(i);
        }
    }

    /**
     * Wypisuje nagłówek tabeli książek wraz z linią oddzielającą.
     */
    private static void printTableHeader() {
        System.out.printf("%-4s %-32s %-28s %-6s %s%n", "Nr", "Tytuł", "Autor", "Rok", "Status");
        System.out.println("-".repeat(85));
    }

    /**
     * Wypisuje jeden wiersz tabeli z danymi wskazanej książki.
     *
     * @param index indeks książki na liście (liczony od 0), w tabeli wyświetlany jako numer od 1
     */
    private static void printBookRow(int index) {
        String status = available.get(index) ? "dostępna" : "wypożyczona";
        System.out.printf("%-4d %-32s %-28s %-6d %s%n",
                index + 1, titles.get(index), authors.get(index), years.get(index), status);
    }

    /**
     * Wypisuje w tabeli tylko książki o wskazanej dostępności. Numery w pierwszej kolumnie
     * to numery z pełnej listy. Gdy nie ma żadnej pasującej książki, nic nie wypisuje.
     *
     * @param wantedAvailable true, aby pokazać książki dostępne, false, aby pokazać wypożyczone
     * @return liczba wypisanych książek
     */
    private static int printBooksByStatus(boolean wantedAvailable) {
        int count = 0;
        for (int i = 0; i < titles.size(); i++) {
            if (available.get(i) == wantedAvailable) {
                if (count == 0) {
                    printTableHeader();
                }
                printBookRow(i);
                count++;
            }
        }
        return count;
    }

    /**
     * Dodaje nową książkę do biblioteki. Pyta o tytuł, autora i rok wydania,
     * sprawdza poprawność danych i odrzuca duplikaty. Nowa książka jest dostępna.
     */
    private static void addBook() {
        System.out.println("--- Dodawanie książki ---");

        String title = readNonEmptyText("Tytuł: ");
        String author = readNonEmptyText("Autor: ");

        if (bookExists(title, author)) {
            System.out.println("Taka książka już jest w bibliotece, nie dodano duplikatu.");
            return;
        }

        int currentYear = Year.now().getValue();
        int year = readIntInRange("Rok wydania: ", MIN_YEAR, currentYear);

        addBookData(title, author, year, true);
        System.out.println("Dodano książkę o numerze " + titles.size() + ".");
    }

    /**
     * Edytuje wybraną książkę. Pokazuje tabelę, pyta o numer książki i o nowe wartości pól.
     * Pusty wpis (sam Enter) zostawia bieżącą wartość. Odrzuca zmianę,
     * jeśli inna książka ma już taki sam tytuł i autora.
     */
    private static void editBook() {
        if (titles.isEmpty()) {
            System.out.println("Biblioteka jest pusta, nie ma czego edytować.");
            return;
        }

        printBooks();
        int number = readIntInRange("Podaj numer książki do edycji (0 = anuluj): ", 0, titles.size());
        if (number == 0) {
            System.out.println("Anulowano edycję.");
            return;
        }
        int index = number - 1;

        System.out.println("Edycja książki nr " + number + ". Enter zostawia bieżącą wartość.");
        String newTitle = readTextOrKeep("Tytuł", titles.get(index));
        String newAuthor = readTextOrKeep("Autor", authors.get(index));

        if (bookExists(newTitle, newAuthor, index)) {
            System.out.println("Inna książka ma już taki tytuł i autora, nie zapisano zmian.");
            return;
        }

        int newYear = readYearOrKeep(years.get(index));

        titles.set(index, newTitle);
        authors.set(index, newAuthor);
        years.set(index, newYear);
        System.out.println("Zapisano zmiany w książce nr " + number + ".");
    }

    /**
     * Usuwa wybraną książkę. Pokazuje tabelę, pyta o numer książki i o potwierdzenie.
     * Nie pozwala usunąć książki, która jest aktualnie wypożyczona.
     * Po usunięciu numery kolejnych książek przesuwają się o jeden w dół.
     */
    private static void deleteBook() {
        if (titles.isEmpty()) {
            System.out.println("Biblioteka jest pusta, nie ma czego usuwać.");
            return;
        }

        printBooks();
        int number = readIntInRange("Podaj numer książki do usunięcia (0 = anuluj): ", 0, titles.size());
        if (number == 0) {
            System.out.println("Anulowano usuwanie.");
            return;
        }
        int index = number - 1;

        if (!available.get(index)) {
            System.out.println("Nie można usunąć książki, która jest aktualnie wypożyczona.");
            return;
        }

        boolean confirmed = readYesNo("Czy na pewno usunąć \"" + titles.get(index) + "\"? (t/n): ");
        if (!confirmed) {
            System.out.println("Anulowano usuwanie.");
            return;
        }

        removeBookData(index);
        System.out.println("Usunięto książkę. Numery kolejnych książek zostały przesunięte.");
    }

    /**
     * Wyszukuje książki po fragmencie tytułu lub autora. Wielkość liter i polskie znaki
     * diakrytyczne nie mają znaczenia. Wyniki są pokazane w tabeli z numerami z pełnej listy,
     * dzięki czemu pasują do numerów używanych przy edycji i usuwaniu.
     * Pusty wpis anuluje wyszukiwanie.
     */
    private static void searchBooks() {
        System.out.print("Szukana fraza w tytule lub autorze (Enter = anuluj): ");
        String phrase = scanner.nextLine().trim();
        if (phrase.isEmpty()) {
            System.out.println("Anulowano wyszukiwanie.");
            return;
        }

        String normalizedPhrase = normalize(phrase);
        int found = 0;

        for (int i = 0; i < titles.size(); i++) {
            boolean matches = normalize(titles.get(i)).contains(normalizedPhrase)
                    || normalize(authors.get(i)).contains(normalizedPhrase);
            if (matches) {
                if (found == 0) {
                    printTableHeader();
                }
                printBookRow(i);
                found++;
            }
        }

        if (found == 0) {
            System.out.println("Nie znaleziono książek pasujących do frazy \"" + phrase + "\".");
        } else {
            System.out.println("Znaleziono: " + found);
        }
    }

    /**
     * Wypożycza książkę. Pokazuje tylko dostępne książki i prosi o numer jednej z nich.
     * Wybrana książka dostaje status wypożyczonej.
     */
    private static void borrowBook() {
        int count = printBooksByStatus(true);
        if (count == 0) {
            System.out.println("Brak dostępnych książek do wypożyczenia.");
            return;
        }

        int index = readBookNumberWithStatus(true, "Podaj numer książki do wypożyczenia (0 = anuluj): ");
        if (index == NO_SELECTION) {
            System.out.println("Anulowano wypożyczenie.");
            return;
        }

        available.set(index, false);
        System.out.println("Wypożyczono książkę: " + titles.get(index));
    }

    /**
     * Przyjmuje zwrot książki. Pokazuje tylko wypożyczone książki i prosi o numer jednej z nich.
     * Wybrana książka dostaje status dostępnej.
     */
    private static void returnBook() {
        int count = printBooksByStatus(false);
        if (count == 0) {
            System.out.println("Żadna książka nie czeka na zwrot.");
            return;
        }

        int index = readBookNumberWithStatus(false, "Podaj numer zwracanej książki (0 = anuluj): ");
        if (index == NO_SELECTION) {
            System.out.println("Anulowano zwrot.");
            return;
        }

        available.set(index, true);
        System.out.println("Zwrócono książkę: " + titles.get(index));
    }

    /**
     * Sprawdza, czy w bibliotece jest już książka o podanym tytule i autorze.
     * Porównanie nie rozróżnia wielkości liter.
     *
     * @param title  szukany tytuł
     * @param author szukany autor
     * @return true, jeśli taka książka już istnieje
     */
    private static boolean bookExists(String title, String author) {
        return bookExists(title, author, -1);
    }

    /**
     * Sprawdza, czy w bibliotece jest książka o podanym tytule i autorze,
     * pomijając książkę na wskazanej pozycji. Przydaje się przy edycji,
     * żeby książka nie była uznana za duplikat samej siebie.
     *
     * @param title        szukany tytuł
     * @param author       szukany autor
     * @param ignoredIndex indeks pomijanej książki, -1 oznacza brak pomijania
     * @return true, jeśli istnieje inna książka o takim tytule i autorze
     */
    private static boolean bookExists(String title, String author, int ignoredIndex) {
        for (int i = 0; i < titles.size(); i++) {
            if (i == ignoredIndex) {
                continue;
            }
            if (titles.get(i).equalsIgnoreCase(title) && authors.get(i).equalsIgnoreCase(author)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Wczytuje przykładowe książki, żeby program od razu miał co pokazać.
     * Dane nie są zapisywane na dysku, więc po zamknięciu programu znikają.
     */
    private static void loadSampleData() {
        addBookData("Pan Tadeusz", "Adam Mickiewicz", 1834, true);
        addBookData("Lalka", "Bolesław Prus", 1890, false);
        addBookData("Solaris", "Stanisław Lem", 1961, true);
        addBookData("Wiedźmin: Ostatnie życzenie", "Andrzej Sapkowski", 1993, true);
        addBookData("Hobbit", "J.R.R. Tolkien", 1937, true);
    }

    /**
     * Zapisuje dane jednej książki we wszystkich czterech listach.
     * Dzięki temu listy zawsze mają tę samą długość.
     *
     * @param title       tytuł książki
     * @param author      autor książki
     * @param year        rok wydania
     * @param isAvailable true, jeśli książka jest dostępna do wypożyczenia
     */
    private static void addBookData(String title, String author, int year, boolean isAvailable) {
        titles.add(title);
        authors.add(author);
        years.add(year);
        available.add(isAvailable);
    }

    /**
     * Usuwa dane jednej książki ze wszystkich czterech list.
     * Dzięki temu listy zawsze mają tę samą długość.
     *
     * @param index indeks usuwanej książki (liczony od 0)
     */
    private static void removeBookData(int index) {
        titles.remove(index);
        authors.remove(index);
        years.remove(index);
        available.remove(index);
    }

    /**
     * Wypisuje komunikat i odczytuje z konsoli liczbę całkowitą.
     * Pyta ponownie, dopóki użytkownik nie poda poprawnej liczby.
     *
     * @param prompt tekst wyświetlany przed odczytem
     * @return liczba wpisana przez użytkownika
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("To nie jest liczba, spróbuj jeszcze raz.");
            }
        }
    }

    /**
     * Wypisuje komunikat i odczytuje z konsoli niepusty tekst.
     * Spacje na początku i na końcu są usuwane. Pyta ponownie, dopóki tekst jest pusty.
     *
     * @param prompt tekst wyświetlany przed odczytem
     * @return niepusty tekst wpisany przez użytkownika
     */
    private static String readNonEmptyText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Wartość nie może być pusta.");
        }
    }

    /**
     * Wypisuje komunikat i odczytuje liczbę całkowitą z podanego zakresu (włącznie z krańcami).
     * Pyta ponownie, dopóki liczba nie mieści się w zakresie.
     *
     * @param prompt tekst wyświetlany przed odczytem
     * @param min    najmniejsza dopuszczalna wartość
     * @param max    największa dopuszczalna wartość
     * @return liczba z zakresu wpisana przez użytkownika
     */
    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Podaj liczbę z zakresu od " + min + " do " + max + ".");
        }
    }

    /**
     * Wypisuje etykietę z bieżącą wartością w nawiasie kwadratowym i odczytuje nowy tekst.
     * Pusty wpis oznacza pozostawienie bieżącej wartości.
     *
     * @param label   nazwa pola wyświetlana użytkownikowi
     * @param current bieżąca wartość pola
     * @return nowy tekst albo bieżąca wartość, gdy użytkownik nic nie wpisał
     */
    private static String readTextOrKeep(String label, String current) {
        System.out.print(label + " [" + current + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? current : input;
    }

    /**
     * Odczytuje rok wydania z możliwością pozostawienia bieżącej wartości.
     * Pusty wpis zostawia bieżący rok, a niepoprawny wpis wywołuje ponowne pytanie.
     *
     * @param current bieżący rok wydania
     * @return nowy rok z przedziału od MIN_YEAR do bieżącego roku albo dotychczasowy rok
     */
    private static int readYearOrKeep(int current) {
        int maxYear = Year.now().getValue();
        while (true) {
            System.out.print("Rok wydania [" + current + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return current;
            }
            try {
                int year = Integer.parseInt(input);
                if (year >= MIN_YEAR && year <= maxYear) {
                    return year;
                }
                System.out.println("Podaj liczbę z zakresu od " + MIN_YEAR + " do " + maxYear + ".");
            } catch (NumberFormatException e) {
                System.out.println("To nie jest liczba, spróbuj jeszcze raz.");
            }
        }
    }

    /**
     * Wypisuje pytanie i odczytuje odpowiedź tak lub nie (t/n, wielkość liter nie ma znaczenia).
     * Pyta ponownie, dopóki odpowiedź nie jest poprawna.
     *
     * @param prompt pytanie wyświetlane użytkownikowi
     * @return true dla odpowiedzi "t", false dla odpowiedzi "n"
     */
    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("t")) {
                return true;
            }
            if (input.equals("n")) {
                return false;
            }
            System.out.println("Wpisz t (tak) albo n (nie).");
        }
    }

    /**
     * Przygotowuje tekst do porównywania przy wyszukiwaniu: zamienia litery na małe
     * i usuwa polskie znaki diakrytyczne, np. "Wiedźmin" zamienia na "wiedzmin".
     *
     * @param text tekst do przetworzenia
     * @return tekst małymi literami bez znaków diakrytycznych
     */
    private static String normalize(String text) {
        String decomposed = Normalizer.normalize(text, Normalizer.Form.NFD);
        String withoutAccents = decomposed.replaceAll("\\p{M}", "");
        return withoutAccents.toLowerCase().replace('ł', 'l');
    }

    /**
     * Prosi o numer książki z pełnej listy i sprawdza, czy ma ona wymaganą dostępność.
     * Pyta ponownie, dopóki numer nie wskazuje właściwej książki. Numer 0 anuluje wybór.
     *
     * @param wantedAvailable true, jeśli wybrana książka ma być dostępna, false, jeśli wypożyczona
     * @param prompt          tekst wyświetlany przed odczytem
     * @return indeks wybranej książki (liczony od 0) albo NO_SELECTION po anulowaniu
     */
    private static int readBookNumberWithStatus(boolean wantedAvailable, String prompt) {
        while (true) {
            int number = readIntInRange(prompt, 0, titles.size());
            if (number == 0) {
                return NO_SELECTION;
            }

            int index = number - 1;
            if (available.get(index) == wantedAvailable) {
                return index;
            }
            System.out.println("Ta książka nie znajduje się w wyświetlonej tabeli, wybierz numer z tabeli.");
        }
    }
}