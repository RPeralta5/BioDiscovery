import java.util.Scanner;

/*
    Public Class: Menu
    Author: Ryan Peralta
    Last Edit: 6/4/2020
        Created the searchMultipleC method.

     Menu class that displays the available options to the user.
     Option 1(searchOneC) is for a query spanning 1 chromosome.
     Option 2(searchMultipleC) is for a query spanning multiple chromosomes.
     Option 3 is to exit the program.

 */
public class Menu {
    //Scanner used to receive the choice and query input by the user
    private Scanner s = new Scanner(System.in);

    /*
        Empty Constructor
     */
    public Menu(){

    }

    /*
        Header method that displays the menu to the user.
        This method waits for the user to input their choice and will check that the inputted value is a valid option.
     */
    public void header(){
        System.out.println("      BioDiscovery Inc. Genomic Data Search      ");
        System.out.println("-------------------------------------------------");
        System.out.println("1.) Search Spanning 1 ChromosomeRecord");
        System.out.println("2.) Search Spanning Multiple Chromosomes");
        System.out.println("3.) Exit");

        //default value before the try catch
        int choice = 0;
        try{
            choice = s.nextInt();
        } catch(NumberFormatException e){
            System.err.println("Please input only integers.");
            System.exit(1);
        }

        // If statements that transfer control of the program to the proper method based on the users input.
        if(choice == 1){
            searchOneC();
        }
        else if(choice == 2){
            searchMultipleC();
        }
        else if(choice == 3){
            System.out.println("Goodbye.");
        }
        else {
            System.out.println("Please enter a valid option.\n\n");
            header();
        }
    }

    /*
        This method prompts the user for searching one chromosome.
        It checks to make sure that the inputted query contains a : and a -.
        It has some input validation but it can be sanitized more.
     */
    public void searchOneC(){
        //Clearing the scanner -> removes the /n character from when we read the last int for the user's choice.
        s.nextLine();


        //chr22:20980876-20980878
        //query format
        System.out.println("Please insert the query in this format: chr_:start-end");
        System.out.println("Example: chr22:20980876-20980878");
        String query = s.nextLine();


        //Check that the query contains a semicolon and a dash(-)
        int indexColon = query.indexOf(":");
        int indexDash = query.indexOf("-");
        if(indexColon == -1 || indexDash == -1){
            System.out.println("Please enter the query in the correct format.");
            searchOneC();
            return;
        }




        //If the query has passed the checks, we can pass the query to the SearchOneChromosome object and begin searching for the records in the target range.
        SearchOneChromosome search1 = new SearchOneChromosome();

        //long startTime = System.nanoTime();

        //The SearchOneChromosome object will take care of the rest of the search and will print out the values returned from the search.
        search1.processQuery(query);
        search1.readFile();
        search1.printValues();

        //long endTime   = System.nanoTime();
        //long totalTime = endTime - startTime;
        //System.out.println(totalTime/1000000);




    }




    /*
        This method prompts the user for searching multiple chromosomes.
        It checks to make sure that the inputted query contains two : and a -.
        It has some input validation but it can be sanitized more.
     */
    public void searchMultipleC(){
        //Clearing the scanner -> removes the /n character from when we read the last int for the user's choice.
        s.nextLine();


        //chr3:5000-chr5:8000
        //query format
        System.out.println("Please insert the query in this format: chr_:start-chr_:end");
        System.out.println("Example: chr3:5000-chr5:8000");
        String query = s.nextLine();


        //Check that the query contains two semicolons and a dash(-)
        int indexDash = query.indexOf("-");
        if(indexDash == -1){
            System.out.println("Please enter the query in the correct format.");
            searchMultipleC();
            return;
        }
        String[] halves = query.split("-");
        int indexColon1 = halves[0].indexOf(":");
        int indexColon2 = halves[1].indexOf(":");
        if(indexColon1 == -1 || indexColon2 == -1){
            System.out.println("Please enter the query in the correct format.");
            searchMultipleC();
            return;
        }




        //If the query has passed the checks, we can pass the query to the SearchMultipleChromosomes object and begin searching for the records in the target range.
        SearchMultipleChromosomes search2 = new SearchMultipleChromosomes();

        //long startTime = System.nanoTime();

        //The SearchMultipleChromosomes object will take care of the rest of the search and will print out the values returned from the search.
        search2.processQuery(query);
        search2.readFile();
        search2.printValues();

        //long endTime   = System.nanoTime();
        //long totalTime = endTime - startTime;
        //System.out.println(totalTime/1000000);
    }
}
