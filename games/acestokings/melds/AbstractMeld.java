package cardgame.games.acestokings.melds;

import java.util.List;
import cardgame.card.Rank;
import cardgame.card.Card;
import cardgame.card.CardBank;

abstract class AbstractMeld
{
    private final Rank jokerRank_;
    
    // Constructor
    protected AbstractMeld(Rank jokerRank)
    {
        jokerRank_ = jokerRank;
    }
    
    // Abstract methods
    // Plays some card(s) to the meld
    protected abstract void play(CardBank hand, PlayOption option);
    
    // Appends {@code options} with plays that can be made with {@code card}
    protected abstract void addCardPlays(List<PlayOption> options, Card card);
    
    // Appends {@code options} with plays that can be made with {@code cards}
    protected abstract void addMeldPlays(List<PlayOption> options,
                                         Card... cards);
    
    @Override
    public abstract String toString();
    
    // Other methods
    // Appends {@code options} with plays that can be made with {@code cards}
    void findPlayOptions(List<PlayOption> options, Card... cards)
    {
        if (cards.length == 1)
            addCardPlays(options, cards[0]);
        else if (cards.length >= MeldsManager.MINIMUM_MELD_SIZE)
            addMeldPlays(options, cards);
    }
    
    // Checks if {@code card} is a joker
    protected boolean isJoker(Card card)
    {
        return card.getRank() == jokerRank_;
    }
}