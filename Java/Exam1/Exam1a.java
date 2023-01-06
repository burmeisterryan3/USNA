public class Exam1a
{
    public void printLions(Lion[] lionArray)
    {
        for (int i = 0; i < lionArray.length; i++){
            System.out.println(lionArray[i].toString());
        }
    }
    public void printMostFit(Lion[] lionArray)
    {
        Lion mostFitLion = lionArray[0];
        for (int i = 1; i < lionArray.length; i++){
            if (lionArray[i].isMoreFitThan(mostFitLion)){
                mostFitLion = lionArray[i];
            }
        }
        System.out.println("Best Lion:  " + mostFitLion.toString());
    }
    public static void main(String[] args)
    {
        Exam1a exam = new Exam1a();
        int length = args.length;
        int numLions = length/3;
        int counter = 0;
        Lion[] lions = new Lion[numLions];
        for (int i = 0; i < lions.length; i++){
            lions[i] = new Lion(Double.parseDouble(args[counter]), 
                                Double.parseDouble(args[counter+1]), 
                                Double.parseDouble(args[counter+2]));
            counter += 3;
        }
        exam.printLions(lions);
        exam.printMostFit(lions);
    }
}