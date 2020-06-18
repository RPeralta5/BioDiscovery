import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/*
    Public Class: SearchMultipleChromosomes
    Author: Ryan Peralta
    Last Edit: 6/4/2020
        Implemented the linearSearch() method.

    SearchMultipleChromosomes reads the text file containing the records and searches for records that fall in the target range of the user's query.
    The class contains multiple methods that check to see if we need to continue skipping lines in the file or if we should jump back and start looking at the data more closely.
    This class implements the SearchQuery interface.
 */
public class SearchMultipleChromosomes implements SearchQuery {
    //Start chromosome.
    private String chrStart;
    //End chromosome.
    private String chrEnd;
    private int startRange;
    private int endRange;
    //Value assigned to each chromosome that is used later to see if we need to keep jumping or if we have already passed it/
    private int chrTargetValueStart;
    private int chrTargetValueEnd;
    //Total length of the file in bytes.
    private long totalLength;

    //LinkedList of ChromosomeRecords that will be read after the search completes. LinkedList is used instead of an array since we do no know the amount of records that will be returned from the search.
    private LinkedList<ChromosomeRecord> recordList = new LinkedList<>();


    //Bytes that we will be jumping each time.
    private final int SMALL_JUMP = 1500000; // ~3million total lines in the file
    //By making the amount of lines jumped smaller we run into less issues when overshooting the range.
    //total bytes per each jump
    //The less total lines per jump will make it the less lines needed to read for Linear Search!!!
    // 60 subsections = 50k lines * 50 characters = 2500000
    // 100 subsections =  30k lines * 50 characters = 1500000
    // 120 subsections = 25k lines * 50 characters = 1250000
    // 500 subsections? = 6k lines * 50 characters = 300000


    //RandomAccessFile declaration so we can use it in multiple methods.
    private RandomAccessFile raf = null;
    //This is the first byte int he file
    private final int START_POSITION = 0;


    @Override
    /*
        Splits the query into the relevant information needed to search.
        There is no user input checks here. That should be taken care of in the Menu class.
     */
    public void processQuery(String query) {
        String[] halves = query.split("-");

        String[] startH = halves[0].split(":");
        String[] endH = halves[1].split(":");

        chrStart = startH[0];
        startRange = Integer.parseInt(startH[1]);
        chrEnd = endH[0];
        endRange = Integer.parseInt(endH[1]);

        String chrIndex = chrStart.substring(chrStart.indexOf("r") +1);
        //Converting the chr value into an int so we can compare targetChr and the chr from a specific record later on.
        if(chrIndex.equals("X")){
            chrTargetValueStart = 23;
        }
        else if(chrIndex.equals("Y")){
            chrTargetValueStart = 24;
        }
        else{
            chrTargetValueStart = Integer.parseInt(chrIndex);
        }

        String chrIndex2 = chrEnd.substring(chrEnd.indexOf("r") +1);
        //Converting the chr value into an int so we can compare targetChr and the chr from a specific record later on.
        if(chrIndex2.equals("X")){
            chrTargetValueEnd = 23;
        }
        else if(chrIndex2.equals("Y")){
            chrTargetValueEnd = 24;
        }
        else{
            chrTargetValueEnd = Integer.parseInt(chrIndex2);
        }

    }


    /*
        Method that searchs for the records individually and saves record that fall within the target range.
        Once the search stops seeing records that fall within the target range it will stop iterating.
     */
    public void linearSearch(long lastPosition){
        //Boolean that keeps track if we have found valid records yet.
        boolean foundRange = false;
        //Boolean that will determine if the for loop will continue to iterate.
        //It will be set to false when we stop seeing valid records.
        boolean check = true;

        try {
            //Jumps to the last valid position.
            raf.seek(lastPosition);
            //Clearing the incomplete line and prepares to read the first complete line.
            raf.readLine();

            //While loop iterates over records until we stop seeing valid records(We must first find valid records before we can stop).
            while(check){

                //Reads the record.
                String line = raf.readLine();
                String[] line_split = line.split("\t");

                //Same code used in the processQuery method for assigning a value to the chromosome for each record.
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


                //We need to check if we are on the right chromosome or not.
                //If the recordIndex < Target, we continue onto the next line.
                if(recordIndex < chrTargetValueStart){
                    //keep going to the next line. (no action required)
                }
                //if the chromosome number exceeds the target we have reached the end of that chromosome.
                //We can stop iterating.

                else if( recordIndex > chrTargetValueEnd){
                    //we passed our potential range. stop searching.
                    check = false;
                }
                //We are in the correct chromosome range
                else{
                    //System.out.println("Correct chromosome range");
                    long startRecord = Long.parseLong(line_split[1]);
                    long endRecord = Long.parseLong(line_split[2]);
                    //Start Chromosome
                    if(recordIndex == chrTargetValueStart){

                        //System.out.println(line);
                        //need to check the start value.
                        if((startRecord <= startRange && endRecord >= startRange) || startRecord >= startRange){
                           //System.out.println("Chromosome 2 " + line );
                            // System.out.println("Record Added");
                            foundRange = true;
                            ChromosomeRecord record = new ChromosomeRecord(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                            recordList.add(record);
                        }
                        else{
                            //we have not found the range yet, we must continue searching.
                        }

                    }
                    //Chromosomes in the middle
                    else if( recordIndex > chrTargetValueStart && recordIndex < chrTargetValueEnd){
                        foundRange = true;
                        ChromosomeRecord record = new ChromosomeRecord(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                        recordList.add(record);
                    }
                    //Ending chromosome
                    else{
                        //Passed the range.
                        if(startRecord > endRange){
                            check = false;
                            //we can now stop.
                        }
                        else{
                            //we have not found reached the end of the range.
                            foundRange = true;
                            ChromosomeRecord record = new ChromosomeRecord(line_split[0], startRecord, endRecord, Double.parseDouble(line_split[3]));
                            recordList.add(record);
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
        Method that recursively jumps until we have passed a portion of the target range.
        We then revert to the location before the last jump and pass it over to linearSearch().
     */
    public void search_R(long lastPosition){
        try{
            raf.seek(lastPosition);
            //Reading the rest of the cutoff line after we jump
            raf.readLine();


            //Reading the next full line.
            String line = raf.readLine();
            String[] line_split = line.split("\t");

            //Check to see if we are on the correct chromosome.
            String recordChr = line_split[0];

            //If the record chromosome is not the target chromosome
            if(!chrStart.equals(recordChr)){
                //if not, we need to check whether we need to keep going or if we passed it.
                String chrIndex = line_split[0].substring(line_split[0].indexOf("r")+1);
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

                //Comparing the target value to the value from the current record.
                //If the target is less, we must jump back.
                if(chrTargetValueStart < value){
                    //we must jump back and do linear search since we overshot the target range, it must be in the last subsection somewhere.
                    linearSearch(lastPosition - SMALL_JUMP);
                }
                //the target must be greater than the current value. We must continue jumping.
                else{
                    //continue jumping forward
                    search_R(lastPosition + SMALL_JUMP);
                }
            }

            //We are on the correct chromosome
            else{
                //Now we need to check if we are in range or not
                //clipping the left side
                if(startRange > Long.parseLong(line_split[1]) &&  Long.parseLong(line_split[2]) > startRange){ //dont worry about
                    //need to jump back. We passed the target range so that means it must be somewhere in the last subsection.
                    //Run linear search on the last subsection.
                    linearSearch(lastPosition - SMALL_JUMP);
                }
                //We are somewhere in the range
                if( Long.parseLong(line_split[1]) > startRange){
                    //need to jump forward
                    linearSearch(lastPosition - SMALL_JUMP);
                }

                // Target range is after the current start from the raf.readLine()
                else{
                    //The last call to search_R said that it didnt contain the range, so if we just go back to the last position, and read we should be fine.
                    search_R(lastPosition + SMALL_JUMP);
                }
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /*
        Read the text file and begin searching recursively.
     */
    @Override
    public void readFile() {

        try {
            // Save the file to the instance we declared at the top of the class.
            raf = new RandomAccessFile(FILE_NAME, "r");
            totalLength = raf.length();
            //Begin searching, starting at the first byte of information.
            search_R(START_POSITION);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
        Prints out all records found that were in the target range.
     */
    @Override
    public void printValues() {
        System.out.println("Records returned from query:");
        System.out.println("ChromosomeRecord\tStart\tEnd\tValue");
        long count = 0;
        for(ChromosomeRecord c: recordList){
            System.out.println(c);
            count++;
        }
        System.out.println("Total Records: " + count);
    }
}
