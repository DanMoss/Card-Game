package cardgame.card;

/**
 * An enum of the four standard playing card suits, along with a joker suit for
 * special cases. The suits have the default natural ordering of clubs, 
 * diamonds, hearts, and finally spades.
 */
public enum Suit
{
    CLUBS   (1, "Clubs",    Colour.BLACK),
    DIAMONDS(2, "Diamonds", Colour.RED),
    HEARTS  (3, "Hearts",   Colour.RED),
    SPADES  (4, "Spades",   Colour.BLACK),
    JOKER   (5, "Joker",    Colour.JOKER);
    
    private enum Colour
    {
        RED  ("Red"),
        BLACK("Black"),
        JOKER("Joker");
        
        private String name_;
        
        // Constructor
        private Colour(String name)
        {
            this.name_ = name;
        }
        
        // Allows access to the name of the colour of a suit
        public String getName()
        {
            return name_;
        }
    }
    
    private final int    value_;
    private final String name_;
    private final Colour colour_;
    
    // Constructor
    private Suit(int value, String name, Colour colour)
    {
        this.value_  = value;
        this.name_   = name;
        this.colour_ = colour;
    }
    
    /**
     * Returns the value of this {@code Suit}.
     * 
     * @return the value of this {@code Suit}
     */
    public int getValue()
    {
        return value_;
    }
    
    /**
     * Returns the name of this {@code Suit} in mixed case.
     * 
     * @return the name of this {@code Suit} in mixed case.
     * @see    java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return name_;
    }
    
    /**
     * Returns the colour of this {@code Suit}.
     * 
     * @return the colour of this {@code Suit}
     */
    public String getColour()
    {
        return colour_.getName();
    }
}
