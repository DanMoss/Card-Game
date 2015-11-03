package cardgame.card.traditional;

import java.util.Comparator;

import cardgame.card.Card;

/**
 * A standard playing card.
 * 
 * @see Rank
 * @see Suit
 */
public class PlayingCard extends Card
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
     * The possible comparators for ordering playing cards.
     */
    public static class Comparators
    {
        /**
         * A {@code Comparator} which orders {@code PlayingCard}s first by
         * their {@code Suit}s, Clubs < Spades < Jokers, and then by their
         * {@code Rank}s, Aces < Kings < Jokers.
         * 
         * @see Comparator
         */
        public static Comparator<PlayingCard> SUIT_FIRST = new Comparator<
                                                               PlayingCard>() {
            @Override
            public int compare(PlayingCard card1, PlayingCard card2)
            {
                int result = Comparators.compareSuits(card1, card2);
                if (result == 0)
                    result = Comparators.compareRanks(card1, card2);
                return result;
            }
        };
        
        /**
         * A {@code Comparator} which orders {@code PlayingCard}s first by
         * their {@code Rank}s, Aces < Kings < Jokers, and then by their
         * {@code Suit}s, Clubs < Spades < Jokers.
         * 
         * @see Comparator
         */
        public static Comparator<PlayingCard> RANK_FIRST = new Comparator<
                                                               PlayingCard>() {
            @Override
            public int compare(PlayingCard card1, PlayingCard card2)
            {
                int result = Comparators.compareRanks(card1, card2);
                if (result == 0)
                    result = Comparators.compareSuits(card1, card2);
                return result;
            }
        };
        
        // Compares the {@code Rank}s of the specified {@code PlayingCard}s.
        // {@code Rank}s are ordered Aces < Kings < Jokers.
        private static int compareRanks(PlayingCard card1, PlayingCard card2)
        {
            return card1.getRank().compareTo(card2.getRank());
        }
        
        // Compares the {@code Suit}s of the specified {@code PlayingCard}s.
        // {@code Suit}s are ordered Clubs < Spades < Jokers.
        private static int compareSuits(PlayingCard card1, PlayingCard card2)
        {
            return card1.getSuit().compareTo(card2.getSuit());
        }
    }
}
