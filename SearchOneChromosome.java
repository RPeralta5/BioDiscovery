import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Random;

public class SearchOneChromosome implements SearchQuery {
    private int indexColon;
    private String chr;
    private int startRange;
    private int endRange;
    private long totalLength;
    private int chromosomeTargetValue;
    private final int JUMP_CHROMOSOME = 6250000; //(50 characters * 125k lines ) * 24 = 3mill ~ lines in file



    //By making the amount of lines jumped smaller we run into less issues when overshooting the range.
    private final int SMALL_JUMP = 1; //( 50 characters * 50k lines) * 60 ~~ 3 mill lines (total lines in file)
    private RandomAccessFile raf = null;

    //This is the first byte of information after the header(Chromosome	Start	End	Value	Array)
    private final int START_POSITION = 33;


    @Override
    /*
        Splits the query into the relevant info needed to search.
     */
    public void processQuery(String query) {
        indexColon = query.indexOf(":");
        chr = query.substring(0,indexColon);
        String range = query.substring(indexColon+1);
        String[] range_split = range.split("-");
        startRange = Integer.parseInt(range_split[0]);
        endRange = Integer.parseInt(range_split[1]);

        String chrIndex = chr.substring(chr.indexOf("r") +1);

        if(chrIndex.equals("X")){
            chromosomeTargetValue = 23;
        }
        else if(chrIndex.equals("Y")){
            chromosomeTargetValue = 24;
        }
        else{
            chromosomeTargetValue = Integer.parseInt(chrIndex);
        }

       // String[] info = {chr, range_split[0], range_split[1]};

    }


    public void search_R(long lastPosition, long currentJumpSize){
        try{
            raf.seek(lastPosition);
            String line = raf.readLine();
            String[] line_split = line.split("\t");

            //Check to see if we are on the correct chromosome.
            if(line_split[0] != chr){
                //if not, we need to check whether we need to keep going or if we passed it.
                //We are getting the chromosome number or letter.
                String chrIndex = line_split[0].substring(line_split[0].indexOf("r") +1);
                int value = 0;
                if(chrIndex.equals("X")){
                    value = 23;
                }
                else if(chrIndex.equals("Y")){
                    value = 24;
                }
                else{
                    value = Integer.parseInt(chrIndex);
                }


                if(chromosomeTargetValue < value){
                    //we must jump back
                }
                else if(chromosomeTargetValue > value){
                    //continue jumping forward
                }
            }

            //We are on the correct chromosome
            else{
                //Now we need to check if we are in range or not
                //Target range is before the current range from the raf.readLine()
                if(endRange < Long.parseLong(line_split[1])){
                    //need to jump back

                    //If i run into implementation issues just do linear search from here.

                }
                // Target range is after the current range from the raf.readLine()
                else if( startRange > Long.parseLong(line_split[2])){
                    //need to jump forward
                    //check the distance b/w current range and target range, if it is sorta small we can just linear search the rest.
                }
                else{
                    //we are somewhere in the range
                    //1.) Clipping the starting range
                    //2.) Inside the range completley
                    //3.) Clipping the ending range


                    //The last call to search_R said that it didnt contain the range, so if we just go back to the last position, and read we should be fine.
                    //Can run into issues with this if we overshot it.
                    //Make the number of lines jumped ~~ 1/2 the number of lines per each chromosome.


                    // Send to another method that jumps back then just does linear search until it finds one that hits the target, and stops when it moves from true->false
                }

            }


            for(String y_part: y_split){
                // System.out.println("test");
                System.out.println(y_part);
            }
















        }catch (IOException ex) {
            ex.printStackTrace();
        }



        //In case we overshoot the target range
        long lastPosition = 0;


        // set the file pointer at the first record
        raf.seek(START_POSITION);

             /*
            Here is how I can read the chromosomes and the endpoints.
                    */
        // Read the line and split the information on \t
        String record = raf.readLine();
        String[] record_split = record.split("\t");

        //Check to see that we are on the right chromosome.



        for(String y_part: y_split){
            // System.out.println("test");
            System.out.println(y_part);
        }

        //about 40-50 characters in each line
        //almost 3 million lines
        //22 chromosomes + X and Y == 24
        // 125k * 24 = 3mil
        //Separate into 30 subgroups?
        //keep going until overshot then go back and do linear search?





        //Add the initial offset, then add the number of characters in the rest of the line!
        raf.seek(15 + 17 + 2); //+2 is needed for the \n character!!
        //String x = raf.readLine();





    }


    @Override
    public void readFile(String[] queryInfo) {

        try {
            // create a new RandomAccessFile with filename probes.txt
            raf = new RandomAccessFile("test.txt", "r");
            totalLength = raf.length();
            search_R(START_POSITION,JUMP_CHROMOSOME );

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public LinkedList<Chromosome> returnValues() {
        return null;
    }
}
