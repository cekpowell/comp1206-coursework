import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles the reading of a configuration file for the application. The class will
 * set up a grid based on the configuration specified in either a text input dialog,
 * or within a file. The class will stop generating the grid if it encounters an error, 
 * and will log this error within a field.
 */

public class ConfigurationReader{

    private File configurationFile; // this is the name of the file containing the configuration of the school.
    private String configurationText;
    private BufferedReader reader; // handles the reading of the configuration file.
    private Game game; // the game instance this reader will be reading for
    private ArrayList<String> configurationLines; // stores the lines within the file
    private String error; // records the error that occurred whilst reading the file
    private boolean fileCorrectFormat; // represents if the format of the file is correct
    private int gridDimension;
    private static final char[] allowedOperators = {'x', '*', '/', '÷', '+', '-'}; // the allowed operators in the cages
    private Grid grid; // stores the grid that is made when reading the file.

    /**
     * Constructor for the class. Takes in the game instance that this file reader is associated with
     * @param file The File object representing the configuration file.
     */
    public ConfigurationReader(Game game){
        // instantiating the member variables
        this.game = game;
        this.error = "UNKNOWN";
        this.configurationLines = new ArrayList<String>();
        this.fileCorrectFormat = false;
        this.gridDimension = 0;
    }

    /**
     * Setter method for the configuration file field.
     * @param file The file representing the configuration file.
     */
    public void setConfigurationFile(File file){
        this.configurationFile = file;

        // attempting to read the file at the specified location
        try{
            this.reader = new BufferedReader(new FileReader(file)); // reading the file specified in constructor
        }
        catch(FileNotFoundException e){
            // handiling exception - no file found using specified filename.
            System.exit(0); // exiting the program if incorrect file is provided
        }

        // adding all of the lines to an array list (so can iterate multiple times)
        while(this.fileIsReady()){
            String currentLine = this.getLine(); // getting the next line in the file

            this.configurationLines.add(currentLine); // adding this line to the lines array list
        }
    }

    /**
     * Setter method for th configuration text field.
     * @param configurationText The configuration text for the object.
     */
    public void setConfigurationText(String configurationText){
        this.configurationText = configurationText;

        // splitting the configuration text down into the lines
        for(String line : configurationText.split("\n")){
            this.configurationLines.add(line);
        }
    }

    /**
     * Getter method for the error that occured whilst reading the file.
     * @return The recorded error message for the reading of this file.
     */
    public String getError(){
        return this.error;
    }

    /**
     * Getter method for the grid object that was read from the file
     * @return The Grid object that was read from the file.
     */
    public Grid getGrid(){
        return this.grid;
    }

    /**
     * Used to check that the format of the file is correct. The method will try to set 
     * up a grid based on the configuration file/text and will return false as soon as it 
     * encounters a problem within the file. This problem will be stored to the "errors" 
     * field of the class. 
     * @return True if the file was of the correct format, false if it wasnt
     */
    public boolean checkConfigurationFormat(){
        // CHECKING FORMAT OF THE LINES IN THE FILE
        if(!this.checkLinesFormat()){
            return false;
        }
        // CHECKING THE DIMENSION OF THE GRID
        if(!this.checkGridDimension()){
            return false;
        }

        // if lines format okay, and grid dimension found, attempt to make the grid
        this.grid = this.makeGrid();
        
        // CHECKING THE FORMAT OF THE GRID THAT WAS MADE 
        if(!this.grid.validateStructure()){
            this.error = "The structure of the grid was invalid.\n\n" +
                         "Please check that:\n" +
                         "\t- Cells within the same cage are adjacent\n" +
                         "\t- All cells are included in the configuration\n" +
                         "\t- Each cell is only part of one cage";
            return false;
        }

        // if the structure has been validated, then formatting the grid so that cages appear bold
        this.grid.styleGrid();

        // if not yet broken out of the method, then the file must be of the correct format
        return true;
    }

    /**
     * Iterates over all of the lines within the file and checks their format. 
     * If a line is of the correct format. This involves checking that each line
     * has an allowed operator, a positive and integer target, and that each cell
     * number is positive integer.
     * @return True if all lines are of the correct format, false otherwise.
     */
    private boolean checkLinesFormat(){
        int lineNumber = 1; // records the current line for error report

        // iterating over each line
        for(String line : this.configurationLines){
            try{
                // testing the target for the line
                String target = line.split(" ")[0];

                // testing if target contains operator
                if(!(target.charAt(target.length() - 1) == 'x' || 
                    target.charAt(target.length() - 1) == '*' || 
                    target.charAt(target.length() - 1) == '/' || 
                    target.charAt(target.length() - 1) == '÷' || 
                    target.charAt(target.length() - 1) == '+' || 
                    target.charAt(target.length() - 1) == '-')){

                    // no operator provided, checking if only one cell
                    // testing the cells for the line
                    int cellCount = 0;
                    String[] cells = line.split(" ")[1].split(","); // getting the cells
                    for(String cell : cells){
                        // trying to cast this number to an int
                        int cellNumber = Integer.parseInt(cell);
                        // testing if the number is positive
                        if(!isPositive(cellNumber)){
                            this.error = "Invalid cell number found for line: " + lineNumber;
                            return false;
                        }
                        cellCount++; // incrementing the cell count
                    }

                    // if cell count = 1, then no operator fine
                    if(cellCount == 1){
                        // continue with rest of checks

                        // testing target number is positive
                        int targetNumber = Integer.parseInt(target.substring(0, target.length()));
                        if(!isPositive(targetNumber)){
                            this.error = "Invalid target number found for line : " + lineNumber + "\n\n" +
                                            "Please make sure the target is a positive integer.";
                            return false;
                        }

                        // testing that no extra spaces were used in the configuration
                        if(line.split(" ").length > 2){
                            this.error = "Invalid format found for line : " + lineNumber + "\n\n" +
                                    "Please only include whitespace inbetween the target and the cells.";
                            return false;
                        }
                    }
                
                    else{
                        this.error = "No/invalid operator specified for line : " + lineNumber;
                        return false;
                    }
                }
                else{
                    char targetOperator = target.charAt(target.length() - 1);
                    int targetNumber = Integer.parseInt(target.substring(0, target.length() -1));
    
                    // testing the target number
                    if(!isPositive(targetNumber)){
                        this.error = "Invalid target number found for line : " + lineNumber + "\n\n" +
                                     "Please make sure the target is a positive integer.";
                        return false;
                    }
                    // testing the target operator
                    if(!isAllowedOperator(targetOperator)){
                        this.error = "Invalid operator found for line : " + lineNumber + "\n\n" +
                        "Allowed operators : 'x', '*', '/', '÷', '+', '-'.";
                        return false;
                    }
    
                    // testing that no extra spaces were used in the configuration
                    if(line.split(" ").length > 2){
                        this.error = "Invalid format found for line : " + lineNumber + "\n\n" +
                                     "Please only include whitespace inbetween the target and the cells.";
                        return false;
                    }
    
                    // testing the cells for the line
                    String[] cells = line.split(" ")[1].split(","); // getting the cells
                    for(String cell : cells){
                        // trying to cast this number to an int
                        int cellNumber = Integer.parseInt(cell);
                        // testing if the number is positive
                        if(!isPositive(cellNumber)){
                            this.error = "Invalid cell number found for line: " + lineNumber;
                            return false;
                        }
                    }
                }
            }
            catch(Exception e){
                // if any exception is thrown, then returning false
                this.error = "Invalid format found for line : " + lineNumber;
                return false;
            }
            lineNumber++;
        }

        // if not returned yet, then the lines must be of a valid structure
        return true;
    }

    /**
     * Given a character, the method will check if this character represents
     * an allowed operator for a cage within the game.
     * @param operator The char to be checked.
     * @return True if the operator is allowed, false if not.
     */
    private static boolean isAllowedOperator(char operator){
        // iterating through the allowed operators array
        for(char allowedOperator : allowedOperators){
            // testing if there is a match with the given operator
            if(operator == allowedOperator){
                return true; // returning true if a match is found
            }
        }
        return false; // returning false if no match was found
    }

    /**
     * Checks if the given number is positive (or equal to 0).
     * @param number : The number to be checked to see if it is positive.
     * @return boolean : 'true' if the number is positive, 'false' if it is not.
     */
    private static boolean isPositive(int number){
        // the number will be positive if it is greater than or equal to 0
        if(number >= 0){
            return true; // returning true if the number is positive
        }
        else{
            return false; // returning false if the number is negative
        }
    }

    /**
     * Checks if the configuration file specifies a dimension for the grid. The dimension
     * will be the square root of the highest cell number found in the file. If this value
     * is an integer, then the grid has a correct dimension. The method tried to find
     * the largest number in the file, and then checks if this number is a square number.
     * @return True if a dimension was found, false if not.
     */
    private boolean checkGridDimension(){
        int highestCellNumber = 0;

        // calculating the highest cell number that was found within the file
        for(String line : this.configurationLines){
            // testing if the highest cell number in this line is greater than the current highest
            int lineHighestCellNumber = this.getHighestCellNumberForLine(line);
            if(lineHighestCellNumber > highestCellNumber){
                highestCellNumber = lineHighestCellNumber;
            }
        }

        // testing if this number is a square number
        if(ConfigurationReader.isSquareNumber(highestCellNumber)){
            // setting the grid dimension and returning true if a dimension was found
            this.gridDimension = (int)Math.sqrt(highestCellNumber);
            return true;
        }
        else{
            // otherwise setting an appropriate error message and returning false
            this.error = "Could not locate dimension for the grid.\n\n" +
                         "Please check the content of the configuration.";
            return false;
        }
    }

    /**
     * Returns the highest cell number found in the line (used in order to find
     * the dimension of the grid). Iterates over all of the cells in a line, and 
     * records which one has the highest value.
     * @return The highest cell number found for this line as an 'int'.
     */
    private int getHighestCellNumberForLine(String line){
        int highestCellNumber = 0;
        // getting the list of cell numbers for the line
        String[] cellNumbers = line.split(" ")[1].split(",");

        // iterating over the cell numbers for this line
        for(String cellNumber : cellNumbers){
            // testing if this number is higher than the current highest
            if(Integer.parseInt(cellNumber) > highestCellNumber){
                highestCellNumber = Integer.parseInt(cellNumber);
            }
        }

        return highestCellNumber; // returning the highest cell number found for this line.
    }

    /**
     * Tests if the given number is a square number. Compares the whole part of the 
     * root of the number to the double representation of the root. If the two are the same,
     * then the number must be a square number.
     * @param number The number to be tested.
     * @return True if the number is a square number, false if not.
     */
    private static boolean isSquareNumber(int number){
        double root = Math.sqrt(number);
        if((int)root == root){
            return true;
        }
        return false;
    }

    /**
     * Reads through the file line by line and makes a grid based on the configuration.
     * Will only make the grid if the file is of the correct format
     * @return The grid object that was made from the file.
     */
    private Grid makeGrid(){

        Grid grid = new Grid(this.game, this.gridDimension); // setting up new grid with the required dimension

        // iterating over the lines in this file
        for(String line : this.configurationLines){
            String target = line.split(" ")[0];
            char targetOperator = '0'; // defualt target operator for one cage cell
            int targetNumber = 0;

            // if cage not one cell, then getting operator and target
            if((target.charAt(target.length() - 1) == 'x' || 
                target.charAt(target.length() - 1) == '*' || 
                target.charAt(target.length() - 1) == '/' || 
                target.charAt(target.length() - 1) == '÷' || 
                target.charAt(target.length() - 1) == '+' || 
                target.charAt(target.length() - 1) == '-')){
                    
                // if its not, getting operator
                targetOperator = target.charAt(target.length() - 1);
                targetNumber = Integer.parseInt(target.substring(0, target.length() -1));
            }
            // else, just getting target
            else{
                targetNumber = Integer.parseInt(target.substring(0, target.length()));
            }

            ArrayList<Cell> cells = new ArrayList<Cell>(); // stores the cells for this cage

            // creating an array list of cell positions for this cage
            String[] cellPositions = line.split(" ")[1].split(","); // getting the cells
            for(String cellPosition : cellPositions){
                // adding the cell at this position to the array list
                cells.add(grid.getCellAtPosition(Integer.parseInt(cellPosition)-1));
            }

            // creating a new cage object with this information and adding it to the grid
            Cage cage = new Cage(grid, cells, targetNumber, targetOperator);
            grid.addCage(cage);
        }

        return grid;
    }

    /**
     * Method for retrieving the next line from a specified file.
     * @return The line retrived if it was found.
     */
    private String getLine(){

        String currentLine; // string var to store the contents of the current line

        // attempting to access the next line within the file
        try{
            // testing if the next line of the file has any content
            if((currentLine = this.reader.readLine()) != null){ 
                return currentLine; //returning this line if it contains data
            }
        }
        catch(IOException e){
            // handiling exception - error reading from the file
        }

        return ""; // returning default value of empty string if no line was found or error occurred
    }

    /**
     * Method will test if the 'reader' object is ready to be read. 
     * @return  Boolean value - true if file is ready to be read, false if it is not.
     */
    public Boolean fileIsReady(){

        // first testing if the reader object is null (i.e if the file was actually read)
        if(this.reader != null){
            // checking  to see if the reader object is ready to be read using '.ready()' method
            try{
                return this.reader.ready(); // .ready() returns true if the file is able to be read, and false if not
            }
            catch(IOException e ){
            }
        }

        // returning false if the reader object is null or an exception was thrown
        return false;
    }
}