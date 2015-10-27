package cardgame.card;

import cardgame.player.Selectable;

/**
 * A standard playing card.
 * 
 * @see Rank
 * @see Suit
 * @see java.lang.Comparable
 * @see cardgame.player.Selectable
 */
public class Card
    implements Comparable<Card>, Selectable
{
    private final Rank rank_;
    private final Suit suit_;
    
    /**
     * Sole constructor.
     * 
     * @param aRank the {@code Rank} this {@code Card} will have
     * @param aSuit the {@code Suit} this {@code Card} will have
     */
    public Card(Rank aRank, Suit aSuit)
    {
        this.rank_ = aRank;
        this.suit_ = aSuit;
    }
    
    /**
     * Returns the {@code Suit} of this {@code Card}.
     * 
     * @return the {@code Suit} of this {@code Card}
     */
    public Suit getSuit()
    {
        return suit_;
    }
    
    /**
     * Returns the {@code Rank} of this {@code Card}.
     * 
     * @return the {@code Rank} of this {@code Card}
     */
    public Rank getRank()
    {
        return rank_;
    }
    
    /**
     * Returns a string representation of this {@code Card}.
     *
     * @return a string representation of this {@code Card}
     */
    @Override
    public String toString()
    {
        return getRank() + " of " + getSuit();
    }
    
    /**
     * Compares this {@code Card} with {@code aCard} for ordering. Returns a
     * negative integer, zero, or a positive integer as this {@code Card} is
     * less than, equal to, or greater than {@code aCard}.
     * <p>
     * {@code Card}s are first compared by the natural ordering of their
     * {@code Suit}s, and then the natural ordering of their {@code Rank}s if
     * they have the same {@code Suit}. The implementation of this method
     * follows the contract set out in {@code Comparable}.
     * 
     * @param aCard the {@code Card} to be compared with
     * @see   Rank
     * @see   Suit
     * @see   java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Card aCard)
    {
        Integer thisSuit  = new Integer(getSuit().getValue());
        Integer otherSuit = new Integer(aCard.getSuit().getValue());
        int     result    = thisSuit.compareTo(otherSuit);
        
        if (result == 0) {
            Integer thisRank  = new Integer(getRank().getValue());
            Integer otherRank = new Integer(aCard.getRank().getValue());
            result            = thisRank.compareTo(otherRank);
        }
        
        return result;
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
     * Compares this {@code Card} with {@code anObject} for equality.
     * <p>
     * Two {@code Card}s are said to be equal if they have the same
     * {@code Rank} and {@code Suit}. The implementation of this method follows
     * the equivalence relation contract set out in {@code equals} in
     * {@code Object}.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object anObject)
    {
        if (anObject == this)
            return true;
        if (!(anObject instanceof Card))
            return false;
        
        Card    aCard    = (Card) anObject;
        boolean sameRank = getRank() == aCard.getRank();
        boolean sameSuit = getSuit() == aCard.getSuit();
        
        return sameRank && sameSuit;
    }
    
    /**
     * Returns a hash value for this {@code Card}.
     * <p>
     * This is a perfect minimal hashing. The implementation of this method
     * follows the contract set out in {@code hashCode} in {@code Object}.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int suitValue = getSuit().getValue();
        int rankValue = getRank().getValue();
        int nRanks    = Rank.values().length;
        int hash      = rankValue + nRanks * suitValue;
        return hash;
    }
}
