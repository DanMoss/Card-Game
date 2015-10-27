package cardgame.player;

/**
 * A player input/output system.
 * 
 * @see Player
 */
public abstract class PlayerIO
{
    /**
     * Sends a string through to this {@code PlayerIO}
     * 
     * @param message the string to send to the {@code Player}
     */
    public abstract void sendMessage(String message);
    
    /**
     * Accepts an integer input from the {@code PlayerIO}.
     * 
     * @param  lowerBound the inclusive lower bound for the range of integers
     * @param  upperBound the exclusive upper bound for the range of integers
     * @return the chosen integer
     */
    public abstract int chooseInt(int lowerBound, int upperBound);
}
