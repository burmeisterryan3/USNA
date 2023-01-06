import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Class which sorts and serializes a list of Car objeects read from a file entered
 * on the command line.  The sorting will occur first by price, followed by 
 * model year, and finally the make of the car if necessary.  The serialized
 * file's name is "sorted.obj".
 */
public class SortCars implements Serializable
{
    /**
     * Sorts the cars. Requires that the Car class implements Comparable and
     * has a compareTo method.
     * 
     * @param cars ArrayList containing the array of Car objects to be sorted
     */
    public void sortCars(ArrayList<Car> cars)
    {
        try
        {
            Collections.sort(cars);
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }
    }
    
    /**
     * Serializes the sorted ArrayList of Car objects and stores the result in
     * "sorted.obj".
     * 
     * @param cars ArrayList containing the array of Car objects to be serialized
     */
    public void serialize(ArrayList<Car> cars)
    {
        try 
        {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("sorted.obj"));
            output.writeObject(cars);
            output.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        /* Reading serial data from a file to an array of Cars
        ArrayList<Car> carsFromFile = new ArrayList<Car>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("sorted.obj"));
            carsFromFile = (ArrayList<Car>)input.readObject();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < carsFromFile.size(); i++)
        {
            System.out.println(carsFromFile.get(i));
        }
        */
    }

    public static void main(String[] args)
    {
        SortCars cars = new SortCars();
        Scanner fin = null;
        try
        {
            fin = new Scanner(new File(args[0]));
            ArrayList<Car> carArray = new ArrayList<Car>();
            String make = new String();
            int year;
            double cost;
            while (fin.hasNext())
            {
                make = fin.next();
                year = fin.nextInt();
                cost = fin.nextDouble();
                Car car = new Car(make, year, cost);
                carArray.add(car);
            }
            cars.sortCars(carArray);
            cars.serialize(carArray);
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
    }
}
