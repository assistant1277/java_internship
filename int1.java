import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    private String username;
    private String password;
    private int pin;

    public User(String username, String password, int pin) {
        this.username = username;
        this.password = password;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPin() {
        return pin;
    }
}

class Account {
    private String accountNumber;
    private double balance;
    private String accountType;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    // other getters and setters

    public void deposit(double amount) {
        balance += amount;
        recordTransaction("Deposit", amount);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            recordTransaction("Withdrawal", amount);
            return true;
        }
        return false;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    private void recordTransaction(String type, double amount) {
        Transaction transaction = new Transaction(type, amount);
        transactionHistory.add(transaction);
    }

    public boolean transferFunds(Account targetAccount, double amount) {
        if (withdraw(amount)) {
            targetAccount.deposit(amount);
            return true;
        }
        return false;
    }
}

class Loan {
    private String loanNumber;
    private double loanAmount;
    private double remainingAmount;
    private double interestRate;
    private boolean approved;

    public Loan(String loanNumber, double loanAmount, double interestRate) {
        this.loanNumber = loanNumber;
        this.loanAmount = loanAmount;
        this.remainingAmount = loanAmount;
        this.interestRate = interestRate;
        this.approved = false;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void approveLoan() {
        approved = true;
    }

    public void makePayment(double amount) {
        if (remainingAmount >= amount) {
            remainingAmount -= amount;
        }
    }
}

class Bank {
    private Map<String, User> users; // Map username to User
    private List<Account> accounts;
    private List<Loan> loans;

    public Bank() {
        users = new HashMap<>();
        accounts = new ArrayList<>();
        loans = new ArrayList<>();
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void createAccount(User user, String accountNumber, double initialBalance, String accountType) {
        if (users.containsKey(user.getUsername()) && !accountNumberExists(accountNumber)) {
            Account account = new Account(accountNumber, initialBalance, accountType);
            accounts.add(account);
        }
    }

    public boolean accountNumberExists(String accountNumber) {
        return accounts.stream().anyMatch(account -> account.getAccountNumber().equals(accountNumber));
    }

    public boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean validatePin(String username, int pin) {
        User user = users.get(username);
        return user != null && user.getPin() == pin;
    }

    public Account getAccount(String accountNumber) {
        return accounts.stream().filter(account -> account.getAccountNumber().equals(accountNumber)).findFirst().orElse(null);
    }

    public double getAccountBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account != null ? account.getBalance() : -1; // Return a negative value for an invalid account
    }

    public void applyForLoan(User user, double loanAmount, double interestRate) {
        if (users.containsKey(user.getUsername())) {
            String loanNumber = generateLoanNumber();
            Loan loan = new Loan(loanNumber, loanAmount, interestRate);
            loans.add(loan);
        }
    }

    public Loan getLoan(String loanNumber) {
        return loans.stream().filter(loan -> loan.getLoanNumber().equals(loanNumber)).findFirst().orElse(null);
    }

    public void approveLoan(String loanNumber) {
        Loan loan = getLoan(loanNumber);
        if (loan != null) {
            loan.approveLoan();
        }
    }

    public void makeLoanPayment(String loanNumber, double amount) {
        Loan loan = getLoan(loanNumber);
        if (loan != null) {
            loan.makePayment(amount);
        }
    }

    private String generateLoanNumber() {
        // Generate a unique loan number (e.g., using a timestamp)
        return "LN" + System.currentTimeMillis();
    }

}

// Class to represent a transaction
class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

public class int1 {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // create users
        User user1 = new User("user1", "password1", 1234); 
        User user2 = new User("user2", "password2", 5678); 

        bank.addUser(user1);
        bank.addUser(user2);

        // create accounts
        bank.createAccount(user1, "123456", 1000.0, "Savings");
        bank.createAccount(user1, "654321", 500.0, "Checking");
        bank.createAccount(user2, "789012", 2000.0, "Savings");

        // authenticate users and perform secure transactions
        if (bank.authenticateUser("user1", "password1") && bank.validatePin("user1", 1234)) {
            Account user1SavingsAccount = bank.getAccount("123456");
            Account user1CheckingAccount = bank.getAccount("654321");

            if (user1SavingsAccount.transferFunds(user1CheckingAccount, 200.0)) {
                System.out.println("Transfer successful");
            } else {
                System.out.println("Transfer failed");
            }

            double user1SavingsBalance = bank.getAccountBalance("123456");
            double user1CheckingBalance = bank.getAccountBalance("654321");

            System.out.println("User 1 Savings Account Balance: " + user1SavingsBalance);
            System.out.println("User 1 Checking Account Balance: " + user1CheckingBalance);

            bank.applyForLoan(user1, 10000.0, 5.0);

            Loan loan = bank.getLoan("LN");
            if (loan != null) {
                bank.approveLoan(loan.getLoanNumber());
            }

            if (loan != null) {
                bank.makeLoanPayment(loan.getLoanNumber(), 1000.0);
            }
        } else {
            System.out.println("Authentication failed");
        }

    }
}
