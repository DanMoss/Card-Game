package cardgame;

public class Card
{
    public enum Suit
    {
        CLUBS(1), DIAMONDS(2), HEARTS(3), SPADES(4);
        
        private int value_;
        
        private Suit(int value)
        {
            value_ = value;
        }
        
        public int getValue()
        {
            return value_;
        }
    }
    
    public enum Face
    {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);
        
        private int value_;
        
        private Face(int value)
        {
            value_ = value;
        }
        
        public int getValue()
        {
            return value_;
        }
    }
    
    private Suit suit_;
    private Face face_;
    
    // Constructors
    public Card(Suit suit, Face face)
    {
        suit_ = suit;
        face_ = face;
    }
    
    public Card(Card card)
    {
        suit_ = card.suit_;
        face_ = card.face_;
    }
    
    // Accessors
    public Suit getSuit()
    {
        return suit_;
    }
    
    public Face getFace()
    {
        return face_;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (object == this)
            return true;
        if (!(object instanceof Card))
            return false;
        
        Card card = (Card) object;
        return ((suit_ == card.suit_) &&
                (face_ == card.face_));
    }
    
    // Future note: Implementation of this is wrong - requires fix
    @Override
    public int hashCode()
    {
        int prime1 = 23; // Non-zero
        int prime2 = 31;
        int result;
        result = prime2 * prime1 + suit_.hashCode();
        result = prime2 * prime1 + face_.hashCode();
        return result;
    }
    
    @Override
    public String toString()
    {
        return (face_ + " OF " + suit_);
    }
}