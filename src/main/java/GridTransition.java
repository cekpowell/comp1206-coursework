import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Class handles the animation of the grid when the game is won.
 */
public class GridTransition extends Transition{

    private Grid grid; 
    private int start, end; // initial and final size of the text
    private boolean gettingBigger;
    private int difference;
    private int lastCellChanged;

    /**
     * Constructor for the class.
     * @param grid The grid that is going to be animated.
     * @param duration The duration of the animation.
     */
    public GridTransition(Grid grid, Duration duration) {
        this.grid = grid;
        this.setCycleDuration(duration);  
        this.setInterpolator(Interpolator.LINEAR); 
        this.lastCellChanged = 0;

        // making first cell change
        this.grid.getCellAtPosition(0).makeWinningStyle();
    }

    @Override
    protected void interpolate(double frac) {
        int nextCellToChange = (int) (frac * (this.grid.getCells().size() - 1));

        if(nextCellToChange < this.grid.getCells().size()){
            for(int cellNumber = this.lastCellChanged + 1; cellNumber <= nextCellToChange; cellNumber++){
                this.grid.getCells().get(cellNumber).makeWinningStyle();
            }
        }

        this.lastCellChanged = nextCellToChange;
    }
}