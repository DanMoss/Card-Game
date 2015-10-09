package cardgame.card;

public class Card
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
        int prime1 = 23; // Non-zero
        int prime2 = 31;
        int result;
        result = prime2 * prime1 + suit_.hashCode();
        result = result * prime1 + rank_.hashCode();
        return result;
    }
    
    @Override
    public String toString()
    {
        return rank_ + " of " + suit_;
    }
}