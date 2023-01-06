/**
 * Class extended from Temperature which operates in degrees Fahrenheit and allows
 * for conversion between units of Celsius and Kelvin
 */
public final class Fahrenheit extends Temperature
{
    /**
     * Default Constructor: sets the temperature to the freezing 
     * point in Fahrenheit (32.00 degrees)
     */
    public Fahrenheit()
    {
        super();
    }
    
    /**
     * Constructor that set the temperature to that of the temperature input by user
     *
     * @param degrees Initial temperature set by user
     */
    public Fahrenheit(double degrees)
    {
        super(degrees);
    }
    
    /**
     * Returns the freezing point of Fahrenheit (32.00 degrees)
     *
     * @return Returns the freezing point of Fahrenheit (32.00 degrees)
     */
    public double freezingPoint()
    {
        return 32.00;
    }
    
    /**
     * Returns the boiling point of Fahrenheit (212.00 degrees)
     *
     * @return Returns the boiling point of Fahrenheit (212.00 degrees)
     */
    public double boilingPoint()
    {
        return 212.00;
    }
    
    /**
     * Returns absolute zero in degrees Fahrenheit (-459.00 degrees)
     *
     * @return Returns absolute zero in degrees Fahrenheit (-459.00 degrees)
     */
    public double absoluteZero()
    {
        return -459.00;
    }

    /**
     * Generates and returns a String with the units and temperature
     *
     * @return Returns a string with the temperature and unit of the temperature (e.g. 73.1 F)
     */
    public String toString()
    {
        String s = new String();
        s = String.format("%.1f F", super.get());
        return s;
    }
    
    /**
     * Returns the value of the temperature in degrees Fahrenheit
     *
     * @return Returns the value of the temperature in degrees Fahreneit
     */
    public double toFahrenheit()
    {
        return super.get();
    }
    
    /**
     * Converts the temperature to Celsius and returns this value
     *
     * @return Returns the value of the temperature in degrees Celsius
     */
    public double toCelsius()
    {
        return (super.get() - 32.00)*5/9;
    }
    
    /**
     * Converts the temperature to Kelvin
     *
     * @return Returns the value of the temperature in Kelvin
     */
    public double toKelvin()
    {
        return ((super.get() -32.00)*5/9) + 273.15;
    }
}
