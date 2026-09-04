package service;

import entity.Book;
import entity.BookStatus;
import factory.BookFactory;
import factory.book_dto.BookRequest;
import factory.book_dto.impl.DefaultBookFactory;
import strategey.SearchType;
import strategey.impl.AuthorSearchStrategy;
import strategey.impl.ISBNSearchStrategy;
import strategey.impl.TitleSearchStrategy;
import validation.book_validation.BookSearchRequest;
import validation.book_validation.BookValidationHandler;
import validation.book_validation.ValidationException;
import validation.book_validation.impl.ISBNValidationHandler;
import validation.book_validation.impl.PublicationYearValidationHandler;
import validation.book_validation.impl.QuantityValidationHandler;
import validation.book_validation.impl.RequiredFieldsValidationHandler;


import java.util.ArrayList;
import java.util.List;

public class BookService {

    private static volatile BookService instance;

    private String isbn;
    private String title;
    private String author;
    private int quantity;
    private BookStatus bookStatus;
    private int publicationYear;
    private boolean isReference;

    List<Book> bookList = new ArrayList<>();
    private SearchType searchType;
    AuthorSearchStrategy authorSearchStrategy;
    ISBNSearchStrategy isbnSearchStrategy;
    TitleSearchStrategy titleSearchStrategy;
    private final BookFactory bookFactory = new DefaultBookFactory();

    private BookService() {
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Book addBook(BookRequest bookRequest) {
        BookSearchRequest request = toSearchRequest(bookRequest);

        BookValidationHandler chain = new ISBNValidationHandler()
                .setNext(new RequiredFieldsValidationHandler()
                .setNext(new QuantityValidationHandler()
                .setNext(new PublicationYearValidationHandler())));

        if (!chain.handle(request)) {
            throw new ValidationException("Validation failed");
        }

        Book book = bookFactory.createBook(bookRequest);
        bookList.add(book);
        return book;
    }

    private BookSearchRequest toSearchRequest(BookRequest bookRequest) {
        BookSearchRequest request = new BookSearchRequest();
        request.setIsbn(bookRequest.getIsbn());
        request.setTitle(bookRequest.getTitle());
        request.setAuthor(bookRequest.getAuthor());
        request.setQuantity(bookRequest.getQuantity());
        request.setPublicationYear(bookRequest.getYear());
        request.setBookStatus(bookStatus);
        request.setReference(isReference);
        return request;
    }

    public void removeBook(String isbn) {
        bookList.removeIf(book -> book.getIsbn().equals(isbn));
    }

    public void removeBookByTitle(String title) {
        bookList.removeIf(book -> book.getTitle().equals(title));
    }

    public void bookSearch(SearchType searchType) {

        //    List<Book> resultList = new ArrayList<>();

        List<Book> bookList = new ArrayList<>();


        switch (searchType) {

            case searchType.AUTHOR -> authorSearchStrategy.searchBook(bookList, author);
            case searchType.ISBN -> isbnSearchStrategy.searchBook(bookList, isbn);
            case searchType.TITLE -> titleSearchStrategy.searchBook(bookList, title);
            //  case searchType.PUBLICATION_YEAR -> publicationYearSearchStrategy.searchBook(bookList, publicationYear);
            default -> {
                break;
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
