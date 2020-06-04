import java.util.LinkedList;

public interface SearchQuery {

    //Method that searches for the correct values.
    public void processQuery(String query);

    public void readFile(String[] queryInfo);

    //Method that returns the values;
    public LinkedList<Chromosome> returnValues();


}
