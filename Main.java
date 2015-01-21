/**
 * The Superb Suggestor is a database of movie ratings.
 * Users can log on or create new accounts and do multiple things with their ratings in the database.
 * However, the main purpose of this program is to suggest movies for the user depending on what they like and what is popular.
 * 
 * @Jamie, Jay, Min, Bethany
 * @version 2.4.1.7.b9
 */

import java.util.*;
import java.io.*;
public class Main
{
    public static void main (String[] args)
    {
        Scanner inner = new Scanner(System.in);

        //declare and initialize movie variables
        int numMovies = countMovies();
        String[] movieList = new String[numMovies];
        movieList = getMovies();

        //create readers to read/write textfiles
        File textFile = new File("Parent.txt");
        FileReader in;
        BufferedReader readFile;     
        FileWriter out;
        BufferedWriter writeFile;

        //create student object array list
        ArrayList<Student> students = new ArrayList<Student>();
        Admin administrators = new Admin();

        String line;
        int place = 0;
        String parentName = "";

        //add each student object to student array list
        try
        {
            in = new FileReader("Parent.txt");
            readFile = new BufferedReader(in);
            while ((line = readFile.readLine()) != null)
            {      
                //create new rating array for each user
                int rating[] = new int[numMovies];
                String named = line.substring(0, line.indexOf(" "));    //grab the name from the first word of the line
                //for the first line, initialize the comparable name, called 'parentName'
                if (place == 0)
                {
                    parentName = named;
                    students.add(new Student(parentName, rating));  //create a student object with first name, and ratings array of 0's
                    place += 1;     //change value so that this statement can only be performed once
                }
                //if the user's name on the line is different from previous user's name...
                if (named.equals(parentName) == false)
                {
                    parentName = named;     //re-initialize the parentName
                    students.add(new Student(parentName, rating));  //create a student object with first name, and ratings array of 0's
                }
                //Thus, purpose is to make only one student object for each user
            }      
            readFile.close();
            in.close();
        } catch (FileNotFoundException e)  //catch any errors
        {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
        }

        //declare and initialize variables for second access to Parent text file
        String tempName;
        place = 0;
        int position = 0;

        //edit ratings of each student according to information in the database
        try
        {
            in = new FileReader("Parent.txt");
            readFile = new BufferedReader(in);
            while ((line = readFile.readLine()) != null)
            {    
                //grab the name, movie, and rating in each line
                int space = line.indexOf(" ");
                tempName = line.substring(0, space);
                int space2 = (line.substring(space+2,line.length())).indexOf(" ") + (space+2);
                String movie = line.substring(space+1, space2);
                int rated = Integer.parseInt(line.substring(space2+1, line.length()));

                //for the first line, initialize comparable parentName in order to ensure no repeats of user names
                if (place == 0)
                {
                    parentName = tempName;
                    place = 1;  //change place number so that statement only occurs once on the first line
                }

                //if name is equal to previous name...
                if (tempName.equals(parentName))
                {
                    for (int i=0; i<numMovies; i++)
                    {
                        //compare the movie on the line to the movie in the list in order to retrieve index
                        if (movieList[i].equals(movie))
                        {
                            students.get(position).addRating(i, rated);     //add the rating of specified movie index to student object
                        }
                    }
                }
                //if name is not equal to previous name, a new user name is being encountered
                else
                {
                    position += 1;      //count the number of user's
                    parentName = tempName;  //re-initialize parentName to new user's name
                    for (int i=0; i<numMovies; i++)
                    {
                        //compare the movie on the line to the movie in the list in order to retrieve index
                        if (movieList[i].equals(movie))
                        {
                            students.get(position).addRating(i, rated);     //add the rating of specified movie index to student object
                        }
                    }
                }
            }      
            readFile.close();
            in.close();
        } catch (FileNotFoundException e)  //catch any errors
        {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
        }

        //begin interaction with user
        String ready;
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||- WELCOME TO THE SUPERB SUGGESTER-|||||||||||||||||||||");
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        String name = "unknown";
        String password = "password";
        //determine whether new user or existing user
        do {
            System.out.println("Press 1 if you are a new user or press 2 if you already have an account.");
            int newUser = Integer.parseInt(inner.nextLine());
            if (newUser == 1)   //NEW USER
            {
                System.out.println("Please enter your desired username (no spaces allowed)");
                boolean taken;
                //check if desired username has already been taken; loop until a new and unused username is acquired
                do
                {
                    taken = false;
                    name = inner.nextLine();
                    if (name.equals("Min") || name.equals("Jay") || name.equals("Bethany"))
                    {
                        taken = true;
                    }
                    for (int checkName=0; checkName< students.size()-1; checkName++)
                    {
                        if ((students.get(checkName).getName()).equals(name))
                        {
                            taken = true;
                        }
                    }
                    if (taken == true)
                    {
                        System.out.println("Username " + name + " has been taken. Please enter another desired username.");
                    }
                    //make sure no spaces are in the desired username
                    if (name.indexOf(" ") != -1)
                    {
                        System.out.println("Do not use spaces. Please enter another desired username.");
                        taken = true;
                    }
                } while (taken == true);
                //create a new array of ratings for new user
                int rating[] = new int[numMovies];
                students.add(new Student(name, rating));    //create new student object using desired name and empty ratings array

                //get new user to begin by rating 25 movies
                System.out.println("Thank-you " + name + ". Before we begin, complete the following instructions:");
                System.out.println("1. We will be giving you a set of 25 movies and you must rate them on a scale.");
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("||||||||||||||||||||||||||||-5, -3, 0, 1, 3, or 5|||||||||||||||||||||||||||");
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("          -5 = Worst movie you have ever seen.");
                System.out.println("          -3 = Terrible; Would not suggest watching!");
                System.out.println("           0 = Haven't seen it...");
                System.out.println("           1 = Impartial. It was a movie...");
                System.out.println("           3 = It was good but could have been better.");
                System.out.println("           5 = Great movie; Would suggest to others!");
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("2. When you have come up with your rating, type it in and click enter.");
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Let's begin:");

                //create array to ensure random numbers do not repeat
                String[] randomUsed = new String[numMovies +1];
                int numRate = 0;
                int num;
                for (int n = 0; n < numMovies; n++)
                {
                    randomUsed[n] = "N";
                }

                //loop until user has rated 25 movies
                do
                {
                    num = randomNum(numMovies);     //call method to get random integer depending on size of the movie list
                    if (randomUsed[num].equals("N"))
                    {
                        numRate += 1;   //keep track of how many movies have been rated by user
                        randomUsed[num] = "Y";  //initialize the random number to 'Y' so number cannot be used again
                        System.out.println("Now rate " + movieList[num] + ": ");
                        boolean badRating = true;
                        int rateNum;
                        //make sure user's input rating is valid
                        do
                        {
                            rateNum = Integer.parseInt(inner.nextLine());
                            for (int check=-5; check<6; check++)
                            {
                                if (rateNum == check)
                                {
                                    badRating = false;  //if rating is anywhere between -5 and 5, set boolean variable to prevent loop
                                }
                            }
                            if (badRating == true)  //if rating is invalid, print message and loop
                            {
                                System.out.println("That rating is invalid. Please input a rating from -5 to 5.");
                            }
                        } while (badRating == true);
                        students.get(students.size()-1).editRating(num, rateNum);   //edit ratings of student depending on user's input
                    }
                } while (numRate != 25);    //do this for 25 movies
            }
            else if (newUser == 2)  //OLD USER
            {
                System.out.println("Please enter your existing username.");
                name = inner.nextLine();
                boolean exist=false;
                //check if existing user is actually an existing user
                for (int checkExist=0; checkExist<students.size(); checkExist++)
                {
                    if ((students.get(checkExist).getName()).equals(name))
                    {
                        exist = true;
                    }
                }
                //admin has a whole seperate set of instructions
                if (name.equals("Min") || name.equals("Jay") || name.equals("Bethany"))
                {
                    System.out.println("You have logged on as an administator. Please enter your password:");
                    String tempPass = inner.nextLine();
                    if (password.equals(tempPass) == false) //if password is incorrent, shut down program
                    {
                        System.out.println("CODE RED. FALSE ADMINISTRATOR. Dialing 911...");
                        System.exit(0);
                    }
                    else
                    {
                        administrators.main(movieList, numMovies);  //if a real adminstrator, call separate class to carry out rest of program
                    }
                }
                //if existing user is not an existing user, print message and set name to 'unknown' in order to loop
                if (exist == false)
                {
                    name = "unknown";
                    System.out.println("User could not be found. Please try again.");
                }              
            }
            else    //if response is invalid, print message and loop
            {
                System.out.println("Please enter a valid response: 1 or 2");
            }
        } while (name.equals("unknown"));

        //find position of student in student object array list
        int arrayPos = -1;
        for (int findNum=0; findNum< students.size(); findNum++)
        {
            if (students.get(findNum).getName().equals(name))
            {
                arrayPos = findNum;
            }
        }

        System.out.println("Thank-you " + name + ", the Superb Suggester will now begin!");
        //ask if user is ready; do not begin program if user is not ready
        do {
            System.out.println("Are you ready?(Y/N)");
            ready = (inner.nextLine()).toUpperCase();
            if (ready.equals("N"))
            {
                System.out.println("When you are ready, please enter 'Y'!");
            }
            else if (ready.equals("N") == false && ready.equals("Y") == false)
            {
                System.out.println("Can you read English?");
            }
        } while (ready.equals("Y") == false);

        boolean done = false;
        //continually get desired actions from user until user logs out (until done equals true)
        do{
            getAction();    //call method to output instructions
            String action = inner.nextLine();
            if (action.equals("1"))  //if user wants to view ratings...
            {
                System.out.println("Here are all the movies and your ratings:");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                //display entire list of movies and user's ratings
                for (int display=0; display<numMovies; display++)
                {
                    System.out.println(movieList[display] + " " + students.get(arrayPos).getRatings(display));
                }
            }
            else if (action.equals("2")) //if user wants to edit ratings...
            {
                //display all movies for user to view
                System.out.println("Here are all the movies in the database:");
                for (int display=0; display<numMovies; display++)
                {
                    System.out.println(movieList[display]);
                }
                System.out.println("Type the movie that you wish to edit the rating of:");
                System.out.println("(You must include '_' in replace of spaces)");
                int posMov;
                boolean found;
                String mov;
                //loop until user inputs a valid movie they want to edit their rating of
                do
                {
                    mov = inner.nextLine();
                    found = false;
                    posMov = -1;
                    for (int searchMov=0; searchMov<numMovies; searchMov++)
                    {
                        if (movieList[searchMov].equals(mov))   //search through movie list until match is found
                        {
                            posMov = searchMov;
                            found = true;
                        }
                    }
                    if (found == false) //if no match is found between desired movie and movie in the movie list, print message and loop
                    {
                        System.out.println("I'm sorry, that movie does not exist in our database. Please try again.");
                    }
                } while (found == false);
                //output user's current rating
                System.out.println("Your current rating is: " + students.get(arrayPos).getRatings(posMov));
                System.out.println("What would you like to change it to?");
                int newRate = Integer.parseInt(inner.nextLine());       //get new rating
                students.get(arrayPos).editRating(posMov, newRate);     //change rating by calling method from Students Class
                System.out.println("Your rating for " + mov + " has been successfully changed to " + newRate + ".");
            }
            else if (action.equals("3"))      //if user wants to get suggestions of movies to watch...
            {
                //declare arrays to store scores
                int[] simScore = new int[students.size()];
                double[] popScore = new double[numMovies];

                //similarity scores
                for (int stu=0; stu<students.size(); stu++)
                {
                    for (int movs=0; movs<numMovies; movs++)
                    {
                        //multiply user's ratings with other all the other user's ratings to retrieve similarity score for each student
                        simScore[stu] += (students.get(arrayPos).getRatings(movs)) * (students.get(stu).getRatings(movs));
                    }
                }
                //set own similarity score to 0 so that own ratings cannot be used
                simScore[arrayPos] = 0;

                //output options for user (what their suggestions are based on)
                System.out.println("Get suggestions based on:");
                System.out.println("1. Most Popular (type 1)");
                System.out.println("2. Own ratings (type 2)");
                System.out.println("3. Combination of the above (type 3)");
                String ans = inner.nextLine();
                System.out.println("How many movies would you like to be recommended?");
                int numMov = Integer.parseInt(inner.nextLine());    //get number of movies user wants to be recommended
                if (ans.equals("1"))      //if suggestions are based on popularity (highest ratings)...
                {
                    for (int searchMov=0; searchMov<numMovies; searchMov++)
                    {
                        for (int searchStu=0; searchStu<students.size(); searchStu++)
                        {
                            //add ratings on the same indices together to acquire a total rating for each movie
                            popScore[searchMov] += students.get(searchStu).getRatings(searchMov);
                        }
                    }
                    //average the total ratings by dividing it by the number of students
                    for (int avg=0; avg<numMovies; avg++)
                    {
                        popScore[avg] = popScore[avg] / students.size();
                    }
                    //arrange the array from highest to lowest average rating
                    int[] popArranged = new int[numMovies];
                    for (int order=0; order<numMovies; order++)
                    {
                        double high = popScore[0];      //initialize the first highest rating for comparison
                        int popHigh = 0;
                        for (int findHighest=0; findHighest<numMovies; findHighest++)
                        {
                            if (high < popScore[findHighest])   //if rating is higher than the last,
                            {
                                high = popScore[findHighest];   //set it to highest
                                popHigh = findHighest;          //grab the index
                            }
                        }
                        popArranged[order] = popHigh;   //set first element of array to position of highest rating
                        popScore[popHigh] = -100;       //set rating to low number so that it can't be used again
                    }

                    System.out.println("Here are popular movies you have not yet watched:");
                    int count = 0;
                    for (int display=0; display<numMovies; display++)
                    {
                        //only print the movies in the array that user has not watched yet (their rating is equal to 0)
                        if (students.get(arrayPos).getRatings(popArranged[display]) == 0)
                        {
                            System.out.println(movieList[popArranged[display]]);
                            count += 1; //count number of movies being printed
                        }
                        if (count == numMov)
                        {
                            break;      //once number of movies printed has reached desired amount, break out of loop
                        }
                    }
                }
                else if (ans.equals("2"))     //if suggestions are based solely on movies that user enjoys...
                {
                    int high = simScore[0];
                    int stuHigh = 0;
                    //find the student with the highest similarity score to user
                    for (int findHighest=0; findHighest<students.size(); findHighest++)
                    {
                        if (high<simScore[findHighest])
                        {
                            high = simScore[findHighest];
                            stuHigh = findHighest;
                        }
                    }
                    //grab their ratings and store them into a separate array
                    int[] stuRatings = new int[numMovies];
                    for (int getRate=0; getRate<numMovies; getRate++)
                    {
                        stuRatings[getRate] = students.get(stuHigh).getRatings(getRate);
                    }
                    //arrange ratings of similar student from highest to lowest
                    int[] stuArranged = new int[numMovies];
                    for (int order=0; order<numMovies; order++)
                    {
                        int highest = stuRatings[0];    //initialize first rating for comparison
                        int stuRateHigh = 0;
                        for (int findHighest=0; findHighest<numMovies; findHighest++)
                        {
                            if (highest < stuRatings[findHighest])  //if rating is higher than the last...
                            {
                                highest = stuRatings[findHighest];  //set it to highest
                                stuRateHigh = findHighest;          //grab the index
                            }
                        }
                        stuArranged[order] = stuRateHigh;   //set first element of array to position of highest rating
                        stuRatings[stuRateHigh] = -100;     //set rating to low number so that it can't be used again
                    }
                    System.out.println("Here are movies you may enjoy:");
                    int count = 0;
                    for (int display=0; display<numMovies; display++)
                    {
                        //only print the movies in the array that user has not watched yet (their rating is equal to 0)
                        if (students.get(arrayPos).getRatings(stuArranged[display]) == 0)
                        {
                            System.out.println(movieList[stuArranged[display]]);
                            count += 1;     //count number of movies being printed
                        }
                        if (count == numMov)
                        {
                            break;  //once number of movies printed has reached desired amount, break out of loop
                        }
                    }
                }
                else if (ans.equals("3"))   //if suggestions are based on a combination of popular movies and movies that user will enjoy...
                {
                    //calculate an ultimate score in a 2D array: rows represent students, columns represent movies
                    int[][] ultimateScore = new int[students.size()][numMovies];
                    for (int s=0; s<students.size(); s++)
                    {
                        for (int m=0; m<numMovies; m++)
                        {
                            ultimateScore[s][m] = students.get(s).getRatings(m) * simScore[s];  // multiply ratings by similarity score for each element
                        }
                    }
                    //exclude sim score to self
                    for (int delMov=0; delMov<numMovies; delMov++)
                    {
                        ultimateScore[arrayPos][delMov] = 0;
                    }
                    //calculate final score by adding up columns so that each movie has its own rating based on popularity and similarity
                    int[] finalScore = new int[numMovies];
                    for (int m=0; m<numMovies; m++)
                    {
                        for (int s=0; s<students.size(); s++)
                        {
                            finalScore[m] += ultimateScore[s][m];
                        }
                    }
                    //arrange final movie scores in an array from highest to lowest
                    int[] movArranged = new int[numMovies];
                    for (int order=0; order<numMovies; order++)
                    {
                        int high = finalScore[0];       //initialize first rating for comparison
                        int movHigh = 0;
                        for (int findHighest=0; findHighest<numMovies; findHighest++)
                        {
                            if (high < finalScore[findHighest])     //if rating is higher than the last...
                            {
                                high = finalScore[findHighest];     //set it to highest
                                movHigh = findHighest;          //grab the index
                            }
                        }
                        movArranged[order] = movHigh;   //set first element of array to position of highest rating
                        finalScore[movHigh] = -100;     //set rating to low number so that it can't be used again
                    }
                    System.out.println("Here are popular movies you may enjoy:");
                    int count = 0;
                    for (int display=0; display<numMovies; display++)
                    {
                        if (students.get(arrayPos).getRatings(movArranged[display]) == 0)
                        {
                            //only print the movies in the array that user has not watched yet (their rating is equal to 0)
                            System.out.println(movieList[movArranged[display]]);
                            count += 1; //count number of movies being printed
                        }
                        if (count == numMov)
                        {
                            break;  //once number of movies printed has reached desired amount, break out of loop
                        }
                    }
                }
                else    //catch invalid input
                {
                    System.out.println("That is not an option.");
                }
            }
            else if (action.equals("4"))   //if user wants to search for a movie...
            {
                System.out.println("Type the name of the movie you would like to search:");
                System.out.println("(Don't forget to include '_' in replacement of spaces)");
                String tempMov = inner.nextLine();
                boolean findMov = false;
                //if inputted movie name equals a movie in the movie list, it exists!
                for (int searchMov=0; searchMov<numMovies; searchMov++)
                {
                    if ((movieList[searchMov].toUpperCase()).equals(tempMov.toUpperCase())) //compare in all caps so that case doesn't matter
                    {
                        findMov = true;
                    }
                }
                if (findMov == true)    //if movie is found, it exists in database
                {
                    System.out.println("Movie exists in database.");
                }
                else    //if movie is not found, it does not exist
                {
                    System.out.println("Movie could not be found. Would you like to add it to the database? (yes/no)"); //request addition to database
                    String ansAdd = inner.nextLine();
                    //add movie to separate text file if user wants to request addition of movie
                    if (ansAdd.equals("yes"))
                    {
                        String countLines;
                        ArrayList<String> movieLines = new ArrayList<String>();
                        int nLines =0;
                        try
                        {
                            in = new FileReader("Suggestions.txt");
                            readFile = new BufferedReader(in);
                            while ((countLines = readFile.readLine()) != null)
                            {
                                movieLines.add(countLines);  //create array list of movies already in text file
                                nLines += 1;                //count number of lines pre-existing in text file
                            }      
                            readFile.close();
                            in.close();
                        } catch (FileNotFoundException e)   //catch any errors
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
                            if (nLines>0)   //if there is more than one line...
                            {
                                writeFile.write(movieLines.get(0));   //write the first movie from array list
                                for (int a=0; a<movieLines.size()-1; a++)   //continue printing movies from array list on new lines
                                {
                                    writeFile.newLine();
                                    writeFile.write(movieLines.get(a+1));
                                }
                                writeFile.newLine();
                            }                          
                            writeFile.write(tempMov);   //print the newly added movie to text file
                            writeFile.close();  
                            out.close();
                        } catch (IOException e)     //catch any errors
                        {
                            System.out.println("Problem writing to file.");
                            System.err.println("IOException: " + e.getMessage());
                        }
                        System.out.println(tempMov + " is waiting to be approved by an administrator.");
                    }
                }
            }
            else if (action.equals("5"))  //if user wants to log out, print message and exit loop
            {
                //re-write Parent text file to save all usernames and ratings
                try 
                {
                    out = new FileWriter("Parent.txt");
                    writeFile = new BufferedWriter(out);
                    for (int i=0; i<students.size(); i++)
                    {
                        for (int a=0; a<numMovies; a++)
                        {
                            writeFile.write(students.get(i).getName());
                            writeFile.write(" ");
                            writeFile.write(movieList[a]);
                            writeFile.write(" ");
                            writeFile.write(String.valueOf(students.get(i).getRatings(a)));
                            writeFile.newLine();
                        }                                               
                    }
                    writeFile.close();
                    out.close();
                } catch (FileNotFoundException e)  //catch any errors
                {
                    System.out.println("File does not exist or could not be found.");
                    System.err.println("FileNotFoundException: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Problem writing to file");
                    System.err.println("IOException: " + e.getMessage());
                }
                System.out.println("Thank-you for using the Superb Suggestor!");
                done = true;
            }
            else    //if user inputs an invalid answer, print message and loop
            {
                System.out.println("You must type an appropriate action. Please try again.");
            }
        } while (done == false);
    }

    //method returns an array of all the movies in the database
    public static String[] getMovies()
    {
        File textFile = new File("Movies.txt");
        FileReader in;
        BufferedReader readFile;
        String lineOfText;
        int position = 0;

        int numLines = countMovies();   //call method to count number of lines in text file (which equals number of movies)

        String[] movie = new String[numLines];   
        try
        {
            in = new FileReader(textFile);
            readFile = new BufferedReader(in);
            while ((lineOfText = readFile.readLine()) != null)
            {
                movie[position] = lineOfText;   //add each line to index of the array
                position += 1;              //increment counter by 1
            }
            readFile.close();
            in.close();
        } catch (FileNotFoundException e) { //catch any errors
            System.out.println("File does not exist or could not be found.");
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
        }
        return movie;   //return the String array of movies
    }

    //method returns the number of movies in text file
    public static int countMovies()
    {
        File textFile = new File("Movies.txt");
        FileReader in;
        BufferedReader readFile;

        String countLines;
        int numLines = 0;

        try
        {
            in = new FileReader("Movies.txt");
            readFile = new BufferedReader(in);
            while ((countLines = readFile.readLine()) != null)
            {
                numLines = numLines + 1;   //increment line counter by 1
            }      
            readFile.close();
            in.close();
        } catch (FileNotFoundException e)  //catch any errors
        {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e)
        {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
        }
        return numLines;    //return the number of lines/ the number of movies in text file
    }

    //method outputs instructions for user
    public static void getAction()
    {
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("Which of the following would you like to do?");
        System.out.println("  1. View your ratings (type 1)");
        System.out.println("  2. Edit your ratings (type 2)");
        System.out.println("  3. Get suggestions (type 3)");
        System.out.println("  4. Search the database for a movie (type 4)");
        System.out.println("  5. Log out (type 5)");
    }

    //method generates random numbers depending on number of movies in database
    public static int randomNum(int num)
    {
        Random numGen = new Random();
        return numGen.nextInt(num);
    }
}
