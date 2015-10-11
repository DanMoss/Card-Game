package cardgame.games.acestokings;

enum MeldType
{
    RANK("A set of cards of the same rank."),
    RUN ("A run of cards of the same suit.");
    
    private final String message_;
    
    // Constructor
    private MeldType(String message)
    {
        message_ = message;
    }
    
    @Override
    public String toString()
    {
        return message_;
    }
}