
/**
 * The Admin class is specifically designed to be carried out only if an administrator logs on.
 * It gives the administrators the ability to add movies to the database. 
 * 
 * @Jamie, Jay, Min, Bethany
 * @version 2.4.1.7.b9
 */
import java.util.*;
import java.io.*;
public class Admin
{
    //declare an array list to keep track of the movies added to the database in one run through of the program
    ArrayList<String> newMovies = new ArrayList<String>();

    //main class of the administrator
    public void main(String[] movieList, int numMov)
    {
        Scanner inner = new Scanner(System.in);    
        //loop program until admin logs out
        for (int i=0;;)
        {
            //instructions for admin
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("What would you like to do?");
            System.out.println("1. Approve movies (type '1')");
            System.out.println("2. Add movies (type '2')");
            System.out.println("3. Log off (type '3')");
            String ans = inner.nextLine();     

            if (ans.equals("1"))
            {
                //if admin wants to approve movies...
                System.out.println("Generating requested movies...");
                int num = movieSuggestions();     //call method to show all user movie suggestions
                if (num==0) //if there are no requested movies, print message
                {
                    System.out.println("There are no movies that need approval.");
                }
                else    //if there are requested movies, ask for approval
                {
                    System.out.println("Which movie would you like to approve?");
                    String pickMovie = inner.nextLine();    //get movie admin wants to approve
                    newMovies.add(pickMovie);               //add movie to array of new movies
                    addMovie(movieList, pickMovie, numMov); //add movie to text file with movie list
                    delMov(pickMovie);                      //delete the movie from the movie suggestions text file
                    System.out.println(pickMovie + " has been added to movie database.");
                }
            }
            else if (ans.equals("2"))
            {
                //if admin wants to add a movie...
                System.out.println("What movie would you like to add to the database?");
                String ansMovie = inner.nextLine();     //get movie admin wants to approve
                newMovies.add(ansMovie);                //add movie to array of new movies
                addMovie(movieList, ansMovie, numMov);  //add movie to text file with movie list
                System.out.println(ansMovie + " has been added to movie database.");
            }
            else if (ans.equals("3"))
            {
                //when admin is finished, they can log out without returning to Main Class
                System.out.println("Thank-you for managing the Superb Suggestor!");
                System.exit(0);
            }
            else
            {
                //if admin inputs an invalid option, tell them and loop it
                System.out.println("That is not an option.");
            }
        } 
    }

    //outputs all suggested movies by users
    public int movieSuggestions()
    {
        File textFile = new File("Suggestions.txt");
        FileReader in;
        BufferedReader readFile;     
        String lineOfText;
        int count=0;
        try
        {
            in = new FileReader("Suggestions.txt");
            readFile = new BufferedReader(in);
            while ((lineOfText = readFile.readLine()) != null)
            {
                System.out.println(lineOfText);  //print each line of text file on terminal window
                count+= 1;
            }      
            readFile.close();
            in.close();
        } catch (FileNotFoundException e)   //catch errors
        {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
        }
        return count;
    }

    //add movie to text file named 'Movies'
    public void addMovie(String[] movies, String mov, int numMov)
    {
        File textFile = new File("Movies.txt");
        FileReader in;
        BufferedReader readFile;     
        FileWriter out;
        BufferedWriter writeFile;
        try 
        {
            out = new FileWriter("Movies.txt");
            writeFile = new BufferedWriter(out); 
            //write all previous movies onto text file
            writeFile.write(movies[0]);   
            for (int i=0; i<numMov-1; i++)
            {
                writeFile.newLine();
                writeFile.write(movies[i+1]);
            }
            //write all new movies added to Movie list
            for (int h=0; h<newMovies.size(); h++)
            {
                writeFile.newLine();
                writeFile.write(newMovies.get(h));
            }
            writeFile.close();  
            out.close();
        } catch (IOException e)     //catch any errors
        {
            System.out.println("Problem writing to file.");
            System.err.println("IOException: " + e.getMessage());
        }
    }

    //delete movie that admin approved from the text file named 'Suggestions' as it is no longer merely a suggestion
    public void delMov(String mov)
    {
        File textFile = new File("Suggestions.txt");
        FileReader in;
        BufferedReader readFile;     
        FileWriter out;
        BufferedWriter writeFile;
        //declare variables
        String lineOfText;
        ArrayList<String> movieSuggest = new ArrayList<String>();
        try
        {
            in = new FileReader("Suggestions.txt");
            readFile = new BufferedReader(in);
            while ((lineOfText = readFile.readLine()) != null)
            {
                movieSuggest.add(lineOfText);  //put each suggested movie in text file into an element on an array list
            }      
            readFile.close();
            in.close();
        } catch (FileNotFoundException e)  
        {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
        }
        try 
        {
            out = new FileWriter("Suggestions.txt");
            writeFile = new BufferedWriter(out); 
            //write all elements of the array list (containing movie suggestions)
            for (int i=0; i<movieSuggest.size(); i++)
            {
                //only write movies that have not been already approved
                if (movieSuggest.get(i).equals(mov) == false)
                {
                    writeFile.write(movieSuggest.get(i));
                    writeFile.newLine();
                }
            }
            writeFile.close();  
            out.close();
        } catch (IOException e)     //catch any errors
        {
            System.out.println("Problem writing to file.");
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
