package cardgame.card;

/**
 * A collection of cards.
 */
public interface CardCollection
{
    /**
     * Returns the number of {@code Card}s in this {@code CardCollection}.
     * 
     * @return the size of this {@code CardCollection}
     */
    int size();
    
    /**
     * Transfers {@code Card}s from this {@code Bank} to a
     * {@code CardCollection}.
     * 
     * @param destination the {@code CardCollection} to send the {@code Card}s
     *                    to
     * @param cards       the {@code Cards} to send
     */
    public default void transferTo(CardCollection destination, Card... cards)
    {
        for (Card aCard : cards) {
            destination.add(aCard);
            this.remove(aCard);
        }
    }
    
    /**
     * Adds a {@code Card} to this {@code CardCollection}.
     * 
     * @param aCard the {@code Card} to be added
     */
    void add(Card aCard);
    
    /**
     * Attempts to remove a specified {@code Card} from this
     * {@code CardCollection}.
     * 
     * @param  aCard the {@code Card} to be removed
     * @return {@code true} if the {@code Card} was in this 
     *         {@code CardCollection} and removed
     */
    boolean remove(Card aCard);
    
    /**
     * Resets this {@code CardCollection} to its original state.
     */
    void reset();
}
