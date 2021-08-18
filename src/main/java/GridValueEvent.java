import java.util.ArrayList;

/**
 * Represents the the change of value to all cells within the grid.
 */
public class GridValueEvent extends ValueEvent{
    public Grid grid; // the cell corresponding to this insertion
    public ArrayList<String> oldValues;

    /**
     * Constructor for the class. Sets up a new GridValueEvent instance,
     * given the grid.
     * @param cell The cell effected by this event.
     * @param oldvalue The value before the event.
     * @param newValue The value after the event.
     */
    public GridValueEvent(Grid grid){
        // instantiating the member variables
        this.grid = grid;
        this.oldValues = new ArrayList<String>();

        // adding the values of the grid to the old values array list
        for(Cell cell : this.grid.getCells()){
            this.oldValues.add(cell.getValue());
        }
    }

    /**
     * Un-does the action caused by this event. This involves
     * iterating through the cells in the grid and setting their value back
     * according to the array list that was stored.
     */
    public void undo(){
        // SET CELL VALUES TO OLD VALUES
        for(int i = 0; i < this.oldValues.size(); i++){
            this.grid.getCellAtPosition(i).setValue(oldValues.get(i));
        }

        // need to check if this undo completes the game or undoes it
        if(this.grid.testIfCorrect(false)){
            this.grid.getGame().displayWinningAnimation();
        }
        else{
            this.grid.makeDefualtStyle();
        }

        if(this.grid.isEmpty()){
            // disable clear grid
            this.grid.getGame().getControlPanel().getToolbar().setClearGridButtonDisable(true);
        }
        else{
            // enable clear grid
            this.grid.getGame().getControlPanel().getToolbar().setClearGridButtonDisable(false);
        }
    }

    /**
     * Re-does the action caused by this event. The only action
     * that can apply to a whole grid is that the grid is cleared, and
     * so to redo this event, the value of all cells in the grid must be cleared.
     */
    public void redo(){
        // SET CELL VALUE TO BACK TO NORMAL
        for(Cell cell : this.grid.getCells()){
            cell.setValue("");
        }

        // need to check if this undo completes the game or undoes it
        if(this.grid.testIfCorrect(false)){
            this.grid.getGame().displayWinningAnimation();
        }
        else{
            this.grid.makeDefualtStyle();
        }

        if(this.grid.isEmpty()){
            // disable clear grid
            this.grid.getGame().getControlPanel().getToolbar().setClearGridButtonDisable(true);
        }
        else{
            // enable clear grid
            this.grid.getGame().getControlPanel().getToolbar().setClearGridButtonDisable(false);
        }
    }
}