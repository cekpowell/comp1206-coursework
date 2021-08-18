import java.io.File;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

/**
 * Class represents the settings that the user can configure within the game.
 */
public class Settings extends Alert{
    // defining member variables
    private Game game; // game associated with this settings instance
    private String errors; // errors that occur whilst changing settings
    private static final ButtonType applyChanges = new ButtonType("Apply Changes", ButtonBar.ButtonData.OK_DONE);
    private static final ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    private static final int minimumGridSize = 2;
    private static final int maximumGridSize = 8;
    private static final int initialGridSize = 5;
    private TabPane settingsPane;

    // loading method controls
    private ToggleGroup loadingMethod;
    private RadioButton previouslySelectedLoadingMethod;
    private RadioButton loadRandomGameButton;
    private Slider gridDimensionSlider;
    private ToggleGroup difficulty;
    private RadioButton easyDifficulty;
    private RadioButton normalDifficulty;
    private RadioButton difficultDifficulty;
    private RadioButton loadGameFromFileButton;
    private Button selectFileButton;
    private RadioButton loadGameFromTextButton;
    private TextArea configurationTextArea;
    private File configurationFile;
    private Label selectedConfigurationFileLabel;

    // font size controls
    private ToggleGroup fontSize;
    private RadioButton smallFont;
    private RadioButton mediumFont;
    private RadioButton largeFont;

    
    /**
     * Constructor for the class. Calls the super constructor.
     */
    public Settings(Game game){
        // setting up the needed alert window
        super(AlertType.CONFIRMATION,"", applyChanges, cancel);
        this.game = game;
        this.errors = "";
        this.previouslySelectedLoadingMethod = null;

        // configuring the dialog pane
        this.setTitle("Settings");
        this.setHeaderText("Configure the Settings");
        this.getDialogPane().setPrefSize(600, 500);

        // setting up the tab pane
        this.settingsPane = new TabPane();
        Tab appearanceTab = new Tab("Appearance");
        appearanceTab.setClosable(false);
        Tab newGameTab = new Tab("New Game");
        newGameTab.setClosable(false);

        this.settingsPane.getTabs().add(newGameTab);
        this.settingsPane.getTabs().add(appearanceTab);

        //////////////////
        // NEW GAME TAB //
        //////////////////
        this.loadingMethod = new ToggleGroup(); // stores which loading method is selected

        // CREATE A RANDOM GAME //

        // radio button to select this method
        this.loadRandomGameButton = new RadioButton("Create a Random Game");
        this.loadRandomGameButton.setToggleGroup(loadingMethod);
        this.loadRandomGameButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        this.loadRandomGameButton.setPadding(new Insets(20,0,0,0));
        this.loadRandomGameButton.setOnMouseClicked(event -> this.newRandomGameButtonPressed());

        // controls to select the grid dimension
        Label loadRandomGameLabel = new Label("\tPlease select the grid dimension:"); // informative label
        loadRandomGameLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        this.gridDimensionSlider = new Slider(minimumGridSize, maximumGridSize, initialGridSize); // slider to configure the grid dimension
        // configuring the slider
        this.gridDimensionSlider.setMajorTickUnit(1);
        this.gridDimensionSlider.setMinorTickCount(0);
        this.gridDimensionSlider.setSnapToTicks(true);
        this.gridDimensionSlider.setShowTickLabels(true);
        this.gridDimensionSlider.setShowTickMarks(true);
        this.gridDimensionSlider.setMaxWidth(300);
        this.gridDimensionSlider.setDisable(true);
        HBox.setHgrow(this.gridDimensionSlider, Priority.ALWAYS);
        // adding the slider to a Hbox
        HBox gridDimensionSliderContainer = new HBox(this.gridDimensionSlider);
        gridDimensionSliderContainer.setAlignment(Pos.CENTER);

        // controls to chose the grid difficulty
        Label difficultyLabel = new Label("\tPlease select the difficulty level:"); // informative label
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        this.difficulty = new ToggleGroup();
        this.easyDifficulty = new RadioButton("Easy");
        this.easyDifficulty.setToggleGroup(this.difficulty);
        this.easyDifficulty.setDisable(true);
        this.normalDifficulty = new RadioButton("Normal");
        this.normalDifficulty.setToggleGroup(this.difficulty);
        this.normalDifficulty.setDisable(true);
        this.difficultDifficulty = new RadioButton("Difficult");
        this.difficultDifficulty.setToggleGroup(this.difficulty);
        this.difficultDifficulty.setDisable(true);
        this.difficulty.selectToggle(this.normalDifficulty);

        // adding the radiobuttons to a HBOX
        HBox difficultyContainer = new HBox();
        difficultyContainer.getChildren().addAll(this.easyDifficulty,
                                                 this.normalDifficulty,
                                                 this.difficultDifficulty);
        difficultyContainer.setAlignment(Pos.CENTER);
        difficultyContainer.setSpacing(20);

        // container to hold the random game components
        VBox loadRandomGameContainer = new VBox();
        loadRandomGameContainer.getChildren().addAll(this.loadRandomGameButton, 
                                                     loadRandomGameLabel, 
                                                     gridDimensionSliderContainer,
                                                     difficultyLabel,
                                                     difficultyContainer);
        loadRandomGameContainer.setAlignment(Pos.CENTER_LEFT);
        loadRandomGameContainer.setSpacing(20);

        // LOAD A GAME FROM A FILE //

        // radio button to select this method
        this.loadGameFromFileButton = new RadioButton("Load a Game From a Configuration File"); 
        this.loadGameFromFileButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        this.loadGameFromFileButton.setToggleGroup(this.loadingMethod);
        this.loadGameFromFileButton.setOnMouseClicked(event -> this.newGameFromFileButtonPressed());

        // controls to select the file
        Label loadGameFromFileLabel = new Label("\tPlease select the file:"); // informative label
        loadGameFromFileLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        this.selectFileButton = new Button("Select File"); // button to select a file
        this.selectFileButton.setOnAction(event->this.openFileChooser());
        this.selectFileButton.setDisable(true); // disabling the select a file button
        this.selectedConfigurationFileLabel = new Label(); // label indicating the currently selected file
        this.selectedConfigurationFileLabel.setText(""); // resetting the text
        // h box to hold these components
        HBox loadGameFromFileHBox = new HBox();
        loadGameFromFileHBox.getChildren().addAll(loadGameFromFileLabel, 
                                                  this.selectFileButton, 
                                                  this.selectedConfigurationFileLabel);
        loadGameFromFileHBox.setAlignment(Pos.CENTER_LEFT);
        loadGameFromFileHBox.setSpacing(20);

        // container for the load from file components
        VBox loadGameFromFileContainer = new VBox();
        loadGameFromFileContainer.getChildren().addAll(this.loadGameFromFileButton, loadGameFromFileHBox);
        loadGameFromFileContainer.setAlignment(Pos.CENTER_LEFT);
        loadGameFromFileContainer.setSpacing(20);

        // LOAD A GAME FROM TEXT //
    
        // radio button to select this method
        this.loadGameFromTextButton = new RadioButton("Load a Game From Text");
        this.loadGameFromTextButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        this.loadGameFromTextButton.setToggleGroup(this.loadingMethod);
        this.loadGameFromTextButton.setOnMouseClicked(event -> this.newGameFromTextButtonPressed());


        // controls to enter text
        Label loadGameFromTextLabel = new Label("\tPlease enter the configuration:"); // informative label
        loadGameFromTextLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        this.configurationTextArea = new TextArea(); // the text area for the user to enter the configuration
        this.configurationTextArea.setDisable(true);
        this.configurationTextArea.setMaxWidth(Double.MAX_VALUE);
        this.configurationTextArea.setPrefHeight(220);

        // container for the load from text controls
        VBox loadGameFromTextContainer = new VBox();
        loadGameFromFileContainer.getChildren().addAll(this.loadGameFromTextButton, 
                                                       loadGameFromTextLabel, 
                                                       this.configurationTextArea);
        loadGameFromFileContainer.setAlignment(Pos.CENTER_LEFT);
        loadGameFromFileContainer.setSpacing(20);

        // ADDING NEW GAME CONTROLS TO TO A CONTAINER AND SCROLL PANE //

        // addign to container
        VBox loadNewGameContainer = new VBox(); // container for the items
        loadNewGameContainer.getChildren().addAll(loadRandomGameContainer,
                                                  loadGameFromFileContainer,
                                                  loadGameFromTextContainer);
        loadNewGameContainer.setSpacing(20);
        // adding to scroll pane
        ScrollPane loadNewGameScrollPane = new ScrollPane();
        loadNewGameScrollPane.setContent(loadNewGameContainer);
        // adding scroll pane to the new game tab
        newGameTab.setContent(loadNewGameScrollPane);

        ////////////////////
        // APPEARANCE TAB //
        ////////////////////
        
        // FONT SIZE //

        // informative label
        Label changingFontSizeLabel = new Label("Font Size"); // informative label
        changingFontSizeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        changingFontSizeLabel.setPadding(new Insets(20,0,0,0));
        
        // setting up the radio buttons
        this.fontSize = new ToggleGroup();
        this.smallFont = new RadioButton("Small");
        this.smallFont.setToggleGroup(this.fontSize);
        this.mediumFont = new RadioButton("Medium");
        this.mediumFont.setToggleGroup(this.fontSize);
        this.largeFont = new RadioButton("Large");
        this.largeFont.setToggleGroup(this.fontSize);
        this.fontSize.selectToggle(this.mediumFont);

        // adding the radio buttons to a hBox
        HBox fontButtonsContainer = new HBox();
        fontButtonsContainer.getChildren().addAll(this.smallFont, this.mediumFont, this.largeFont);
        fontButtonsContainer.setAlignment(Pos.CENTER);
        fontButtonsContainer.setSpacing(20);

        // ADDING ALL APPEARANCE CONTROLS TO THE CONTAINER //
        
        // adding to container
        VBox appearanceContainer = new VBox(); // container for the items
        appearanceContainer.getChildren().addAll(changingFontSizeLabel, fontButtonsContainer);
        appearanceContainer.setSpacing(20);
        // adding to scroll pane
        ScrollPane appearanceScrollPane = new ScrollPane();
        appearanceScrollPane.setContent(appearanceContainer);
        // adding scroll pane to tab
        appearanceTab.setContent(appearanceScrollPane);


        // adding the tab pane to the dialog window
        this.getDialogPane().setContent(this.settingsPane);
    }

    /**
     * Configures the settings window so that only the user can 
     * select the grid dimension for a new random game.
     */
    private void newRandomGameButtonPressed(){
        // testing if this method was already selected
        if(this.previouslySelectedLoadingMethod != null){
            if(this.previouslySelectedLoadingMethod == this.loadRandomGameButton){
                // if it is, deselecting this toggle
                this.loadingMethod.selectToggle(null);
                this.previouslySelectedLoadingMethod = null;
                this.gridDimensionSlider.setDisable(true);
                this.easyDifficulty.setDisable(true);
                this.normalDifficulty.setDisable(true);
                this.difficultDifficulty.setDisable(true);
                return;
            }
        }
        this.previouslySelectedLoadingMethod = this.loadRandomGameButton;

        // disabling the use of the other methods
        this.configurationTextArea.setText("");
        this.configurationTextArea.setDisable(true);
        this.selectFileButton.setDisable(true); 
        this.selectedConfigurationFileLabel.setText("");
        this.configurationFile = null;

        // enabling the use of this method
        this.gridDimensionSlider.setDisable(false);
        this.easyDifficulty.setDisable(false);
        this.normalDifficulty.setDisable(false);
        this.difficultDifficulty.setDisable(false);

    }

    /**
     * Configures the settings window so that the user can select a 
     * configuration file for a game.
     */
    private void newGameFromFileButtonPressed(){
        // testing if this method was already selected
        if(this.previouslySelectedLoadingMethod != null){
            if(this.previouslySelectedLoadingMethod == this.loadGameFromFileButton){
                // if it is, deselecting this toggle
                this.loadingMethod.selectToggle(null);
                this.previouslySelectedLoadingMethod = null;
                this.selectFileButton.setDisable(true);
                this.selectedConfigurationFileLabel.setText("");
                this.configurationFile = null;
                return;
            }
        }

        this.previouslySelectedLoadingMethod = this.loadGameFromFileButton;

        // disabling the use of the other methods
        this.configurationTextArea.setText("");
        this.configurationTextArea.setDisable(true);
        this.gridDimensionSlider.setDisable(true);
        this.easyDifficulty.setDisable(true);
        this.normalDifficulty.setDisable(true);
        this.difficultDifficulty.setDisable(true);

        // enabling the ability to select a file
        this.selectFileButton.setDisable(false); 
    }

    /**
     * Configures the settings window so that the user can enter text representing
     * a grid configuration.
     */
    private void newGameFromTextButtonPressed(){
        // testing if this method was already selected
        if(this.previouslySelectedLoadingMethod != null){
            if(this.previouslySelectedLoadingMethod == this.loadGameFromTextButton){
                // if it is, deselecting this toggle
                this.loadingMethod.selectToggle(null);
                this.previouslySelectedLoadingMethod = null;
                this.configurationTextArea.setText("");
                this.configurationTextArea.setDisable(true);
                return;
            }
        }

        this.previouslySelectedLoadingMethod = this.loadGameFromTextButton;

        // disbaling the use of the other methods
        this.selectFileButton.setDisable(true);
        this.selectedConfigurationFileLabel.setText("");
        this.gridDimensionSlider.setDisable(true);
        this.configurationFile = null;
        this.easyDifficulty.setDisable(true);
        this.normalDifficulty.setDisable(true);
        this.difficultDifficulty.setDisable(true);

        // enabling the ability to enter text
        this.configurationTextArea.setDisable(false);
    }

    /**
     * Opens a the settings dialog for the user. Will update the changes that need to be made
     * if the user selects apply.
     */
    public void showSettingsDialog(){
        // resetting the screen
        this.errors = "";
        this.loadingMethod.selectToggle(null);
        this.selectFileButton.setDisable(true);
        this.selectedConfigurationFileLabel.setText("");
        this.gridDimensionSlider.setDisable(true);
        this.configurationFile = null;
        this.configurationTextArea.setText("");
        this.configurationTextArea.setDisable(true);
        this.previouslySelectedLoadingMethod = null;
        this.difficulty.selectToggle(this.normalDifficulty);
        this.easyDifficulty.setDisable(true);
        this.normalDifficulty.setDisable(true);
        this.difficultDifficulty.setDisable(true);
        SingleSelectionModel<Tab> selectionModel = this.settingsPane.getSelectionModel();
        selectionModel.select(0); // selecting the first tab in the settings pane

        // showing the dialog pane
        Optional<ButtonType> result = this.showAndWait();

        // testing if the apply changes button was pressed
        if(result.get() == applyChanges){
            this.applyChanges(); // applying the changes
        }
    }

    /**
     * Opens a new file chooser for the user to select a configuration file.
     */
    private void openFileChooser(){
        // setting up the file chooser
        FileChooser fileChooser = new FileChooser();
        //fileChooser.setInitialDirectory();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // getting the configuration file and the reader object
        this.configurationFile = fileChooser.showOpenDialog(this.getOwner());

        // updating the selected file label accordingly based on this file choice
        if(this.configurationFile!=null){
            this.selectedConfigurationFileLabel.setText(this.configurationFile.getName());
        }
    }

    /**
     * Applies the new settings configuration that the user has selected within the
     * settings window.
     */
    private void applyChanges(){
        /**
         * LOAD NEW GAME
         *      - Check which method was selected in toggle group
         *      - if none was selected, then do nothing
         *      - if it was load random game:
         *          - user has to have chosen grid size (as there is defualt),
         *            so load new random game with this grid size
         *      - if it was load by file:
         *          - check file is not null -> pass it onto loader if it isnt
         *          - if no file was chosen, then display error message
         *      - if it it was load from text:
         *          - check that text is not null -> pass text onto loader
         *          - if no text was entered -> display error message 
         * APPEARANCE:
         *      - check if user selected a different font option to the current one
         *          - update the font if they have
         */

        //////////////////////
        // LOADING NEW GAME //
        //////////////////////

        // testing if the user selected a loading method
        Toggle selectedLoadingMethod = this.loadingMethod.getSelectedToggle();

        if(selectedLoadingMethod != null){
            // testing what sort of game needs to be loaded (if any)
            if(selectedLoadingMethod.equals(this.loadRandomGameButton)){
                // random game needs to be generated
                int dimension = (int)this.gridDimensionSlider.getValue();

                // getting the difficulty level
                if(this.difficulty.getSelectedToggle() == this.easyDifficulty){
                    // EASY
                    // making a new game of this dimension
                    this.game.newRandomGrid(dimension, 1);        
                }
                else if(this.difficulty.getSelectedToggle() == this.difficultDifficulty){
                    // HARD

                    // making a new game of this dimension
                    this.game.newRandomGrid(dimension, 3);        
                }
                else{
                    // MEDIUM

                    // making a new game of this dimension
                    this.game.newRandomGrid(dimension, 2);
                }
            }
            else if(selectedLoadingMethod.equals(this.loadGameFromFileButton)){
                // game needs to be loaded from file

                // testing if the selected file is null
                if(this.configurationFile != null){
                    // file was chosen -> passing the file onto the grid loader
                    this.game.newGridFromConfigurationFile(this.configurationFile);
                }
                else{
                    // file was not chosen -> adding error message
                    this.errors += ("You selected to load a game from a file, but no file was chosen. " + 
                                    "No game was loaded as a result.\n\n");
                }
            }
            else if(selectedLoadingMethod.equals(this.loadGameFromTextButton)){
                // game needs to be loaded from text

                // testing if the configuring string is null
                if(!this.configurationTextArea.getText().equals("")){
                    // text was given -> passing the text onto the grid loader
                    this.game.newGridFromConfigurationText(this.configurationTextArea.getText());
                }
                else{
                    // no text was given -> adding error message
                    this.errors += ("You selected to load a game from a text, but no text was given. " + 
                                    "No game was loaded as a result.\n\n");
                }
            }
        }
    
        /////////////////////////////
        // UPDATING THE APPEARANCE //
        /////////////////////////////

        // dont update appearance if game doesnt have grid
        if(this.game.getGrid() == null){
            // displaying the error messages to the user if errors occcured
            if(!this.errors.equals("")){
                this.displayErrors();
            }

            return;
        }

        // changing the font size

        Toggle selectedFontSize = this.fontSize.getSelectedToggle();
        if(selectedFontSize == this.smallFont){
            // change the font size to small
            this.game.getGrid().setFontSize(0);
        }
        else if(selectedFontSize == this.mediumFont){
            // change the font size to medium
            this.game.getGrid().setFontSize(1);
        }
        else if(selectedFontSize == this.largeFont){
            // change the font size to large
            this.game.getGrid().setFontSize(2);
        }

        // displaying the error messages to the user if errors occcured
        if(!this.errors.equals("")){
            this.displayErrors();
        }
    }

    /**
     * Displays the errors from this settings configuration to the user in the form of a
     * pop up window.
     */
    private void displayErrors(){
        // displaying an error message if the file was of the incorrect format
        Alert invalidFormatAlert = new Alert(AlertType.ERROR,
                                            "Error(s) occured when configuring these settings.\n\n" +
                                            "Cause:\n\n" + this.errors);
        invalidFormatAlert.setHeaderText("Error with Settings");
        invalidFormatAlert.showAndWait();
    }
}