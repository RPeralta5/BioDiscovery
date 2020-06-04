/*
    Public Class: Chromosome
    Author: Ryan Peralta
    Last Edit: 6/2/2020
        Created the instance variables for each chromosome.
 */
public class Chromosome {

    public String chromosomeNumber;
    //Ints works!
    public long start;
    public long end;

    public double value;


    public Chromosome(String chromosomeNumber, long start, long end, double value){
        this.chromosomeNumber = chromosomeNumber;
        this.start = start;
        this.end = end;
        this.value = value;
    }
    public Chromosome(){

    }


    @Override
    public String toString() {
        return chromosomeNumber + "\t" + start + "\t" +  end + "\t" + value;
    }

}
