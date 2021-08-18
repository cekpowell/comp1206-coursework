import java.util.ArrayList;

/**
 * Class handles the solving of a grid object
 */
public class GridSolver{
    private Grid grid; // the grid that is being solved
    private ArrayList<ArrayList<Integer>> solutions; // the solutions that were found
    private int solutionCount; // stores the number of solutions for this grid

    /**
     * Constructor for the class. Instantiates the grid 
     * object that is to be solved by this grid solver.
     */
    GridSolver(Grid grid){
        this.grid = grid;
        this.solutions = new ArrayList<ArrayList<Integer>>();
        this.solutionCount = 0;
    }

    /**
     * Solves the grid instance associated with this solver.
     * Returns an array of cell values for every solution that 
     * is found for the grid
     * @return The arraylist of arraylist of cell values for the 
     * solution for the grid.
     */
    public void solve(){
        ArrayList<Integer> solution = new ArrayList<Integer>();

        int rowCount  =  0;
        // iterating through rows
        for(ArrayList<Cell> row : this.grid.getRows()){
            int cellCount = 0;
            // iterating over row
            for(Cell cell : row){
                // testing if cell is blank
                if(cell.getPossibleValue() == 0){
                    // if blank, iterating values
                    for(int possibleValue = 1; possibleValue <= this.grid.getDimension(); possibleValue++){
                        // testing if value is possible
                        if(isPossible(cell, possibleValue)){
                            // if possible, inserting it
                            cell.setPossibleValue(possibleValue);                   
                            solve(); // recursive call
                            cell.setPossibleValue(0);        
                        }
                    }
                    return;
                }
                cellCount++;
            }
            rowCount++;
        }

        // at this point, the state of the grid is a solution, so need to store this solution
        this.solutions.add(new ArrayList<Integer>());
        for(ArrayList<Cell> row : this.grid.getRows()){
            for(Cell cell : row){
                this.solutions.get(this.solutionCount).add(cell.getPossibleValue());
            }
        }
        this.solutionCount++; // incrementing the solution count
    }

    /**
     * Given a Cell object, and a value, will test if this value can 
     * be placed into the cell.
     * @param cell The Cell object to be checked.
     * @param value The value to be tested within the cell.
     * @return  True if the value can be inserted, false if not.
     */
    public boolean isPossible(Cell targetCell, int targetValue){
        /**
         * CHECKING IF CELL VALUE IS POSSIBLE:
         *      -check if samevalue is in any of the other cells in the column
         *      - check if the same value is inany of the other cells in the row
         *      - checl if the value can be put into the cage and the cage still be okay
         *      - if none of these constraints broke, the value must be okay, and so return true
         */

        // checking if the value can be placed into this cell's row
        ArrayList<Cell> row = this.grid.getRowFromCell(targetCell);
        for(Cell cell : row){
            if(cell.getPossibleValue() != 0){
                if(cell.getPossibleValue() == targetValue && !(cell==targetCell)){
                    return false; // returning false if a cell is found with this  value
                }
            }
        }

        // checking if the value can be placed into the cell's column
        ArrayList<Cell> column = this.grid.getColumnFromCell(targetCell);
        for(Cell cell : column){
            if(cell.getPossibleValue() != 0){
                if(cell.getPossibleValue() == targetValue && !(cell==targetCell)){
                    return false; // returning false if a cell is found with this  value
                }
            }
        }
        
        // checking if the value can be placed into the cells cage
        if(!targetCell.getCage().isPossible(targetValue)){
            return false;
        }

        return true; // returning true if the value is allowed
    }

    /**
     * Getter method for the number of solutions that were found for this grid.
     * @return The number of solutions for this grid.
     */
    public int getSolutionCount(){
        return this.solutionCount;
    }

    /**
     * Getter methos for the solutions found.
     * @return The solutions found.
     */
    public ArrayList<ArrayList<Integer>> getSolutions(){
        return this.solutions;
    }
}