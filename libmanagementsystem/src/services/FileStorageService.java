package libmanagementsystem.src.services;
import java.io.*;
import java.util.*;

import libmanagementsystem.src.model.*;

public class FileStorageService {
    private static final String DATA_DIR = "libmanagementsystem/data/";
    private static final String BOOKS_FILE = DATA_DIR + "Book.txt";
    private static final String MEMBERS_FILE = DATA_DIR + "member.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "transactin.txt";
    
    // Ensure data directory exists
    public FileStorageService() {
        new File(DATA_DIR).mkdirs();
    }
    
    // Save books to file
    public void saveBooks(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.println(book.getId() + "|" +
                              book.getTitle() + "|" +
                              book.getAuthor() + "|" +
                              book.getIsbn() + "|" +
                              book.getQuantity() + "|" +
                              book.getAvailableCount());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving books: " + e.getMessage());
        }
    }
    
    // Load books from file
    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        
        if (!file.exists()) return books;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Book book = new Book(
                        Integer.parseInt(parts[0]),
                        parts[1], parts[2], parts[3],
                        Integer.parseInt(parts[4])
                    );
                    book.setAvailableCount(Integer.parseInt(parts[5]));
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading books: " + e.getMessage());
        }
        return books;
    }
    
    public void saveMembers(List<Member> members) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MEMBERS_FILE))) {
            for (Member member : members) {
                writer.println(member.getId() + "|" +
                              member.getName() + "|" +
                              member.getEmail() + "|" +
                              member.getPhone() + "|" +
                              member.getMembershipDate() + "|" +
                              member.getBooksIssued());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving members: " + e.getMessage());
        }
    }

    public void saveTransactions(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction t : transactions) {
                String returnDate = t.getReturnDate() != null
                    ? t.getReturnDate().toString()
                    : "NULL";
                writer.println(t.getId() + "|" +
                              t.getBookId() + "|" +
                              t.getMemberId() + "|" +
                              t.getIssueDate() + "|" +
                              t.getDueDate() + "|" +
                              returnDate + "|" +
                              t.getStatus());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving transactions: " + e.getMessage());
        }
    }

    public List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        File file = new File(MEMBERS_FILE);
        
        if (!file.exists()) return members;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    Member member = new Member(
                        Integer.parseInt(parts[0]),
                        parts[1], parts[2], parts[3]
                    );
                    if (parts.length > 4) {
                        member.setMembershipDate(parts[4]);
                    }
                    if (parts.length > 5) {
                        member.setBooksIssued(Integer.parseInt(parts[5]));
                    }
                    members.add(member);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading members: " + e.getMessage());
        }
        return members;
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);
        
        if (!file.exists()) return transactions;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    Transaction transaction = new Transaction(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                    );
                    if (parts.length > 3) {
                        transaction.setIssueDate(java.time.LocalDate.parse(parts[3]));
                    }
                    if (parts.length > 4) {
                        transaction.setDueDate(java.time.LocalDate.parse(parts[4]));
                    }
                    if (parts.length > 5 && !parts[5].equalsIgnoreCase("null")) {
                        transaction.setReturnDate(java.time.LocalDate.parse(parts[5]));
                    }
                    if (parts.length > 6) {
                        transaction.setStatus(parts[6]);
                    }
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading transactions: " + e.getMessage());
        }
        return transactions;
    }
}