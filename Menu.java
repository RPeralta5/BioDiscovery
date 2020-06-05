import java.util.Scanner;

/*
    Public Class: Chromosome
    Author: Ryan Peralta
    Last Edit: 6/3/2020
        Created the options and header methods.
 */
public class Menu {
    Scanner s = new Scanner(System.in);

    public Menu(){

    }


    public void header(){
        System.out.println("      BioDiscovery Inc. Genomic Data Search      ");
        System.out.println("-------------------------------------------------");
        System.out.println("1.) Search Spanning 1 Chromosome");
        System.out.println("2.) Search Spanning Multiple Chromosomes");
        System.out.println("3.) Exit");

        //default value before try catch
        int choice = 0;
        try{
            choice = s.nextInt();
        } catch(NumberFormatException e){
            System.err.println("Please input only integers.");
            System.exit(1);
        }


        if(choice == 1){
            option1();
        }
        else if(choice == 2){
            option2();
        }
        else if(choice == 3){
            System.out.println("Goodbye.");
        }
        else {
            System.out.println("Please enter a valid option.\n\n");
            header();
        }
    }

    public void option1(){
        //Clearing the scanner
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
            option1();
            return;
        }
        SearchOneChromosome search1 = new SearchOneChromosome();
        search1.processQuery(query);
        search1.readFile();
        search1.printValues();




    }





    public void option2(){

    }
}
