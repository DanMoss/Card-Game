package cardgame.games.acestokings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Queue;

import cardgame.card.Deck;
import cardgame.card.traditional.PlayingCard;
import cardgame.card.traditional.Rank;
import cardgame.card.traditional.Suit;

/**
 * A specialised deck for the card game Aces to Kings.
 */
public class AcesToKingsDeck extends Deck<PlayingCard>
{
    private final Queue<Rank>      roundRanks_;
    private final Collection<Suit> suits_;
    
    /**
     * Sole constructor. Creates a {@code Deck} consisting of the 52 standard
     * {@code PlayingCard}s, along with a joker for each {@code Suit}.
     * <p>
     * Additionally, creates a queue of {@code Rank}s that the
     * {@code AcesToKingsDeck} will cycle through, starting with the ace and
     * looping back after the king. On each {@code Rank}'s round, the
     * {@code Rank} will be removed from the {@code AcesToKingsDeck}.
     * 
     * @param dealAmount the amount of {@code PlayingCard}s that this
     *                   {@code AcesToKingsDeck} will draw when 
     *                   {@link Deck#dealTo} is called.
     * @see   Deck#Deck(Collection, int)
     */
    public AcesToKingsDeck(int dealAmount)
    {
        super(AcesToKingsDeck.createInitialCards(), dealAmount);
        this.roundRanks_ = new ArrayDeque<Rank>(EnumSet.range(
                                                Rank.ACE, Rank.KING));
        this.suits_      = EnumSet.range(Suit.CLUBS, Suit.SPADES);
    }
    
    // Returns a collection consisting of the traditional 52
    // {@code PlayingCard}s along with four jokers.
    private static Collection<PlayingCard> createInitialCards()
    {
        Collection<Rank>        ranks = EnumSet.range(Rank.ACE, Rank.JOKER);
        Collection<Suit>        suits = EnumSet.range(Suit.CLUBS, Suit.SPADES);
        Collection<PlayingCard> cards = new ArrayList<PlayingCard>();
        for (Rank aRank : ranks) {
            for (Suit aSuit : suits)
                cards.add(new PlayingCard(aRank, aSuit));
        }
        return cards;
    }
    
    /**
     * Resets this {@code AcesToKingsDeck} and removes any {@code PlayingCard}
     * with the next {@code Rank} in the queue.
     * 
     * @see Deck#reset()
     * @see Deck#remove(cardgame.card.Card)
     */
    public void incrementJoker()
    {
        reset();
        Rank nextRank = this.roundRanks_.remove();
        this.roundRanks_.add(nextRank);
        for (Suit aSuit : this.suits_)
            remove(new PlayingCard(nextRank, aSuit));
    }
}
