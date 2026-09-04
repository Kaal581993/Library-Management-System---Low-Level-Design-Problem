package strategey.impl;

import entity.Book;
import strategey.BookSearchStrategy;

import java.util.ArrayList;
import java.util.List;

public class CombinedSearchStrategy implements BookSearchStrategy {



    public List<Book> searchBook(List<Book> books, String ISBN, String author, String title) {
        List<Book> bookList = new ArrayList<>();
        for(Book currentBook : books) {
            if(
                    currentBook.getIsbn().equalsIgnoreCase(ISBN)
                    || currentBook.getAuthor().equalsIgnoreCase(author)
                    || currentBook.getTitle().equalsIgnoreCase(title)
            ) {
                bookList.add(currentBook);
            }
        }
        return bookList;
    }

    @Override
    public List<Book> searchBook(List<Book> books, String query) {
        List<Book> bookList = new ArrayList<>();
        for(Book currentBook : books) {
            if(
                            currentBook.getIsbn().matches(query)
                            || currentBook.getAuthor().matches(query)
                            || currentBook.getTitle().matches(query)
            ) {
                bookList.add(currentBook);
            }
        }
        return bookList;
    }
}
