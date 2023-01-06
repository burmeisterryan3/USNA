/**
 * Class extended from Temperature which operates in degrees Kelvin and allows
 * for conversion between units of Celsius and Fahrenheit
 */
public final class Kelvin extends Temperature
{
    /**
     * Class extended from Temperature which operates in degrees Kelvin and allows
     * for conversion between units of Celsius and Temperature
     */
    public Kelvin()
    {
        super();
    }
    
    /**
     * Constructor that set the temperature to that of the temperature input by user
     *
     * @param degrees Initial temperature set by user
     */
    public Kelvin(double degrees)
    {
        super(degrees);
    }
    
    /**
     * Returns the freezing point of Kelvin (273.15 degrees)
     *
     * @return Returns the freezing point of Kelvin (273.15 degrees)
     */
    public double freezingPoint()
    {
        return 273.15;
    }
    
    /**
     * Returns the boiling point of Kelvin (373.15 degrees)
     *
     * @return Returns the boiling point of Fahrenheit (373.15 degrees)
     */
    public double boilingPoint()
    {
        return 373.15;
    }
    
    /**
     * Returns absolute zero in degrees Kelvin (0.00 degrees)
     *
     * @return Returns absolute zero in degrees Fahrenheit (0.00 degrees)
     */
    public double absoluteZero()
    {
        return 0;
    }

    /**
     * Generates and returns a String with the units and temperature
     *
     * @return Returns a string with the temperature and unit (e.g. 73.1 K)
     */
    public String toString()
    {
        String s = new String();
        s = String.format("%.1f K", super.get());
        return s;
    }
    
    /**
     * Returns the value of the temperature in Fahrenheit
     *
     * @return Returns the value of the temperature in Fahrenheit
     */
    public double toFahrenheit()
    {
        return ((9/5*(super.get()-273.15)) + 32.00);
    }
    
    /**
     * Converts the temperature to Celsius and returns this value
     *
     * @return Returns the value of the temperature in degrees Celsius
     */
    public double toCelsius()
    {
        return super.get() - 273.15;
    }
    
    /**
     * Returns the value of the temperature in Kelvin
     *
     * @return Returns the value of the temperature in Kelvin
     */
    public double toKelvin()
    {
        return super.get();
    }
}
