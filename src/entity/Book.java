package entity;

public class Book {
    private String bookId;
    private String isbn;
    private String title;
    private int quantity;
    private String author;
    private int publicationYear;
    private boolean isReference;
    private BookStatus bookStatus;

    private Book(BookBuilder builder) {
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.quantity = builder.quantity;
        this.author = builder.author;
        this.publicationYear = builder.publicationYear;
        this.isReference = builder.isReference;
    }

    public String getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }


    public int getPublicationYear() {
        return publicationYear;
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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static class BookBuilder {
        private String isbn;
        private String title;
        private String author;
        private int quantity;
        private int publicationYear;
        private boolean isReference;
        private BookStatus bookStatus;
        public BookBuilder setIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public BookBuilder setPublicationYear(int publicationYear) {
            this.publicationYear = publicationYear;
            return this;
        }

        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder setYear(int year) {
            this.publicationYear = year;
            return this;
        }
        public Book build() {
            return new Book(this);
        }


        public BookBuilder setIsReference(boolean isReference) {
            this.isReference=isReference;
            return this;
        }

        public BookBuilder setBookStatus(BookStatus bookStatus) {
            this.bookStatus = bookStatus;
            return this;
        }

        public BookBuilder setReference(boolean isReference) {
            this.isReference = isReference;
            return this;
        }
    }
}
