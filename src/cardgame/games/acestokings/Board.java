package cardgame.games.acestokings;

import java.util.ArrayList;
import java.util.List;

import cardgame.card.CardCollection;
import cardgame.card.Deck;
import cardgame.card.DrawListener;
import cardgame.card.Drawable;
import cardgame.card.PlayingCard;
import cardgame.card.Stack;
import cardgame.games.acestokings.melds.MeldsManager;
import cardgame.player.PlayerIO;
import cardgame.player.Selector;

/**
 * An Aces to Kings game board. Holds melds, the deck, and the discard pile.
 * 
 * @see MeldsManager
 * @see Deck
 * @see Stack
 */
class Board
    implements DrawListener
{
    private static final int    INITIAL_HAND_SIZE = 7;
    private static final String DISCARDS          = "The discard pile";
    
    private final AcesToKingsDeck    deck_;
    private final Stack<PlayingCard> discards_;
    private final MeldsManager       melds_;
    
    /**
     * Sole constructor.
     */
    public Board()
    {
        this.deck_     = new AcesToKingsDeck(INITIAL_HAND_SIZE);
        this.deck_.addListener(this);
        this.discards_ = new Stack<PlayingCard>(Board.DISCARDS);
        this.melds_    = new MeldsManager();
    }
    
    /**
     * On being notified, refills the {@code AcesToKingsDeck} with all but the
     * top {@code PlayingCard} of the discard pile. The {@code AcesToKingsDeck}
     * is then shuffled.
     * 
     * @see DrawListener#onNotification()
     */
    public void onNotification()
    {
        int nCardsLeft = this.deck_.size();
        if (nCardsLeft == 0) {
            PlayingCard topCard      = this.discards_.draw();
            int         discardsSize = this.discards_.size();
            for (int i = 0; i < discardsSize; i++)
                this.deck_.add(this.discards_.draw());
            this.deck_.shuffle();
            this.discards_.add(topCard);
        }
    }
    
    /**
     * Sets up the board for the start of the next round.
     * <p>
     * More specifically; the {@code AcesToKingsDeck} is refilled with the
     * updated joker {@code Rank} and shuffled, then the remaining
     * {@code PlayingCard}s left on this {@code Board} are removed, and finally
     * the top {@code PlayingCard} of the {@code AcesToKingsDeck} is placed on
     * the discard pile.
     * 
     * @see AcesToKingsDeck#incrementJoker()
     */
    public void setUpNextRound()
    {
        this.deck_.incrementJoker();
        this.deck_.shuffle();
        this.discards_.reset();
        this.melds_.reset();
        this.discards_.add(this.deck_.draw());
    }
    
    // Deals the initial hand to a {@code CardCollection}.
    void dealInitialHand(CardCollection<? super PlayingCard> aCollection)
    {
        this.deck_.dealTo(aCollection);
    }
    
    // Prompts a {@code PlayerIO} to choose a {@code Drawable} to draw from,
    // and then returns the drawn {@code PlayingCard}.
    PlayingCard draw(PlayerIO aPlayerIO)
    {
        String message = "Would you like to draw from the deck, or take the "
                       + this.discards_.peek() + " from the discard pile?";
        List<Drawable<PlayingCard>> options = new ArrayList<
                                                      Drawable<PlayingCard>>();
        options.add(this.deck_);
        options.add(this.discards_);
        Drawable<PlayingCard> choice = Selector.select(aPlayerIO, message,
                                                       options);
        return choice.draw();
    }
    
    /**
     * Returns the top {@code PlayingCard} of the discard pile.
     * 
     * @return the top {@code PlayingCard} of the discard pile
     * @see    Stack#peek()
     */
    public PlayingCard peekAtDiscards()
    {
        return this.discards_.peek();
    }
    
    /**
     * Attempts to play the specified {@code PlayingCard}s from the specified
     * {@code PlayerIO}'s {@code CardCollection} to an {@code AbstractMeld} on
     * this {@code Board}. Wrapper for 
     * {@link MeldsManager#play(PlayerIO, CardCollection, PlayingCard...)}.
     * 
     * @param aPlayerIO   the {@code PlayerIO} to interact with
     * @param aCollection the source of the {@code PlayingCard}s to play
     * @param cards       the {@code PlayingCard}s to play
     */
    public void playToMeld(PlayerIO aPlayerIO,
                           CardCollection<PlayingCard> aCollection,
                           PlayingCard... cards)
    {
        this.melds_.play(aPlayerIO, aCollection, cards);
    }
    
    /**
     * Adds the specified {@code PlayingCard} to the top of the discard pile.
     * 
     * @param aCard the {@code PlayingCard} to add
     */
    public void addToDiscards(PlayingCard aCard)
    {
        this.discards_.add(aCard);
    }
}
