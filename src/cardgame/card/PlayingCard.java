package cardgame.card;

/**
 * A standard playing card.
 * 
 * @see Rank
 * @see Suit
 */
public class PlayingCard extends Card
    implements Comparable<PlayingCard>
{
    private final Rank rank_;
    private final Suit suit_;
    
    /**
     * Sole constructor.
     * 
     * @param aRank the {@code Rank} this {@code PlayingCard} will have
     * @param aSuit the {@code Suit} this {@code PlayingCard} will have
     */
    public PlayingCard(Rank aRank, Suit aSuit)
    {
        this.rank_ = aRank;
        this.suit_ = aSuit;
    }
    
    /**
     * Returns the {@code Rank} of this {@code PlayingCard}.
     * 
     * @return the {@code Rank} of this {@code PlayingCard}
     */
    public Rank getRank()
    {
        return this.rank_;
    }
    
    /**
     * Returns the {@code Suit} of this {@code PlayingCard}.
     * 
     * @return the {@code Suit} of this {@code PlayingCard}
     */
    public Suit getSuit()
    {
        return this.suit_;
    }
    
    /**
     * Returns a string representation of this {@code PlayingCard}.
     *
     * @return the string representation of this {@code PlayingCard}
     */
    @Override
    public String toString()
    {
        return getRank() + " of " + getSuit();
    }
    
    /**
     * Compares this {@code PlayingCard} with {@code anObject} for equality.
     * <p>
     * Two {@code PlayingCard}s are said to be equal if they have the same
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
        if (!(anObject instanceof PlayingCard))
            return false;
        
        PlayingCard aCard    = (PlayingCard) anObject;
        boolean     sameRank = getRank() == aCard.getRank();
        boolean     sameSuit = getSuit() == aCard.getSuit();
        
        return sameRank && sameSuit;
    }
    
    /**
     * Returns a hash value for this {@code PlayingCard}.
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
    
    /**
     * Compares this {@code PlayingCard} with another specified
     * {@code PlayingCard} {@code aCard} for ordering. Returns a negative
     * integer, zero, or a positive integer as this {@code PlayingCard} is less
     * than, equal to, or greater the specified {@code PlayingCard}.
     * <p>
     * {@code PlayingCard}s are first compared by the natural ordering of their
     * {@code Suit}s, and then the natural ordering of their {@code Rank}s if
     * they have the same {@code Suit}. The implementation of this method
     * follows the contract set out in {@code Comparable}.
     * 
     * @param aCard the {@code PlayingCard} to be compared with
     * @see   Rank
     * @see   Suit
     * @see   java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(PlayingCard aCard)
    {
        int result = getSuit().compareTo(aCard.getSuit());
        if (result == 0)
            result = getRank().compareTo(aCard.getRank());
        return result;
    }
}
