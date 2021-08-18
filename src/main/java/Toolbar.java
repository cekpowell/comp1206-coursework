import javafx.scene.layout.*;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 * Represents the control panel for the application's game
 * window.
 */
public class Toolbar extends FlowPane{

    // definining the controls for the control panel
    private Button undoButton;
    private Button redoButton;
    private Button clearCellButton;
    private Button clearGridButton;
    private Button loadFromFileButton;
    private Button loadFromTextButton;
    private Button showMistakesButton;
    private Button settingsButton;
    private Button solveButton;
    private Button showHintButton;
    private Game game; // reference to the game instance that this control panel is associated with
    private boolean disable;

    /**
     * Constructor for the class. Initialises the components within the control panel.
     */
    public Toolbar(Game game){
        // initialising the member variables
        this.game = game;
        this.undoButton = new Button("UNDO");
        this.redoButton = new Button("REDO");
        this.clearCellButton = new Button("CLEAR CELL");
        this.clearGridButton = new Button("CLEAR GRID");
        //this.loadFromFileButton = new Button("LOAD FROM FILE");
        //this.loadFromTextButton = new Button("LOAD FROM TEXT");
        this.showMistakesButton = new Button("SHOW MISTAKES");
        this.settingsButton  = new Button("SETTINGS");
        this.solveButton = new Button("SOLVE");
        this.showHintButton = new Button("SHOW HINT");

        this.clearCellButton.setDisable(true);
        this.clearGridButton.setDisable(true);

        // adding these controls to the panel 
        this.getChildren().addAll(this.undoButton, 
                                  this.redoButton, 
                                  this.clearCellButton,
                                  this.clearGridButton, 
                                  //this.loadFromFileButton, 
                                  //this.loadFromTextButton, 
                                  this.showMistakesButton,
                                  this.settingsButton,
                                  this.solveButton,
                                  this.showHintButton);

        // formatting the control panel
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10,10,20,10));
        this.setHgap(10);

        // adding event filters to the buttons
        this.undoButton.setOnAction(event-> this.game.undoLastValueEvent());
        this.redoButton.setOnAction(event-> this.game.redoLastValueEvent());
        this.clearCellButton.setOnAction(event-> this.game.getGrid().clearSelectedCell());
        this.clearGridButton.setOnAction(event->this.game.getGrid().clear());
        //this.loadFromFileButton.setOnAction(event->this.game.newGridFromConfigurationFile());
        //this.loadFromTextButton.setOnAction(event->this.game.newGridFromTextInput());
        this.showMistakesButton.setOnMousePressed(event->this.game.getGrid().testIfCorrect(true));
        this.showMistakesButton.setOnMouseReleased(event->this.game.getGrid().makeCurrentStyle());
        this.settingsButton.setOnAction(event->this.game.getSettings().showSettingsDialog());
        this.solveButton.setOnAction(event->this.game.solveGrid());
        this.showHintButton.setOnAction(event->this.game.getGrid().showHint());

    }

    /**
     * Sets the 'disable' property of the button.
     * @param disable true for disabled, false for enabled   
     * */
    public void setUndoButtonDisable(boolean disable){
        this.undoButton.setDisable(disable);
    }

    /**
     * Sets the 'disable' property of the button.
     * @param disable true for disabled, false for enabled
     */
    public void setRedoButtonDisable(boolean disable){
        this.redoButton.setDisable(disable);
    }

    /**
     * Sets the 'disable' property for the clear cell button.
     * @param disable Boolean representing if the button should be 
     * disabled or enabled.
     */
    public void setClearCellButtonDisable(boolean disable){
        this.clearCellButton.setDisable(disable);
    }

    /**
     * Sets the 'disable' property for the clear grid button.
     * @param disable Boolean representing if the button should
     * be enabled or disabled.
     */
    public void setClearGridButtonDisable(boolean disable){
        this.clearGridButton.setDisable(disable);
    }

    /**
     * Disables the buttons on the toolbar. Used when no game has been
     * loaded yet (start screen).
     * @param disable Boolean representing if the buttons on the toolbar
     * are disabled or not.
     */
    public void setToolbarDisable(boolean disable){
        this.showMistakesButton.setDisable(disable);
        this.solveButton.setDisable(disable);
        this.showHintButton.setDisable(disable);
    }
}