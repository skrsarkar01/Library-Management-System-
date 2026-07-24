package libmanagementsystem.src;
import java.util.List;
import java.util.Scanner;

import libmanagementsystem.src.model.Book;
import libmanagementsystem.src.model.Member;
import libmanagementsystem.src.model.Transaction;
import libmanagementsystem.src.services.FileStorageService;
import libmanagementsystem.src.services.LibraryService;

public class main {
    private static LibraryService library = new LibraryService();
    private static Scanner scanner = new Scanner(System.in);
    private static FileStorageService storage = new FileStorageService();
    
    public static void main(String[] args) {
        // Load previous data
        loadData();
        
        System.out.println("📚 WELCOME TO LIBRARY MANAGEMENT SYSTEM 📚");
        
        while (true) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1: bookManagementMenu(); break;
                case 2: memberManagementMenu(); break;
                case 3: transactionMenu(); break;
                case 4: 
                    saveData();
                    System.out.println("👋 Thank you for using Library System!");
                    System.exit(0);
                    break;
                default: System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. 📖 Book Management");
        System.out.println("2. 👥 Member Management");
        System.out.println("3. 🔄 Issue/Return Books");
        System.out.println("4. 💾 Save & Exit");
        System.out.println("=================================");
    }
    
    private static void bookManagementMenu() {
        while (true) {
            System.out.println("\n📖 ========== BOOK MANAGEMENT ==========");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Back to Main Menu");
            System.out.println("========================================");
            
            int choice = getIntInput("Enter choice: ");
            
            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    library.addBook(title, author, isbn, qty);
                    persistData();
                    break;
                    
                case 2:
                    library.viewAllBooks();
                    break;
                    
                case 3:
                    searchBookMenu();
                    break;
                    
                case 4:
                    return;
                    
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void searchBookMenu() {
        System.out.println("\n1. Search by ID");
        System.out.println("2. Search by Title");
        int choice = getIntInput("Enter choice: ");
        
        if (choice == 1) {
            int id = getIntInput("Enter Book ID: ");
            Book book = library.searchBookById(id);
            if (book != null) {
                System.out.println("📖 Found: " + book.getTitle() + " by " + book.getAuthor());
            } else {
                System.out.println("❌ Book not found!");
            }
        } else if (choice == 2) {
            System.out.print("Enter title (or part of it): ");
            String title = scanner.nextLine();
            List<Book> results = library.searchBookByTitle(title);
            if (results.isEmpty()) {
                System.out.println("❌ No books found!");
            } else {
                System.out.println("Found " + results.size() + " book(s):");
                for (Book b : results) {
                    System.out.println("  - " + b.getTitle() + " (ID: " + b.getId() + ")");
                }
            }
        }
    }
    
    private static void transactionMenu() {
        while (true) {
            System.out.println("\n🔄 ========== ISSUE/RETURN ==========");
            System.out.println("1. Issue Book to Member");
            System.out.println("2. Return Book");
            System.out.println("3. View Currently Issued Books");
            System.out.println("4. View All Transactions");
            System.out.println("5. Back to Main Menu");
            System.out.println("=====================================");
            
            int choice = getIntInput("Enter choice: ");
            
            switch (choice) {
                case 1:
                    library.viewAllBooks();
                    int bookId = getIntInput("Enter Book ID to issue: ");
                    
                    library.viewAllMembers();
                    int memberId = getIntInput("Enter Member ID: ");
                    
                    if (library.issueBook(bookId, memberId)) {
                        persistData();
                    }
                    break;
                    
                case 2:
                    int returnBookId = getIntInput("Enter Book ID to return: ");
                    int returnMemberId = getIntInput("Enter Member ID: ");
                    if (library.returnBook(returnBookId, returnMemberId)) {
                        persistData();
                    }
                    break;
                    
                case 3:
                    library.viewIssuedBooks();
                    break;

                case 4:
                    library.viewAllTransactions();
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void memberManagementMenu() {
        while (true) {
            System.out.println("\n👥 ========== MEMBER MANAGEMENT ==========");
            System.out.println("1. Add New Member");
            System.out.println("2. View All Members");
            System.out.println("3. Search Member");
            System.out.println("4. Back to Main Menu");
            System.out.println("=========================================");
            
            int choice = getIntInput("Enter choice: ");
            
            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    library.addMember(name, email, phone);
                    persistData();
                    break;
                    
                case 2:
                    library.viewAllMembers();
                    break;
                    
                case 3:
                    int id = getIntInput("Enter Member ID: ");
                    Member member = library.searchMemberById(id);
                    if (member != null) {
                        System.out.println("👤 Found: " + member.getName());
                        System.out.println("   Email: " + member.getEmail());
                        System.out.println("   Books issued: " + member.getBooksIssued());
                    } else {
                        System.out.println("❌ Member not found!");
                    }
                    break;
                    
                case 4:
                    return;
                    
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("❌ Please enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }
    
    private static void loadData() {
        System.out.println("📂 Loading previous data...");
        List<Book> books = storage.loadBooks();
        List<Member> members = storage.loadMembers();
        List<Transaction> transactions = storage.loadTransactions();
        library.loadData(books, members, transactions);
        System.out.println("✅ Loaded " + books.size() + " books, " +
                           members.size() + " members, " +
                           transactions.size() + " transactions.");
    }
    
    private static void persistData() {
        storage.saveBooks(library.getBooks());
        storage.saveMembers(library.getMembers());
        storage.saveTransactions(library.getTransactions());
    }

    private static void saveData() {
        System.out.println("💾 Saving data...");
        persistData();
        System.out.println("✅ Data saved successfully!");
    }
}