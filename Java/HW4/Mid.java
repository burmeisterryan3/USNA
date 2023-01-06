public class Mid
{
    private int alpha;
    private String firstName;
    private String lastName;
    private int company;
    
    /*
    ** Constructor Method-- Initializes values
    */
    public Mid()
    {
        alpha = 0;
        firstName = "";
        lastName  = "";
        company = 0;
    }
    /*
    **Getters-- Return value necessary.
    */
    public int getAlpha()
    {
        return alpha;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public int getCompany()
    {
        return company;
    }

    /*
    **Setters-- set values in variables
    */
    public void setAlpha(int a)
    {
        alpha = a;
    }
    public void setFirstName(String fname)
    {
        firstName = fname;
    }
    public void setLastName(String lname)
    {
        lastName = lname;
    }
    public void setCompany(int c)
    {
        company = c;
    }
    
    /*
    **Establish print method
    */
    public void print()
    {
        if (alpha < 100000)
      {
	System.out.print("0");
      }
    if (alpha < 10000)
      {
	System.out.print("0");
      }
    if (alpha < 1000)
      {
	System.out.print("0");
      }
    if (alpha < 100)
      {
	System.out.print("0");
      }
    if (alpha < 10)
      {
	System.out.print("0");
      }
    System.out.println(alpha + " " + lastName + " " + firstName + " " + company);
    }

    /*
    **Establish method to determine if mid is in certain company
    */
    public boolean isInCompany(int c)
    {
        return (company == c);
    }
}