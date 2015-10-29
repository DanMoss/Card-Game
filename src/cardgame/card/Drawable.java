package cardgame.card;

import java.util.NoSuchElementException;

import cardgame.player.Selectable;

/**
 * A collection of cards that can be drawn from.
 */
public interface Drawable extends CardCollection, Selectable
{
    /**
     * Draws a {@code Card} from this {@code Drawable}.
     * 
     * @return the drawn {@code Card}
     * @throws NoSuchElementException if this {@code Drawable} contains no
     *                                {@code Card}s
     */
    Card draw()
        throws NoSuchElementException;
    
    /**
     * Shuffles this {@code Drawable}.
     */
    void shuffle();
}
