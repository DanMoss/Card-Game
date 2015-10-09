package cardgame.card;

public enum Rank
{
    ACE  ( 1, "Ace"),
    TWO  ( 2, "Two"),
    THREE( 3, "Three"),
    FOUR ( 4, "Four"),
    FIVE ( 5, "Five"),
    SIX  ( 6, "Six"),
    SEVEN( 7, "Seven"),
    EIGHT( 8, "Eight"),
    NINE ( 9, "Nine"),
    TEN  (10, "Ten"),
    JACK (11, "Jack"),
    QUEEN(12, "Queen"),
    KING (13, "King");
    
    private final int    value_;
    private final String name_;
    
    // Constructor
    private Rank(int value, String name)
    {
        value_ = value;
        name_  = name;
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
}