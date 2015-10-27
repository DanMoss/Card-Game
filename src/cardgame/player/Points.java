package cardgame.player;

/**
 * A point system.
 * 
 * @see Player
 */
public class Points
{
    private       int nPoints_;
    private final int minNumber_;
    
    /**
     * Sole constructor.
     * 
     * @param nPoints   the initial number of {@code Point}s owned
     * @param minNumber the minimum possible number of {@code Point}s owned
     */
    public Points(int nPoints, int minNumber)
    {
        this.minNumber_ = minNumber;
        setAmount(nPoints);
    }
    
    /**
     * Returns the number of {@code Point}s owned.
     * 
     * @return the number of {@code Point}s owned
     */
    public int getAmount()
    {
        return this.nPoints_;
    }
    
    /**
     * Modifies the number of {@code Point}s owned by incrementing it by
     * {@code increment}.
     * 
     * @param increment the number of {@code Point}s to add/subtract
     */
    public void modify(int increment)
    {
        int total = getAmount() + increment;
        setAmount(total);
    }
    
    // Sets {@code nPoints_} equal to the minimum of {@code nPoints} and
    // {@code minNumber_}.
    private void setAmount(int nPoints)
    {  
        this.nPoints_ = nPoints < this.minNumber_ ? this.minNumber_ : nPoints;
    }
}
