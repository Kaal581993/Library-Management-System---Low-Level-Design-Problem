package service;

import entity.Book;
import factory.BookFactory;
import factory.book_dto.BookRequest;
import factory.book_dto.impl.DefaultBookFactory;
import strategy.BookSearchStrategy;
import strategy.SearchType;
import strategy.book_impl.AuthorSearchStrategy;
import strategy.book_impl.CombinedSearchStrategy;
import strategy.book_impl.ISBNSearchStrategy;
import strategy.book_impl.TitleSearchStrategy;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;
import validation.book_validation.impl.ISBNValidationHandler;
import validation.book_validation.impl.PublicationYearValidationHandler;
import validation.book_validation.impl.QuantityValidationHandler;
import validation.book_validation.impl.RequiredFieldsValidationHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BookService {

    private static volatile BookService instance;

//    private String isbn;
//    private String title;
//    private String author;
//    private int quantity;
//    private BookStatus bookStatus;
//    private int publicationYear;
//    private boolean isReference;

    List<Book> bookList = new ArrayList<>();
    private SearchType searchType;
//    AuthorSearchStrategy authorSearchStrategy;
//    ISBNSearchStrategy isbnSearchStrategy;
//    TitleSearchStrategy titleSearchStrategy;
    private final BookFactory bookFactory = new DefaultBookFactory();

    private final Map<SearchType, BookSearchStrategy> strategies = new EnumMap<>(SearchType.class);
    private BookService() {
        strategies.put(SearchType.AUTHOR, new AuthorSearchStrategy());
        strategies.put(SearchType.ISBN, new ISBNSearchStrategy());
        strategies.put(SearchType.TITLE, new TitleSearchStrategy());
        strategies.put(SearchType.COMBINED, new CombinedSearchStrategy());
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Book addBook(BookRequest bookRequest) {
        BookValidationHandler chain = new ISBNValidationHandler()
                .setNext(new RequiredFieldsValidationHandler()
                .setNext(new QuantityValidationHandler()
                .setNext(new PublicationYearValidationHandler())));

        if (!chain.handle(bookRequest)) {
            throw new ValidationException("Validation failed");
        }

        Book book = bookFactory.createBook(bookRequest);
        bookList.add(book);
        return book;
    }

    public void removeBook(String isbn) {
        bookList.removeIf(book -> book.getIsbn().equals(isbn));
    }

    public void removeBookByTitle(String title) {
        bookList.removeIf(book -> book.getTitle().equals(title));
    }


    public Book getBook(String bookId) {
        // Return book by ID
        return bookList.stream()
                .filter(book -> book.getIsbn().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAllBooks() {
        // Return all books
        return bookList;
    }
//    public void bookSearch(SearchType searchType) {
//        //    List<Book> resultList = new ArrayList<>();
//        List<Book> bookList = new ArrayList<>();
//        switch (searchType) {
//            case searchType.AUTHOR -> authorSearchStrategy.searchBook(bookList, author);
//            case searchType.ISBN -> isbnSearchStrategy.searchBook(bookList, isbn);
//            case searchType.TITLE -> titleSearchStrategy.searchBook(bookList, title);
//            //  case searchType.PUBLICATION_YEAR -> publicationYearSearchStrategy.searchBook(bookList, publicationYear);
//            default -> {
//                break;
//            }
//        }
//    }


    public List<Book> searchBooks(SearchType type, String query) {
        BookSearchStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy registered for search type: " + type);
        }
        return strategy.searchBook(bookList, query);
    }

    public void updateBook(Book book) {
        if (book == null) {
            return;
        }
        for (int i = 0; i < bookList.size(); i++) {
            Book existing = bookList.get(i);
            if (existing.getIsbn().equals(book.getIsbn())) {
                bookList.set(i, book);
                return;
            }
        }
    }


    public static BookService getInstance() {
        if (instance == null) { // First check (no lock)
            synchronized (BookService.class) {
                if (instance == null) { // Second check (with lock)
                    instance = new BookService();


                }
            }
        }
        return instance;


    }
}
