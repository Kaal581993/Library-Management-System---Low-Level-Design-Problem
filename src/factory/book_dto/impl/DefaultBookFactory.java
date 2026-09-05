package factory.book_dto.impl;

import entity.Book;
import entity.util.IdGenerator;
import factory.BookFactory;
import factory.book_dto.BookRequest;

public class DefaultBookFactory implements BookFactory {

    @Override
    public Book createBook(BookRequest bookRequest) {
        return new Book.BookBuilder().
                setBookId(IdGenerator.generateBookId()).
                setIsbn(bookRequest.getIsbn() != null ? bookRequest.getIsbn() : IdGenerator.generateISBN()).
                setTitle(bookRequest.getTitle()).
                setAuthor(bookRequest.getAuthor()).
                setYear(bookRequest.getYear()).
                setQuantity(bookRequest.getQuantity()).build();
    }

    @Override
    public Book createReferenceBook(BookRequest bookRequest) {
        return new Book.BookBuilder().
                setBookId(IdGenerator.generateBookId()).
                setIsbn(bookRequest.getIsbn() != null ? bookRequest.getIsbn() : IdGenerator.generateISBN()).
                setTitle(bookRequest.getTitle()).
                setAuthor(bookRequest.getAuthor()).
                setYear(bookRequest.getYear()).
                setIsReference(true).
                setQuantity(bookRequest.getQuantity()).build();
    }

    @Override
    public Book createEBook(BookRequest bookRequest) {
        return new Book.BookBuilder().
                setBookId(IdGenerator.generateBookId()).
                setIsbn(bookRequest.getIsbn() != null ? bookRequest.getIsbn() : IdGenerator.generateISBN()).
                setTitle(bookRequest.getTitle()).
                setAuthor(bookRequest.getAuthor()).
                setYear(bookRequest.getYear()).
                setQuantity(1).
                build();
    }
}
