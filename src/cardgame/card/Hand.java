package cardgame.card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A hand for holding cards.
 * 
 * @param <T> the type of {@code Card}s to hold.
 */
public class Hand<T extends Card>
    implements CardCollection<T>
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
}
