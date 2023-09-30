import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Book {
    private int bookID;
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;
    private LocalDate dueDate;

    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
        this.dueDate = null; 
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookID + "\nTitle: " + title + "\nAuthor: " + author + "\nGenre: " + genre +
                "\nAvailability: " + (isAvailable ? "Available" : "Not Available") +
                (dueDate != null ? "\nDue Date: " + dueDate : "");
    }
}

class Patron {
    private int patronID;
    private String name;
    private String contactInfo;
    private List<Book> borrowedBooks;

    public Patron(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getPatronID() {
        return patronID;
    }

    public void setPatronID(int patronID) {
        this.patronID = patronID;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            book.setDueDate(LocalDate.now().plusWeeks(2)); // Set due date to 2 weeks from today
            borrowedBooks.add(book);
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            borrowedBooks.remove(book);
            book.setAvailable(true);
            book.setDueDate(null);
        }
    }

    public double calculateFine() {
        double totalFine = 0.0;
        LocalDate today = LocalDate.now();

        for (Book book : borrowedBooks) {
            LocalDate dueDate = book.getDueDate();
            if (dueDate != null && dueDate.isBefore(today)) {
                long daysOverdue = today.toEpochDay() - dueDate.toEpochDay();
                totalFine += daysOverdue * 1.0; // Fine of $1 per day overdue
            }
        }

        return totalFine;
    }

    @Override
    public String toString() {
        StringBuilder borrowedBooksList = new StringBuilder();
        for (Book book : borrowedBooks) {
            borrowedBooksList.append("\n - ").append(book.getTitle());
        }

        return "Patron ID: " + patronID + "\nName: " + name + "\nContact Info: " + contactInfo +
                "\nBorrowed Books:" + (borrowedBooks.isEmpty() ? "\n - None" : borrowedBooksList.toString());
    }
}

class Library {
    private List<Book> books;
    private List<Patron> patrons;
    private int bookIDCounter;
    private int patronIDCounter;

    public Library() {
        this.books = new ArrayList<>();
        this.patrons = new ArrayList<>();
        this.bookIDCounter = 1;
        this.patronIDCounter = 1;
    }

    public void addBook(Book book) {
        book.setBookID(bookIDCounter++);
        books.add(book);
    }

    public void addPatron(Patron patron) {
        patron.setPatronID(patronIDCounter++);
        patrons.add(patron);
    }

    public void removeBook(int bookID) {
        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getBookID() == bookID) {
                bookToRemove = book;
                break;
            }
        }
        if (bookToRemove != null) {
            books.remove(bookToRemove);
        }
    }

    public void removePatron(int patronID) {
        Patron patronToRemove = null;
        for (Patron patron : patrons) {
            if (patron.getPatronID() == patronID) {
                patronToRemove = patron;
                break;
            }
        }
        if (patronToRemove != null) {
            patrons.remove(patronToRemove);
        }
    }

    public Book findBook(int bookID) {
        for (Book book : books) {
            if (book.getBookID() == bookID) {
                return book;
            }
        }
        return null;
    }

    public Patron findPatron(int patronID) {
        for (Patron patron : patrons) {
            if (patron.getPatronID() == patronID) {
                return patron;
            }
        }
        return null;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public List<Patron> getAllPatrons() {
        return patrons;
    }

    public boolean borrowBook(int patronID, int bookID) {
        Patron patron = findPatron(patronID);
        Book book = findBook(bookID);

        if (patron != null && book != null && book.isAvailable()) {
            patron.borrowBook(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(int patronID, int bookID) {
        Patron patron = findPatron(patronID);
        Book book = findBook(bookID);

        if (patron != null && book != null && !book.isAvailable()) {
            patron.returnBook(book);
            return true;
        }
        return false;
    }

    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Book> getBorrowedBooks() {
        return books.stream()
                .filter(book -> !book.isAvailable())
                .collect(Collectors.toList());
    }

    public List<Patron> getPatronsWithFines() {
        return patrons.stream()
                .filter(patron -> patron.calculateFine() > 0.0)
                .collect(Collectors.toList());
    }

    public void generateBookAvailabilityReport() {
        List<Book> availableBooks = getAvailableBooks();
        System.out.println("\nAvailable Books:");
        for (Book book : availableBooks) {
            System.out.println(book);
        }
    }

    public void generateBorrowingHistoryReport() {
        List<Book> borrowedBooks = getBorrowedBooks();
        System.out.println("\nBorrowing History:");
        for (Book book : borrowedBooks) {
            System.out.println(book);
        }
    }

    public void generateFinesReport() {
        List<Patron> patronsWithFines = getPatronsWithFines();
        System.out.println("\nPatrons with Fines:");
        for (Patron patron : patronsWithFines) {
            System.out.println(patron.getName() + ": $" + patron.calculateFine());
        }
    }
}

public class int2{
    public static void main(String[] args) {
        Library library = new Library();

        library.generateBookAvailabilityReport();
        library.generateBorrowingHistoryReport();
        library.generateFinesReport();
    }
}
