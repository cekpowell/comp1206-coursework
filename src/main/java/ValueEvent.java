
/**
 * Abstract class to represent a value changing within the grid.
 */
public abstract class ValueEvent{

    /**
     * Will undo the event that this object represents.
     */
    public abstract void undo();

    /**
     * Will redo the event that this object represents (after
     * it was undone).
     */
    public abstract void redo();
}