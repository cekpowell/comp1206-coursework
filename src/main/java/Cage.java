import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Represents a Cage within the Mathdoku game.
 */
public class Cage{
    private Grid grid; // the grid instance this cage belongs to
    private ArrayList<Cell> cells; // represents the cells associated with this cage.
    private int targetNumber; // the target number for this cage
    private char targetOperator; // the target operator for this cage
    private String targetString; // the target 
    private static final int defaultInset = 3;
    private static final String mistakeColor =  "-fx-background-color:black, rgb(255, 144, 125);";
    private static final char[] operators = new char[] {'x' , '÷', '+', '-'}; // the operators for the cage

    /**
     * Constructor for the class.
     */
    public Cage(){
    }

    /**
     * Constructor for the class. Allows for the propereties of the cage to be set at 
     * instantiation.
     * @param grid The grid object associated with this cage.
     * @param cells The cells contained within this cage.
     * @param targetNumber The target numbere for this cage.
     * @param targetOperator The target operator for this cage.
     */
    public Cage(Grid grid, ArrayList<Cell> cells, int targetNumber, char targetOperator){
        // initialising the member variables
        this.grid =  grid;
        this.cells = cells;
        this.targetNumber = targetNumber;
        this.targetOperator = targetOperator;
        this.targetString = String.valueOf(targetNumber) + targetOperator;
        // setting the target label for this string
        this.setCageLabel(targetString);

        // setting the cage for the cells in the cage
        for(Cell cell : this.cells){
            cell.setCage(this);
        }
    }

    /**
     * Setter method for the grid for this cage.
     * @param grid The Grid object that this cage belongs to.
     */
    public void setGrid(Grid grid){
        this.grid = grid;
    }

    /**
     * Setter method for the cells within this cage.
     * @param cells The cells within this cage.
     */
    public void setCells(ArrayList<Cell> cells){
        this.cells = cells;
        this.orderCells();

        // setting the cage for the cells in the cage
        for(Cell cell : this.cells){
            cell.setCage(this);
        }
    }

    /**
     * Adds a cell to this cage.
     * @param cell The cell to be added to this cage.
     */
    public void addCell(Cell cell){
        this.cells.add(cell);
        this.orderCells();

        // setting the cage for this cell
        cell.setCage(this);
    }

    /**
     * Getter method for the cells within this cage.
     * @return Arraylist of cells within this cage.
     */
    public ArrayList<Cell> getCells(){
        return this.cells;
    }

    /**
     * Setter method for the target for this cage.
     * @param target The target for this cage as a String.
     */
    public void setTargetString(String targetString){
        this.targetString = targetString;
        this.setCageLabel(targetString);
    }

    /**
     * Setter method for the target number for the cage.
     * @param targetNumber The target number of this cage as an int.
     */
    public void setTargetNumber(int targetNumber){
        this.targetNumber = targetNumber;
    }

    /**
     * Getter method for the target number for this cage
     * @return The target number for the cell as an int
     */
    public int getTargetNumber(){
        return this.targetNumber;
    }

    /**
     * Setter method for the target operator of the cage
     * @param targetOperator The target operator of the cage as a char.
     */
    public void setTargetOperator(char targetOperator){
        this.targetOperator = targetOperator;
    }

    /**
     * Getter method for the target operator for the cage
     * @return The target operator for the cage as a char.
     */
    public char getTargetOperator(){
        return this.targetOperator;
    }

    /**
     * Orders the cells within the cells arraylist for this cage so that they are 
     * stored within the array list in order of their positions.
     */
    private void orderCells(){
        ArrayList<Cell> orderedCells = new ArrayList<Cell>(); // array list for the ordered cells
        ArrayList<Integer> cellPositions = new ArrayList<Integer>(); // array list for their positons

        // adding the cell positions to the arraylist
        for(Cell cell : this.cells){
            cellPositions.add(cell.getPosition());
        }
        // ordering these positions
        Collections.sort(cellPositions);

        // iterating back over cell positions
        for(Integer cellPosition : cellPositions){
            for(Cell cell : this.cells){
                // adding the cell to the ordered list based on their positions
                if(cell.getPosition() == cellPosition){
                    orderedCells.add(cell);
                }
            }
        }

        // restting the cells arraylist using the ordered cells
        this.cells = orderedCells;
    }

    /**
     * Places the target in the upper left most cell of the cage.
     * @param target The target string for this cage.
     */
    private void setCageLabel(String target){
        /**
         * The cell which is the most upper left is the cell
         * with the smallest position value. As such, by iterating
         * through the cells in this cage and finding the one with the 
         * lowest value, the upper-left most cell can be found.
         *  */ 
        Cell upperLeftCell = this.cells.get(0);
        for(Cell cell : this.cells){
            if(cell.getPosition() < upperLeftCell.getPosition()){
                upperLeftCell = cell;
            }
        }

        // setting the target label for this cell
        if(this.cells.size() != 1){
            upperLeftCell.setTargetLabel(target);
        }
        else{
            upperLeftCell.setTargetLabel(String.valueOf(this.targetNumber));
        }
    }

    /**
     * Getter method for the number of cells within this cage. Used to validate
     * the structure of a grid.
     * @return The number of cells within this cage as an int.
     */
    public int getNumberOfCells(){
        return this.cells.size();
    }

    /**
     * Tests if the given value can be inserted into the cage without
     * breaking the rules of the cage.
     * @param value The value to be tested within the cage.
     * @return True if the value can be inserted, false if not.
     */
    public boolean isPossible(int targetValue){
        // apply the opeerator between this value and all others
        // for multiply and add, check if it is above target
        // for subtract and divide, check if it is below target

        // getting current list of values within the cage
        ArrayList<Integer> values  = new ArrayList<Integer>();
        for(Cell cell : this.cells){
            if(cell.getPossibleValue() != 0){
                values.add(cell.getPossibleValue());
            }
        }
        values.add(targetValue);
        // sorting these values into descending order
        Collections.sort(values, Collections.reverseOrder());

        // finding the current result for the cage
        int result = values.get(0);
        for(int nextNumber = 1; nextNumber < values.size(); nextNumber ++){
            // checking for multiply
            if(this.targetOperator == 'x' || this.targetOperator == '*'){
                result = result * values.get(nextNumber);
            }
            // checking for addition
            if(this.targetOperator == '+'){
                result = result + values.get(nextNumber);
            }
            // checking for subtraction
            if(this.targetOperator == '-'){
                result = result - values.get(nextNumber);
            }
            // checking for divide
            if(this.targetOperator == '÷' || this.targetOperator == '/'){
                result = result / values.get(nextNumber);
            }
        }

        // checking if this result is still allowed
    
        // with multiply and addition, can check is allowed before all values are entered
        if(this.cells.size() != values.size()){
            // checking for multiply
            if(this.targetOperator == 'x' || this.targetOperator == '*'){
                if(result > this.targetNumber){
                    return false;
                }
            }
            // checking for addition
            if(this.targetOperator == '+'){
                if(result > this.targetNumber){
                    return false;
                }
            }
        }

        // check for if cage is full (result must be target)
        else{
            if(result != targetNumber){
                return false;
            }
        }

        return true; // returning true if there was no breach
    }

    /**
     * Method to check if the cage is complete according to the rules of mathdoku.
     * @param boolean decides if any mistakes are highlighted to the user or not.
     * @return True if the cage is complete, false if not.
     */
    public boolean testIfCorrect(boolean showMistake){
        /**
         * CHECKING IF CAGE IS COMPLETE
         *      - check if cage is full
         *          - if it is, check if it is right
         *          - if it isnt right, check if need to show mistakes
         *          - if not showing mistakes, return false
         */

        // checking if full
        if(!isFull(this.cells)){
            // this cage is not full, returning false
            return false;

        }
        else{
            // this cage is full, checking if target is met
            if(this.targetMet()){
                // target is met, returning true
                return true;
            }
            else{
                // if target not met, showing mistake if need to
                if(showMistake){
                    for(Cell cell : this.cells){
                        cell.makeMistakeStyle(mistakeColor);
                    }
                }
                // returning false as cage is not correct
                return false;
            }
        }
    }

    /**
     * Given an array list of cells representing a row, column or whole grid 
     * check if all cells are full. That is, if there is a value within
     * each one of the given cells.
     * @param cells The array list of cells to be checked
     * @return True if the column or row is full, false if not.
     */
    private static boolean isFull(ArrayList<Cell> cells){
        // testing to see if any of the cells dont have a value
        for(Cell cell : cells){
            if(cell.getValue().equals("")){
                return false; // returning false if cell found with no value
            }
        }
        // if no cell found with no value, then the row/column/grid must be full
        return true;
    }

    /**
     * Method will check if the cage is correct. That is, if the values combine 
     * with the operator to make the target.
     * @return True if the cage is correct, false if it is not.
     */
    private boolean targetMet(){
        // adding all of the values within the cage to an arraylist
        ArrayList<Integer> values = new ArrayList<>();
        for(Cell cell : this.cells){
            values.add(Integer.parseInt(cell.getValue()));
        }

        // sorting these values into descending order
        Collections.sort(values, Collections.reverseOrder());

        // iterating through the values and applying operator
        int result = values.get(0);
        for(int nextNumber = 1; nextNumber < values.size(); nextNumber ++){
            // checking for multiply
            if(this.targetOperator == 'x' || this.targetOperator == '*'){
                result = result * values.get(nextNumber);
            }
            // checking for addition
            if(this.targetOperator == '+'){
                result = result + values.get(nextNumber);
            }
            // checking for subtraction
            if(this.targetOperator == '-'){
                result = result - values.get(nextNumber);
            }
            // checking for divide
            if(this.targetOperator == '÷' || this.targetOperator == '/'){
                result = result / values.get(nextNumber);
            }
        }

        // checking if result is correct
        if(result==this.targetNumber){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the cage is an allowed cage. An allowed cage is one where the cells
     * contained within the cage are adjacent.
     * @return True if it is, false if it isnt.
     */
    public boolean validateStructure(){
        // cage structure must be fine if only contains one cell
        if(this.cells.size() ==  1){
            return true;
        }
        else{
            /**
             * for each cell, we try to find another cell which is adjacent.
             *  If one is found, the bool is set to true. If at the end of the loop,
             * one is not found, then the bool is still false, and we return false, as these
             * cells are not adjacent.
             */ 
            for(Cell cell : this.cells){
                boolean foundAdjacent = false;

                // second for loop - cell to compare this cell to
                for(Cell cell2 : this.cells){
                    int difference = cell.getPosition() - cell2.getPosition(); // finding difference between this cell and the next
                    difference = Math.abs(difference); // getting absolute of difference
                    if(difference == 1 || difference == this.grid.getDimension()){
                        foundAdjacent = true;
                    }
                }
                // if cant find an adjacent cell, then the cage does not have a valid structure, so returning false
                if(!foundAdjacent){
                    return false;
                }
            }
        }

        // if at this point, then the structure of the cage must be valid
        return true;
    }

    /**
     * Used to set a random cage target value and operator when making random cages.
     */
    public void setRandomTarget(){
        // removing cage label from cells
        for(Cell cell : this.cells){
            cell.setTargetLabel("");
        }

        /**
         * TO MAKE RANDOM CAGE TARGET: 
         *      - PICK RANDOM OPERATOR
         *      - APPLY OPERATOR BETWEEN ALL VALUES IN DESCENDING ORDER
         *      - CHECK THAT THE RESULT IS POSITIVE AND INTEGER
         *      - RESULT BECOMES TARGET NUMBER
         *      - IF CAGE ONLY HAS ONE CELL, TARGET STRING DOESNT INCLUDE OPERATOR
         *      - SET TARGET LABEL INTO CAGE
         */

        // RANDOM OPERATOR //

        Random random = new Random();
        int randomIndex = random.nextInt(4);
        this.targetOperator = operators[randomIndex];

        // TARGET NUMBER //

        // odering the values of all of the cells within the cage
        ArrayList<Integer> values = new ArrayList<>();
        for(Cell cell : this.cells){
            values.add(cell.getSolutionValue());
        }

        // iterating over values and applying operator
        double result = values.get(0);
        for(int nextNumber = 1; nextNumber < values.size(); nextNumber ++){
            // checking for multiply
            if(this.targetOperator == 'x' || this.targetOperator == '*'){
                result = result * values.get(nextNumber);
            }
            // checking for addition
            if(this.targetOperator == '+'){
                result = result + values.get(nextNumber);
            }
            // checking for subtraction
            if(this.targetOperator == '-'){
                result = result - values.get(nextNumber);
            }
            // checking for divide
            if(this.targetOperator == '÷' || this.targetOperator == '/'){
                result = result / values.get(nextNumber);
            }
        }

        // making sure the result is positive
        if(result <= 0 || (int)result != result){
            this.setRandomTarget();
            return;
        }

        // setting the target into the cage
        this.targetNumber = (int)result;
        // checling if cage has more than one cell
        if(this.cells.size() > 1){
            this.targetString = String.valueOf(this.targetNumber) + this.targetOperator;
        }
        // setting the just the value into the cage otherwise
        else{
            this.targetString = String.valueOf(this.targetNumber);
        }
        this.setCageLabel(this.targetString);
    }

    /**
     * Styles the cage to add bolding around the outside. The method will go through each cell
     * and find out where the adjacent cells in the cage are. If a cell is found that is adjacent
     * to the current one, one of the insets is reduced accordingly based on the location of the 
     * adjacent cell. This means that the outline of the cage will be bold, and the indside will be 
     * thinner.
     */
    public void styleCage(){
        // iterate through cage cells, and find adjacent cells to each cell
        for(Cell cell1 : this.cells){
            int leftInset = defaultInset;
            int rightInset = defaultInset;
            int topInset = defaultInset;
            int bottomInset = defaultInset;

            // second for loop - cell to compare this cell to
            for(Cell cell2 : this.cells){
                int difference = cell2.getPosition() - cell1.getPosition(); // finding difference between this cell and the next

                // finding out the position of the cell relative to this one

                if(difference == 1){
                    // cell1 is to the left of cell 2, ro right inset must be reduced
                    rightInset = 0;
                }
                else if(difference == -1){
                    // cell1 is to the right of cell 2, so left inset must be reduced
                    leftInset = 0;
                }
                else if(difference == this.grid.getDimension()){
                    // cell1 is above cell2, to bottom inset must be reducts
                    bottomInset = 0;
                }
                else if(difference == -this.grid.getDimension()){
                    // cell1 is below cell2, so top inset must be reduced
                    topInset = 0;
                }
            }

            // FORMATTING CELLS ON THE OUTSIDE
            if(cell1.getPosition() <= this.grid.getDimension()){
                // this cell is in top row
                topInset = 6;
            }
            if(cell1.getPosition() > ((this.grid.getDimension() * this.grid.getDimension()) - this.grid.getDimension())){
                // this cell is in bottom row
                bottomInset = 6;
            }
            if(cell1.getPosition() % this.grid.getDimension() == 0){
                // cell on the right
                rightInset = 6;
            }
            if((cell1.getPosition()-1) % this.grid.getDimension() == 0){
                // cell on the left
                leftInset = 6;
            }
            
            // setting the insets for this cell when all of the adjacent cells have been found
            cell1.setInsets(new int[] {topInset,rightInset,bottomInset,leftInset});
        }
    }
}