/* Calculates the sum of the integers of the command line arguments */
public class Sum
{
    /* Tracks the sum of the command line integers */
    static int sum;
    /* 
     * Calculates the sum of the command line integers and throws
     * a Magic47Exception if the sum equals 47 once finished
     *
     * @param nums Integer array of command line integers
     * @return Returns the sum of the command line integers
     */
    public static int sumNumbers (int [] nums)
    {
        for (int i = 0; i < nums.length; i++)
        {
            sum += nums[i];
        }
        if (sum == 47)
        {
            throw new Magic47Exception("You got 47!!");
        }
        return sum;
    }
    
    public static void main (String[] args)
    {
        int[] intArray = new int[args.length];
        for (int i = 0; i < intArray.length; i++)
        {
            intArray[i] = Integer.parseInt(args[i]);
        }
        try
        {
            sum = sumNumbers(intArray);
            System.out.println(sum);
        }
        catch (Magic47Exception mfse)
        {
            mfse.printStackTrace();
        }
    }    
}
