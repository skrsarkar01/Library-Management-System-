package libmanagementsystem.src.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int quantity;      
    private int availableCount; 
    
    // Constructor
    public Book(int id, String title, String author, String isbn, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        this.availableCount = quantity; 
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getQuantity() { return quantity; }
    public int getAvailableCount() { return availableCount; }
    public void setAvailableCount(int availableCount) { this.availableCount = availableCount; }
    public int getId() { return id; }
}