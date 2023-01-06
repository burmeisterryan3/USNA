import java.io.Serializable;

/**
 * Class which represents a car throught its year of production, make, and cost.
 * Requires the implementation of the interfaces Comparable and Serializable.
 */
public class Car implements Comparable<Car>, Serializable
{
    //Contains the make of the car (i.e. Chevy, Toyota, Ford)
    private String make;
    //Contains the year of production for the car
    private int year;
    //Contains the cost of the car
    private double cost;
    
    /**
     * Constructor which assigns values to the make, year, and cost
     *
     * @param make Contains the make of the car
     * @param year Contains the year of production for the car
     * @param cost Contains the cost of the car
     */
    public Car(String make, int year, double cost)
    {
        this.make = make;
        this.year = year;
        this.cost = cost;
    }
    
    /**
     * Overrides the toString() method of the Object class to allow
     * for the printing of a Car object. <br>
     * Example: 2016 Toyota, $16000
     *
     * @return Returs the string format of a Car object
     */
    public String toString()
    {
        return (""+year+" "+make+", $"+cost);
    }
    
    /**
     * Getter for the make of the car
     *
     * @return Returns the make of the car
     */
    public String getMake()
    {
        return make;
    }
    
    /**
     * Setter for the make of the car
     *
     * @param make String to set make to
     */
    public void setMake(String make)
    {
        this.make = make;
    }
    
    /**
     * Getter for the year of the car
     *
     * @return Returns the year of the car
     */
    public int getYear()
    {
        return year;
    }
    
    /**
     * Setter for the year of the car
     *
     * @param year Int to set year to
     */
    public void setYear(int year)
    {
        this.year = year;
    }
    
    /**
     * Getter for the cost of the car
     *
     * @return Returns the cost of the car
     */
    public double getCost()
    {
        return cost;
    }
    
    /**
     * Setter for the cost of the car
     *
     * @param cost Dobule to set cost to
     */
    public void setCost(double cost)
    {
        this.cost=cost;
    }
    
    /**
     * Must be defined due to the implementation of the Comparable interface <br>
     * Compares the year, make, and cost to determine how to sort two Car objects <br>
     * Will sort first by cost, then by year, and finally make
     *
     * @param other The Car object to compare "this" Car object to
     * @return Returns -1 if "this" Car object should be sorted prior to the "other" car object
     *         and 1 otherwise
     */
    public int compareTo(Car other)
    {
        int returnVal = 1;
        
        if (this.cost > other.getCost())
        {
            returnVal = -1;
        }
        else if (this.cost == other.getCost())
        {
            if (this.year > other.getYear())
            {
                returnVal = -1;
            }
            else if (this.year == other.getYear())
            {
                returnVal = this.make.compareTo(other.getMake());
            }
        }  
            
        return returnVal;
    }
}
