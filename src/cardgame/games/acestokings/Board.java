package cardgame.games.acestokings;

import java.util.EnumSet;
import java.util.Set;

import cardgame.card.CardCollection;
import cardgame.card.Deck;
import cardgame.card.DrawListener;
import cardgame.card.Drawable;
import cardgame.card.PlayingCard;
import cardgame.card.Rank;
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
    
    private final Deck<PlayingCard>  deck_;
    private final Stack<PlayingCard> discards_;
    private final MeldsManager       melds_;
    private       Rank               nextJoker_;
    private       boolean            drewFromDiscards_;
    
    // Sole constructor
    Board()
    {
        this.deck_      = new Deck();
        this.deck_.addListener(this);
        this.discards_  = new Stack<PlayingCard>(Board.DISCARDS);
        this.melds_     = new MeldsManager();
        this.nextJoker_ = Rank.ACE;
    }
    
    /**
     * On being notified, refills the {@code Deck} with all but the top
     * {@code PlayingCard} of the discard pile. The {@code Deck} is then
     * shuffled.
     * 
     * @see DrawListener#onNotification()
     */
    public void onNotification()
    {
        int nCardsLeft = this.deck_.size();
        if (nCardsLeft == 0) {
            PlayingCard topCard      = this.discards_.draw();
            int         discardsSize = this.discards_.size();
            for (int i = 0; i < discardsSize; i++) {
                PlayingCard aCard = this.discards_.peek();
                this.discards_.transferTo(this.deck_, aCard);
            }
            this.deck_.shuffle();
            this.discards_.add(topCard);
        }
    }
    
    // Sets up the board for the start of the next round.
    // 
    // More specifically; all {@code PlayingCard}s on this {@code Board} are
    // removed and the {@code Deck} is refilled, then the {@code Rank} of the
    // jokers is updated, and finally the {@code Deck} is shuffled with the top
    // {@code PlayingCard} placed on the discard pile.
    void setUpRound()
    {
        this.deck_.reset();
        this.discards_.reset();
        this.melds_.reset();
        Set<Rank> ranks = EnumSet.complementOf(EnumSet.of(this.nextJoker_));
        this.deck_.setRanks(ranks);
        this.deck_.shuffle();
        this.nextJoker_ = this.nextJoker_.getNeighbour(true);
        this.discards_.add(this.deck_.draw());
    }
    
    // Deals the initial hand to a {@code CardCollection}.
    void dealInitialHand(CardCollection<PlayingCard> hand)
    {
        for (int i = 0; i < Board.INITIAL_HAND_SIZE; i++)
            hand.add(this.deck_.draw());
    }
    
    // Prompts a {@code PlayerIO} to choose a {@code Drawable} to draw from,
    // and then returns the drawn {@code PlayingCard}.
    PlayingCard draw(PlayerIO aPlayerIO)
    {
        String message = "Would you like to draw from the deck, or take the "
                       + this.discards_.peek() + " from the discard pile?";
        aPlayerIO.sendMessage(message);
        Drawable<PlayingCard>[] options = {this.deck_, this.discards_};
        Drawable<PlayingCard>   choice  = Selector.select(aPlayerIO, options);
        this.drewFromDiscards_ = choice == this.discards_ ? true : false;
        return choice.draw();
    }
    
    // Checks if the last {@code Card} drawn was from the discard pile.
    boolean checkIfLastDrewFromDiscards()
    {
        return this.drewFromDiscards_;
    }
    
    // Wrapper for the play method in MeldsManager.
    void playToMeld(PlayerIO aPlayerIO, CardCollection<PlayingCard> hand,
                    PlayingCard... cards)
    {
        this.melds_.play(aPlayerIO, hand, cards);
    }
    
    // Discards the specified {@code PlayingCard} to the discard pile.
    void addToDiscards(PlayingCard aCard)
    {
        this.discards_.add(aCard);
    }
}
