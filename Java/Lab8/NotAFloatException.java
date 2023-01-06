/* Class which extends exception and informs user if the input was not a float */
public class NotAFloatException extends Exception
{
    /* Default constructor which calls the default Exception constructor */
    public NotAFloatException()
    {
        super();
    }
    
    /* Default constructor which calls the Exception constructor with a string argument */
    public NotAFloatException(String message)
    {
        super(message);
    }
}
