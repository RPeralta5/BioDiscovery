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
    private LinkedList<Chromosome> recordList;


    //By making the amount of lines jumped smaller we run into less issues when overshooting the range.
    private final int SMALL_JUMP = 1500000; // ~3million total lines in the file
    //total bytes per each jump
    //The less total lines per jump will make it the less lines needed to read for Linear Search!!!
    // 60 subsections = 50k lines * 50 characters = 2500000
    // 100 subsections =  30k lines * 50 characters = 1500000
    // 120 subsections = 25k lines * 50 characters = 1250000



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
    }



    public void linearSearch(long lastPosition){
        boolean foundRange = false;
        boolean check = true;

        try {
            raf.seek(lastPosition);
            while(check){

                String line = raf.readLine();
                String[] line_split = line.split("\t");
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
                    foundRange = true;
                    Chromosome record = new Chromosome(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                    recordList.add(record);
                }
                else if(startRecord >= startRange && endRecord <= endRange){
                    foundRange = true;
                    Chromosome record = new Chromosome(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                    recordList.add(record);
                }
                else if(startRecord < endRange && endRecord >= endRange){
                    foundRange = true;
                    Chromosome record = new Chromosome(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                    recordList.add(record);
                }
                //The record does not fit in the range.
                else {
                    //we passed the target range, we can now stop
                    if(foundRange){
                        check = false;
                    }
                    //we have not found the range yet, we must continue searching.
                    else{

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

                    //we must jump back and do linear search since we overshot the target range, it must be in the last subsection somewhere.
                }
                else if(chromosomeTargetValue > value){
                    //continue jumping forward
                    search_R(lastPosition + SMALL_JUMP);
                }
            }

            //We are on the correct chromosome
            else{
                //Now we need to check if we are in range or not
                //Target range is before the current range from the raf.readLine()
                if(endRange < Long.parseLong(line_split[1])){
                    //need to jump back. We passed the target range so that means it must be somewhere in the last subsection.
                    //Run linear search on the last subsection.
                    linearSearch(lastPosition);

                }
                // Target range is after the current range from the raf.readLine()
                else if( startRange > Long.parseLong(line_split[2])){
                    //need to jump forward
                    search_R(lastPosition + SMALL_JUMP);
                }
                else{
                    //we are somewhere in the range
                    //The last call to search_R said that it didnt contain the range, so if we just go back to the last position, and read we should be fine.
                    linearSearch(lastPosition);
                }
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void readFile(String[] queryInfo) {

        try {
            // create a new RandomAccessFile with filename probes.txt
            raf = new RandomAccessFile("test.txt", "r");
            totalLength = raf.length();
            search_R(START_POSITION);

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