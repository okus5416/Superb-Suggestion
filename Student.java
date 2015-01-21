
/**
 * The Student Class allows access to student's name and ratings. 
 * 
 * @Jamie, Jay, Min, Bethany 
 * @version 2.4.1.7.b9
 */
import java.util.*;
public class Student
{
    private String name;
    private int[] ratings = new int[150];

    public Student(String n, int[] r)
    {
        name = n;
        ratings = r;        
    }

    public void addRating(int pos, int rate)
    {
        ratings[pos] = rate;
    }

    public void editRating(int pos, int rate)
    {
        ratings[pos] = rate;
    }

    public String getName()
    {
        return name;
    }

    public int getRatings(int a)
    {
        return ratings[a];
    }

}