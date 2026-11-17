package pl.tajko.library;

import java.util.ArrayList;
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
                case 2, 3, 4, 5, 6, 7 -> System.out.println("Ta funkcja jest jeszcze w przygotowaniu.");
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

        // %-32s oznacza “tekst, wyrównany do lewej, na szerokości 32 znaków”, %-4d to samo dla liczby,
        // a %n to znak nowej linii zależny od systemu (na Windowsie \r\n).
        System.out.printf("%-4s %-32s %-28s %-6s %s%n", "Nr", "Tytuł", "Autor", "Rok", "Status");
        System.out.println("-".repeat(85));

        for (int i = 0; i < titles.size(); i++) {
            String status = available.get(i) ? "dostępna" : "wypożyczona";
            System.out.printf("%-4d %-32s %-28s %-6d %s%n",
                    i + 1, titles.get(i), authors.get(i), years.get(i), status);
        }
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
}