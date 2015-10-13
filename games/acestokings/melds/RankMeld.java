package cardgame.games.acestokings.melds;

import java.util.List;
import java.util.ArrayList;
import cardgame.card.Suit;
import cardgame.card.Rank;
import cardgame.card.Card;
import cardgame.card.CardBank;

class RankMeld extends AbstractMeld
{
    private final int MELD_CAPACITY = Suit.values().length;
    
    private final Rank       meldRank_;
    private final List<Card> meld_;
    private       int        nJokers_;
    
    // Constructor
    protected RankMeld(Rank meldRank, Rank jokerRank)
    {
        super(jokerRank);
        meldRank_ = meldRank;
        meld_     = new ArrayList<Card>(MELD_CAPACITY);
        nJokers_  = 0;
    }
    
    // Implementations of abstract methods in AbstractMeld
    // Plays some card(s) to the meld
    protected void play(CardBank hand, PlayOption option)
    {
        Card[] cards = option.getCards();
        place(hand, cards);
    }
    
    // Appends {@code options} with plays that can be made with {@code card}
    protected void addCardPlays(List<PlayOption> options, Card card)
    {
        boolean meldExists = meld_.size() >= MeldsManager.MINIMUM_MELD_SIZE;
        boolean canPlay    = meldExists   && allCorrectRank(card);
        canPlay            = canPlay      && hasSpace(card);
        if (canPlay)
            addOption(options, card);
    }
    
    // Appends {@code options} with plays that can be made with {@code cards}
    protected void addMeldPlays(List<PlayOption> options, Card... cards)
    {
        boolean canPlay = hasSpace(cards) && allCorrectRank(cards);
        if (canPlay)
            addOption(options, cards);
    }
    
    public String toString()
    {
        return "a set of " + meldRank_.getPlural();
    }
    
    // Methods for checking meld conditions
    // Checks that {@code cards} are all of the correct rank for the meld
    private boolean allCorrectRank(Card... cards)
    {
        boolean allCorrect = true;
        for (Card card : cards) {
            boolean singleCorrect = card.getRank() == meldRank_;
            singleCorrect         = singleCorrect  || isJoker(card);
            allCorrect            = allCorrect     && singleCorrect;
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
    
    // Other methods
    // Places {@code cards}, picking up any jokers as necessary
    // Note that jokers are always played to the start of the meld
    private void place(CardBank hand, Card... cards)
    {
        int nJokersToReplace = findNJokersToReplace(cards);
        
        for (int i = 0; i < nJokersToReplace; i++) {
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
    
    // Finds out how many jokers to pick up when playing {@code cards}
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
    
    // Appends {@code options} with a new option
    private void addOption(List<PlayOption> options, Card... cards)
    {
        PlayOption option = new PlayOption(this, cards);
        options.add(option);
    }
}