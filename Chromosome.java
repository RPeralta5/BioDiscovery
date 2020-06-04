/*
    Public Class: Chromosome
    Author: Ryan Peralta
    Last Edit: 6/2/2020
        Created the instance variables for each chromosome.
 */
public class Chromosome {

    public int number;
    //Ints works!
    public int start;
    public int end;

    public double value;


    public Chromosome(int number, int start, int end, double value){
        this.number = number;
        this.start = start;
        this.end = end;
        this.value = value;
    }
    public Chromosome(){

    }


    @Override
    public String toString() {
        return "Chromosome{" +
                "number=" + number +
                ", start=" + start +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

}
