package cardgame.card;

import cardgame.player.Selectable;

/**
 * A card.
 */
public abstract class Card
    implements Selectable
{
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public abstract boolean equals(Object anObject);
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public abstract int hashCode();
    
    /* (non-Javadoc)
     * @see Selectable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return this.toString();
    }
}
