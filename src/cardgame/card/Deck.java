package cardgame.card;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * A deck of cards.
 *
 * @param <T> the type of {@code Card}s that will define this {@code Deck}.
 */
public class Deck<T extends Card>
    implements Drawable<T>
{
    private final List<T>            definingCards_;
    private final ArrayDeque<T>      cardsInDeck_;
    private final List<DrawListener> listeners_;
    
    /**
     * Sole constructor. Initialises a {@code Deck} full of the specified
     * {@code Card}s.
     * 
     * @param cards the defining {@code Card}s for this {@code Deck}
     */
    public Deck(Collection<T> cards)
    {
        this.definingCards_ = new ArrayList<T>(cards);
        this.cardsInDeck_   = new ArrayDeque<T>(cards);
        this.listeners_     = new ArrayList<DrawListener>();
    }
    
    /* (non-Javadoc)
     * @see CardCollection#size()
     */
    @Override
    public int size()
    {
        return this.cardsInDeck_.size();
    }
    
    /**
     * Adds a specified {@code Card} to this {@code Deck}. Does not add it to
     * the list of defining {@code Card}s.
     * 
     * @see CardCollection#add(Card)
     */
    @Override
    public void add(T aCard)
    {
        this.cardsInDeck_.add(aCard);
    }
    
    /**
     * Removes a specified {@code Card} from this {@code Deck}. Does not remove
     * it from the list of defining {@code Card}s.
     * 
     * @see CardCollection#remove(Card)
     */
    @Override
    public boolean remove(T aCard)
    {
        boolean cardRemoved = this.cardsInDeck_.remove(aCard);
        if (cardRemoved)
            notifyListeners();
        return cardRemoved;
    }
    
    /* (non-Javadoc)
     * @see CardCollection#reset()
     */
    @Override
    public void reset()
    {
        this.cardsInDeck_.clear();
        this.cardsInDeck_.addAll(this.definingCards_);
    }
    
    /* (non-Javadoc)
     * @see Selectable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return "Deck";
    }
    
    /* (non-Javadoc)
     * @see Drawable#draw()
     */
    @Override
    public T draw()
        throws NoSuchElementException
    {
        T aCard;
        try {
            aCard = this.cardsInDeck_.pop();
            notifyListeners();
        }
        catch (NoSuchElementException exception) {
            throw new NoSuchElementException(this.getMessage() + " has no"
                                            + " cards left to draw!");
        }
        return aCard;
    }
    
    /* (non-Javadoc)
     * @see Drawable#addListener(DrawListener)
     */
    @Override
    public void addListener(DrawListener listener)
    {
        this.listeners_.add(listener);
    }

    /* (non-Javadoc)
     * @see Drawable#removeListener(DrawListener)
     */
    @Override
    public void removeListener(DrawListener listener)
    {
        this.listeners_.remove(listener);
    }
    
    /* (non-Javadoc)
     * @see Drawable#notifyListeners()
     */
    @Override
    public void notifyListeners()
    {
        for (DrawListener aListener : this.listeners_)
            aListener.onNotification();
    }
    
    /**
     * Shuffles the {@code Card}s in this {@code Deck}.
     */
    public void shuffle()
    {
        List<T> temp = new ArrayList<T>(this.cardsInDeck_);
        Random  rng  = new Random();
        Collections.shuffle(temp, rng);
        this.cardsInDeck_.clear();
        this.cardsInDeck_.addAll(temp);
    }
    
    /**
     * Adds a specified {@code Card} to the list of defining {@code Card}s of
     * this {@code Deck}. The {@code Card} is not added to the current set of
     * {@code Card}s in play.
     * 
     * @param aCard the {@code Card} to add
     */
    public void addDefiningCard(T aCard)
    {
        this.definingCards_.add(aCard);
    }
    
    /**
     * Removes a specified {@code Card} from the list of defining {@code Card}s
     * of this {@code Deck}. Also removes the {@code Card} from the set of
     * {@code Card}s currently in this {@code Deck} if a copy of the 
     * {@code Card} has not yet been drawn.
     * 
     * @param  aCard the {@code Card} to remove
     * @return {@code true} if the {@code Card} was removed
     */
    public boolean removeDefiningCard(T aCard)
    {
        int     definingCardCounter = cardCounter(this.definingCards_, aCard);
        int     cardsInDeckCounter  = cardCounter(this.cardsInDeck_,   aCard);
        boolean cardRemoved;
        if (definingCardCounter > cardsInDeckCounter) {
            cardRemoved = this.definingCards_.remove(aCard);
        }
        else {
            cardRemoved = this.definingCards_.remove(aCard)
                       && this.cardsInDeck_.remove(aCard);
            notifyListeners();
        }
        return cardRemoved;
    }
    
    // Counts the number of times the specified {@code Card} appears in the
    // specified {@code Collection}.
    private int cardCounter(Collection<T> aCollection, T aCard)
    {
        int counter = 0;
        for (T collectionCard : aCollection) {
            if (collectionCard.equals(aCard))
                counter++;
        }
        return counter;
    }
}
