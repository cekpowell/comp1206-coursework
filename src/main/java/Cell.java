import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.MouseEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * Class represents a single cell within the grid
 */
public class Cell extends BorderPane{
    
    // defining the attributes that will exist within the cell
    private Label targetLabel;
    private Text value;
    private int solutionValue;
    private int possibleValue; // used in the generation of a cell
    private int primarySolutionValue; // the first solution for this cell
    private Grid grid;  // the grid innstance that this cell belongs to
    private int position; // sets the position of the cell relative to the grid
    private int[] insets; // represents the insets for the cell for styling
    private String defualtStyle;
    private String mistakeStyle;
    private String winningStyle;
    private boolean editable;
    private static String selectedStyle = "-fx-border-color: red;" +
                                          "-fx-border-style: solid;" +
                                          "-fx-border-width: 4px;" + 
                                          "-fx-background-color:black, cornsilk;";
    private String currentStyle;
    private Cage cage; //  the cage instance associated with this cell.
    private static final int[] valueFontSizes = {8, 6, 4};
    private static final int[] targetFontSizes = {16, 12, 8};


    /**
     * Constructor for the class.
     * @param grid The Grid object that this cell belongs to.
     * @param position The position of this cell within the grid.
     */
    public Cell(Grid grid, int position){
        // instantiating the components for this cell
        this.grid = grid;
        this.position = position;
        this.targetLabel = new Label();
        this.value = new Text("");
        this.possibleValue = 0;
        this.insets = new int[4];
        this.defualtStyle = "-fx-border-color: black;" +
                            "-fx-border-style: solid;" +
                            "-fx-border-width: 1px;" + 
                            "-fx-background-color:black, white;";
        this.mistakeStyle = "-fx-border-color: black;" +
                            "-fx-border-style: solid;" +
                            "-fx-border-width: 1px;" + 
                            "-fx-background-color:black, rgb(255, 144, 125);";
        this.winningStyle = "-fx-border-color: black;" +
                            "-fx-border-style: solid;" +
                            "-fx-border-width: 1px;" + 
                            "-fx-background-color:black, palegreen;";    
        this.currentStyle = defualtStyle;
        this.cage = null;
        this.makeDefaultStyle();
        this.editable = true;

        HBox valueHBox = new HBox(this.value);
        valueHBox.setAlignment(Pos.CENTER);
        HBox targetHBox = new HBox(this.targetLabel);
        targetHBox.setAlignment(Pos.CENTER);
    

        // adding the components to the cell
        this.setCenter(valueHBox);
        this.setTop(targetHBox);
        //this.getChildren().addAll(this.targetLabel, valueHBox);

        // formatting the text
        this.value.setTextAlignment(TextAlignment.CENTER);
        this.value.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        this.targetLabel.setTextAlignment(TextAlignment.LEFT);

        // formatting the cell
        //this.setAlignment(Pos.CENTER);
        this.setMinHeight(47);
        this.setMinWidth(47);

        // adding the event handler to this cell for when it is clicked
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.grid.setSelectedCell(this));

        valueHBox.setPadding(new Insets(10,0,20,0));
        targetHBox.setPadding(new Insets(5,0,0,0));
    }

    /**
     * Getter method for the grid that this cell belongs to.
     * @return The Grid object that this cell belongs to.
     */
    public Grid getGrid(){
        return this.grid;
    }

    /**
     * Getter for the value within this cell.
     * @return The Text object for this cell.
     */
    public String getValue(){
        return this.value.getText();
    }

    /**
     * Setter method for the value of the cell. This is the actual number within the cell.
     * @param value The value to be set into the cell in String form.
     */
    public void setValue(String value){
        this.value.setText(value); // updating the text object
    }

    /**
     * Setter method for the editable property of the cell.
     * @param editable Boolean representing if the cell can be edited or not.
     */
    public void setEditable(boolean editable){
        this.editable = editable;
    }

    /**
     * Changes the colour of the text of the cell to represrnt the cell
     * being part of a solution grid (when showing the solutions to the
     * user).
     */
    public void makeSolutionStyle(){
        this.value.setFill(Color.RED);
    }

    /**
     * Getter for the possible value within this cell.
     * @return The possible value for this cell
     */
    public int getPossibleValue(){
        return this.possibleValue;
    }

    /**
     * Setter method for the possiblevalue of the cell. This is the actual number within the cell.
     * @param value The value to be set into the cell in String form.
     */
    public void setPossibleValue(int possibleValue){
        this.possibleValue = possibleValue; // updating the text object
    }

    /**
     * Setter method for the solution value of the cell. This is used in grid generation
     * @param value The solution value for this cell.
     */
    public void setSolutionValue(int value){
        this.solutionValue = value;
    }

    /**
     * Getter method for the solution value for this cell.
     * @return The solution value for this cell as an int.
     */
    public int getSolutionValue(){
        return this.solutionValue;
    }

    /**
     * Setter method for the primary solution value of the cell. This is the first rcorrect number for the cell.
     * @param value The primary solution value for this cell.
     */
    public void setPrimarySolutionValue(int value){
        this.primarySolutionValue = value;
    }

    /**
     * Getter method for the primary solution value for this cell.
     * @return The primary solution value for this cell as an int.
     */
    public int getPrimarySolutionValue(){
        return this.primarySolutionValue;
    }

    /**
     * Sets the text of the target label.
     * @param target The text to be set to the target label.
     */
    public void setTargetLabel(String target){
        this.targetLabel.setText(target);
    }

    /**
     * Setter method for the cage associated with this cell.
     * @param cage The Cage that this cell belongs to.
     */
    public void setCage(Cage cage){
        this.cage = cage;
    }

    /**
     * Getter method for the Cage associated with this cell.
     * @return The Cage object associated with this cell.
     */
    public Cage getCage(){
        return this.cage;
    }

    /**
     * Gets rid of the value within the cell.
     */
    public void clear(){
        this.value.setText("");
    }

    /**
     * Returning the position of this cell relative to its grid.
     * @return int representing this cells posiiton in its grid.
     */
    public int getPosition(){
        return this.position;
    }

    /**
     * Reveals the value of the cell to the user
     */
    public void showHint(){
        this.grid.setSelectedCell(this);
        this.grid.enterValueIntoSelectedCell(String.valueOf(this.primarySolutionValue));
    }

    /**
     * Used to determine if the state of the cell is the primary solution value.
     * @return True if the cell has the primary solution value, false if not
     */
    public boolean hasPrimarySolutionValue(){
        return this.value.getText().equals(String.valueOf(this.primarySolutionValue));
    }

    /**
     * Setter method for the insets for the cell
     * @param insets The insets for this cell
     */
    public void setInsets(int[] insets){
        this.insets = insets;
        // setting the defualt style
        this.defualtStyle += "\n-fx-background-insets: 0," + this.insets[0] + " " 
                                                           + this.insets[1] + " " 
                                                           + this.insets[2] + " " 
                                                           + this.insets[3] + ";";
        // setting the style for this cell
        this.setStyle(this.defualtStyle);
        this.currentStyle = this.defualtStyle;

        // setting the mistake style
        this.mistakeStyle += "\n-fx-background-insets:0," + this.insets[0] + " " 
                                                          + this.insets[1] + " " 
                                                          + this.insets[2] + " " 
                                                          + this.insets[3] + ";";


        // setting the winning style for the cell
        this.winningStyle += "\n-fx-background-insets:0," + this.insets[0] + " " 
                                                          + this.insets[1] + " " 
                                                          + this.insets[2] + " " 
                                                          + this.insets[3] + ";";
    }

    /**
     * Changes the style of the cell to show there is a mistake with this cell. Will
     * use the colour string specified in the argument to set the colour of the cell.
     */
    public void makeMistakeStyle(String colour){
        this.setStyle("-fx-border-color: black;" +
                      "-fx-border-style: solid;" +
                      "-fx-border-width: 1px;" + 
                      colour +
                      "\n-fx-background-insets:0," + this.insets[0] + " " 
                                                   + this.insets[1] + " " 
                                                   + this.insets[2] + " " 
                                                   + this.insets[3] + ";");
    }

    /**
     * Changes the style of the cell so that it is selected.
     */
    public void makeSelectedStyle(){
        this.setStyle(selectedStyle);
    }

    /**
     * Reverts the cell back to its original style class.
     */
    public void makeDefaultStyle(){
        this.currentStyle = this.defualtStyle;
        this.setStyle(this.currentStyle);
    }

    /**
     * Makes the cell style change when the grid is complete.
     */
    public void makeWinningStyle(){
        this.currentStyle = this.winningStyle;
        this.setStyle(this.currentStyle);
    }

    /**
     * Changes the style of the cell to be the 
     */
    public void makeCurrentStyle(){
        this.setStyle(this.currentStyle);
    }

    /**
     * Removes the font bindings for the cell
     */
    public void removeFontSizeBindings(){
        this.value.styleProperty().unbind();
        this.targetLabel.styleProperty().unbind();
    }
    

    /**
     * Changes the font size for the cell (the value and the
     * target label).
     * @param fontSize Integer representing the font size to be changed
     * to.
     */
    public void setFontSize(int fontSize){
        // changing the font size for the value
        DoubleProperty valueFontSize = new SimpleDoubleProperty(10);
        valueFontSize.bind(this.widthProperty().add(this.heightProperty()).divide(valueFontSizes[fontSize]));
        this.value.styleProperty().bind(Bindings.concat("-fx-font-size: ", valueFontSize.asString(), ";"));

        // changing the font size for the target
        DoubleProperty targetFontSize = new SimpleDoubleProperty(10);
        targetFontSize.bind(this.widthProperty().add(this.heightProperty()).divide(targetFontSizes[fontSize]));
        this.targetLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", targetFontSize.asString(), ";"));
    }
}