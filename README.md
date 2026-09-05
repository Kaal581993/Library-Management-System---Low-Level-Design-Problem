# Library Management System

A Java-based Library Management System demonstrating OOP principles, SOLID design, and multiple design patterns including Singleton, Factory, Strategy, Chain of Responsibility, State, and Observer.

## Features

- **Book Management**: Add, remove, update, and search books by ISBN, title, or author
- **Patron Management**: Register patrons, search by name/email/ID/fine status, view details, suspend/activate accounts, pay fines
- **Loan Management**: Checkout books, return books, renew loans, view active loans, overdue detection, fine calculation
- **Reservation System**: Reserve unavailable books, priority-based queue processing, automatic notification when books become available

## Design Patterns

| Pattern | Usage |
|---|---|
| **Singleton** | `BookService`, `PatronService`, `LoanService`, `ReservationService` |
| **Factory** | `DefaultBookFactory`, `DefaultLoanFactory`, `DefaultReservationFactory`, `StudentPatronFactory`, `FacultyPatronFactory`, `StaffPatronFactory`, `ExternalPatronFactory` |
| **Strategy** | Book search (`TitleSearchStrategy`, `AuthorSearchStrategy`, `ISBNSearchStrategy`, `CombinedSearchStrategy`), Patron search (`NameSearchStrategy`, `EmailSearchStrategy`, `IdSearchStrategy`, `BorrowingHistorySearchStrategy`, `FineStatusSearchStrategy`), Fine calculation (`StandardFineStrategy`, `VIPFineStrategy`, `StudentFineStrategy`, `HolidayFineStrategy`, `NoFineStrategy`), Reservation priority (`FIFOPriorityStrategy`, `VIPPriorityStrategy`, `CategoryPriorityStrategy`) |
| **Chain of Responsibility** | Book validation (`ISBNValidationHandler`, `RequiredFieldsValidationHandler`, `QuantityValidationHandler`, `PublicationYearValidationHandler`), Loan validation (`PatronEligibilityHandler`, `BookAvailabilityHandler`, `LoanLimitHandler`, `DueDateValidationHandler`), Reservation validation (`BookAvailabilityValidationHandler`, `PatronEligibilityValidationHandler`, `BorrowingLimitValidationHandler`, `DuplicateReservationValidationHandler`), Patron validation (`EmailValidationHandler`, `NameValidationHandler`, `IdValidationHandler`, `BorrowingLimitValidationHandler`) |
| **State** | Loan lifecycle (`CheckedOutState`, `ReturnedState`, `OverdueState`, `RenewedState`) |
| **Observer** | `BookSubject` / `BookObserver` — `ReservationService` observes `Book` status changes to auto-fulfill reservations |
| **Builder** | `Book`, `Patron`, `Reservation` entities use fluent builders for construction |

## Class Diagram

```
┌─────────────────────┐
│       Main          │
│  (entry point)      │
└──────────┬──────────┘
           │ uses
           ▼
┌─────────────────────┐       ┌──────────────────────┐
│   BookMenuHandler   │       │   PatronMenuHandler  │
│  (UI - Books)       │       │   (UI - Patrons)     │
└──────────┬──────────┘       └──────────┬───────────┘
           │ uses                        │ uses
           ▼                              ▼
┌─────────────────────┐       ┌──────────────────────┐
│    BookService      │       │   PatronService      │
│  (Singleton)        │       │   (Singleton)        │
│  - strategies: Map  │       │  - strategies: Map   │
│  - bookList         │       │  - patrons: List     │
│  + addBook()        │       │  + addPatron()       │
│  + searchBooks()    │       │  + searchPatrons()   │
│  + removeBook()     │       │  + calculateFine()   │
│  + updateBook()     │       │  + suspendPatron()   │
└──────────┬──────────┘       └──────────┬───────────┘
           │ uses                        │ uses
           ▼                              ▼
┌─────────────────────┐       ┌──────────────────────┐
│  DefaultBookFactory │       │  PatronFactory       │
│  (implements        │       │  (interface)         │
│   BookFactory)      │       │  ├─StudentPatronFactory
│  + createBook()     │       │  ├─FacultyPatronFactory
│  + createEBook()    │       │  ├─StaffPatronFactory
└──────────┬──────────┘       │  └─ExternalPatronFactory
           │ uses              └──────────┬───────────┘
           ▼                               │ uses
┌─────────────────────┐                    ▼
│       Book          │       ┌──────────────────────┐
│  (extends BookSubject)│      │   PatronValidation   │
│  - bookId, isbn,    │       │   Handler (chain)    │
│    title, author... │       └──────────────────────┘
└─────────────────────┘

┌──────────────────────┐       ┌───────────────────────┐
│   LoanMenuHandler    │       │ ReservationMenuHandler│
│   (UI - Loans)       │       │   (UI - Reservations)│
└──────────┬───────────┘       └──────────┬────────────┘
           │ uses                          │ uses
           ▼                                ▼
┌──────────────────────┐       ┌───────────────────────┐
│    LoanService       │       │  ReservationService   │
│   (Singleton)        │       │   (Singleton)         │
│  - loans: List       │       │  - reservations: List │
│  - fineStrategies    │       │  - reservationQueue   │
│  + checkoutBook()    │       │  + createReservation()│
│  + returnBook()      │       │  + fulfillReservation()│
│  + renewLoan()       │       │  + processNextReservation()
│  + calculateFine()   │       └──────────┬────────────┘
│  + updateOverdueLoans()│                │ implements
└──────────┬───────────┘                ▼
           │ uses          ┌───────────────────────┐
           ▼                │      BookObserver      │
┌──────────────────────┐    │   (interface)         │
│  DefaultLoanFactory  │    └───────────────────────┘
│  (implements         │
│   LoanFactory)       │
│  + createLoan()      │
└──────────┬───────────┘
           │ uses
           ▼
┌──────────────────────┐
│        Loan          │
│  - loanId, patronId, │
│    bookId, dates...  │
│  - currentState      │
└──────────┬───────────┘
           │ state
           ▼
┌──────────────────────┐
│     LoanState        │
│   (interface)        │
│  ├─CheckedOutState   │
│  ├─ReturnedState     │
│  ├─OverdueState      │
│  └─RenewedState      │
└──────────────────────┘

┌──────────────────────┐
│  BookSearchStrategy  │
│   (interface)        │
│  ├─TitleSearchStrategy
│  ├─AuthorSearchStrategy
│  ├─ISBNSearchStrategy
│  └─CombinedSearchStrategy
└──────────────────────┘

┌──────────────────────┐
│ PatronSearchStrategy │
│   (interface)        │
│  ├─NameSearchStrategy│
│  ├─EmailSearchStrategy│
│  ├─IdSearchStrategy  │
│  ├─BorrowingHistory..│
│  └─FineStatusSearch..│
└──────────────────────┘

┌──────────────────────┐
│ FineCalculationStrategy
│   (interface)        │
│  ├─StandardFineStrategy
│  ├─VIPFineStrategy   │
│  ├─StudentFineStrategy│
│  ├─HolidayFineStrategy│
│  └─NoFineStrategy    │
└──────────────────────┘
```

## How to Run

```bash
javac src/**/*.java
java Main
```

Or run `Main.java` directly from your IDE.

## Sample Output

```
/home/kaal/.jdks/ms-25.0.4/bin/java -Dvaadin.copilot.pluginDotFilePath=/home/kaal/IdeaProjects/LibraryManagementSystem/.idea/.copilot-plugin -javaagent:/usr/share/idea/lib/idea_rt.jar=41991 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /home/kaal/IdeaProjects/LibraryManagementSystem/out/production/LibraryManagementSystem Main
=== Library Management System ===
Book Management
Patron Management
Loan Management
Reservation Management
Exit
Enter choice: 1
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 1
Enter ISBN: GDSBN4839455
Enter Title: The Battle of Morgoth
Enter Author: J.R.R. Tolkein
Enter Publication Year: 1967
Enter Quantity: 30
Book added successfully.
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 1
Enter ISBN: GSBN58603
Enter Title: Journey to the Middle Earth
Enter Author: J.R.R. Tolkein
Enter Publication Year: 1945
Enter Quantity: 20
Book added successfully.
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 1
Enter ISBN: HSBN
Enter Title: Birth of Balrog in Inferno
Enter Author: J.R.R. TOLKEIN
Enter Publication Year: 1957
Enter Quantity: 50
Book added successfully.
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 4
GDSBN4839455 | The Battle of Morgoth | J.R.R. Tolkein | Qty: 30
GSBN58603 | Journey to the Middle Earth | J.R.R. Tolkein | Qty: 20
HSBN | Birth of Balrog in Inferno | J.R.R. TOLKEIN | Qty: 50
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 3
Enter ISBN of the book to remove: HSBN
Book removed (if it existed).
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 4
GDSBN4839455 | The Battle of Morgoth | J.R.R. Tolkein | Qty: 30
GSBN58603 | Journey to the Middle Earth | J.R.R. Tolkein | Qty: 20
=== Book Management ===
Add Book
Search Book
Remove Book
List All Books
Back to Main Menu
Enter choice: 5
=== Library Management System ===
Book Management
Patron Management
Loan Management
Reservation Management
Exit
Enter choice: 2
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 1
Enter Patron ID: 2
Enter First Name: Viral Prajapati
Enter Last Name: Prajapati
Enter Email: viral.prajapati.nmims@gmail.com
Select Patron Type:
STUDENT
FACULTY
STAFF
EXTERNAL
Enter choice: 2
Patron registered successfully. Patron ID: kaal581993
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 1
Enter Patron ID: kaalI$tech
Enter First Name: Kaal
Enter Last Name: Prajapati
Enter Email: kaal@581993.com
Select Patron Type:
STUDENT
FACULTY
STAFF
EXTERNAL
Enter choice: 3
Patron registered successfully. Patron ID: kaalI$tech
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 2
Search by:
Name
Email
ID
Fine Status
Enter choice: kaal581993
Invalid choice.
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice:
2
Invalid choice. Please try again.
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: Search by:
Name
Email
ID
Fine Status
Enter choice: 1
Enter name: Viral Prajapati
kaal581993 | Viral Prajapati Prajapati | viral.prajapati.nmims@gmail.com | ACTIVE
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 4
Enter Patron ID: kaal581993
Current Status: ACTIVE
Select New Status:
ACTIVE
SUSPENDED
INACTIVE
Enter choice: 1
Patron activated.
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 3
Enter Patron ID: 581993kaal
Patron not found.
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 3
Enter Patron ID: kaal581993
=== Patron Details ===
ID: kaal581993
Name: Viral Prajapati Prajapati
Email: viral.prajapati.nmims@gmail.com
Type: FACULTY
Status: ACTIVE
Fine Amount: 0.0
Fine Status: CLEAR
Borrowing Limit: 10
Loan Period: 30 days
Fine Rate: 0.25 per day
--- Current Loans ---
No active loans.
--- Reservations ---
No reservations.
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 5
Enter Patron ID: kaal581993
Current Fine: 0.0
Enter payment amount: 0.0
Payment processed. Remaining fine: 0.0
=== Patron Management ===
Register New Patron
Search Patrons
View Patron Details
Update Patron Status
Pay Fine
Back to Main Menu
Enter choice: 6
=== Library Management System ===
Book Management
Patron Management
Loan Management
Reservation Management
Exit
Enter choice: 3
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 1
Enter Patron ID: kaal581993
Enter Book ISBN: GDSBN4839455
Select Loan Type:
REGULAR
REFERENCE
INTER_LIBRARY
DIGITAL
Enter choice: 1
Loan created successfully. Loan ID: LOAN-20260905181938-6495
Due Date: Sat Sep 19 18:19:38 IST 2026
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 2
Enter Loan ID to return: LOAN-20260905181938-6495
Book returned successfully.
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 3
Enter Loan ID to renew: LOAN-20260905182106-0140
Error: Cannot renew a returned loan
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 1
Enter Patron ID: kaal581993
Enter Book ISBN: GDSBN4839455
Select Loan Type:
REGULAR
REFERENCE
INTER_LIBRARY
DIGITAL
Enter choice: 1
Loan created successfully. Loan ID: LOAN-20260905182106-0140
Due Date: Sat Sep 19 18:21:06 IST 2026
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 3
Enter Loan ID to renew: LOAN-20260905182106-0140
Loan renewed successfully. New Due Date: Sat Oct 03 18:21:06 IST 2026
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 4
Enter Patron ID: kaal581993
Active Loans:
Loan ID: LOAN-20260905182106-0140 | Book ID: GDSBN4839455 | Due Date: Sat Oct 03 18:21:06 IST 2026 | Status: CheckedOutState
=== Loan Management ===
Checkout Book
Return Book
Renew Loan
View Active Loans
Back to Main Menu
Enter choice: 5
=== Library Management System ===
Book Management
Patron Management
Loan Management
Reservation Management
Exit
Enter choice: 4
=== Reservation Management ===
Create Reservation
Cancel Reservation
View My Reservations
Back to Main Menu
Enter choice: 1
Enter Patron ID: kaal581993
Enter Book ISBN to reserve: GDSBN4839455
Reservation created successfully. Reservation ID: RES-20260905182207-6633
=== Reservation Management ===
Create Reservation
Cancel Reservation
View My Reservations
Back to Main Menu
Enter choice: 3
Enter Patron ID: kaal581993
Your Reservations:
Reservation ID: RES-20260905182207-6633 | Book: The Battle of Morgoth | Status: PENDING | Date: Sat Sep 05 18:22:07 IST 2026
=== Reservation Management ===
Create Reservation
Cancel Reservation
View My Reservations
Back to Main Menu
Enter choice: 2
Enter Reservation ID to cancel: RES-20260905182207-6633
Reservation cancelled successfully.
=== Reservation Management ===
Create Reservation
Cancel Reservation
View My Reservations
Back to Main Menu
Enter choice: 3
Enter Patron ID: kaal581993
Your Reservations:
Reservation ID: RES-20260905182207-6633 | Book: The Battle of Morgoth | Status: CANCELLED | Date: Sat Sep 05 18:22:07 IST 2026
=== Reservation Management ===
Create Reservation
Cancel Reservation
View My Reservations
Back to Main Menu
Enter choice: 5
Invalid choice. Please try again.
=== Reservation Management ===
Create Reservation
Cancel Reservation
View My Reservations
Back to Main Menu
Enter choice: 4
=== Library Management System ===
Book Management
Patron Management
Loan Management
Reservation Management
Exit
Enter choice: 5
Exiting...
Process finished with exit code 0
```
