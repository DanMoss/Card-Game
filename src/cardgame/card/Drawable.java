package cardgame.card;

import java.util.NoSuchElementException;

/**
 * A collection of cards that can be drawn from.
 * 
 * @param <T> the type of {@code Card}s this {@code Deck} will consist of
 */
public interface Drawable<T extends Card>
    extends CardCollection<T>
{
    /**
     * Draws a {@code Card} from this {@code Drawable}.
     * 
     * @return the drawn {@code Card}
     * @throws NoSuchElementException if this {@code Drawable} contains no
     *                                {@code Card}s
     */
    T draw()
        throws NoSuchElementException;
    
    /**
     * Adds a specified {@code DrawListener} to the list of listeners.
     * 
     * @param listener the {@code DrawListener} to add
     */
    void addListener(DrawListener listener);
    
    /**
     * Removes a specified {@code DrawListener} from the list of listeners.
     * 
     * @param listener the {@code DrawListener} to remove
     */
    void removeListener(DrawListener listener);
    
    /**
     * Notifies all {@code DrawListener}s of a draw.
     */
    void notifyListeners();
}
