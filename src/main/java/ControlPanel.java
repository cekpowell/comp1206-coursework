import javafx.scene.layout.*;
import javafx.geometry.HPos;
import javafx.geometry.Pos;

/**
 * Represents the control panel for the application's game window. The control panel
 * contains the controls the user can interact with whilst playing the game. This includes the
 * number pad for the game and the toolbar. The control panel also contains a 
 */
public class ControlPanel extends GridPane{

    // definining the controls for the control panel
    private Game game; // reference to the game instance that this control panel is associated with
    private GameMessage gameMessage;
    private NumberPad numberPad;
    private Toolbar toolbar;

    /**
     * Constructor for the class. Initialises the components within the control panel.
     */
    public ControlPanel(Game game){
        // initialising the member variables
        this.game = game;

        // setting up the message to the user
        this.gameMessage = new GameMessage(this.game, "Configure a game in the settings!");

        // setting up the control panel 
        this.toolbar = new Toolbar(this.game); 

        // adding the bottom controls to the window and formatting
        this.setAlignment(Pos.CENTER);
        GridPane.setHalignment(this.gameMessage, HPos.CENTER); // makes message centered
        this.add(this.gameMessage, 1, 1, 1,  1); // adds message to top of grid
        this.add(this.toolbar, 1, 3, 1,  1); // adds toolbar to bottom (space for number pad in the middle)
        GridPane.setHgrow(this.toolbar, Priority.ALWAYS); // making the full toolbar be displayed
    }

    /**
     * Deletes the current number pad and makes a new one in its place.
     * @param dimension The dimension of the game instance the number pad is for.
     */
    public void newNumberPad(int dimension){
        // first, removing the current number pad if there is one
        if(this.numberPad != null){
            this.getChildren().remove(this.numberPad);
        }

        // making new number pad 
        this.numberPad = new NumberPad(this.game, dimension);
        this.add(this.numberPad,1,2,1,1);

        // updating the control message
        this.gameMessage.setText("You are currently playing a " + dimension + " x " + dimension + " mathdoku game!");
    }

    /**
     * Getter method for the toolbar object of the control panel.
     * @return
     */
    public Toolbar getToolbar(){
        return this.toolbar;
    }
}