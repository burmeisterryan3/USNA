import java.util.Arrays;
public class Bookstore
{
    ReadingMaterial[] readings;
    
    public Bookstore(ReadingMaterial[] readings)
    {
        this.readings = readings;
    }
    
    public void buy(String title)
    {
        int i = 0;
        while(i < readings.length && !readings[i].getTitle().equals(title))
        {
            i++;
        }
        if (i < readings.length && readings[i].getTitle().equals(title))
        {
            try
            {
                readings[i].buy();
            }
            catch (OutOfStockException oose)
            {
                throw new OutOfStockException();
            }
        }
        else {
            throw new OutOfStockException();
        }
    }
    
    public void printAll()
    {
        for (int i = 0; i < readings.length; i++)
        {
            System.out.println(readings[i]);
        }
    }
    
    public void sort()
    {
        Arrays.sort(readings);
    }
}
