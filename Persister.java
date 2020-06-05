import java.io.*;

public class Persister {

    public void serialiseChromosome(Chromosome c) throws IOException{

        BufferedWriter writer = new BufferedWriter(new FileWriter("Test.txt"));
        writer.write(c.number);
        writer.write(c.start);
        writer.write(c.end);
        //writer.write(c.value);


    }

    public Chromosome deserialiseChromosome() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        reader.readLine(); // this will read the first line
        String line1=null;
        Chromosome ts = new Chromosome();
        while ((line1 = reader.readLine()) != null){ //loop will run from 2nd line
            String[] vals = line1.split(" ");
            if(vals[0] == "chr1"){
                ts.number = 1;
            }
            ts.start = Integer.parseInt(vals[1]);
            ts.end = Integer.parseInt(vals[2]);
            ts.value = Double.parseDouble(vals[3]);

        }
        return ts;
    }

   // public static void main(String[] args) throws  IOException{
       // Chromosome test = new Chromosome(1, 15253, 15278, -0.05000932514667511);
     //   Persister p = new Persister();
      //  Chromosome x = p.deserialiseChromosome();

     //   if(!test.toString().equals(x.toString())){
      //      System.out.println(test);
      //      System.out.println(x);
      //  }
   // }
}
