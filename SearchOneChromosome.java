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
    private LinkedList<Chromosome> recordList = new LinkedList<>();
    private final String FILE_NAME = "probes.txt";


    //By making the amount of lines jumped smaller we run into less issues when overshooting the range.
    private final int SMALL_JUMP = 1500000; // ~3million total lines in the file
    //total bytes per each jump
    //The less total lines per jump will make it the less lines needed to read for Linear Search!!!
    // 60 subsections = 50k lines * 50 characters = 2500000
    // 100 subsections =  30k lines * 50 characters = 1500000
    // 120 subsections = 25k lines * 50 characters = 1250000



    private RandomAccessFile raf = null;
    //This is the first byte of information after the header(Chromosome	Start	End	Value	Array)
    private final int START_POSITION = 0;


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

        System.out.println(chr + " " + startRange + " " + endRange);

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
    }



    public void linearSearch(long lastPosition){
        boolean foundRange = false;
        boolean check = true;

        try {
            raf.seek(lastPosition);
            //clearing the noncomplete line.
            raf.readLine();


            while(check){
                //System.out.println("Check loop");




                String line = raf.readLine();
               // System.out.println(line);
                String[] line_split = line.split("\t");


                //System.out.println(chr + " " + startRange + " " + endRange);

                String chrIndexRecord = line_split[0].substring(line_split[0].indexOf("r") +1);
                int recordIndex = 0;

                if(chrIndexRecord.equals("X")){
                    recordIndex = 23;
                }
                else if(chrIndexRecord.equals("Y")){
                    recordIndex = 24;
                }
                else{
                    recordIndex = Integer.parseInt(chrIndexRecord);
                }



                //Need to check whether we are on the right chromosome or not!
                //keep going until we are on the right
                if(recordIndex < chromosomeTargetValue){
                    //keep going

                }
                else if( recordIndex > chromosomeTargetValue){
                    //we passed our potential range. stop searching.
                    check = false;
                }
                //We are on the correct chromosome
                else{
                  //  System.out.println("We are on the right chromosome.(LS) " + line);




                    long startRecord = Long.parseLong(line_split[1]);
                    long endRecord = Long.parseLong(line_split[2]);
                    // if the record doesnt fit in the range, read the next line
                    // if the record doesnt fit and we have already found the range -> check = false;
                    // if the record fits it the range, add it to the linked list of chromosomes.

                    /*
                    The record fits in the target range.
                    1.) startRecord <= startRange && endRecord < endRange == clipping the start of the target range
                    2.) startRecord >= startRange && endRecord <= endRange == entire record range is inside the target range
                    3.) startRecord < endRange && endRecord >= endRange == clipping the end of the target range
                     */
                    if(startRecord <= startRange && endRecord < endRange){
                        System.out.println("Record Added");
                        foundRange = true;
                        Chromosome record = new Chromosome(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                        recordList.add(record);
                    }
                    else if(startRecord >= startRange && endRecord <= endRange){
                        System.out.println("Record Added");
                        foundRange = true;
                        Chromosome record = new Chromosome(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                        recordList.add(record);
                    }
                    else if(startRecord < endRange && endRecord >= endRange){
                        System.out.println("Record Added");
                        foundRange = true;
                        Chromosome record = new Chromosome(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                        recordList.add(record);
                    }
                    //The record does not fit in the range.
                    else {
                        //we passed the target range, we can now stop
                        if(foundRange){
                            check = false;
                            System.out.println("Loop stopped");
                        }
                        //we have not found the range yet, we must continue searching.
                        else{

                        }
                    }
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



    public void search_R(long lastPosition){
        //Dont need to include jumpsize anymore since I am always jumping a constant number of lines.
        try{
            raf.seek(lastPosition);

            //Reading the rest of the cutoff line after we jump
            raf.readLine();


            //Reading the next full line.
            String line = raf.readLine();
            //System.out.println("Jump!  -> " + line);
           // System.out.println("ReadLine from last position:" + line);
            String[] line_split = line.split("\t");

            //Check to see if we are on the correct chromosome.
            String recordChr = line_split[0];
            System.out.println(recordChr + " = " + chr + " ---> " + chr.equals(recordChr));

            if(!chr.equals(recordChr)){
                //if not, we need to check whether we need to keep going or if we passed it.
                //We are getting the chromosome number or letter.
                //System.out.println(line_split[0]);
                String chrIndex = line_split[0].substring(line_split[0].indexOf("r")+1);
                //System.out.println(chrIndex);


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
                    //we must jump back and do linear search since we overshot the target range, it must be in the last subsection somewhere.
                    System.out.println("Passed chromosome. Go back and linear search." + line);
                    //System.out.print(line);
                    linearSearch(lastPosition - SMALL_JUMP);
                }
                else{
                    //continue jumping forward
                    System.out.println("Keep jumping forward : " + line);
                    //System.out.print(line);

                    search_R(lastPosition + SMALL_JUMP);
                }
            }

            //We are on the correct chromosome
            else{
                System.out.println("Correct CHromosome: " + recordChr + " = " + chr);
                System.out.println(line);
                System.out.println(chr.equals(recordChr));
                //System.out.println(line_split[0]);
                //Now we need to check if we are in range or not
                //Target range is before the current range from the raf.readLine()
                if(endRange < Long.parseLong(line_split[1])){
                    //need to jump back. We passed the target range so that means it must be somewhere in the last subsection.
                    //Run linear search on the last subsection.
                    System.out.println("We passed the target! Go back and Call linear search");
                    linearSearch(lastPosition - SMALL_JUMP);


                }
                // Target range is after the current range from the raf.readLine()
                else if( startRange > Long.parseLong(line_split[2])){
                    //need to jump forward
                    System.out.println("Jump!  -> " + line);
                    search_R(lastPosition + SMALL_JUMP);
                }
                else{
                    //we are somewhere in the range
                    //The last call to search_R said that it didnt contain the range, so if we just go back to the last position, and read we should be fine.
                    System.out.println("We are somewhere in range. Call linear search");
                    linearSearch(lastPosition - SMALL_JUMP);
                }
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void readFile() {

        try {
            // create a new RandomAccessFile with filename probes.txt
            raf = new RandomAccessFile(FILE_NAME, "r");
            totalLength = raf.length();
            //raf.seek(lastPosition);
           // String line = raf.readLine();
            //System.out.println("ReadLine:" + line);
           // String line2 = raf.readLine();
           // System.out.println("ReadLine:" + line2);
            search_R(START_POSITION);

            //works! (5:05pm - 6/4)
          //  raf.seek(START_POSITION);
           // String line3 = raf.readLine();
           // System.out.println("Seek:" + line3);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void printValues() {
        System.out.println("Chromosome\tStart\tEnd\tValue");
        for(Chromosome c: recordList){
            System.out.println(c);
        }
    }
}
