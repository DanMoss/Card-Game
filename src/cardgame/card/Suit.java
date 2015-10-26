package cardgame.card;

public enum Suit
{
    CLUBS   (1, "Clubs"),
    DIAMONDS(2, "Diamonds"),
    HEARTS  (3, "Hearts"),
    SPADES  (4, "Spades");
    
    private final int    value_;
    private final String name_;
    
    // Constructor
    private Suit(int value, String name)
    {
        value_ = value;
        name_  = name;
    }
    
    // Accessors
    public int getValue()
    {
        return value_;
    }
    
    @Override
    public String toString()
    {
        return name_;
    }
}