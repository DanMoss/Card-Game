package cardgame.card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cardgame.player.PlayerIO;
import cardgame.player.Selector;

/**
 * A hand for holding cards.
 * 
 * @param <T> the type of {@code Card}s to hold.
 */
public class Hand<T extends Card>
    implements CardCollection<T>, Iterable<T>
{
    private final String  name_;
    private final List<T> cards_;
    
    /**
     * Sole constructor. The initialised {@code Hand} will be empty.
     * 
     * @param name the name of the {@code Hand}
     */
    public Hand(String name)
    {
        this.name_  = name;
        this.cards_ = new ArrayList<T>();
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
        this.cards_.add(aCard);
    }
    
    /* (non-Javadoc)
     * @see CardCollection#remove(Card)
     */
    @Override
    public boolean remove(T aCard)
    {
        return this.cards_.remove(aCard);
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
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator()
    {
        return this.cards_.iterator();
    }
    
    /**
     * Returns the {@code Card} at the specified index in this {@code Hand}.
     * 
     * @param  index the index of the {@code Card}
     * @return the requested {@code Card}
     * @see    List#get(int)
     */
    public T get(int index)
    {
        return this.cards_.get(index);
    }
    
    /**
     * Returns the name of this {@code Hand}.
     * 
     * @return the name of this {@code Hand}.
     */
    public String getName()
    {
        return this.name_;
    }
    
    /**
     * Sorts the {@code Card}s in this {@code Hand} according to the order
     * induced by the specified {@code Comparator}.
     * 
     * @param aComparator the {@code Comparator} used to compare list elements
     * @see   List#sort(Comparator)
     */
    public void sort(Comparator<? super T> aComparator)
    {
        this.cards_.sort(aComparator);
    }
    
    /**
     * Returns a list of a specified number of {@code Card}s from this
     * {@code Hand} selected by a specified {@code PlayerIO}. The returned list
     * will not contain more {@code Card}s than the number currently in this
     * {@code Hand}.
     * 
     * @param  aPlayerIO the {@code PlayerIO} that will pick the {@code Card}s
     * @param  nCards    the number of {@code Card}s to pick
     * @return a list of {@code Card}s from this {@code Hand}
     */
    public List<T> pickCards(PlayerIO aPlayerIO, int nCards)
    {
        int     handSize       = size();
        int     nCardsPickable = nCards < handSize ? nCards : handSize;
        List<T> selections     = new ArrayList<T>(nCardsPickable);
        List<T> handCopy       = new ArrayList<T>(this.cards_);
        String  message        = "Please select a card.";
        for (int i = 0; i < nCardsPickable; i++) {
            T aCard = Selector.select(aPlayerIO, message, handCopy);
            selections.add(aCard);
            handCopy.remove(aCard);
        }
        return selections;
    }
}
