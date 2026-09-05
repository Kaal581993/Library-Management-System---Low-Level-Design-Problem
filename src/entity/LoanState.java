package entity;

public interface LoanState {
    public void checkout(Loan loan);
    public void returnBook(Loan loan);
    public void renew(Loan loan);

    public double calculateFine(Loan loan);
    public boolean isOverDue(Loan loan);
}
