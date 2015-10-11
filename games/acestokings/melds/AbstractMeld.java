package cardgame.games.acestokings.melds;

import cardgame.card.Rank;
import cardgame.card.Card;
import cardgame.player.Player;

public abstract class AbstractMeld
{
    public static final int MINIMUM_MELD_SIZE = 3;
    
    private final Rank jokerRank_;
    
    // Constructor
    protected AbstractMeld(Rank jokerRank)
    {
        jokerRank_ = jokerRank;
    }
    
    // Attempts to play {@code cards}
    // Future note: Look into the possibility of playing 2 single cards
    public boolean play(Player player, Card... cards)
    {
        boolean cardsPlayed;
        
        if (cards.length == 1)
            cardsPlayed = addCardToMeld(player, cards[0]);
        else if (cards.length >= MINIMUM_MELD_SIZE)
            cardsPlayed = playMeld(player, cards);
        else
            cardsPlayed = false;
            
        return cardsPlayed;
    }
    
    // Abstract methods
    // Attempts to add {@code card} to an existing meld
    protected abstract boolean addCardToMeld(Player player, Card card);
    
    // Attempts to play {@code cards} as a meld
    protected abstract boolean playMeld(Player player, Card... cards);
    
    // Other methods
    // Checks that {@code cards} are all different
    protected boolean allCardsDifferent(Card... cards)
    {
        boolean allCardsDifferent = true;
        int     nCards = cards.length;
        
        for (int i = 0; i < nCards; i++) {
            for (int j = i + 1; j < nCards; j++) {
                if (cards[i].equals(cards[j]))
                    allCardsDifferent = false;
            }
        }
        
        return allCardsDifferent;
    }
    
    // Checks if {@code card} is a joker
    protected boolean isJoker(Card card)
    {
        return card.getRank() == jokerRank_;
    }
}