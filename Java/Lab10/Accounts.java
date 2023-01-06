/**
 * Class which keeps track of each of the 10 accounts and allows
 * for incrementing and decrementing by a specified amount
 */
public class Accounts
{
    //Array which represents each of the 10 accounts
    private double[] accountArray = new double[10];
    
    /**
     * Increments the account specified by the deposit amount
     *
     * @param accountNumber Array index of accountArray to be incremented
     * @param deposit Amount the account will receive
     */    
    public synchronized void increment(int accountNumber, double deposit)
    {
        accountArray[accountNumber] += deposit;
    }
 
    /**
     * Decrments the account specified by the withdrawal amount.  If the specified
     * account does not have sufficient funds, an error message is printed and the value
     * of the account remains unchanged.
     *
     * @param accountNumber Array index of accountArray to be decremented
     * @param withdrawal Amount the account will lose, if sufficient funds are present 
     */   
    public synchronized void decrement(int accountNumber, double withdrawal)
    {
        accountArray[accountNumber] -= withdrawal;
        if (accountArray[accountNumber] < 0)
        {
            System.err.println("ERROR: not enough money in " + accountNumber);
            accountArray[accountNumber] += withdrawal;
        }
    }

    /**
     * Allows for the printing of each of the 10 accounts (e.g. Account 3: 23422.00)
     * 
     * @return Returns a string of the each of the 10 accounts in accountArray
     */    
    public String toString()
    {
        String accountString = "";
        for (int i = 0; i < accountArray.length; i++)
        {
            accountString += "Account " + i + ": " + accountArray[i] + "\n";
        }
        return accountString;
    }
}
