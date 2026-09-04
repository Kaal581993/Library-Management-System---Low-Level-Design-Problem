package service;

import entity.Book;
import entity.BookStatus;
import strategey.SearchType;
import strategey.impl.AuthorSearchStrategy;
import strategey.impl.ISBNSearchStrategy;
import strategey.impl.TitleSearchStrategy;

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
    private BookService() {

    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }


    public Book addBook(Book.BookBuilder bookBuilder) {
        Book book = bookBuilder.
                setAuthor(author).
                setTitle(title).
                setIsbn(isbn).
                setBookStatus(bookStatus).
                setPublicationYear(publicationYear).
                setReference(isReference).
                setQuantity(quantity).
                build();
        bookList.add(book);
        return book;
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
