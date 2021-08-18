import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.control.ButtonBar;


/*
 * Window displayed to user when a game is won.
 */
public class WinningWindow extends Alert{

    // member vars
    private Game game;
    private Label congratulationsText;
    private Label windowText;

    // static vars
    private static final ButtonType finish = new ButtonType("Finish", ButtonBar.ButtonData.OK_DONE);

    /**
     * Constuctor for the class, 
     * @param game The game instance associated with this window.
     */
    public WinningWindow(Game game){
        super(AlertType.NONE, "", finish);
        this.game = game;

        // configuring the dialog pane
        this.setTitle("Congratulations");
        this.getDialogPane().setPrefSize(450, 400);

        // adding content to the dialog
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(40);

        this.congratulationsText = new Label("CONGRATULATIONS");
        this.congratulationsText.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
        HBox congratulationsHBox = new HBox(congratulationsText);
        congratulationsHBox.setMinHeight(80);
        congratulationsHBox.setAlignment(Pos.CENTER);

        this.windowText = new Label("Well Done, the grid was completed!\n\n" +
                                    "You can continue playing to find other solutions\n (if there are any)," + 
                                    " or you can load another game!");
        this.windowText.setFont(Font.font("Verdana", FontPosture.ITALIC, 15));
        this.windowText.setTextAlignment(TextAlignment.CENTER);


        container.getChildren().addAll(congratulationsHBox, this.windowText);
        this.getDialogPane().setContent(container);

        // playing the animation
        TextSizeTransition transition = new TextSizeTransition(this.congratulationsText, 
                                                               25, 
                                                               35, 
                                                               Duration.millis(350));

        transition.play();
    }
}