/* Abstract class that allows for conversion between Fahrenheit, Kelvin, and Celsius */
public abstract class Temperature
{
    /* Double to keep track of the temperature */
    private double degrees;
    
    /* Returns the freezing point of the temperature */
    public abstract double freezingPoint();
    /* Returns the boiling point of the temperature */
    public abstract double boilingPoint();
    /* Returns the absolute zero temperature based on the class the user is working within */
    public abstract double absoluteZero();
    /* Returns a string of the temperature with units */
    public abstract String toString();
    /* Returns the Fahrenheit equivalent of the temperature */
    public abstract double toFahrenheit();
    /* Returns the Celsius equivalent of the temperature */
    public abstract double toCelsius();
    /* Returns the Kelvin equivalent of the temperature */
    public abstract double toKelvin();
    
    /** 
     * Constructor to set temperature when given initial temperature
     *
     * @param degrees Initial temperature determined by user
     */
    protected Temperature(double degrees)
    {
        this.degrees = degrees;
        
        if (this.toKelvin() < 0.0)
        {
            throw new IllegalArgumentException(degrees + " is not a valid input!");
        }
    }
    
    /* Default Constructor: sets temperature to freezing point of unit user is working in */
    protected Temperature()
    {
        degrees = freezingPoint();
    }

    /**
     * Sets the temperature to the temperature determined by the user
     *
     * @param n Temperature is set to this value input by user
     */
    public void set(double n)
    {
        degrees = n;
        
        if (this.toKelvin() < 0.0)
        {
            throw new IllegalArgumentException(degrees + " is not a valid input!");
        }
    }
    
    /**
     * Getter which returns the temperature
     *
     * @return Returns the temperature
     */
    public double get()
    {
        return degrees;
    }
}
