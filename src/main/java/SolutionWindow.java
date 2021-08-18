import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ButtonBar;

/**
 * Handles the displaying of a solution to the user.
 */
public class SolutionWindow extends Alert{

    private TabPane solutionsPane;
    private Game game;

    private static final ButtonType finish = new ButtonType("Finish", ButtonBar.ButtonData.OK_DONE);

    /**
     * Constructor for the class
     * @param game The game instance that this solution window is displaying solutions for.
     */
    public SolutionWindow(Game game){
        // calling the super constructor
        super(AlertType.CONFIRMATION, "", finish);
        this.game = game;

        // configuring the dialog pane
        this.setTitle("Solutions");
        this.setHeaderText("Solutions");
        this.getDialogPane().setPrefSize(600, 600);

        // setting up the tab pane
        this.solutionsPane= new TabPane();

        // adding the solutions to the tab pane
        this.addSolutions();

        // adding the tab pane to the dialog window
        this.getDialogPane().setContent(solutionsPane);
    }

    /**
     * Adds the solutions to the current game to the window.
     */
    private void addSolutions(){
        /**
         * ADDING SOLUTIONS:
         *      - use grid solver object to work out number of solutions
         *      - for loop for this array of solutions
         *      - create a new tab for each solution
         *      - make a grid object within this tab with these values
         *      - make sure the grid cant be edited
         */

        // solving the grid
        GridSolver gridSolver = new GridSolver(this.game.getGrid());
        gridSolver.solve();

        // iterating over the solutions
        int currentSolutionNumber = 1;
        for(ArrayList<Integer> solution : gridSolver.getSolutions()){
            // creating a new tab for this solution
            Tab solutionTab = new Tab("Solution " + currentSolutionNumber);
            solutionTab.setClosable(false);

            // making  grid for this solution
            Grid solutionGrid = this.game.getGrid().copy();
            solutionGrid.setEditable(false);

            // setting the values for the cells
            for(int index = 0; index < solution.size(); index++){
                solutionGrid.getCells().get(index).setValue(String.valueOf(solution.get(index)));

                // making the cell the solution style
                solutionGrid.getCells().get(index).makeSolutionStyle();
            }

            // adding the grid to the tab
            solutionTab.setContent(solutionGrid);

            // adding tab to the window
            this.solutionsPane.getTabs().add(solutionTab);
            currentSolutionNumber++;
        }
    }
}