import java.io.File;
import javafx.util.Duration;
import java.util.Optional;
import java.util.Stack;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

/**
 * Represents a Mathdoku game. Extends Vbox, so that messages can be added to the 
 * game instance and displayed on screen, such as hints. 
 */
public class Game extends BorderPane{

    private Title title; // the title for this game session
    private Grid gameGrid;
    private Grid solutionGrid; 
    private ControlPanel controlPanel;
    private Settings settings;
    private int dimension; // represents the dimension of this game 
    private Stack<ValueEvent> undoStack;
    private Stack<ValueEvent> redoStack;
    private RandomGridMaker gridMaker;
    private boolean currentGridHasSolution;
    private boolean winningState;
    private int difficulty;
    private boolean firstTimeShowingMistakes;

    /**
     * Constructor for the class. Displays the start screen until the newGame
     * method is run.
     */
    public Game(){
        // setting up the title page  and adding to the top of the window
        this.title = new Title("mathdoku", "charles powell");
        this.setTop(this.title);

        // setting up the control panel for this game
        this.controlPanel = new ControlPanel(this);
        this.setBottom(this.controlPanel);

        // setting up the settings dialog
        this.settings = new Settings(this);  
    
        // setting up the stacks
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        // disabling the undo/redo buttons, as both stacks will be empty at the start
        this.controlPanel.getToolbar().setUndoButtonDisable(true);
        this.controlPanel.getToolbar().setRedoButtonDisable(true);

        this.gridMaker = new RandomGridMaker(this);
        this.currentGridHasSolution = false;
        this.winningState = false;
        this.firstTimeShowingMistakes = true;
    }

    /**
     * Makes the start screen for the user when the application is first loaded.
     */
    public void showStartScreen(){
        // showing label to configure game in settings
        Text welcomeLabel = new Text("Welcome to Mathduko!\n\n"+
                                       "Use the settings to configure/load a game!");
        welcomeLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
        welcomeLabel.setFill(Color.GREY);

        welcomeLabel.setTextAlignment(TextAlignment.CENTER);
        this.setCenter(welcomeLabel);

        // disabling toolbar until grid is loaded
        this.controlPanel.getToolbar().setToolbarDisable(true);
    }

    /**
     * Getter method for the grid in this game.
     * @return The grid object in this game instance
     */
    public Grid getGrid(){
        return this.gameGrid;
    }

    /**
     * Getter method for the settings object for this game instance.
     * @return The settings object associated with this game instance.
     */
    public Settings getSettings(){
        return this.settings;
    }

    /**
     * Getter method for the first time showing mistakes button.
     * @return Boolean representing first time showing mistakes.
     */
    public boolean getFirstTimeShowingMistakes(){
        return this.firstTimeShowingMistakes;
    }

    /**
     * Setter method for the first time showing mistakes property.
     */
    public void setFirstTimeShowingMistakes(boolean firstTimeShowingMistakes){
        this.firstTimeShowingMistakes = firstTimeShowingMistakes;
    }

    /**
     * Getter method for the control panel in this game instance.
     * @return The control panel instance associated with this game.
     */
    public ControlPanel getControlPanel(){
        return this.controlPanel;
    }

    /**
     * Sets up a new mathdoku session based on the given dimension. 
     * @param dimension The dimension of this new game.
     */
    public void newRandomGrid(int dimension, int difficulty){
        this.dimension = dimension; // setting the dimension based on this parameter
        this.difficulty = difficulty;

        // deleting the current grid if there is one
        if(this.gameGrid != null){
            this.getChildren().remove(this.gameGrid);
        }

        // setting up a grid of the required dimension
        Grid randomGrid = this.gridMaker.makeRandomGrid(this.dimension, this.difficulty);
        this.updateGrid(randomGrid);
    
        GridPane.setHgrow(randomGrid, Priority.NEVER);
    }

    /**
     * Loads a game from a configuration file. Will test the file as it is being loaded
     * to ensure that the file is valid.
     */
    public void newGridFromConfigurationFile(){
        // setting up the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // getting the configuration file and the reader object
        File configurationFile = fileChooser.showOpenDialog(this.getScene().getWindow());

        // only dealing with the file if a file was chosen
        if(configurationFile != null){
            ConfigurationReader configurationReader = new ConfigurationReader(this);
            configurationReader.setConfigurationFile(configurationFile);
    
            // checking the format of the file
            if(!configurationReader.checkConfigurationFormat()){
                // displaying an error message if the file was of the incorrect format
                Alert invalidFormatAlert = new Alert(AlertType.ERROR,
                                                    "The selected file could not be loaded.\n\n" +
                                                    "Cause: " + configurationReader.getError());
                invalidFormatAlert.setHeaderText("Error Reading File");
                invalidFormatAlert.showAndWait();
            }
            // if the format was okay, then loading the grid that was found into the game
            else{
                this.updateGrid(configurationReader.getGrid());
            }
        }
    }

    /**
     * Loads a game from a configuration file. Will test the file as it is being loaded
     * to ensure that the file is valid.
     * @param configurationFile The configuration file that the game will be loaded from
     */
    public void newGridFromConfigurationFile(File configurationFile){
        ConfigurationReader configurationReader = new ConfigurationReader(this);
        configurationReader.setConfigurationFile(configurationFile);

        // checking the format of the file
        if(!configurationReader.checkConfigurationFormat()){
            // displaying an error message if the file was of the incorrect format
            Alert invalidFormatAlert = new Alert(AlertType.ERROR,
                                                "The selected file could not be loaded.\n\n" +
                                                "Cause: " + configurationReader.getError());
            invalidFormatAlert.setHeaderText("Error Reading File");
            invalidFormatAlert.showAndWait();
        }
        // if the format was okay, then loading the grid that was found into the game
        else{
            this.updateGrid(configurationReader.getGrid());
        }
    }

    /**
     * Loads a game from a configuration text. Will first open a dialog to allow 
     * for the user to enter the text, and then will perform checks on the text to
     * make sure it is a valid grid.
     */
    public void newGridFromTextInput(){
        Alert textInputAlert = new Alert(AlertType.CONFIRMATION);

        // configuring the alert
        textInputAlert.setTitle("Load a Game From Text");
        textInputAlert.setHeaderText("Load a Game From Text");
        textInputAlert.setContentText("Please enter the configuration:");

        // configuring the content for the alert
        Label contextLabel = new Label("Please enter the configuration:"); // informative label

        TextArea textInputArea = new TextArea(); // the text area for the user to enter the configuration
        textInputArea.setEditable(true);
        textInputArea.setMaxWidth(Double.MAX_VALUE);
        textInputArea.setMaxHeight(Double.MAX_VALUE);

        VBox container = new VBox(); // container for the items
        container.getChildren().addAll(contextLabel, textInputArea);
        container.setSpacing(20);

        // adding the content to the dialog pane
        textInputAlert.getDialogPane().setContent(container);

        // showing the dialog pane
        Optional<ButtonType> result = textInputAlert.showAndWait();
        // dealing with user option
        if (result.get() == ButtonType.OK){
            ConfigurationReader configurationReader = new ConfigurationReader(this);
            configurationReader.setConfigurationText(textInputArea.getText());

            // checking the format of the configuration text
            if(!configurationReader.checkConfigurationFormat()){
                // displaying an error message if the file was of the incorrect format
                Alert invalidFormatAlert = new Alert(AlertType.ERROR,
                                                    "The selected configuration could not be loaded.\n\n" +
                                                    "Cause: " + configurationReader.getError());
                invalidFormatAlert.setHeaderText("Error Reading Configuration");
                invalidFormatAlert.showAndWait();
            }
            // if the format was okay, then loading the grid that was found into the game
            else{
                this.updateGrid(configurationReader.getGrid());
            }
        } else {
            // if the user selects cancel, then no further action is required
        }
    }

    /**
     * Loads a game from a configuration text. Will first open a dialog to allow 
     * for the user to enter the text, and then will perform checks on the text to
     * make sure it is a valid grid.
     * @param textInput The configuration text to load this grid from.
     */
    public void newGridFromConfigurationText(String textInput){
        ConfigurationReader configurationReader = new ConfigurationReader(this);
        configurationReader.setConfigurationText(textInput);

        // checking the format of the configuration text
        if(!configurationReader.checkConfigurationFormat()){
            // displaying an error message if the file was of the incorrect format
            Alert invalidFormatAlert = new Alert(AlertType.ERROR,
                                                "The selected configuration could not be loaded.\n\n" +
                                                "Cause: " + configurationReader.getError());
            invalidFormatAlert.setHeaderText("Error Reading Configuration");
            invalidFormatAlert.showAndWait();
        }
        // if the format was okay, then loading the grid that was found into the game
        else{
            this.updateGrid(configurationReader.getGrid());
        }
    }

    /**
     * Sets the grid of the game to the new grid. Will also change the number pad
     * to reflect this grid.
     * @param grid The grid to be changed to.
     */
    private void updateGrid(Grid grid){
        this.dimension = grid.getDimension(); // setting the dimension based on this parameter
        this.winningState = false;

        // deleting the current grid if there is one
        if(this.gameGrid != null){
            this.getChildren().remove(this.gameGrid);
        }

        // setting up a grid of the required dimension
        this.gameGrid = grid;
        this.setCenter(this.gameGrid);

        // adding a new number pad to the contol panel
        this.controlPanel.newNumberPad(this.dimension);

        // emptying the stacks
        this.undoStack.clear();
        this.controlPanel.getToolbar().setUndoButtonDisable(true);
        this.redoStack.clear();
        this.controlPanel.getToolbar().setRedoButtonDisable(true);

       // testing the solutions for this grid

        GridSolver gridSolver = new GridSolver(this.gameGrid);
        gridSolver.solve();

        // testing if grid has solution
        if(gridSolver.getSolutionCount() >= 1){
            // if it has solution, writing the solution into the grid
            this.gameGrid.setSolutions(gridSolver.getSolutions());
            this.currentGridHasSolution = true;
        }
        else{
            // if grid doesnt have solution, displaying warning
            this.currentGridHasSolution = false;
            Alert noSolutionAlert = new Alert(AlertType.WARNING);
            noSolutionAlert.setTitle("No Solution Found");
            noSolutionAlert.setContentText("Warning: No solutions were found for this grid.\n" + 
                                            "The grid cannot be solved.");
            noSolutionAlert.showAndWait();
        }

        this.controlPanel.getToolbar().setToolbarDisable(false);
        this.controlPanel.getToolbar().setClearGridButtonDisable(true);
    }

    /**
     * Adds the given CellValueEvent object to the undo stack for the game.
     * Is used by the grid instance of the game object whenever a new value is 
     * entered into a cell, or one is cleared.
     * @param event The CellValueEvent that has just occured.
     */
    public void addValueEvent(ValueEvent event){
        this.controlPanel.getToolbar().setUndoButtonDisable(false); // enabling the undo button
        this.undoStack.push(event); // adding the event to the stack
    }

    /**
     * Used to deal with the winning animation when a grid has been completed
     */
    public void displayWinningAnimation(){
        // disabling control panel
        this.controlPanel.setDisable(true);
        
        // displaying the grid animation
        GridTransition trans = new GridTransition(this.gameGrid, Duration.millis(this.gameGrid.getDimension() * 80));
        trans.setOnFinished(e->this.displayWinningWindow());
        trans.play();
    }

    /**
     * Displays the winning window to the user
     */
    private void displayWinningWindow(){
        try{
            Thread.sleep(150);
        }
        catch(Exception e){}
        // showing winning window
        WinningWindow winningWindow = new WinningWindow(this);
        winningWindow.show();
        this.controlPanel.setDisable(false);
    }

    /**
     * Setter method for the wining state of the grid.
     */
    public void setWinningState(boolean winningState){
        this.winningState = winningState;
    }

    /**
     * Getter method for the winning state of the game.
     * @return The winning state of the game.
     */
    public boolean getWinningState(){
        return this.winningState;
    }

    /**
     * Solves the mathduko grid.
     */
    public void solveGrid(){
        // testing if the grid has solutions
        if(!this.currentGridHasSolution){
            // showing error if grid has no solutions
            Alert noSolutionsAlert = new Alert(AlertType.ERROR);
            noSolutionsAlert.setTitle("Show Grid Solutions");
            noSolutionsAlert.setHeaderText("Error Showing Solutions");
            noSolutionsAlert.setContentText("There are no solutions for this grid.\n\n" +
                                            "Nothing can be shown");
            noSolutionsAlert.showAndWait();
            return;
        }

        // setting up confirmation dialog to make sure user wants to solve
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Show Grid Solutions");
        confirmationAlert.setHeaderText("You are about to see the solution(s) to this grid.");
        confirmationAlert.setContentText("Are you sure you want to see the solution(s)?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            // showing the solutions to the user
            SolutionWindow solutionWindow = new SolutionWindow(this);
            solutionWindow.showAndWait();

        }
    }

    /**
     * Undoes the last event that was completed by the user. Does this
     * by popping the first object off of the undo stack and running the
     * 'undo()' method on the corresponding CellValueEvent object. Will then 
     * add this object to the redo stack, so that the action can be redone.
     */
    public void undoLastValueEvent(){
        // deselecting currently selected cell (to look better)
        this.gameGrid.deselectSelectedCell();

        ValueEvent lastEvent = this.undoStack.pop(); // getting the most recent event
        lastEvent.undo(); // undoing this event
        this.redoStack.push(lastEvent); // adding this event to the redo stack

        // configuring undo/redobuttons
        if(this.undoStack.empty()){
            // disabling undo button if stack is now empty (nothing to undo)
            this.controlPanel.getToolbar().setUndoButtonDisable(true);
        }
        // will now be something to redo  (this undo), so can enable the redo
        this.controlPanel.getToolbar().setRedoButtonDisable(false);
    }

    /**
     * Redoes the last event that was undone by the user. Does this
     * by popping the first object off of the redo stack and running the
     * 'undo()' method on the corresponding CellValueEvent object. Will then
     * add this object to the undo stack, so that the action can be undone.
     */
    public void redoLastValueEvent(){
        // deselecting currently selected cell (to look better)
        this.gameGrid.deselectSelectedCell();
        
        ValueEvent lastEvent = this.redoStack.pop(); // getting the most recent event
        lastEvent.redo(); // redoing this event
        this.undoStack.push(lastEvent); // adding this event to the undo stack

        // configuring undo/redobutton based on the stack
        if(this.redoStack.empty()){
            // disabling redo button if stack is now empty (nothing to redo)
            this.controlPanel.getToolbar().setRedoButtonDisable(true);
        }
        // will now be something to undo (this redo), so can enable the undo
        this.controlPanel.getToolbar().setUndoButtonDisable(false);
        this.controlPanel.getToolbar().setClearCellButtonDisable(true);
    }
}