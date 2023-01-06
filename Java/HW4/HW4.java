import java.util.Scanner;
public class HW4
{
    /*
    **Main Method
    */
    public static void main(String [] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mids? ");
        Mid [] midshipman = new Mid[scanner.nextInt()];
        for(int i = 0; i < midshipman.length; i++)
        {
            midshipman[i] = new Mid();
            System.out.print("Alpha? ");
            midshipman[i].setAlpha(scanner.nextInt());
            System.out.print("First name? ");
            midshipman[i].setLastName(scanner.next());
            System.out.print("Last name? ");
            midshipman[i].setFirstName(scanner.next());
            System.out.print("Company? ");
            midshipman[i].setCompany(scanner.nextInt());
        }
        System.out.print("What company would you like to print out? ");
        int coCheck = scanner.nextInt();
        for (int ii = 0; ii < midshipman.length; ii++)
        {
            if (midshipman[ii].isInCompany(coCheck))
            {
                midshipman[ii].print();
            }
        }
    }
}