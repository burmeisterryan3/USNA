/**
 * Class which allows for multiple transactions, using a text file, between an 
 * Account ojbect to occur simultaneously.
 */
public class Banks
{
    /**
     * Creates the separate threads for each text file and creates a thread
     * for each of them to allow for separate transactions from multiple files
     * to occur at once.  Will print the status of the accounts when finished.
     */
    public static void main(String[] args)
    {
        Accounts accounts = new Accounts();
        BankThread[] bankThreadArray = new BankThread[args.length];
        for (int i = 0; i < bankThreadArray.length; i++)
        {
            bankThreadArray[i] = new BankThread(args[i], accounts);
            bankThreadArray[i].start();
        }
        try
        {
            for (int i = 0; i < bankThreadArray.length; i++)
            {
                bankThreadArray[i].join();
            }
        }
        catch (InterruptedException ie)
        {
            ie.printStackTrace();
        }
        System.out.println("ACCOUNT BALANCES");
        System.out.println(accounts);
    }
}
