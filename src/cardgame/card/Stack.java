package cardgame.card;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A last-in-first-out stack of cards.
 * 
 * @param <T> the type of {@code Card}s that will make up this {@code Stack}
 */
public class Stack<T extends Card>
    implements Drawable<T>
{
    private final String             name_;
    private final Deque<T>           cards_;
    private final List<DrawListener> listeners_;
    
    /**
     * Sole constructor. The initialised {@code Stack} will be empty.
     * 
     * @param name the name this {@code Stack} will have
     */
    public Stack(String name)
    {
        this.name_      = name;
        this.cards_     = new ArrayDeque<T>();
        this.listeners_ = new ArrayList<DrawListener>();
    }
    
    /* (non-Javadoc)
     * @see CardCollection#size()
     */
    @Override
    public int size()
    {
        return this.cards_.size();
    }
    
    /* (non-Javadoc)
     * @see CardCollection#add(Card)
     */
    @Override
    public void add(T aCard)
    {
        this.cards_.addFirst(aCard);
    }
    
    /* (non-Javadoc)
     * @see CardCollection#remove(Card)
     */
    @Override
    public boolean remove(T aCard)
    {
        boolean cardRemoved = this.cards_.remove(aCard);
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
        this.cards_.clear();
    }
    
    /* (non-Javadoc)
     * @see Selectable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return this.name_;
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
            aCard = this.cards_.removeFirst();
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
     * Returns, but does not remove, the first {@code Card} of this
     * {@code Stack}, or {@code null} if empty.
     * 
     * @return the head of this {@code Stack}, or {@code null} if empty
     */
    public T peek()
    {
        return this.cards_.peekFirst();
    }
}
