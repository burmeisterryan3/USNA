import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Class which allows for the transaction between different accounts given
 * a list of transactions through a text file
 */
public class BankThread extends Thread
{
    //String which contains the filepath of the text file to be read
    private String filepath;
    //Contains the information about each of the 10 accounts that be transacted with
    private Accounts account;

    /**
     * Constructor for BankThread objects
     * 
     * @param filepath String containing the filepath with transactions
     * @param account Accounts object to be kept transacted with
     */
    public BankThread(String filepath, Accounts account)
    {
        this.filepath = filepath;
        this.account = account;
    }
    
    /**
     * Will read from the filepath and make the transactions as stated within
     * the text file.  Ensures that withdrawals cannot occur without sufficient funds.
     */
    public void run()
    {
        try
        {
            Scanner fin = new Scanner(new File(filepath));
            String command;
            int accountNumber;
            double amount;
            while (fin.hasNext())
            {
                command = fin.next();
                accountNumber = fin.nextInt();
                amount = (double)fin.nextInt();
                if (command.equals("deposit"))
                {
                    account.increment(accountNumber, amount);
                }
                else
                {
                    account.decrement(accountNumber, amount);
                }
            }
        }
        catch(FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
    }
}
