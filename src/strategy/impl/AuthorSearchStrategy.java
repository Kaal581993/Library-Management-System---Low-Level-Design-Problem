package strategy.impl;

import entity.Book;
import strategy.BookSearchStrategy;

import java.util.ArrayList;
import java.util.List;

public class AuthorSearchStrategy implements BookSearchStrategy {



    @Override
    public List<Book> searchBook(List<Book> books, String author) {
        List<Book> bookList = new ArrayList<>();
        for(Book currentBook : books) {
            if(currentBook.getAuthor().equalsIgnoreCase(author)) {
                bookList.add(currentBook);
            }
        }
        return bookList;
    }
}
