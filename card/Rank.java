package cardgame.card;

public enum Rank
{
    ACE  ( 1, "Ace",   "aces"),
    TWO  ( 2, "Two",   "twos"),
    THREE( 3, "Three", "threes"),
    FOUR ( 4, "Four",  "fours"),
    FIVE ( 5, "Five",  "fives"),
    SIX  ( 6, "Six",   "sixes"),
    SEVEN( 7, "Seven", "sevens"),
    EIGHT( 8, "Eight", "eights"),
    NINE ( 9, "Nine",  "nines"),
    TEN  (10, "Ten",   "tens"),
    JACK (11, "Jack",  "jacks"),
    QUEEN(12, "Queen", "queens"),
    KING (13, "King",  "kings");
    
    private final int    value_;
    private final String name_;
    private final String plural_;
    
    // Constructor
    private Rank(int value, String name, String plural)
    {
        value_  = value;
        name_   = name;
        plural_ = plural;
    }
    
    public int getValue()
    {
        return value_;
    }
    
    @Override
    public String toString()
    {
        return name_;
    }
    
    public String getPlural()
    {
        return plural_;
    }
}