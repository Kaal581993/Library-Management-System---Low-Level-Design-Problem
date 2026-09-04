package entity;

public class InventoryItem {
    private String copyId;
    private BookStatus bookStatus;
    private int shelfNumber;
    private Book book;
    private Branch branch;
    private int quantity;

    private InventoryItem(InventoryBuilder builder) {
        this.copyId = builder.copyId;
        this.bookStatus = builder.bookStatus;
        this.shelfNumber = builder.shelfNumber;
        this.book = builder.book;
        this.branch = builder.branch;
        this.quantity = builder.quantity;
    }

    public static class InventoryBuilder {
        private String copyId;
        private BookStatus bookStatus;
        private int shelfNumber;
        private Book book;
        private Branch branch;
        private int quantity;

        public InventoryBuilder copyId(String copyId) {
            this.copyId = copyId;
            return this;
        }

        public InventoryBuilder bookStatus(BookStatus bookStatus) {
            this.bookStatus = bookStatus;
            return this;
        }

        public InventoryBuilder shelfNumber(int shelfNumber) {
            this.shelfNumber = shelfNumber;
            return this;
        }

        public InventoryBuilder book(Book book) {
            this.book = book;
            return this;
        }

        public InventoryBuilder branch(Branch branch) {
            this.branch = branch;
            return this;
        }

        public InventoryBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public InventoryItem build() {
            return new InventoryItem(this);
        }
    }
}
