/*
    Public Class: ChromosomeRecord
    Author: Ryan Peralta
    Last Edit: 6/3/2020
        Created the toString() method.

    This class allows us to store the information for the record returned by our SearchQuery Objects.
    We will print these objects after the query finishes searching.
 */
public class ChromosomeRecord {

    public String chromosomeNumber;
    public long start;
    public long end;
    public double value;

    /*
        Constructor take takes the relevant information from the record in the text document and stores them in this object.
     */
    public ChromosomeRecord(String chromosomeNumber, long start, long end, double value){
        this.chromosomeNumber = chromosomeNumber;
        this.start = start;
        this.end = end;
        this.value = value;
    }

    /*
        Overriding the toString() method so we can print out the record returned by our SearchQuery Objects.
     */
    @Override
    public String toString() {
        return chromosomeNumber + "\t" + start + "\t" +  end + "\t" + value;
    }

}
