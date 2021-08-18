/**
 * Represents the the event of the insertion of a 
 * value into a cell within the grid.
 */
public class CellValueEvent extends ValueEvent{
    public Cell cell; // the cell corresponding to this insertion
    public String oldValue; // the value before the insertion
    public String newValue; // the value after the insertion

    /**
     * Constructor for the class. Sets up a new GridInsertionEvent instance,
     * given the effected cell, and the values.
     * @param cell The cell effected by this event.
     * @param oldvalue The value before the event.
     * @param newValue The value after the event.
     */
    public CellValueEvent(Cell cell, String oldValue, String newValue){
        // instantiating the member variables
        this.cell = cell;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Un-does the action caused by this event.
     */
    public void undo(){
        // SET CELL VALUE TO OLD VALUE
        this.cell.setValue(this.oldValue);

        // need to check if this undo completes the game or undoes it
        if(this.cell.getGrid().testIfCorrect(false)){
            this.cell.getGrid().getGame().displayWinningAnimation();
        }
        else{
            this.cell.getGrid().makeDefualtStyle();
        }

        // checking if the clear grid button needs to be disabled or enabled
        if(this.cell.getGrid().isEmpty()){
            // disable clear grid
            this.cell.getGrid().getGame().getControlPanel().getToolbar().setClearGridButtonDisable(true);
        }
        else{
            // enable clear grid
            this.cell.getGrid().getGame().getControlPanel().getToolbar().setClearGridButtonDisable(false);
        }
    }

    /**
     * Re-does the action caused by this event.
     */
    public void redo(){
        // SET CELL VALUE TO NEW VALUE
        this.cell.setValue(this.newValue);

        // need to check if this undo completes the game
        if(this.cell.getGrid().testIfCorrect(false)){
            this.cell.getGrid().getGame().displayWinningAnimation();
        }
        else{
            this.cell.getGrid().makeDefualtStyle();
        }

        // checking if the clear cell and clear grid need to be disabled or enabled
        if(this.cell.getValue().equals("")){
            // disable clear cell
            this.cell.getGrid().getGame().getControlPanel().getToolbar().setClearCellButtonDisable(true);
        }
        else{
            // enable clear cell
            this.cell.getGrid().getGame().getControlPanel().getToolbar().setClearCellButtonDisable(false);
        }

        if(this.cell.getGrid().isEmpty()){
            // disable clear grid
            this.cell.getGrid().getGame().getControlPanel().getToolbar().setClearGridButtonDisable(true);
        }
        else{
            // enable clear grid
            this.cell.getGrid().getGame().getControlPanel().getToolbar().setClearGridButtonDisable(false);
        }
    }
}