package cardgame.games.acestokings;

import cardgame.games.acestokings.melds.AbstractMeld;

enum TurnOption
{
    END_TURN ("End your turn and discard.", 0),
    ADD_CARD ("Add a card to an set.", 1),
    PLAY_MELD("Play a set of cards.", AbstractMeld.MINIMUM_MELD_SIZE);
    
    private final String message_;
    private final int    nCardsToPlay_;
    
    // Constructor
    private TurnOption(String message, int nCardsToPlay)
    {
        message_      = message;
        nCardsToPlay_ = nCardsToPlay;
    }
    
    @Override
    public String toString()
    {
        return message_;
    }
    
    public int getNCardsToPlay()
    {
        return nCardsToPlay_;
    }
}