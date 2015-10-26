package cardgame.games.acestokings.melds;

import cardgame.card.Card;
import cardgame.card.Rank;
import cardgame.card.Suit;
import cardgame.card.CardBank;
import cardgame.player.PlayerIO;
import cardgame.player.Selector;
import java.util.List;
import java.util.ArrayList;

public class MeldsManager
{
    public static final int MINIMUM_MELD_SIZE = 3;
    
    private final RankMeld[] rankMelds_;
    private final RunMeld[]  runMelds_;
    
    // Constructor
    public MeldsManager(Rank jokerRank)
    {
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();
        rankMelds_   = new RankMeld[ranks.length];
        runMelds_    = new RunMeld [suits.length];
        
        int i = 0;
        for (Rank rank : ranks) {
            rankMelds_[i] = new RankMeld(rank, jokerRank);
            i++;
        }
        
        int j = 0;
        for (Suit suit : suits) {
            runMelds_[j] = new RunMeld(suit, jokerRank);
            j++;
        }
    }
    
    // Prompts the player to choose from a list of possible plays using
    // {@code cards}, or informs them if no plays can be made.
    public void play(PlayerIO playerIO, CardBank hand, Card... cards)
    {
        List<PlayOption> optionsList = findPlayOptions(cards);
        int              nOptions    = optionsList.size();
        
        if (nOptions > 0) {
            PlayOption[] options = new PlayOption[nOptions];
            options              = optionsList.toArray(options);
            PlayOption   choice  = Selector.select(playerIO, options);
            choice.play(hand);
        }
        else {
            playerIO.sendMessage("These cards cannot be played anywhere yet.");
        }
    }
    
    // Creates a list of possible plays using {@code cards}
    private List<PlayOption> findPlayOptions(Card... cards)
    {
        List<PlayOption> options = new ArrayList<PlayOption>();
        
        for (RankMeld rankMeld : rankMelds_)
            rankMeld.findPlayOptions(options, cards);
            
        for (RunMeld runMeld : runMelds_)
            runMeld.findPlayOptions(options, cards);
        
        return options;
    }
}