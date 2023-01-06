public abstract class ReadingMaterial implements Comparable<ReadingMaterial>
{
    private String title;
    private double cost;
    private int quantity;
    
    public ReadingMaterial(String title, double cost, int quantity)
    {
        this.title = title;
        this.cost = cost;
        this.quantity = quantity;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public double getCost()
    {
        return cost;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public void buy()
    {
        if (quantity == 0)
        {
            throw new OutOfStockException();
        }
        else {
            this.quantity = quantity-1;
        }
    }
    
    public abstract String toString();
    
    public int compareTo(ReadingMaterial other)
    {
        int i = 1;
        if (this.cost < other.cost)
        {
            i = -1;
        }
        
        return i;
    }
}
