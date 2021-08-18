import java.util.ArrayList;
import java.util.Random;

/**
 * Handles the making of a random Mathdoku grid.
 */
public class RandomGridMaker{

    private Game game; // the game instance associated with this game maker
    private Random random;
    private static final int maxCageSize = 4;
    private static final int minCageSize = 2;
    private Grid randomGrid;
    private ArrayList<Cell> cagedCells;
    private ArrayList<Cell> uncagedCells;

    /**
     * Constructor for the class, links the game instance to the grid maker.
     * @param game
     */
    public RandomGridMaker(Game game){
        this.game = game;
        this.random = new Random();
    }

    /**
     * Makes a random Mathdoku grid of the given dimension, that has 
     * only one solution
     * @param dimension The dimension for the random grid.
     * @return Grid object that was created as a result.
     */
    public Grid makeRandomGrid(int dimension, int difficulty){
        /**
         * TO MAKE THE RANDOM GRID: 
         *      - make a random cell arrangement that follows the rules
         *      - keep a list of uncaged cells
         *      - and a list of caged cells (dont know if actually needed)
         *      - pick a random uncaged cell
         *      - pick a cage size
         *      - join adjacent cells into this cell until at cage size
         *      - pick random operator based on these cells
         *      - apply operator between cell values and get target
         *      - cage complete
         *      - repeat process until no uncaged cells
         *      - if cell has no adjacent cells, it becomes a single celled cage
         *      - when all cells are in a cage, style the grid and return it.
         * 
         *      - finally, check if grid has only one solution
         */
    
        boolean gridWithUniqueSolutionFound = false;
        int numberOfOneCellCages = 0;

        // deciding on number of cages to only have 1 cell
        if(difficulty == 1){
            // easy
            numberOfOneCellCages = dimension;
        }
        else if(difficulty == 2){
            // normal
            numberOfOneCellCages = dimension/2; //  takes integer part of division
        }
        else{
            // hard
            numberOfOneCellCages = 1;
        }

        while(!gridWithUniqueSolutionFound){
            int oneCellCages  = numberOfOneCellCages;


            this.randomGrid = new Grid(this.game, dimension);

            ////////////////////////////////
            // MAKING THE RANDOM SOLUTION //
            ////////////////////////////////

            // getting the random grid solution
            ArrayList<Integer> randomSolutionValues = makeRandomSolution(dimension);
            this.randomGrid.setSolutionValues(randomSolutionValues); // setting this solution into the grid

            //////////////////////
            // MAKING THE CAGES //
            //////////////////////

            // array lists to store what cells have been assigned to cages
            this.cagedCells = new ArrayList<Cell>();
            this.uncagedCells = new ArrayList<Cell>();
            for(Cell cell : this.randomGrid.getCells()){
                uncagedCells.add(cell);
            }

            // iterating through until all cells are in cages
            while(!this.uncagedCells.isEmpty()){
                // make a new cage

                // assessing if this is cage with random size or with only 1 cell
                if(oneCellCages > 0){
                    Cage newCage = this.makeNewRandomCage(1);
                    if(newCage != null){
                        this.randomGrid.addCage(newCage);
                    }
                    oneCellCages--;
                }
                else{
                    // making cage with random size
                    int cageSize = this.random.nextInt(maxCageSize - minCageSize + 1) + minCageSize;
                    Cage newCage = this.makeNewRandomCage(cageSize);
                    if(newCage != null){
                        this.randomGrid.addCage(newCage);
                    }
                }
            }        

            //////////////////////////////////
            // CHECKING FOR UNIQUE SOLUTION //
            //////////////////////////////////

            // solving the grid
            GridSolver gridSolver = new GridSolver(this.randomGrid);
            gridSolver.solve();

            // checking number of solutions
            if(gridSolver.getSolutionCount() == 1){
                gridWithUniqueSolutionFound = true;
            }
        }

        // at this point, the grid has been made, and the the cages have been added, so the grid must be styled
        this.randomGrid.styleGrid();

        return this.randomGrid; // returning the finished grid instance
    }

    /**
     * Makes a random solution to a grid of the given dimension.
     * @param dimension The dimension of the grid that a solution
     * must be made for.
     */
    private static ArrayList<Integer> makeRandomSolution(int dimension){
        // storing the rows and columns in this new solution
        ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> columns = new ArrayList<ArrayList<Integer>>();

        // stores all of the values that have been added into the grid
        ArrayList<Integer> allValues = new ArrayList<Integer>(); 

        Random random = new Random();

        /**
         * METHOD
         *      - iterate through 0-dimension to go over all rows
         *      - go into the row for this number
         *      - iterate through 0-dimension again for cells in this row
         *      - pick random number for this position that is not
         *        in this row already, and that is not in the corresponding column
         *      - when the number is found, add it to the list, and the column list, and the all values list
         *      - continue until all iterations are complete
         * 
         *      - if no number can be found for a cell, then delete this row and start again.
         * 
         */

        // populating the arraylist of rows and columns
        for(int index = 0; index < dimension; index++){
            rows.add(new ArrayList<Integer>()); // new array list to store this row of cells        
            columns.add(new ArrayList<Integer>()); // new array list to store this row of cells        
        }

        // iterating through the rows
        for(int rowNumber = 0; rowNumber < dimension; rowNumber++){
            ArrayList<Integer> currentRow = rows.get(rowNumber); // the current row

            // iterating through the cells in this row
            for(int cellNumber = 0; cellNumber < dimension; cellNumber++){
                ArrayList<Integer> currentColumn = columns.get(cellNumber);
                boolean numberFound = false;
                ArrayList<Integer> numbersTried = new ArrayList<Integer>();

                // continually generating a random number until one is found that
                // satisfies the constraints
                while(!numberFound){
                    // generating a random number for this cell
                    int randomNumber = random.nextInt(dimension) + 1;

                    // testing if allowed
                    if((!currentRow.contains(randomNumber) && !currentColumn.contains(randomNumber))){
                        // if it is allowed, then setting it into the arraylist
                        currentRow.add(randomNumber);
                        currentColumn.add(randomNumber);

                        numberFound = true; // a number has been found, so bool can be set to true
                    }
                    // if the number is not allowed, testing if all possible numbers have been tried
                    else{
                        // adding this number to the list of numbers tried
                        if(!numbersTried.contains(randomNumber)){
                            numbersTried.add(randomNumber);
                        }
                        // testing if all numbers have been tried
                        if(numbersTried.size() == dimension){
                            // if all numbers have been tried, then need to backtrack and do this row again

                            // delete all elements in this row
                            currentRow.clear();

                            // delete most recent element in columns up to this cell
                            for(int index = 0; index < cellNumber; index++){
                                columns.get(index).remove(columns.get(index).size() - 1);
                            }

                            // start this row again by manually breaking out of the loop
                            numberFound = true;
                            rowNumber -=1;
                            cellNumber = dimension - 1; 
                        }
                    }
                }
            }
        }

        // when all rows are complete, adding all the elements to a single list
        for(ArrayList<Integer> row : rows){
            for(Integer cellValue : row){
                allValues.add(cellValue);
            }
        }

        // returning this list of cell values
        return allValues;
    }

    /**
     * Makes a new cage given the uncaged cells.
     * @return The cage object that was made.
     */
    private Cage makeNewRandomCage(int cageSize){
        // making an arraylist to hold the cells within this cage
        ArrayList<Cell> cells = new ArrayList<Cell>();

        // selecting a random uncaged cell
        Cell startingCell = this.uncagedCells.get(this.random.nextInt(this.uncagedCells.size()));
        cells.add(startingCell);
        this.uncagedCells.remove(startingCell);
        this.cagedCells.add(startingCell);
        Cell nextAdjacentCell = startingCell;
        boolean joinedToSeperateCage = false;

        // making sure cage size is not 1 before doing this
        if(cageSize != 1){
            // finding adjacent cells to this one until the cage is complete, or until there are no more adjacent cells
            boolean cageComplete = false;
            while(!cageComplete){
                // find new adjacent cell
                nextAdjacentCell = this.findUnCagedAdjacentCell(nextAdjacentCell);

                // testing if the cell is null
                if(!(nextAdjacentCell == null)){
                    // if the cell is not null, adding it to the cage, removing it from the uncaged cells, adding it to the caged cells
                    cells.add(nextAdjacentCell);
                    this.cagedCells.add(nextAdjacentCell);
                    this.uncagedCells.remove(nextAdjacentCell);

                    // check if cage size is now the required size
                    if(cells.size() == cageSize){
                        // if it is, then the cage is complete
                        cageComplete = true;
                    }
                }
                else{
                    // if returned null, an adjacent cell cannot be found, so the cage is finished (update to try with starting cell as well)
                    cageComplete = true;

                    // JOINS SINGLE CELLS TO OTHER CAGES. MAKES LOADING VERY SLOW SO DIDNT INCLUDE //

                    // // checking if single cell cage made accidently
                    // if(cells.size() == 1){
                    //     // joining this cell to an adjacent cage if it was
                    //     this.joinCellToAdjacentCage(startingCell);
                    //     joinedToSeperateCage = true;
                    // }
                }
            }
        }

        if(!joinedToSeperateCage){
            // make new cage object with the cells array list
            Cage cage = new Cage();
            cage.setGrid(this.randomGrid);
            cage.setCells(cells);
            cage.setRandomTarget(); // setting random target for this cage
            
            return cage; // returning the completed cage
        }

        return null;
    }

    /**
     * Given a cell, will add this cell to an adjacent cage.
     * @param cell The cell to be added to an adjacent cage.
     */
    private void joinCellToAdjacentCage(Cell cell) {
        // finding the adjacent cell in a cage
        Cell adjacentCagedCell = this.findCagedAdjacentCell(cell);
        
        // makes sure only joins it if it isnt a one caged cell (cant include as 
        // doesnt accoutn for case where only cells surrounding it are all one caged cells, which
        // would give an infinite loop, just very unlikley)

        // // checking if it is going to join it to a single celled cage
        // if(adjacentCagedCell.getCage().getCells().size() == 1){
        //     this.joinCellToAdjacentCage(cell);
        //     return;
        // }

        // adding the cell to this adjacent cage
        if(adjacentCagedCell != null){
            Cage adjacentCage = adjacentCagedCell.getCage(); // getting the cage
            adjacentCage.addCell(cell); // adding the cell

            adjacentCage.setRandomTarget();
        }
	}

	/**
     * Given a cell, will find a cell that is adjacent to it, and is not currently in a cage.
     * @param cell The cell for which an adjacent cell will be found.
     * @return The cell that was found, returns null if no cell was found.
     */
    private Cell findUnCagedAdjacentCell(Cell cell){
        int cellPosition = cell.getPosition(); // the position of this cell

        // storing the directions that need to be checked for this cell
        ArrayList<Integer> directions = new ArrayList<Integer>();
        directions.add(0); // up
        directions.add(1); // right
        directions.add(2); // down
        directions.add(3); // left
        ArrayList<Integer> directionsChecked = new ArrayList<Integer>();

        // iterating through the directions to check
        while(directionsChecked.size() != directions.size()){
            // getting a random direction that has not yet been checked
            boolean randomDirectionFound = false;
            int randomDirection = 0;
            while(!randomDirectionFound){
                randomDirection = this.random.nextInt(4);
                if(!directionsChecked.contains(randomDirection)){
                    randomDirectionFound = true;
                }
            }

            // testing if there is a cell in the given direction

            // checking for above
            if(randomDirection == 0){
                // checking if cell exists in this position
                if(cellPosition - this.randomGrid.getDimension() > 0){
                    // checking if the cell at this position is uncaged
                    if(!this.cagedCells.contains(this.randomGrid.getCells().get(cellPosition - this.randomGrid.getDimension() - 1))){
                        // if it is uncaged, returning this cell
                        return this.randomGrid.getCells().get(cellPosition - this.randomGrid.getDimension() - 1);
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
            // checking for right
            if(randomDirection == 1){
                // checking if cell exists in this position
                if(!((cellPosition) % this.randomGrid.getDimension() == 0)){
                    // checking if the cell at this position is uncaged
                    if(!this.cagedCells.contains(this.randomGrid.getCells().get(cellPosition))){
                        // if it is uncaged, returning this cell
                        return this.randomGrid.getCells().get(cellPosition);
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
            // checking for below
            if(randomDirection == 2){
                // checking if cell exists in this position
                if(cellPosition + this.randomGrid.getDimension() <= this.randomGrid.getDimension()){
                    // checking if the cell at this position is uncaged
                    if(!this.cagedCells.contains(this.randomGrid.getCells().get(cellPosition + this.randomGrid.getDimension() - 1))){
                        // if it is uncaged, returning this cell
                        return this.randomGrid.getCells().get(cellPosition + this.randomGrid.getDimension() - 1);
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
            // checking for left
            if(randomDirection == 3){
                // checking if cell exists in this position
                if(!((cellPosition - 1) % this.randomGrid.getDimension() == 0)){
                    // checking if the cell at this position is uncaged
                    if(!this.cagedCells.contains(this.randomGrid.getCells().get(cellPosition - 2))){
                        // if it is uncaged, returning this cell
                        return this.randomGrid.getCells().get(cellPosition - 2);
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
        }

        // if no cell was found, then there is no adjacent cell, so returning null
        return null;
    }
    
	/**
     * Given a cell, will find a cell that is adjacent to it, and is currently in a cage.
     * @param cell The cell for which an adjacent cell will be found.
     * @return The cell that was found, returns null if no cell was found.
     */
    private Cell findCagedAdjacentCell(Cell cell){
        int cellPosition = cell.getPosition(); // the position of this cell

        // storing the directions that need to be checked for this cell
        ArrayList<Integer> directions = new ArrayList<Integer>();
        directions.add(0); // up
        directions.add(1); // right
        directions.add(2); // down
        directions.add(3); // left
        ArrayList<Integer> directionsChecked = new ArrayList<Integer>();

        // iterating through the directions to check
        while(directionsChecked.size() != directions.size()){
            // getting a random direction that has not yet been checked
            boolean randomDirectionFound = false;
            int randomDirection = 0;
            while(!randomDirectionFound){
                randomDirection = this.random.nextInt(4);
                if(!directionsChecked.contains(randomDirection)){
                    randomDirectionFound = true;
                }
            }

            // testing if there is a cell in the given direction

            // checking for above
            if(randomDirection == 0){
                // checking if cell exists in this position
                if(cellPosition - this.randomGrid.getDimension() > 0){
                    Cell adjacentCell = this.randomGrid.getCells().get(cellPosition - this.randomGrid.getDimension() - 1);
                    // checking if the cell at this position is uncaged
                    if(this.cagedCells.contains(adjacentCell)){
                        // if it is uncaged, returning this cell
                        return adjacentCell;
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
            // checking for right
            if(randomDirection == 1){
                // checking if cell exists in this position
                if(!((cellPosition) % this.randomGrid.getDimension() == 0)){
                    Cell adjacentCell = this.randomGrid.getCells().get(cellPosition);
                    // checking if the cell at this position is uncaged
                    if(this.cagedCells.contains(adjacentCell)){
                        // if it is uncaged, returning this cell
                        return adjacentCell;
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
            // checking for below
            if(randomDirection == 2){
                // checking if cell exists in this position
                if(cellPosition + this.randomGrid.getDimension() <= this.randomGrid.getDimension()){
                    // checking if the cell at this position is uncaged
                    Cell adjacentCell = this.randomGrid.getCells().get(cellPosition + this.randomGrid.getDimension() - 1);
                    if(this.cagedCells.contains(adjacentCell)){
                        // if it is uncaged, returning this cell
                        return adjacentCell;
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
            // checking for left
            if(randomDirection == 3){
                // checking if cell exists in this position
                if(!((cellPosition - 1) % this.randomGrid.getDimension() == 0)){
                    // checking if the cell at this position is uncaged
                    Cell adjacentCell = this.randomGrid.getCells().get(cellPosition - 2);
                    if(this.cagedCells.contains(adjacentCell)){
                        // if it is uncaged, returning this cell
                        return adjacentCell;
                    }
                    else{
                        // if this cell is in a cage, adding it to the directions checked list
                        directionsChecked.add(randomDirection);
                    }
                }
                else{
                    // if this cell is in a cage, adding it to the directions checked list
                    directionsChecked.add(randomDirection);
                }
            }
        }

        // if no cell was found, then there is no adjacent cell, so returning null
        return null;
    }
}