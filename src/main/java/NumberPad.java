import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Represents the number pad that the user use in order
 * to select cell values.
 */
public class NumberPad extends FlowPane{

    int dimension; // defines the 
    Game game; // represents the game instance associated with this number pad
    ArrayList<Button> buttons; // stores the buttons within this number pad

    /**
     * Constructor for the class. Links the NumberPad instance to its corresponding
     * game instance.
     * @param game The game instance that this number pad is linked to.
     * @param dimension The dimension of the game this number pad is linked to.
     */
    public NumberPad(Game game, int dimension){
        // initializing the member variable
        this.game = game;
        this.dimension = dimension;
        this.buttons = new ArrayList<Button>();

        // adding buttons to this number pad based on the dimension
        for(int i=1; i <=dimension; i++){
            Button button = new Button(String.valueOf(i)); // creating new button
            this.getChildren().add(button); // adding this button to the number pad
            this.buttons.add(button); // adding this button to the array list of buttons

            // formatting the button
            button.setPrefHeight(40);
            button.setPrefWidth(40);

            // adding the event handler for this button
            button.setOnAction(event -> {
                // calling the method to insert this number into the selected cell
                this.game.getGrid().enterValueIntoSelectedCell(button.getText());
            });
        }

        // formatting the number pad
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setPadding(new Insets(10,10,10,10));
    }
}