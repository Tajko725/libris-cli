package pl.tajko.library;

import java.util.Scanner;

/**
 * Główna klasa aplikacji Libris CLI.
 * Wyświetla menu główne i obsługuje wybór opcji użytkownika.
 */
public class Main {

    /**
     * Wspólny obiekt do odczytu danych z konsoli.
     * Nie zamykam go celowo, bo zamknięcie zamknęłoby też System.in.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Punkt wejścia programu. Pokazuje menu w pętli,
     * aż użytkownik wybierze opcję wyjścia.
     *
     * @param args argumenty wiersza poleceń (nieużywane)
     */
    static void main(String[] args) {
        System.out.println("=== Libris - biblioteka w konsoli ===");
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Wybierz opcję: ");

            switch (choice) {
                case 1, 2, 3, 4, 5, 6, 7 -> System.out.println("Ta funkcja jest jeszcze w przygotowaniu.");
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