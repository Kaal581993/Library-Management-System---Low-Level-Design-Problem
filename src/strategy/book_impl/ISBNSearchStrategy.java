package strategy.book_impl;

import entity.Book;
import strategy.BookSearchStrategy;

import java.util.ArrayList;
import java.util.List;

public class ISBNSearchStrategy implements BookSearchStrategy {



    @Override
    public List<Book> searchBook(List<Book> books, String ISBN) {
        List<Book> bookList = new ArrayList<>();
        for(Book currentBook : books) {
            if(currentBook.getIsbn().equalsIgnoreCase(ISBN)) {
                bookList.add(currentBook);
            }
        }
        return bookList;
    }
}
