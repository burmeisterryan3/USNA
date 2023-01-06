/* Class which extends exception and informs user if the input was not an int */
public class NotAnIntException extends Exception
{
    /* Default constructor which calls the default Exception constructor */
    public NotAnIntException()
    {
        super();
    }
    
    /* Default constructor which calls the Exception constructor with a string argument */
    public NotAnIntException(String message)
    {
        super(message);
    }
}
