import java.io.*;
public class Demo {
    public static void main(String[] args) {



        try {
            // create a new RandomAccessFile with filename test
            RandomAccessFile raf = new RandomAccessFile("test.txt", "r");

            //This is the first byte of information after the header(Chromosome	Start	End	Value	Array)
            int startPosition = 33;

            // set the file pointer at the first record
            raf.seek(startPosition);



            //Reads the rest of the line -> count number of characters and seek 1 more
            String x = raf.readLine();
            System.out.println("" + x);


            //about 40-50 characters in each line
            //almost 3 million lines
            //22 chromosomes + X and Y == 24
            // 125k * 24 = 3mil
            //Separate into 30 subgroups?
            //keep going until overshot then go back and do linear search?





            //Add the initial offset, then add the number of characters in the rest of the line!
            raf.seek(15 + 17 + 2); //+2 is needed for the \n character!!
            //String x = raf.readLine();



            /*
            Here is how I can read the chromosomes and the endpoints.
             */
            String y = raf.readLine();
            String[] y_split = y.split("\t");



            for(String y_part: y_split){
               // System.out.println("test");
                System.out.println(y_part);
            }
            //System.out.println("" + raf.readLine());



            // set the file pointer at 5 position
           // raf.seek(5);

            // write something in the file
         //   raf.writeUTF("This is an example");

         //   // set the file pointer at 0 position
       //     raf.seek(0);

            // print the string
           // System.out.println("" + raf.readUTF());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

