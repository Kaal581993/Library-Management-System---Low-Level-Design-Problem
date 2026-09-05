package entity.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private IdGenerator() {
    }

    public static String generateBookId() {
        return "BOOK-" + LocalDateTime.now().format(FORMATTER) + "-" + randomDigits(4);
    }

    public static String generatePatronId() {
        return "PAT-" + LocalDateTime.now().format(FORMATTER) + "-" + randomDigits(4);
    }

    public static String generateLoanId() {
        return "LOAN-" + LocalDateTime.now().format(FORMATTER) + "-" + randomDigits(4);
    }

    public static String generateReservationId() {
        return "RES-" + LocalDateTime.now().format(FORMATTER) + "-" + randomDigits(4);
    }

    public static String generateISBN() {
        return generateISBN13();
    }

    private static String generateISBN13() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        int checkDigit = calculateISBNDigit(sb.toString());
        sb.append(checkDigit);
        return sb.toString();
    }

    private static int calculateISBNDigit(String isbn) {
        int sum = 0;
        for (int i = 0; i < isbn.length(); i++) {
            int digit = Character.digit(isbn.charAt(i), 10);
            sum += (i % 2 == 0) ? digit * 1 : digit * 3;
        }
        int remainder = sum % 10;
        return (remainder == 0) ? 0 : 10 - remainder;
    }

    private static String randomDigits(int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
