import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * COMP1206: Programming 2 - Coursework
 * 
 * Main class for the program. Runs the application.
 * 
 * @author Charles Powell
 */
public class Mathdoku extends Application {

    // defining properties of the application
    private static final String titleName = "mathdoku";
    private static final String authorName = "charles powell";
    private Game game;

    @Override
    public void start(Stage stage) {
        // setting up the game and showing start screen
        this.game = new Game();
        this.game.showStartScreen();

        // configuring scene with game
        Scene scene = new Scene(game, 800, 700);

        // adding event listener to scene to register keypress
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            // testing if it was a digit, but not 0
            if(event.getCode().isDigitKey() && !event.getCode().equals(KeyCode.DIGIT0)){
                try{
                    // making sure the digit is less than the dimension
                    if(Integer.parseInt(event.getText()) <= this.game.getGrid().getDimension()){
                        // passing the digit into the method to add it
                        this.game.getGrid().enterValueIntoSelectedCell(event.getText());
                    }
                }
                catch(Exception e){}
            }
            // removing the number in the cell if it was a backspace
            else if(event.getCode().equals(KeyCode.BACK_SPACE)){
                this.game.getGrid().clearSelectedCell();
            }

            // testing if it was arrow key
            else if(event.getCode().equals(KeyCode.UP)){
                // move selected cell up
                this.game.getGrid().moveSelectedCell(-this.game.getGrid().getDimension());
            }
            else if(event.getCode().equals(KeyCode.DOWN)){
                // move selected cell down
                this.game.getGrid().moveSelectedCell(this.game.getGrid().getDimension());
            }
            else if(event.getCode().equals(KeyCode.LEFT)){
                // move selected cell left
                this.game.getGrid().moveSelectedCell(-1);
            }
            else if(event.getCode().equals(KeyCode.RIGHT)){
                // move selected cell right
                this.game.getGrid().moveSelectedCell(+1);
            }
        });

        // setting up the stage using this scene
        stage.setTitle(titleName + " by " + authorName);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}