package cs2050m01;


public class LibraryAppTest 
{

    /**
     * Entry point for the program
     */
    public static void main(String[] args) 
    {
    	
    	// --- unit test checks for Book ---
    	System.out.println("Unit Test Book Class");
    	Book unitTestBook = new Book("Unmasking AI", "Joy Buolamwini", 2023);
    	System.out.println("getTitle():   " + unitTestBook.getTitle());
    	System.out.println("getAuthor():  " + unitTestBook.getAuthor());
    	System.out.println("getYear():    " + unitTestBook.getYear());
    	System.out.println("stringOfBookDetails():   " + unitTestBook.stringOfBookDetails());
    	System.out.println();
    	System.out.println("Setting up Test Library");
    	int numberOfShelves = 3;
    	int shelfCapacity = 4;
    	System.out.println("Shelves (rows): " + numberOfShelves);
    	System.out.println("Slots per shelf (columns): " + shelfCapacity);
    	System.out.println("Total capacity: " + (numberOfShelves * shelfCapacity));
    	System.out.println();
    	Library library = new Library("Test Library", numberOfShelves, shelfCapacity);
    	library.displayCountPerShelf();
    	library.printAllBooks();
    	library.displayOldest();
    	// Row 0
    	library.addBook(null);
    	library.addBook(new Book("Unmasking AI", "Joy Buolamwini", 2023));
    	library.addBook(new Book("Hello World", "Hannah Fry", 2018));
    	library.addBook(new Book("Race After Technology", "Ruha Benjamin", 2019));
    	library.addBook(new Book("Deep Learning", "Ian Goodfellow", 2016));
    	library.displayCountPerShelf();
    	library.printAllBooks();
    	library.displayOldest();
    	// Row 1
    	library.addBook(new Book("Algorithms to Live By", "Brian Christian", 2016));
    	library.addBook(new Book("Weapons of Math Destruction", "Cathy O'Neil", 2016));
    	library.addBook(new Book("The Mythical Man-Month", "Fred Brooks", 1975));
    	library.addBook(new Book("Refactoring", "Martin Fowler", 1999));
    	// Row 2
    	library.addBook(new Book("The Pragmatic Programmer", "Andrew Hunt & David Thomas", 1999));
    	library.addBook(new Book("Peopleware", "Tom DeMarco & Tim Lister", 1987));
    	library.addBook(new Book("Computer Lib / Dream Machines", "Ted Nelson", 1975));
    	library.displayCountPerShelf();
    	library.printAllBooks();
    	library.displayOldest();
    	System.out.println();
    	System.out.println("Test add more books than capacity...");
    	library.addBook(new Book("Extra Title", "Extra Author", 2024)); // should trigger "full" message
    	library.displayCountPerShelf();
    	library.printAllBooks();
    	library.displayOldest();
   }// end main

}

/**
 * Represents a book with a title, author, and publication year
 */
class Book {
    private final String name;
    private final String author;
    private final int year;

    /**
     * Constructs a new book
     */
    public Book(String name, String author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
    }

    /** Returns the title of the book */
    public String getTitle() {
        return name;
    }

    /** Returns the author of the book */
    public String getAuthor() {
        return author;
    }

    /** Returns the publication year of the book. */
    public int getYear() {
        return year;
    }

    /**
     * Returns a string with book details using a quoted title
     */
    public String stringOfBookDetails() {
        return "\"" + name + "\" by " + author + " (" + year + ")";
    }

    @Override
    public String toString() {
        return stringOfBookDetails();
    }
}

/**
 * Represents a library
 */
class Library {
    private static final String SEP =
            "------------------------------------------------------------";

    private final String name;
    private final int shelvesCount;
    private final int shelfCapacity;
    private final Book[][] shelves;

    /**
     * Constructs the shelves for the library
     */
    public Library(String name, int numberOfShelves, int shelfCapacity) {
        this.name = name;
        this.shelvesCount = numberOfShelves;
        this.shelfCapacity = shelfCapacity;
        this.shelves = new Book[numberOfShelves][shelfCapacity];
    }

    /**
     * Adds a book to the first available slot
     */
    public void addBook(Book b) {
        if (b == null) {
            System.out.println("Invalid book.");
            return;
        }
        for (int r = 0; r < shelvesCount; r++) {
            for (int c = 0; c < shelfCapacity; c++) {
                if (shelves[r][c] == null) {
                    shelves[r][c] = b;
                    System.out.println("Added " + b.stringOfBookDetails()
                            + " at shelf " + (r + 1) + ", slot " + (c + 1));
                    return;
                }
            }
        }
        System.out.println("Library is full. Could not add: " + b.stringOfBookDetails());
    }

    /**
     * Returns the book to a given shelf + slot
     */
    public Book getAt(int shelf, int slot) {
        if (!inBounds(shelf, slot)) {
            return null;
        }
        return shelves[shelf][slot];
    }

    /** Prints all books in the library */
    public void printAllBooks() {
        System.out.println(SEP);
        System.out.println("All books in " + name);
        System.out.println("Shelf   Slot   Book Details");
        System.out.println(SEP);

        int filled = 0;
        for (int r = 0; r < shelvesCount; r++) {
            for (int c = 0; c < shelfCapacity; c++) {
                Book b = shelves[r][c];
                if (b != null) {
                    System.out.printf("%4d%6d  %s%n", (r + 1), (c + 1),
                            b.stringOfBookDetails());
                    filled++;
                }
            }
        }
        int total = shelvesCount * shelfCapacity;
        System.out.println("(" + filled + " of " + total + " slots filled)");
    }

    /**
     * Displays the number of books on each shelf
     */
    public void displayCountPerShelf() {
        for (int r = 0; r < shelvesCount; r++) {
            int count = 0;
            for (int c = 0; c < shelfCapacity; c++) {
                if (shelves[r][c] != null) {
                    count++;
                }
            }
            System.out.println("Shelf " + (r + 1) + " has " + count + " books");
        }
    }

    /**
     * Displays the oldest book(s) in the library by publication year
     * Prints an empty-state message if there are no books found
     */
    public void displayOldest() {
        int earliest = Integer.MAX_VALUE;
        boolean any = false;

        // First pass: find earliest year
        for (int r = 0; r < shelvesCount; r++) {
            for (int c = 0; c < shelfCapacity; c++) {
                Book b = shelves[r][c];
                if (b != null) {
                    any = true;
                    if (b.getYear() < earliest) {
                        earliest = b.getYear();
                    }
                }
            }
        }

        if (!any) {
            int total = shelvesCount * shelfCapacity;
            System.out.println("(0 of " + total + " slots filled)");
            System.out.println("Display Oldest: Library is empty.");
            return;
        }

        System.out.println(SEP);
        System.out.println("Oldest books in " + name);
        System.out.println("Earliest publication year: " + earliest);

        // Second pass: print all books matching earliest year
        for (int r = 0; r < shelvesCount; r++) {
            for (int c = 0; c < shelfCapacity; c++) {
                Book b = shelves[r][c];
                if (b != null) {
                    if (b.getYear() == earliest) {
                        System.out.println(b.stringOfBookDetails());
                    }
                }
            }
        }
    }

    /**
     * Checking whether a given shelf/slot coordinate is valid
     */
    private boolean inBounds(int shelf, int slot) {
        if (shelf < 0) {
            return false;
        }
        if (shelf >= shelvesCount) {
            return false;
        }
        if (slot < 0) {
            return false;
        }
        if (slot >= shelfCapacity) {
            return false;
        }
        return true;
    }
}


