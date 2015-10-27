package cardgame.card;

/**
 * A collection of cards.
 */
public interface CardCollection
{
    /**
     * Returns the number of {@code Card}s in this collection.
     * 
     * @return the size of this collection
     */
    int size();
    
    /**
     * Adds a {@code Card} to this collection.
     * 
     * @param aCard the {@code Card} to be added
     */
    void add(Card aCard);
    
    /**
     * Resets this collection to its original state.
     */
    void reset();
}
