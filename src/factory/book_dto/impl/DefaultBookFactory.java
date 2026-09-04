package factory.book_dto.impl;

import entity.Book;
import factory.BookFactory;
import factory.book_dto.BookRequest;

public class DefaultBookFactory implements BookFactory {



    @Override
    public Book createBook(BookRequest bookRequest) {
        return new Book.BookBuilder().
                setAuthor(bookRequest.getAuthor()).
                setTitle(bookRequest.getTitle()).
                setIsbn(bookRequest.getIsbn()).
                setYear(bookRequest.getYear()).
                setQuantity(bookRequest.getQuantity()).build();
    }

    @Override
    public Book createReferenceBook(BookRequest bookRequest) {
        return new Book.BookBuilder().
                setAuthor(bookRequest.getAuthor()).
                setTitle(bookRequest.getTitle()).
                setIsbn(bookRequest.getIsbn()).
                setYear(bookRequest.getYear()).
                setIsReference(true).
                setQuantity(bookRequest.getQuantity()).build();
    }

    @Override
    public Book createEBook(BookRequest bookRequest) {
        return new Book.BookBuilder().
                setAuthor(bookRequest.getAuthor()).
                setTitle(bookRequest.getTitle()).
                setIsbn(bookRequest.getIsbn()).
                setYear(bookRequest.getYear()).
                setQuantity(1).
                build();
    }
}
