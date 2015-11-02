package cardgame.card;

import java.util.NoSuchElementException;

import cardgame.player.Selectable;

/**
 * A collection of cards.
 * 
 * @param <T> the type of {@code Card}s this {@code CardCollection} will
 *            consist of
 */
public interface CardCollection <T extends Card>
    extends Selectable
{
    /**
     * Returns the number of {@code Card}s in this {@code CardCollection}.
     * 
     * @return the number of {@code Card}s
     */
    int size();
    
    /**
     * Transfers the specified {@code Card}s from this {@code CardCollection}
     * to another.
     * 
     * @param  destination            the {@code CardCollection} to deliver to
     * @param  cards                  the {@code Card}s to send
     * @throws NoSuchElementException if a specified {@code Card} does not
     *                                exist in this {@code CardCollection}
     */
    // Warning arises from the use the generic array {@param cards}. However, if
    // an object not of type T is present in the array, it could not be in this
    // CardCollection<T> and will throw an exception when it is called to be
    // removed in the following code.
    // (This is my understanding as of 30/10/2015)
    @SuppressWarnings("unchecked")
    default void transferTo(CardCollection<? super T> destination, T... cards)
        throws NoSuchElementException
    {
        for (T aCard : cards) {
            if(!this.remove(aCard))
                throw new NoSuchElementException(aCard + " does not exist in "
                                                 + this.getMessage());
            destination.add(aCard);
        }
    }
    
    /**
     * Adds a specified {@code Card} to this {@code CardCollection}.
     * 
     * @param aCard the {@code Card} to be added
     */
    void add(T aCard);
    
    /**
     * Attempts to remove a specified {@code Card} from this
     * {@code CardCollection}.
     * 
     * @param  aCard the {@code Card} to be removed
     * @return {@code true} if the {@code Card} was successfully removed from
     *         this {@code CardCollection}
     */
    boolean remove(T aCard);
    
    /**
     * Resets this {@code CardCollection} to its original state.
     */
    void reset();
}
