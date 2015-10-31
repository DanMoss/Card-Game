package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import cardgame.card.PlayingCard;
import cardgame.card.CardCollection;
import cardgame.card.Rank;
import cardgame.card.Suit;
import cardgame.player.PlayerIO;
import cardgame.player.Selector;

/**
 * A manager for the various kinds of melds. Handles find plays for selections
 * of cards and selecting a play for them.
 * 
 * @see AbstractMeld
 * @see PlayOption
 * @see cardgame.player.PlayerIO
 * @see cardgame.player.Selector
 */
public class MeldsManager
{
    static final int MINIMUM_MELD_SIZE = 3;
    
    private final AbstractMeld[] rankMelds_;
    private final AbstractMeld[] runMelds_;
    
    /**
     * Sole constructor. Creates all necessary {@code AbstractMeld}s for a
     * game.
     */
    public MeldsManager()
    {
        Set<Rank> ranks = EnumSet.range(Rank.ACE, Rank.KING);
        Set<Suit> suits = EnumSet.range(Suit.CLUBS, Suit.SPADES);
        this.rankMelds_ = new AbstractMeld[ranks.size()];
        this.runMelds_  = new AbstractMeld[suits.size()];
        
        int i = 0;
        for (Rank aRank : ranks) {
            this.rankMelds_[i] = new RankMeld(aRank);
            i++;
        }
        
        int j = 0;
        for (Suit aSuit : suits) {
            this.runMelds_[j] = new RunMeld(aSuit);
            j++;
        }
    }
    
    /**
     * Finds possible plays that can be made with some {@code PlayingCard}s,
     * and then prompts the {@code PlayerIO} to pick one.
     * 
     * @param aPlayerIO the {@code PlayerIO} to interact with
     * @param hand      the source of the {@code PlayingCard}s
     * @param cards     the {@code PlayingCard}s to play
     */
    public void play(PlayerIO aPlayerIO, CardCollection<PlayingCard> hand,
                     PlayingCard... cards)
    {
        List<PlayOption> optionsList = findPlayOptions(cards);
        int              nOptions    = optionsList.size();
        
        if (nOptions > 0) {
            PlayOption[] options = new PlayOption[nOptions];
            options              = optionsList.toArray(options);
            PlayOption   choice  = Selector.select(aPlayerIO, options);
            choice.play(hand);
        }
        else {
            aPlayerIO.sendMessage("These cards cannot be played anywhere yet.");
        }
    }
    
    // Creates a list of possible plays for some {@code PlayingCard}s
    private List<PlayOption> findPlayOptions(PlayingCard... cards)
    {
        List<PlayOption> options = new ArrayList<PlayOption>();
        
        for (AbstractMeld aRankMeld : this.rankMelds_)
            aRankMeld.findPlayOptions(options, cards);
            
        for (AbstractMeld aRunMeld : this.runMelds_)
            aRunMeld.findPlayOptions(options, cards);
        
        return options;
    }
    
    /**
     * Resets all of the {@code AbstractMeld}s to their initial state (empty).
     */
    public void reset()
    {
        for (AbstractMeld aRankMeld : this.rankMelds_)
            aRankMeld.reset();
        for (AbstractMeld aRunMeld : this.runMelds_)
            aRunMeld.reset();
    }
}
