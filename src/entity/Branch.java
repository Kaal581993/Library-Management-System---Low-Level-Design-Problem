package entity;


import java.util.Date;
import java.util.List;

public class Branch {
    private String branchId;
    private String branchName;
    private String branchAddress;
    private int phoneNo;
    private String email;
    private Date operatingHours;
    private List<Book> catalog;
    private List<Patron> patrons;
    private List<Loan> loans;
    private List<Reservation> reservations;

    public String getBranchId() {
        return branchId;
    }



    public String getBranchName() {
        return branchName;
    }

    private Branch(BranchBuilder builder) {

        this.branchName = builder.branchName;
        this.branchAddress = builder.branchAddress;

    }

    public class BranchBuilder{
        private String branchId;
        private String branchName;
        private String branchAddress;
        private int phoneNo;
        private String email;
        private Date operatingHours;
        private List<Book> catalog;
        private List<Patron> patronsList;
        private List<Loan> loanList;
        private List<Reservation> reservations;
    public BranchBuilder(
            String branchName,
            String branchAddress,
            int phoneNo,
            String email,
            Date operatingHours,
            List<Book> catalog,
            List<Patron> patronsList,
            List<Loan> loanList,
            List<Reservation> reservations
            ){

        this.branchName=branchName;
        this.branchAddress=branchAddress;
        this.phoneNo=phoneNo;
        this.operatingHours=operatingHours;
        this.email=email;
        this.catalog=catalog;
        this.patronsList=patronsList;
        this.loanList=loanList;
        this.reservations=reservations;



    }

        public BranchBuilder setBranchName(String branchName){
            this.branchName = branchName;
            return this;
        }

        public BranchBuilder setBranchAddress(String branchAddress){
            this.branchAddress = branchAddress;
            return this;
        }

        public BranchBuilder setPhoneNo(int phoneNo){
            this.phoneNo = phoneNo;
            return this;
        }

        public BranchBuilder setOperatingHours(Date OperatingHours){
            this.operatingHours = OperatingHours;
            return this;
        }

        public BranchBuilder setEmail(String email){
            this.email = email;
            return this;
        }

        public BranchBuilder setCatalog(List<Book> catalog){
            this.catalog = catalog;
            return this;
        }

        public BranchBuilder setPatrons(List<Patron> patronList){
            this.patronsList = patronList;
            return this;
        }

        public BranchBuilder setLoans(List<Loan> loanList){
            this.loanList = loanList;
            return this;
        }

        public BranchBuilder setReservations(List<Reservation> reservations){
            this.reservations = reservations;
            return this;
        }

        public Branch build(){
            return new Branch(this);
        }

    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public Date getOperatingHours() {
        return operatingHours;
    }

    public List<Book> getAllCatalog() {
        return catalog;
    }

    public List<Patron> getAllPatrons() {
        return patrons;
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}
