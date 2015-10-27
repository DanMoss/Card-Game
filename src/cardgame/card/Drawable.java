package cardgame.card;

import java.util.NoSuchElementException;

/**
 * A collection of cards that can be drawn from.
 */
public interface Drawable extends CardCollection
{
    /**
     * Draws a {@code Card} from the {@code Drawable}.
     * 
     * @return the drawn {@code Card}
     * @throws NoSuchElementException if this {@code Drawable} contains no
     *                                {@code Card}s
     */
    Card draw()
        throws NoSuchElementException;
}
