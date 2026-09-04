package validation.book_validation;

public class ValidationException extends Throwable {

    private String message;

    public ValidationException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
