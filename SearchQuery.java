import java.util.LinkedList;

public interface SearchQuery {

    //Method that searches for the correct values.
    public void processQuery(String query);

    public void readFile(String[] queryInfo);

    //Method that prints the values of chromosomes returned from the search;
    public void printValues();


}
