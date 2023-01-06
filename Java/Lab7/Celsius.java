/**
 * Class extended from Temperature which operates in degrees Celsius and allows
 * for conversion between units of Fahrenheit and Kelvin
 */
public final class Celsius extends Temperature
{
    /**
    * Class extended from Temperature which operates in degrees Celsius and allows
    * for conversion between units of Fahrenheit and Kelvin
    */
    public Celsius()
    {
        super();
    }
    
    /**
     * Constructor that set the temperature to that of the temperature input by user
     *
     * @param degrees Initial temperature set by user
     */
    public Celsius(double degrees)
    {
        super(degrees);
    }
    
    /**
     * Returns the freezing point of Celsius (0.00 degrees)
     *
     * @return Returns the freezing point of Celsius (0.00 degrees)
     */
    public double freezingPoint()
    {
        return 0.00;
    }
    
    /**
     * Returns the boiling point of Celsius (100.00 degrees)
     *
     * @return Returns the boiling point of Celsius (100.00 degrees)
     */
    public double boilingPoint()
    {
        return 100.00;
    }
    
    /**
     * Returns absolute zero in degrees Celsius (-273.15 degrees)
     *
     * @return Returns absolute zero in degrees Fahrenheit (-273.15 degrees)
     */
    public double absoluteZero()
    {
        return -273.15;
    }

    /**
     * Generates and returns a String with the units and temperature
     *
     * @return Returns a string with the temperature and unit (e.g. 73.1 C)
     */
    public String toString()
    {
        String s = new String();
        s = String.format("%.1f C", super.get());
        return s;
    }
    
    /**
     * Returns the value of the temperature in degrees Fahrenheit
     *
     * @return Returns the value of the temperature in degrees Fahrenheit
     */
    public double toFahrenheit()
    {
        return ((9/5*super.get()) + 32.00);
    }
    
    /**
     * Converts the temperature to Celsius and returns this value
     *
     * @return Returns the value of the temperature in degrees Celsius
     */
    public double toCelsius()
    {
        return super.get();
    }
    
    /**
     * Returns the value of the temperature in Kelvin
     *
     * @return Returns the value of the temperature in Kelvin
     */
    public double toKelvin()
    {
        return super.get() + 273.15;
    }
}
