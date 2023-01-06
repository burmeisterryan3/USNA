public class Magazine extends ReadingMaterial
{
    public Magazine(String title, double cost, int quantity)
    {
        super(title, cost, quantity);
    }
    
    public String toString()
    {
        return (super.getTitle() + ": $" + super.getCost());
    }
    
    public int compareTo(ReadingMaterial other)
    {
        return (super.compareTo(other));
    }
}
