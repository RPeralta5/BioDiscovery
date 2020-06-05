import java.util.LinkedList;

public interface SearchQuery {

    //Method that searches for the correct values.
    public void processQuery(String query);

    //Creates the randomFile object and begins searching.
    public void readFile();

    //Method that prints the values of chromosomes returned from the search;
    public void printValues();


}
