package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import cardgame.card.Suit;
import cardgame.card.Rank;
import cardgame.card.Card;
import cardgame.card.CardBank;
import cardgame.player.Player;
import cardgame.games.acestokings.CardBanks;

public class RankMeld extends AbstractMeld
{
    private final int MELD_CAPACITY = Suit.values().length;
    
    private final Rank            meldRank_;
    private final ArrayList<Card> meld_;
    private       int             nJokers_;
    
    // Constructor
    public RankMeld(Rank meldRank, Rank jokerRank)
    {
        super(jokerRank);
        meldRank_ = meldRank;
        meld_     = new ArrayList<Card>(MELD_CAPACITY);
        nJokers_  = 0;
    }
    
    // Implementations of abstract methods
    // Attempts to add {@code card} to an existing meld
    protected boolean addCardToMeld(Player player, Card card)
    {
        boolean canPlay = canPlaySingle(card);
        if (canPlay)
            place(player, card);
        return canPlay;
    }
    
    // Attempts to play {@code cards} as a meld
    protected boolean playMeld(Player player, Card... cards)
    {
        boolean canPlay = canPlayMeld(cards);
        if (canPlay)
            place(player, cards);
        return canPlay;
    }
    
    // Places {@code cards}, picking up any jokers as necessary
    private void place(Player player, Card... cards)
    {
        CardBank hand             = player.findCardBank(CardBanks.HAND);
        int      nJokersToReplace = findNJokersToReplace(cards);
        
        for (int i = 1; i <= nJokersToReplace; i++) {
            hand.add(meld_.remove(0));
            nJokers_--;
        }
        
        for (Card card : cards) {
            if (isJoker(card)) {
                meld_.add(0, card);
                nJokers_++;
            }
            else {
                meld_.add(card);
            }
            
            hand.discard(card);
        }
    }
    
    // Validity checking methods
    // Checks if {@code card} is playable as a single card
    private boolean canPlaySingle(Card card)
    {
        boolean meldExists = meld_.size() >= MINIMUM_MELD_SIZE;
        boolean canPlay    = meldExists && allCorrectRank(card);
        canPlay            = canPlay    && hasSpace(card);
        return canPlay;
    }
    
    // Checks if {@code cards} are playable as a meld
    private boolean canPlayMeld(Card... cards)
    {
        boolean canPlay = allCardsDifferent(cards);
        canPlay = canPlay && allCorrectRank(cards);
        canPlay = canPlay && hasSpace(cards);
        return canPlay;
    }
    
    // Checks that {@code cards} are all of the correct rank for the meld
    private boolean allCorrectRank(Card... cards)
    {
        boolean allCorrect = true;
        for (Card card : cards) {
            boolean singleCorrect = card.getRank() == meldRank_;
            singleCorrect         = singleCorrect || isJoker(card);
            allCorrect            = allCorrect    && singleCorrect;
        }
        return allCorrect;
    }
    
    // Checks that there is space to play {@code cards}
    private boolean hasSpace(Card... cards)
    {
        int     nJokersToReplace = findNJokersToReplace(cards);
        int     endMeldSize      = meld_.size() + cards.length;
        endMeldSize              = endMeldSize - nJokersToReplace;
        boolean withinCapacity   = endMeldSize <= MELD_CAPACITY;
        return withinCapacity;
    }
    
    // Counts the number of jokers in {@code cards}
    private int findNJokersToReplace(Card... cards)
    {
        int nNonJokers = 0;
        for (Card card : cards) {
            if (!isJoker(card))
                nNonJokers++;
        }
        int nJokersToReplace = nNonJokers < nJokers_ ? nNonJokers : nJokers_;
        return nJokersToReplace;
    }
}