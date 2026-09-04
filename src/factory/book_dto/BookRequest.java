package factory.book_dto;

import entity.Book;
import factory.BookFactory;

public class BookRequest {

    private String author;
    private String title;
    private String isbn;
    private int year;
    private int quantity;




    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public int getQuantity() {
        return quantity;
    }
}
