package cardgame.card;

/**
 * An enum of the thirteen standard playing card ranks, along with a joker rank
 * for special cases. The ranks follow their natural ordering, with the ace
 * classed as being low.
 */
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
    KING (13, "King",  "kings"),
    JOKER(14, "Joker", "jokers");
    
    private final int    value_;
    private final String name_;
    private final String plural_;
    
    // Constructor
    private Rank(int value, String name, String plural)
    {
        this.value_  = value;
        this.name_   = name;
        this.plural_ = plural;
    }
    
    /**
     * Returns the value of this {@code Rank}.
     * 
     * @return the value of this {@code Rank}
     */
    public int getValue()
    {
        return value_;
    }
    
    /**
     * Returns the name of this {@code Rank} in mixed case.
     * 
     * @return the name of this {@code Rank} in mixed case
     * @see    java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return name_;
    }
    
    /**
     * Returns the plural of this {@code Rank}'s name in lower case.
     * 
     * @return the name of a group of cards with this {@code Rank}
     */
    public String getPlural()
    {
        return plural_;
    }
    
    /**
     * Returns the neighbouring {@code Rank} of this {@code Rank}.
     * <p>
     * The neighbour returned is specified by the {@code boolean} 
     * {@code after}, which if true specifies the neighbour is after this
     * {@code Rank} and not before it. The neighbours are considered to wrap
     * around so that the first element of the {@code Rank} {@code Enum} is a
     * neighbour with the last element.
     * 
     * @param  after whether the neighbour is before or after this
     *               {@code Rank}
     * @return the specified neighbouring {@code Rank}
     */
    public Rank getNeighbour(boolean after)
    {
        Rank[] ranks          = Rank.values();
        int    direction      = after ? 1 : -1;
        int    neighbourIndex = (this.ordinal() + direction) % ranks.length;
        return ranks[neighbourIndex];
    }
}
