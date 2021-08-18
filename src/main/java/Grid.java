import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.geometry.*;

/**
 * Class represents the actual grid within the application.
 */
public class Grid extends GridPane{

    private int dimension; // the dimension of the grid
    private Game game; // represents the game instance associated with this grid
    private ArrayList<Cell> cells; // stores the cells within the grid in order from top left to bottom right
    private Cell selectedCell; // represents the cell that the user has currently selected
    private ArrayList<Cage> cages; // stores the cages within this grid
    private int numberOfCellsInCages; // represents how many cells have been added to a cage
    private ArrayList<ArrayList<Cell>> rows; // stores the rows of cells within the grid
    private ArrayList<ArrayList<Cell>> columns; // stores the columns of cells within the grid
    private ArrayList<Integer> solutionValues; // stores the solution for all of the cells within the grid (for random grids)
    private ArrayList<ArrayList<Integer>> solutions; // stores the soltutions for this grid
    private ArrayList<Integer> primarySolution; // stores the primary solution for the grid
    private int remainingHintsCount;
    private boolean editable; // determines if the grid can be edited

    // constants
    private static final String rowMistakeColour = "-fx-background-color:black, rgb(245, 191, 105);";
    private static final String columnMistakeColour = "-fx-background-color:black, rgb(193, 153, 242);";
    private static final int numberOfHints = 3;
    private static final int minimumNumberOfCellsForHint = 2;

    /**
     * Constructor for the class. Sets up a grid of the required dimension.
     * @param dimension The dimension of the grid to be made.
     */
    public Grid(Game game, int dimension){
        // setting the member variables for the grid
        this.dimension = dimension;
        this.game = game;
        this.selectedCell = null;
        this.cells = new ArrayList<Cell>();
        this.cages = new ArrayList<Cage>();
        this.rows = new ArrayList<ArrayList<Cell>>();
        this.columns = new ArrayList<ArrayList<Cell>>();
        this.numberOfCellsInCages = 0;
        this.solutions = new ArrayList<ArrayList<Integer>>();
        this.primarySolution = new ArrayList<Integer>();
        this.remainingHintsCount = numberOfHints;
        this.editable = true;

        // making a new grid of the required dimension
        for(int r = 0; r < dimension; r++){
            this.rows.add(new ArrayList<Cell>()); // new array list to store this row of cells

            for(int c = 0; c < dimension; c++){
                // creating a new arraylist for the column if needed
                if(this.columns.size() < this.dimension){
                    this.columns.add(new ArrayList<Cell>()); // new array list to storre this column
                } 

                Cell cell = new Cell(this, this.cells.size() + 1); // making new cell for this position in the grid

                // formatting the size of the cell so that it is square and matches the size of the grid
                cell.prefHeightProperty().bind(this.widthProperty()); // will be fixed proportion of height of grid
                cell.prefWidthProperty().bind(cell.heightProperty()); // will be same as height
                this.add(cell, c, r, 1, 1); // adding this cell object to the grid at the required position
                this.cells.add(cell); // adding this cell to the list of cells for the grid
            
                cell.getStyleClass().add("normal-cell");

                // adding this cell to the row and column list
                this.rows.get(r).add(cell);
                this.columns.get(c).add(cell);
            }
        }

        // formatting the grid
        this.setPadding(new Insets(10,20,20,20));
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Getter method for the game that this grid belongs to.
     * @return The Game object that this grid belongs to.
     */
    public Game getGame(){
        return this.game;
    }

    /**
     * Getter method for the dimension of the grid
     * @return The dimension of the grid.
     */
    public int getDimension(){
        return this.dimension;
    }

    /**
     * Setter method for the editable property of the grid.
     * @param editable Boolean representing if this grid is editable or not.
     */
    public void setEditable(boolean editable){
        this.editable = editable;

        // passing this onto the cells
        for(Cell cell : this.cells){
            cell.setEditable(editable);
        }
    }

    /**
     * Setter method for the solution values within the grid.
     * @param solutionValues The solution values for all of the cells as an
     * ArrayList.
     */
    public void setSolutionValues(ArrayList<Integer> solutionValues){
        this.solutionValues = solutionValues;

        int cellCount = 0;
        for(Integer solutionValue : this.solutionValues){
            this.cells.get(cellCount).setSolutionValue(solutionValue);
            cellCount++;
        }
    }

    /**
     * Getter method for the solution values within the grid.
     * @return The solution values for the grid as an ArrayList of integers.
     */
    public ArrayList<Integer> getSolutionValues(){
        return this.solutionValues;
    }

    /**
     * Adds a given solution to the solutiions for this grid.
     * @param solution The solution to be added to this grid
     * as an array of integers.
     */
    public void addSolution(ArrayList<Integer> solution){
        this.solutions.add(solution);
    }

    /**
     * Setter method for the solutions for this grid.
     * @param solutions The solutions for this grid.
     */
    public void setSolutions(ArrayList<ArrayList<Integer>> solutions){
        this.solutions = solutions;

        // setting the primary solution into the grid and the cells
        // using the first solution
        this.setPrimarySolution(this.solutions.get(0));
    }

    /**
     * Getter method for the solutions within this grid.
     * @return The solutions for this grid.
     */
    public ArrayList<ArrayList<Integer>> getSolutions(){
        return this.solutions;
    }

    public void setPrimarySolution(ArrayList<Integer> primarySolution){
        this.primarySolution = primarySolution;

        // setting primary solution for cells
        for(int index  = 0; index < this.primarySolution.size(); index ++){
            this.cells.get(index).setPrimarySolutionValue(this.primarySolution.get(index));
        }
    }

    /**
     * Getter method for the number of solutions for this grid.
     * @return The number of solutions found for this grid as an int.
     */
    public int getSolutionCount(){
        return this.solutions.size();
    }

    /**
     * Getter method for a Cell at the defined position.
     * @param position The position of the cell to be retrieved.
     * @return The Cell object at this position.
     */
    public Cell getCellAtPosition(int position){
        //TODO: CHECK THAT THE POSITION IS VALID (ARRAY INDEX OUT OF BOUNDS EXCEPTION)
        return this.cells.get(position);
    }

    /**
     * Getter method for the cells contained within this grid.
     * @return The array list of cells within this grid.
     */
    public ArrayList<Cell> getCells(){
        return this.cells;
    }

    /**
     * Getter method for the rows within this grid.
     * @return The rows within this grid.
     */
    public ArrayList<ArrayList<Cell>> getRows(){
        return this.rows;
    }

    /**
     * Getter method for the columns within this grid.
     * @return The column within this grid.
     */
    public ArrayList<ArrayList<Cell>> getColumns(){
        return this.columns;
    }

    /**
     * Given a cell contained within the grid, will return the row of cells
     * that this cell is contained within.
     * @param cell The cell that is being searched for.
     * @return ArrayList of cells representing this cells row of cells.
     */
    public ArrayList<Cell> getRowFromCell(Cell cell){
        // iterating over all of the rows in this grid
        for(ArrayList<Cell> row : this.rows){
            // testing if this row contains the cell
            if(row.contains(cell)){
                return row; // returning row when match is found
            }
        }

        // if no match is found, then returning null
        return null;
    }

    /**
     * Given a cell contained within the grid, will return the column of cells
     * that this cell is contained within.
     * @param cell The cell that is being searched for.
     * @return ArrayList of cells representing this cells column of cells.
     */
    public ArrayList<Cell> getColumnFromCell(Cell cell){
        // iterating over all of the columns in this grid
        for(ArrayList<Cell> column : this.columns){
            // testing if this column contains the cell
            if(column.contains(cell)){
                return column; // returning column when match is found
            }
        }

        // if no match is found, then returning null
        return null;
    }

    /**
     * Getter method for the selected cell withi the grid. Returns null if
     * no cell is currently selected.
     * @return The cell object that is currently selected by the user.
     */
    public Cell getSelectedCell(){
        return this.selectedCell;
    }

    /**
     * Setter method for the selected cell within the grid.
     * @param cell The cell that is to be selected within the grid.
     * @return True if the cell was selected, false if not.
     */
    public void setSelectedCell(Cell cell){
        // returning if not able to edit (used for showing solution)
        if(!this.editable){
            return;
        }

        // changing the currently selected cell back to normal
        this.deselectSelectedCell();

        // then make this new cell the currently selected cell
        this.selectedCell = cell;
        this.selectedCell.makeSelectedStyle();

        // testing if the clear cell button should be enabled or disabled
        boolean cellIsEmpty = cell.getValue().equals("");
        this.game.getControlPanel().getToolbar().setClearCellButtonDisable(cellIsEmpty);
    }

    /**
     * Moves the selected cell based on the movement specified (used to allow for arrow
     * keys to navigate the grid).
     * @param movement The amount the selected cell will be adjusted by.
     */
    public void moveSelectedCell(int movement){
        if(this.selectedCell!=null){
            // attempting to move the selected cell by the given amount.
            try{
                
                // locating the needed cell
                Cell newSelectedCell = this.getCellAtPosition(this.selectedCell.getPosition() + movement - 1);
    
                // if able to locate this cell, then making it selected
                this.deselectSelectedCell();
                this.setSelectedCell(newSelectedCell);
            }
            // exception 1 - array index out of bounds - cant move in the given position
            // exception 2 - null pointer exception - no cell currently selected
            catch(Exception e){
                //TODO: make it play sound when cannot move in that direction
            }
        }
    }

    /**
     * Will deselect the currently selected cell, if one is currently selected
     * @return True if the cell was deselected, flase if it wasnt
     */
    public boolean deselectSelectedCell(){
        // testing if there is currently a cell deslected
        if(this.selectedCell!= null){
            // styling the cell back to the current style(as could still be winning or normal)
            this.selectedCell.makeCurrentStyle();
            this.selectedCell = null;
            this.game.getControlPanel().getToolbar().setClearCellButtonDisable(true);

            return true;
        }
        // returning false if there was no selected cell
        else{
            return false;
        }
    }

    /**
     * Inserts a number into the currently selected cell. Used when key is pressed
     * and when button pressed on numberpad.
     */
    public void enterValueIntoSelectedCell(String value){
        // testing if a cell is currently selected, and if the value is different to the current one
        if(this.selectedCell!=null && !this.selectedCell.getValue().equals(value)){
            // adding this event to the undo stack on the game object
            this.game.addValueEvent(new CellValueEvent(this.selectedCell, 
                                                   this.selectedCell.getValue(), 
                                                   value));

            // placing the given value into the cell
            this.selectedCell.setValue(value);

            // checking if the grid is complete
            if(this.testIfCorrect(false)){
                // displaying the winning animation if the grid is complete
                this.deselectSelectedCell();
                this.game.displayWinningAnimation();
                this.game.setWinningState(true);
            }
            else{
                // if the grid is not complete, making sure the grid is in default style
                this.makeDefualtStyle();
                // this will get rid of selected cell styling, so styling it again
                this.setSelectedCell(this.selectedCell);

                // removing the winning state property from the game
                this.game.setWinningState(false);
            }
        }

        // enabling/disabling clear grid button
        this.game.getControlPanel().getToolbar().setClearGridButtonDisable(this.isEmpty());
    }

    /**
     * Clearing the currently selected cell of the grid. Used when the backspace
     * key is pressed, or when the clear button is pressed for an individual cell.
     * The cell will only be cleared if a cell is selected, and if the cell contains
     * a value.
     */
    public void clearSelectedCell(){
        // testing if a cell is currently selected and that it has a value
        if(this.selectedCell!=null && !this.selectedCell.getValue().equals("")){
            // adding this event to the undo stack on the game object
            this.game.addValueEvent(new CellValueEvent(this.selectedCell, 
                                                   this.selectedCell.getValue(), 
                                                   ""));

            // if a cell is selected, then setting its value to empty string
            this.selectedCell.clear();

            this.makeDefualtStyle(); // restting the styling of the grid if winning animation was shown
            this.setSelectedCell(this.selectedCell); // srtting the currently selected cell back to this cell
            this.game.setWinningState(false);
            // enabling/disabling clear grid button
            this.game.getControlPanel().getToolbar().setClearGridButtonDisable(this.isEmpty());
        }
    }

    /**
     * Iterates over all cells in the grid, and runs the clear value on
     * them, which will set their value to nothing.
     */
    public void clear(){
        // only want to clear if some of the cells have any content
        if(!this.isEmpty()){
            // setting up confirmation dialog to make sure user wants to clear
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Clear Entire grid");
            alert.setHeaderText("You are about to clear the entire grid.");
            alert.setContentText("Are you sure you want to clear the entire grid?\n\n" + 
                                 "This action can be undone with the 'undo' button.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                // adding this event to the stack
                this.game.addValueEvent(new GridValueEvent(this));
                this.makeDefualtStyle(); // styling the grid back to normal in case the winning animation was shown
                this.deselectSelectedCell(); // deselecting current cell
                this.game.setWinningState(false);
                
                // iterate over all cells and wipe them
                for(Cell cell : this.cells){
                    cell.clear();
                }  
                
                // disabling the clear cell and clear grid buttons
                this.game.getControlPanel().getToolbar().setClearCellButtonDisable(true);
                this.game.getControlPanel().getToolbar().setClearGridButtonDisable(true);
            } else {
                // ... user chose CANCEL or closed the dialog
                // no action required
            }
        }
    }

    public boolean isEmpty(){
        // testing if any of the cells have any content
        for(Cell cell : this.cells){
            if(!cell.getValue().equals("")){
                // returning false if cell content is found
                return false;
            }
        }
        // returning true if no cells were found that contain any content
        return true;
    }

    /**
     * Adds a Cage to the grid.
     * @param cage The cage object to be added to this grid.
     */
    public void addCage(Cage cage){
        this.cages.add(cage);
        this.numberOfCellsInCages += cage.getNumberOfCells(); // incrementing the number of cells in cages
    }

    /**
     * Getter method for the cages within the grid.
     * @return The arraylist of cages within this grid.
     */
    public ArrayList<Cage> getCages(){
        return this.cages;
    }

    /**
     * Will reveal a hint to the user by revealing the value of one of the cells.
     */
    public void showHint(){
        // need to check that the grid has solutions 
        if(this.solutions.size() == 0){
            // displaying error message
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No hint can be shown");
            alert.setContentText("The grid has no solutions, no hint can be shown.");
            alert.showAndWait();    
            return;
        }

        // cannot show hing if game is in winning state
        if(this.game.getWinningState()){
            // displaying error message
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No hint can be shown");
            alert.setContentText("The grid is completed, no hint can be shown.");
            alert.showAndWait();
            return;
        }

        // checking if cell is too complete to show hint
        int cellsRemainingCount = this.cells.size();
        for(Cell cell : this.cells){
            // counting number of cells that are complete
            if(cell.hasPrimarySolutionValue()){
                cellsRemainingCount--;
            }
        }
        if(cellsRemainingCount <= minimumNumberOfCellsForHint){
            // displaying error message
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No hint can be shown");
            alert.setContentText("There are too few incorrect cells left to show a hint.");
            alert.showAndWait();
            return;
        }

        // checking user has enough hints left
        if(this.remainingHintsCount == 0){
            // displaying error message
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No hint can be shown");
            alert.setContentText("You have used all of your hints.");
            alert.showAndWait();
            return; 
        }

        // ask if they are sure if they want to see a hint
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Hint");
        alert.setHeaderText("You have : " + this.remainingHintsCount + " hint(s) left!");
        alert.setContentText("Are you sure you want to show a hint?\n\n");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK, reveal one of the cells

            // pick random cell, check if this cell has correct value or any value at all
            // if not, run becomeHintCell method the cell, to make it permenantly revealed
            // on the cell, set the editable property to false 

            // while loop to make sure cell found
            boolean cellFound = false;
            while(!cellFound){
                // picking random number
                Random random = new Random();
                int randomNumber = random.nextInt(this.cells.size());

                // testing if this cell has correct value
                if(!this.cells.get(randomNumber).hasPrimarySolutionValue()){
                    // showing hint for this cell if it doesnt
                    this.deselectSelectedCell();
                    this.cells.get(randomNumber).showHint();
                    cellFound = true;
                }
            }

            // decrementing the number of remaining hints
            this.remainingHintsCount--;
            
        } else {
            // ... user chose CANCEL or closed the dialog
            // no action required
        }
    }

    /**
     * Checks if the grid is an allowed mathdoku grid.
     * @return True if it is, false if it isnt.
     */
    public boolean validateStructure(){
        // check all cells are in a cage and that there are not more cells
        // than the dimension allows for
        if(this.numberOfCellsInCages != this.dimension*this.dimension){
            return false;
        }
        // validating the structure of the cages
        for(Cage cage : this.cages){
            if(!cage.validateStructure()){
                return false;
            }
        }

        return true; // if at this point, then grid must be valid, so returning true
    }

    /**
     * Checks if the grid is correct according to the rules of Mathdoku. Will go through
     * the rows and columns of the grid and the cages and check if they are correct. WIll
     * highlight the mistakes depending on the boolean passed in. The method will use this 
     * parametere to decide when to break out of the method. If not showing mistakes (and 
     * therefore trying to see if the grid is complete), the method will break as soon as a 
     * mistake is found. However, if showing mistakes, then the method will continue to 
     * iterate over all rows, columns and cages, so that all mistakes can be found.
     * @param showMistakes Boolean declares if the mistakes that are detected are to be highlighted
     * @return True if the grid is correct, false if not.
     */
    public boolean testIfCorrect(boolean showMistakes){
        // testing if this is the first time showing mistakes
        if(this.game.getFirstTimeShowingMistakes() && showMistakes){
            // showing alert if it is
            Alert firstTimeShowingMistakesAlert = new Alert(AlertType.INFORMATION, 
                                                            "", 
                                                            new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));

            // configuring the dialog pane
            firstTimeShowingMistakesAlert.setTitle("Mistakes");
            firstTimeShowingMistakesAlert.setHeaderText("Showing Mistakes");
            firstTimeShowingMistakesAlert.getDialogPane().setPrefSize(500, 300);
            firstTimeShowingMistakesAlert.setContentText("This is a button that" +
                                                         " is used to show the mistakes on the grid.\n\n" + 
                                                         "HOLD the button down to reveal the mistakes:\n\n" + 
                                                         "\t- A row is highlighted yellow if there is a " +
                                                         "mistake in the row.\n" + 
                                                         "\t- A column is highlighted purple if there is a " +
                                                         "mistake in the column.\n" + 
                                                         "\t- A cage is highlighted red if there is a " +
                                                         "mistake in the cage.\n");

            firstTimeShowingMistakesAlert.showAndWait();
            this.game.setFirstTimeShowingMistakes(false);
            return false; // exiting out
        }


        boolean mistakeFound = false; // records if a mistake was found 

        /**  CHECKING ROWS
         *      - Check if it follows the rules:
         *          - if it doesnt: 
         *                  - check if need to show mistakes
         *                  - if showing mistakes, show them
         *          - if it does:
         *                  - check if it is full:
         *                       - if it is full:    
         *                              - return true, grid is correct
         *                              - else return false
         *                       - if it isnt full:
         *                              - check if showing mistakes:
         *                                     - if not, return false
         *                                     - otherwise continue  
         * */
        for(int row = 0; row < this.dimension; row++){
            // checking the row follows the rules
            if(!rowOrColumnisCorrect(this.rows.get(row), this.dimension)){
                mistakeFound = true;
                // this row does not follow the rules
                if(showMistakes){
                    // showing the mistakes in the row
                    for(Cell cell : this.rows.get(row)){
                        cell.makeMistakeStyle(rowMistakeColour);
                        mistakeFound = true;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                // this row follows the rules
                // check if it is full and if need to show mistakes
                if(!this.isFull()){
                    mistakeFound = true;
                    // this column is not full
                    if(!showMistakes){
                        // not showing mistakes, so break out of loop
                        return false;
                    }
                }
            }
        }

        /**  CHECKING COLUMNS
         *      - Check if it follows the rules:
         *          - if it doesnt: 
         *                  - check if need to show mistakes
         *                  - if showing mistakes, show them
         *          - if it does:
         *                  - check if it is full:
         *                       - if it is full:    
         *                              - return true, grid is correct
         *                       - if it isnt full:
         *                              - check if showing mistakes:
         *                                     - if not, return false
         *                                     - otherwise continue                     
         * */
        for(int column = 0; column < this.dimension; column++){
            // if the column is full, then checking it follows the rules
            if(!rowOrColumnisCorrect(this.columns.get(column), this.dimension)){
                // this column does not follow the rules
                if(showMistakes){
                    // showing the mistake for this cell
                    for(Cell cell : this.columns.get(column)){
                        cell.makeMistakeStyle(columnMistakeColour);
                        mistakeFound = true;
                    }
                }
                else{
                    // do not need to show mistakes, so returning false
                    return false;
                }
            }
            else{
                // this column follows the rules
                // check if it is full and if need to show mistakes
                if(!this.isFull()){
                    mistakeFound = true;
                    // this column is not full
                    if(!showMistakes){
                        // not showing mistakes, so break out of loop
                        return false;
                    }
                }
            }
        }

        // CHECKING CAGES
        for(Cage cage :  this.cages){
            if(!cage.testIfCorrect(showMistakes)){
                mistakeFound = true;
                // this cage is not correct, seeing if showing mistakes
                if(!showMistakes){
                    // if not showing mistakes, then returning false
                    return false;
                }
            }
        }

        // returning whether a mistake was found
        return !mistakeFound;
    }

    /**
     * Given an array list of cells representing a row, column or whole grid 
     * check if all cells are full. That is, if there is a value within
     * each one of the given cells.
     * @param cells The array list of cells to be checked
     * @return True if the column or row is full, false if not.
     */
    private boolean isFull(){
        // testing to see if any of the cells dont have a value
        for(Cell cell : this.cells){
            if(cell.getValue().equals("")){
                return false; // returning false if cell found with no value
            }
        }
        // if no cell found with no value, then the row/column/grid must be full
        return true;
    }

    /**
     * Given an array list of cells, representing a row or column, will check that 
     * the row or column follows the rules of Mathdoku. That is, the row or column
     * includes each number from 1 - dimension exactly once.
     * @param cells The array list of cells representing the row or column
     * @return True if the row or column follows the rules.
     */
    private static boolean rowOrColumnisCorrect(ArrayList<Cell> rowOrColumn, int dimension){
        ArrayList<String> values = new ArrayList<String>();
        // iterating through the list, and adding the unique numbers to the array list
        for(Cell cell : rowOrColumn){
            if(!values.contains(cell.getValue()) || cell.getValue().equals("")){
                values.add(cell.getValue());
            }
            else{
                return false;
            }
        }

        // if at this point, then the row or column must follow the rules
        return true;
    }

    /**
     * Will add outline to the cages contained within this grid.
     */
    public void styleGrid(){
        // looping through the cages in the grid and styling them
        for(Cage cage : this.cages){
            cage.styleCage();
        }
    }

    /**
     * Makes all of the cells in the grid have the default styling for the grid.
     */
    public void makeDefualtStyle(){
        for(Cell cell :  this.cells){
            cell.makeDefaultStyle();
        }
    }

    /**
     * Makes all the cells in the grid have their current styling. This will either
     * be the default styling, or the winning styling. This method is needed, otherwise, 
     * when presseing the 'show mistakes' button on the game, it will remove the winning styling
     * if the 'make defaultstyle method' is used.
     */
    public void makeCurrentStyle(){
        for(Cell cell : this.cells){
            cell.makeCurrentStyle();
        }
        if(this.selectedCell!=null){
            this.setSelectedCell(this.selectedCell);
        }
    }

    /**
     * Sets the font size for the cells within the grid based
     * on the int passed in.
     * @param fontSize Integer represtnting the font size to be 
     * changed to.
     */
    public void setFontSize(int fontSize){
        // setting the font size for all of the cells
        for(Cell cell : this.cells){
            cell.setFontSize(fontSize);
        }
    }

    /**
     * Creates a new grid instance that is the same as this object
     * @return The copy of this grid object.
     */
    public Grid copy(){
        // start with blank grid object
        Grid gridCopy = new Grid(this.game, this.getDimension());

        // iterating over cages in the original grid
        for(Cage cage : this.getCages()){
            // creating a new cage using this data
            ArrayList<Cell> cellsInCage = new ArrayList<Cell>();

            // iterating over cells in this cage
            for(Cell cell : cage.getCells()){
                // finding corresponding cell within the grid copy
                Cell cellCopy = gridCopy.getCells().get(cell.getPosition() - 1);
                cellsInCage.add(cellCopy);
            }

            // making new cage copy
            Cage cageCopy = new Cage(gridCopy, 
                                     cellsInCage, 
                                     cage.getTargetNumber(), 
                                     cage.getTargetOperator());

            // adding the cage copy to the grid
            gridCopy.addCage(cageCopy);
        }

        // styling the grid and returning it
        gridCopy.styleGrid();

        return gridCopy;
    }
}