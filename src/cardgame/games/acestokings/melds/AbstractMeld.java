package cardgame.games.acestokings.melds;

import java.util.List;

import cardgame.card.Card;
import cardgame.card.CardCollection;
import cardgame.card.Rank;

/**
 * A meld of cards. May contain jokers mimicking other cards.
 * 
 * @see PlayOption
 * @see cardgame.card.Card
 * @see cardgame.card.CardCollection
 */
abstract class AbstractMeld
    implements CardCollection
{
    /**
     * Plays some {@code Card}s from a {@code PlayOption} to this
     * {@code AbstractMeld}, picking up jokers as necessary.
     * 
     * @param collection the source of the {@code PlayOption}
     * @param anOption   the {@code PlayOption} to play
     */
    protected abstract void play(CardCollection collection, PlayOption anOption);
    
    /**
     * Finds the possible positions that a {@code Card} can be added to in this
     * {@code AbstractMeld}, then appends them to a list of
     * {@code PlayOption}s.
     * 
     * @param options the current list of {@code PlayOption}s
     * @param aCard   the {@code Card} to consider
     */
    protected abstract void addCardPlays(List<PlayOption> options, Card aCard);
    
    /**
     * Finds the possible melds that can be played to this {@code AbstractMeld}
     * with a set of {@code Card}s, then appends them to a list of
     * {@code PlayOption}s.
     * 
     * @param options the current list of {@code PlayOption}s
     * @param cards   the set of {@code Card}s to consider
     */
    protected abstract void addMeldPlays(List<PlayOption> options,
                                         Card... cards);
    
    /**
     * Returns a string representation of this {@code AbstractMeld}.
     * 
     * @return the string representation of this {@code AbstractMeld}
     */
    @Override
    public abstract String toString();
    
    // Finds the possible {@code PlayOption}s that can be made to this
    // {@code AbstractMeld} with a set of {@code Card}s, then appends them to a
    // list of {@code PlayOption}s.
    void findPlayOptions(List<PlayOption> options, Card... cards)
    {
        if (cards.length == 1)
            addCardPlays(options, cards[0]);
        else if (cards.length >= MeldsManager.MINIMUM_MELD_SIZE)
            addMeldPlays(options, cards);
    }
    
    /**
     * Checks if a {@code Card} is a joker.
     * 
     * @param  aCard the {@code Card} to check
     * @return true if the {@code Card} is a joker
     */
    protected boolean isJoker(Card aCard)
    {
        return aCard.getRank() == Rank.JOKER;
    }
}
