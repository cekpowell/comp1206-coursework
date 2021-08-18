import javafx.scene.control.Label;

/**
 * Class represents the label that is displayed within the game interface.
 */
public class GameMessage extends Label{

    public GameMessage(Game game, String message){
        super(message);
    }
}