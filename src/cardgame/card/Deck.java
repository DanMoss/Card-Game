package cardgame.card;

import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A deck of cards.
 * 
 * @see Card
 * @see Drawable
 * @see DeckEventSource
 */
public class Deck
    implements Drawable, DeckEventSource
{
    private       EnumSet<Rank>           ranks_;
    private       EnumSet<Suit>           suits_;
    private final Deque<Card>             cards_;
    private final List<DeckEventListener> listeners_;
    
    /**
     * Sole constructor. Initially the {@code Deck} is generated as a
     * {@code Deck} of traditional playing {@code Card}s.
     */
    public Deck()
    {
        this.cards_     = new LinkedList<Card>();
        this.listeners_ = new ArrayList<DeckEventListener>();
        this.ranks_     = EnumSet.range(Rank.ACE, Rank.KING);
        this.suits_     = EnumSet.range(Suit.CLUBS, Suit.SPADES);
        this.reset();
    }
    
    /**
     * Sets the {@code Rank}s to be used by this {@code Deck}. This
     * {@code Deck} will then be reset.
     * 
     * @param ranks the {@code Rank}s to use in this {@code Deck}
     * @see   #reset()
     */
    public void setRanks(EnumSet<Rank> ranks)
    {
        this.ranks_ = ranks;
        this.reset();
    }
    
    /**
     * Sets the {@code Suit}s to be used by this {@code Deck}. This
     * {@code Deck} will then be reset.
     * this.
     * 
     * @param suits the {@code Suit}s to use in this {@code Deck}
     * @see   #reset()
     */
    public void setSuits(EnumSet<Suit> suits)
    {
        this.suits_ = suits;
        this.reset();
    }
    
    /* (non-Javadoc)
     * @see CardCollection#size()
     */
    public int size()
    {
        return this.cards_.size();
    }
    
    /* (non-Javadoc)
     * @see CardCollection#add(Card)
     */
    @Override
    public void add(Card aCard)
    {
        this.cards_.add(aCard);
    }
    
    
    /* (non-Javadoc)
     * @see CardCollection#remove(Card)
     */
    @Override
    public boolean remove(Card aCard)
    {
        return this.cards_.remove(aCard);
    }
    
    /**
     * Resets this {@code Deck} to its original state.
     * <p>
     * Draws all remaining {@code Card} in the {@code Deck} and refills it with
     * one of each {@code Card} that can be created from the {@code Rank}s and
     * {@code Suit}s. Notifies {@code DeckEventListener}s of the possible
     * change in size.
     * 
     * @see CardCollection#reset()
     * @see #notifyListeners()
     */
    @Override
    public void reset()
    {
        int nCards = cards_.size();
        for (int i = 0; i < nCards; i++)
            this.draw();
        
        for (Suit aSuit : suits_) {
            for (Rank aRank : ranks_)
                this.add(new Card(aRank, aSuit));
        }
        
        notifyListeners();
    }
    
    /**
     * Draws the top {@code Card} of this {@code Deck}. Notifies
     * {@code DeckEventListener}s if a {@code Card} is drawn.
     * 
     * @see Drawable#draw()
     * @see #notifyListeners()
     */
    public Card draw()
        throws NoSuchElementException
    {
        Card aCard;
        try {
            aCard = this.cards_.remove();
            notifyListeners();
        }
        catch (NoSuchElementException exception) {
            throw new NoSuchElementException("No cards left in the deck.");
        }
        return aCard;
    }

    /* (non-Javadoc)
     * @see DeckEventSource#addListener(DeckEventListener)
     */
    @Override
    public void addListener(DeckEventListener listener)
    {
        this.listeners_.add(listener);
    }

    /* (non-Javadoc)
     * @see DeckEventSource#removeListener(DeckEventListener)
     */
    @Override
    public void removeListener(DeckEventListener listener)
    {
        this.listeners_.remove(listener);
    }

    /** 
     * Notifies all {@code DeckEventListener}s whenever this {@code Deck} may
     * decrease in size.
     * 
     * @see DeckEventSource#notifyListeners()
     */
    @Override
    public void notifyListeners()
    {
        for (DeckEventListener aListener : listeners_)
            aListener.onNotification();
    }
}
