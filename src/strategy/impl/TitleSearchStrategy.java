package strategy.impl;

import entity.Book;
import strategy.BookSearchStrategy;

import java.util.ArrayList;
import java.util.List;

public class TitleSearchStrategy implements BookSearchStrategy {



    @Override
    public List<Book> searchBook(List<Book> books, String title) {
        List<Book> bookList = new ArrayList<>();
        for(Book currentBook : books) {
            if(currentBook.getTitle().equalsIgnoreCase(title)) {
                bookList.add(currentBook);
            }
        }
        return bookList;
    }
}
