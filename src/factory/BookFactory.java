package factory;

import entity.Book;
import factory.book_dto.BookRequest;

/**
 *
 * 1. FACTORY PATTERN (HIGH PRIORITY - IMPLEMENT)
 * What Problem It Solves:
 *
 * Centralizes Book object creation logic
 * Encapsulates validation rules during book creation
 * Hides the complexity of Builder pattern from client code
 * Ensures valid Book objects are always created
 * Why It's Needed:
 *
 * Problem statement requires adding/updating books with validation
 * Builder pattern is already implemented but clients need a simpler interface
 * Books may need validation (ISBN format, required fields) before creation
 * Different types of books may need different creation logic (e.g., reference books, regular books)
 * What Happens If You Don't Implement It:
 *
 * Client code must directly use BookBuilder everywhere
 * Validation logic scattered across the codebase
 * Violates Single Responsibility Principle (creation logic mixed with business logic)
 * Difficult to add new book types or change validation rules
 * How to Implement:
 *
 * Create BookFactory class with static factory methods
 * Methods: createBook(String isbn, String title, String author, int year, int quantity), createReferenceBook(...), createEBook(...)
 * Factory internally uses BookBuilder and applies validation
 * Returns fully validated Book objects
 *
 * */
public interface BookFactory {

    public  Book createBook(BookRequest bookRequest);
    public Book createReferenceBook(BookRequest bookRequest);
    public Book createEBook(BookRequest bookRequest);
}
