package cardgame.card;

import cardgame.player.Selectable;

public class Card
    implements Selectable
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
        return rank_ + " of " + suit_;
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
        
        Card card = (Card) object;
        return ((suit_ == card.suit_) &&
                (rank_ == card.rank_));
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