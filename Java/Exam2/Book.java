public class Book extends ReadingMaterial
{
    private String author;
    
    public Book(String title, String author, double cost, int quantity)
    {
        super(title, cost, quantity);
        this.author = author;
    }
    
    public String toString()
    {
        return (author + ", " + super.getTitle() + ": $" + super.getCost());
    }
    
    public int compareTo(ReadingMaterial other)
    {
        return (super.compareTo(other));
    }
}
