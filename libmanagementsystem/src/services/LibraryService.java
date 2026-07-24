package libmanagementsystem.src.services;
import java.util.*;

import libmanagementsystem.src.model.*;

public class LibraryService {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private int nextBookId = 1;
    private int nextMemberId = 1;
    private int nextTransactionId = 1;
    
    // ========== BOOK OPERATIONS ==========
    
    public void addBook(String title, String author, String isbn, int quantity) {
        Book book = new Book(nextBookId++, title, author, isbn, quantity);
        books.add(book);
        System.out.println("✅ Book added successfully! Book ID: " + book.getId());
    }
    
    public void viewAllBooks() {
        if (books.isEmpty()) {
            System.out.println("📭 No books in library.");
            return;
        }
        
        System.out.println("\n📚 ========== ALL BOOKS ==========");
        System.out.printf("%-5s %-25s %-15s %-10s %-10s\n", 
                         "ID", "Title", "Author", "Total", "Available");
        System.out.println("------------------------------------------------");
        
        for (Book book : books) {
            System.out.printf("%-5d %-25s %-15s %-10d %-10d\n",
                book.getId(), 
                truncate(book.getTitle(), 25),
                truncate(book.getAuthor(), 15),
                book.getQuantity(),
                book.getAvailableCount());
        }
    }
    
    public Book searchBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
    
    public List<Book> searchBookByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
    
    // ========== MEMBER OPERATIONS ==========
    
    public void addMember(String name, String email, String phone) {
        Member member = new Member(nextMemberId++, name, email, phone);
        members.add(member);
        System.out.println("✅ Member added successfully! Member ID: " + member.getId());
    }
    
    public void viewAllMembers() {
        if (members.isEmpty()) {
            System.out.println("📭 No members registered.");
            return;
        }
        
        System.out.println("\n👥 ========== ALL MEMBERS ==========");
        System.out.printf("%-5s %-20s %-25s %-15s %-10s\n", 
                         "ID", "Name", "Email", "Phone", "Books Issued");
        System.out.println("------------------------------------------------------------");
        
        for (Member member : members) {
            System.out.printf("%-5d %-20s %-25s %-15s %-10d\n",
                member.getId(),
                truncate(member.getName(), 20),
                truncate(member.getEmail(), 25),
                member.getPhone(),
                member.getBooksIssued());
        }
    }
    
    public Member searchMemberById(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }
    
    // ========== ISSUE/RETURN OPERATIONS ==========
    
    public boolean issueBook(int bookId, int memberId) {
        Book book = searchBookById(bookId);
        Member member = searchMemberById(memberId);
        
        // Validation checks
        if (book == null) {
            System.out.println("❌ Book not found!");
            return false;
        }
        
        if (member == null) {
            System.out.println("❌ Member not found!");
            return false;
        }
        
        if (book.getAvailableCount() <= 0) {
            System.out.println("❌ No copies available for this book!");
            return false;
        }
        
        if (member.getBooksIssued() >= 5) {  // Max 5 books per member
            System.out.println("❌ Member already has maximum books issued (5)!");
            return false;
        }
        
        // Process issue
        Transaction transaction = new Transaction(nextTransactionId++, bookId, memberId);
        transactions.add(transaction);
        
        // Update counts
        book.setAvailableCount(book.getAvailableCount() - 1);
        member.setBooksIssued(member.getBooksIssued() + 1);
        
        System.out.println("✅ Book issued successfully!");
        System.out.println("   Due date: " + transaction.getDueDate());
        return true;
    }
    
    public boolean returnBook(int bookId, int memberId) {
        // Find active transaction
        Transaction activeTransaction = null;
        for (Transaction t : transactions) {
            if (t.getBookId() == bookId && 
                t.getMemberId() == memberId && 
                t.getStatus().equals("ISSUED")) {
                activeTransaction = t;
                break;
            }
        }
        
        if (activeTransaction == null) {
            System.out.println("❌ No active issue found for this book and member!");
            return false;
        }
        
        // Process return
        activeTransaction.setReturnDate(java.time.LocalDate.now());
        activeTransaction.setStatus("RETURNED");
        
        // Update counts
        Book book = searchBookById(bookId);
        Member member = searchMemberById(memberId);
        book.setAvailableCount(book.getAvailableCount() + 1);
        member.setBooksIssued(member.getBooksIssued() - 1);
        
        // Calculate fine if overdue
        if (java.time.LocalDate.now().isAfter(activeTransaction.getDueDate())) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                activeTransaction.getDueDate(), 
                java.time.LocalDate.now()
            );
            int fine = (int) daysLate * 5;  // ₹5 per day
            System.out.println("⚠️ Book is overdue by " + daysLate + " days!");
            System.out.println("💰 Fine amount: ₹" + fine);
        }
        
        System.out.println("✅ Book returned successfully!");
        return true;
    }
    
    public void viewIssuedBooks() {
        System.out.println("\n📋 ========== CURRENTLY ISSUED BOOKS ==========");
        boolean found = false;
        
        for (Transaction t : transactions) {
            if (t.getStatus().equals("ISSUED")) {
                Book book = searchBookById(t.getBookId());
                Member member = searchMemberById(t.getMemberId());
                System.out.printf("Book: %s | Member: %s | Due: %s\n",
                    book.getTitle(), member.getName(), t.getDueDate());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No books currently issued.");
        }
    }

    public void viewAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("📭 No transactions recorded.");
            return;
        }

        System.out.println("\n📋 ========== ALL TRANSACTIONS ==========");
        System.out.printf("%-5s %-8s %-10s %-12s %-12s %-12s %-10s\n",
            "ID", "Book ID", "Member ID", "Issue Date", "Due Date", "Return Date", "Status");
        System.out.println("------------------------------------------------------------------------");

        for (Transaction t : transactions) {
            String returnDate = t.getReturnDate() != null
                ? t.getReturnDate().toString()
                : "NULL";
            System.out.printf("%-5d %-8d %-10d %-12s %-12s %-12s %-10s\n",
                t.getId(), t.getBookId(), t.getMemberId(),
                t.getIssueDate(), t.getDueDate(), returnDate, t.getStatus());
        }
    }
    
    public void loadData(List<Book> loadedBooks, List<Member> loadedMembers,
                         List<Transaction> loadedTransactions) {
        books = new ArrayList<>(loadedBooks);
        members = new ArrayList<>(loadedMembers);
        transactions = new ArrayList<>(loadedTransactions);
        updateNextIds();
    }

    public List<Book> getBooks() { return books; }
    public List<Member> getMembers() { return members; }
    public List<Transaction> getTransactions() { return transactions; }

    private void updateNextIds() {
        nextBookId = books.stream().mapToInt(Book::getId).max().orElse(0) + 1;
        nextMemberId = members.stream().mapToInt(Member::getId).max().orElse(0) + 1;
        nextTransactionId = transactions.stream().mapToInt(Transaction::getId).max().orElse(0) + 1;
    }

    // Helper method to truncate long strings
    private String truncate(String str, int length) {
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}