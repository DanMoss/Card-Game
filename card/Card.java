package cardgame.card;

import cardgame.player.Selectable;
import java.lang.Comparable;

public class Card
    implements Comparable<Card>, Selectable
{
    private final Rank rank_;
    private final Suit suit_;
    
    // Constructors
    public Card(Rank rank, Suit suit)
    {
        rank_ = rank;
        suit_ = suit;
    }
    
    // Accessors
    public Suit getSuit()
    {
        return suit_;
    }
    
    public Rank getRank()
    {
        return rank_;
    }
    
    @Override
    public String toString()
    {
        return getRank() + " of " + getSuit();
    }
    
    // Implementation of Comparable
    // Compares two cards, ordering first by suit and then by rank.
    public int compareTo(Card card)
    {
        int thisSuit = getSuit().getValue();
        int cardSuit = card.getSuit().getValue();
        int result;
        
        if (thisSuit == cardSuit) {
            int thisRank = getRank().getValue();
            int cardRank = card.getRank().getValue();
            
            if (thisRank == cardRank)
                result = 0;
            else
                result = thisRank < cardRank ? -1 : 1;
        }
        else {
            result = thisSuit < cardSuit ? -1 : 1;
        }
        
        return result;
    }
    
    // Implementation of Selectable
    // Used by Selector to convey the option to the player
    public String getMessage()
    {
        return toString(); // Code duplication here, consider other options
    }
    
    // Other methods
    @Override
    public boolean equals(Object object)
    {
        if (object == this)
            return true;
        if (!(object instanceof Card))
            return false;
        
        Card    card     = (Card) object;
        boolean sameRank = getRank() == card.getRank();
        boolean sameSuit = getSuit() == card.getSuit();
        return sameRank && sameSuit;
    }
    
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