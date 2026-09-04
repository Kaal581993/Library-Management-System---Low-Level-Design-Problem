package strategy;


import entity.Book;

import java.util.List;

/**
 *
 * 2. STRATEGY PATTERN (HIGH PRIORITY - IMPLEMENT)
 * What Problem It Solves:
 *
 * Allows different search algorithms without modifying BookService
 * Enables flexible search by different criteria (title, author, ISBN, genre)
 * Makes search logic interchangeable and testable
 * Why It's Needed:
 *
 * Problem statement explicitly requires search by title, author, or ISBN
 * Search algorithms may vary (exact match, partial match, fuzzy search)
 * New search criteria can be added without changing existing code
 * Follows Open/Closed Principle
 * What Happens If You Don't Implement It:
 *
 * BookService will have multiple search methods
 * (searchByTitle, searchByAuthor, searchByISBN)
 * Adding new search criteria requires modifying BookService
 * Search logic tightly coupled to service
 * Difficult to unit test individual search algorithms
 * How to Implement:
 *
 * Create BookSearchStrategy interface with search(List<Book> books, String query)
 * Implement strategies: TitleSearchStrategy, AuthorSearchStrategy, ISBNSearchStrategy, CombinedSearchStrategy
 * BookService delegates search to appropriate strategy
 * Client can inject custom strategies
 *
 * */
public interface BookSearchStrategy {
    public List<Book> searchBook(List<Book> books, String query);
}
