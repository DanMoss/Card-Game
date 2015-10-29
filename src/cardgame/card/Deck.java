package cardgame.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

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
    private       Set<Rank>               ranks_;
    private       Set<Suit>               suits_;
    private final List<Card>              cards_;
    private final List<DeckEventListener> listeners_;
    
    /**
     * Sole constructor. Initially the {@code Deck} is generated as a
     * {@code Deck} of traditional playing {@code Card}s.
     */
    public Deck()
    {
        this.cards_     = new ArrayList<Card>();
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
    public void setRanks(Set<Rank> ranks)
    {
        this.ranks_ = ranks;
        this.reset();
    }
    
    /**
     * Sets the {@code Suit}s to be used by this {@code Deck}. This
     * {@code Deck} will then be reset.
     * 
     * @param suits the {@code Suit}s to use in this {@code Deck}
     * @see   #reset()
     */
    public void setSuits(Set<Suit> suits)
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
        this.cards_.clear();
        for (Suit aSuit : suits_) {
            for (Rank aRank : ranks_)
                this.add(new Card(aRank, aSuit));
        }
        notifyListeners();
    }
    
    /* (non-Javadoc)
     * @see cardgame.player.Selectable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return "A deck of cards";
    }
    
    /**
     * Draws the top {@code Card} of this {@code Deck}. Notifies
     * {@code DeckEventListener}s if a {@code Card} is drawn.
     * 
     * @see Drawable#draw()
     * @see #notifyListeners()
     */
    @Override
    public Card draw()
        throws NoSuchElementException
    {
        Card aCard;
        try {
            aCard = this.cards_.remove(0);
            notifyListeners();
        }
        catch (IndexOutOfBoundsException exception) {
            throw new NoSuchElementException("No cards left in the deck.");
        }
        return aCard;
    }
    
    /**
     * Shuffles this {@code Deck}.
     * <p>
     * Creates a new random number generator using the standard library
     * {@code Random} class. This generator is then fed to the standard library
     * {@code Collections} class to shuffle the {@code Card}s in this
     * {@code Deck}.
     * 
     * @see Drawable#shuffle()
     * @see java.util.Random#Random()
     * @see java.util.Collections#shuffle(List, Random)
     */
    @Override
    public void shuffle()
    {
        Random rng = new Random();
        Collections.shuffle(this.cards_, rng);
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
