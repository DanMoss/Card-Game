package cardgame.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import cardgame.player.Selectable;

/**
 * A bank of cards.
 * 
 * @see Card
 * @see Drawable
 * @see Selectable
 */
public class Bank
    implements Drawable, Selectable
{
    private final String     name_;
    private final List<Card> cards_;
    
    /**
     * Sole constructor. The {@code Bank} is initially empty.
     * 
     * @param name the name of the {@code Bank}
     */
    public Bank(String name)
    {
        this.name_  = name;
        this.cards_ = new ArrayList<Card>();
    }
    
    /**
     * Returns the name of this {@code Bank}.
     */
    @Override
    public String toString()
    {
        return this.name_;
    }
    
    /**
     * Returns the {@code Card} located at {@code index}.
     * 
     * @param index the index of the {@code Card}
     * @return the {@code Card} located at {@code index}
     */
    public Card getCard(int index)
    {
        return this.cards_.get(index);
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
    public void add(Card aCard)
    {
        this.cards_.add(aCard);
    }
    
    /* (non-Javadoc)
     * @see CardCollection#reset()
     */
    @Override
    public void reset()
    {
        this.cards_.clear();
    }
    
    /**
     * Draws the first {@code Card} of this {@code Bank}.
     * 
     * @see Drawable#draw()
     */
    @Override
    public Card draw()
        throws NoSuchElementException
    {
        Card aCard;
        try {
            aCard = this.cards_.get(0);
            this.cards_.remove(aCard);
        }
        catch (Exception exception) {
            throw new NoSuchElementException("No cards left in the deck.");
        }
        return aCard;
    }
    
    /* (non-Javadoc)
     * @see cardgame.player.Selectable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return toString();
    }
    
    /**
     * Transfers {@code Card}s from this {@code Bank} to a
     * {@code CardCollection}.
     * 
     * @param destination the {@code CardCollection} to send the {@code Card}s
     *                    to
     * @param cards       the {@code Cards} to send
     */
    public void transferTo(CardCollection destination, Card... cards)
    {
        for (Card aCard : this.cards_)
            destination.add(aCard);
    }
    
    /**
     * Shuffles this {@code Bank}.
     * <p>
     * Creates a new random number generator using the standard library
     * {@code Random} class. This generator is then fed to the standard library
     * {@code Collections} class to shuffle the {@code Card}s in this
     * {@code Bank}.
     * 
     * @see java.util.Random#Random()
     * @see java.util.Collections#shuffle(List, Random)
     */
    public void shuffle()
    {
        Random rng = new Random();
        Collections.shuffle(this.cards_, rng);
    }
    
    /**
     * Discards a specified {@code Card} if it is contained in this
     * {@code Bank}.
     * 
     * @param  aCard the {@code Card} to attempt to discard
     * @return       {@code true} if the {@code Bank} contained {@code aCard}.
     */
    public boolean discard(Card aCard)
    {
        return this.cards_.remove(aCard);
    }
    
    /**
     * Converts this {@code Bank} into an array of {@code Card}s.
     * <p>
     * This is implemented through the use of {@code toArray} in {@code List}.
     * 
     * @return an array of the {@code Card}s in this {@code Bank}
     * @see    java.util.List#toArray(Object[])
     */
    public Card[] toArray()
    {
        Card[] cards = new Card[this.size()];
        return this.cards_.toArray(cards);
    }
    
    /**
     * Sorts this {@code Bank} according to the natural ordering on
     * {@code Card}.
     * 
     * @see java.util.Collections#sort(List)
     * @see Card#compareTo(Card)
     */
    public void sort()
    {
        Collections.sort(this.cards_);
    }
}
