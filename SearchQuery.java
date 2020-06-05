/*
    Public Interface: SearchQuery
    Author: Ryan Peralta
    Last Edit: 6/3/2020
        Switched printValues from returning a LinkedList to just printing the records.

    This interfaces lays out the main methods both Search Objects will be using.
    Reading the file, processing the query, and printing the ChromosomeRecords object stored in their respective LinkedLists.
 */
public interface SearchQuery {
    //File that we will be reading the records from.
    final String FILE_NAME = "probes.txt";

    //Method that process the query and stores the relevant information.
    public void processQuery(String query);

    //Creates the randomFile object and begins searching.
    public void readFile();

    //Method that prints the values of chromosomes returned from the search.
    public void printValues();
}
