package validation.book_validation;

import entity.Book;
import entity.BookStatus;
import strategey.SearchType;
import strategey.impl.AuthorSearchStrategy;
import strategey.impl.ISBNSearchStrategy;
import strategey.impl.TitleSearchStrategy;

import java.util.ArrayList;
import java.util.List;

public class BookSearchRequest {
    private String isbn;
    private String title;
    private String author;
    private int quantity;
    private BookStatus bookStatus;
    private int publicationYear;
    private boolean isReference;


//    PublicationYearSearchStrategy publicationYearSearchStrategy;




    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }






}
