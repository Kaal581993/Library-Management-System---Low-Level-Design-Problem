import ui_handlers.BookMenu;
import ui_handlers.LoanMenuHandler;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Book Management");
            System.out.println("2. Loan Management");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String choice = new java.util.Scanner(System.in).nextLine();
            switch (choice) {
                case "1" -> new BookMenu().displayMenu();
                case "2" -> new LoanMenuHandler().displayMenu();
                case "3" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
