package strategey.impl;

import entity.Book;
import strategey.BookSearchStrategy;

import java.util.ArrayList;
import java.util.List;

public class CombinedSearchStrategy implements BookSearchStrategy {

    @Override
    public List<Book> searchBook(List<Book> books, String query) {
        List<Book> bookList = new ArrayList<>();
        if (query == null || query.isBlank()) {
            return bookList;
        }
        String lowerQuery = query.toLowerCase();
        for (Book currentBook : books) {
            if (
                    currentBook.getIsbn().toLowerCase().contains(lowerQuery)
                    || currentBook.getAuthor().toLowerCase().contains(lowerQuery)
                    || currentBook.getTitle().toLowerCase().contains(lowerQuery)
            ) {
                bookList.add(currentBook);
            }
        }
        return bookList;
    }
}
