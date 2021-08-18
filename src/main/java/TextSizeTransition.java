import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class TextSizeTransition extends Transition{

    private Labeled UIcontrol; // a little generic -> subclasses: ButtonBase, Cell, Label, TitledPane
    private int start, end; // initial and final size of the text
    private boolean gettingBigger;
    private int difference;

    public TextSizeTransition(Labeled UIcontrol, int start, int end, Duration duration) {
        this.UIcontrol = UIcontrol;
        this.start = start;
        this.end = end - start; // minus start because of (end * frac) + start in interpolate() 
        this.difference = end - this.start;
        this.gettingBigger = true;
        setCycleDuration(duration);  
        setInterpolator(Interpolator.LINEAR); 
        setCycleCount(Animation.INDEFINITE);
        this.setAutoReverse(true);
        // and a lot of other methods 
    }

    @Override
    protected void interpolate(double frac) {
        // frac value goes from 0 to 1
        // when frac is zero -> size is start
        // when frac is 1 -> size is end + start 
        //(that's why we this.end = end - start; above to back to original end value)
        int sizeChange = (int) (frac * this.difference);
        UIcontrol.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, start + sizeChange));
    }
}