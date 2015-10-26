package cardgame.games.acestokings.melds;

import java.util.List;
import cardgame.card.Suit;
import cardgame.card.Rank;
import cardgame.card.Card;
import cardgame.card.CardBank;

class RunMeld extends AbstractMeld
{
    private final Suit   meldSuit_;
    private final Card[] meld_;
    
    // Constructor
    protected RunMeld(Suit meldSuit, Rank jokerRank)
    {
        super(jokerRank);
        meldSuit_  = meldSuit;
        int nRanks = Rank.values().length;
        meld_      = new Card[nRanks];
    }
    
    // Implementations of abstract methods in AbstractMeld
    // Plays some card(s) to the meld
    protected void play(CardBank hand, PlayOption option)
    {
        int    index = option.getIndex();
        Card[] cards = option.getCards();
        place(hand, index, cards);
    }
    
    // Appends {@code options} with plays that can be made with {@code card}
    protected void addCardPlays(List<PlayOption> options, Card card)
    {
        if (allCorrectSuit(card)) {
            if (isJoker(card)) {
                for (Rank rank : Rank.values()) {
                    // Scaling for min index = 0, min rank = 1 (Ace)
                    int     index   = rank.getValue() - 1;
                    boolean canPlay = isEmpty(index) && isNextToAMeld(index);
                    if (canPlay)
                        addOption(options, index, card);
                }
            }
            else {
                Rank    rank    = card.getRank();
                // Scaling for min index = 0, min rank = 1 (Ace)
                int     index   = rank.getValue() - 1;
                boolean canPlay = isNextToAMeld(index);
                if (canPlay)
                    addOption(options, index, card);
            }
        }
    }
    
    // Appends {@code options} with plays that can be made with {@code cards}
    protected void addMeldPlays(List<PlayOption> options, Card... cards)
    {
        if (isARun(cards) && allCorrectSuit(cards)) {
            int maxStartingIndex = meld_.length - cards.length;
            
            for (int i = 0; i <= maxStartingIndex; i++) {
                boolean canPlay = true;
                
                for (int j = 0; j < cards.length; j++)
                    canPlay = canPlay && canPlayTo(cards[j], i + j);
                
                if (canPlay)
                    addOption(options, i, cards);
            }
        }
    }
    
    public String toString()
    {
        return "a run of " + meldSuit_;
    }
    
    // Methods for checking that {@code cards} form a run of the correct suit
    // Checks that {@code cards} are all of the correct suit for the meld
    private boolean allCorrectSuit(Card... cards)
    {
        boolean allCorrect = true;
        for (Card card : cards) {
            boolean singleCorrect = card.getSuit() == meldSuit_;
            singleCorrect         = singleCorrect  || isJoker(card);
            allCorrect            = allCorrect     && singleCorrect;
        }
        return allCorrect;
    }
    
    // Checks if {@code cards} form an ascending run
    private boolean isARun(Card... cards)
    {
        boolean isARun = true;
        int     nCards = cards.length;
        for (int i = 1; i < nCards; i++)
            isARun = isARun && ranksAreConcurrent(cards[i - 1], cards[i]);
        
        return isARun;
    }
    
    // Checks if the ranks of {@code card1} and {@code card2} are concurrent
    private boolean ranksAreConcurrent(Card card1, Card card2)
    {
        boolean eitherIsJoker = isJoker(card1) || isJoker(card2);
        boolean concurrentRanks;
        if (eitherIsJoker) {
            concurrentRanks = true;
        }
        else {
            int card1Value  = card1.getRank().getValue();
            int card2Value  = card2.getRank().getValue();
            concurrentRanks = card2Value - card1Value == 1;
        }
        return concurrentRanks;
    }
    
    // Methods for checking other meld conditions
    // Checks if there is a card at {@code index}
    private boolean isEmpty(int index)
    {
        return meld_[index] == null;
    }
    
    // Checks if a meld is adjacent to {@code index}
    private boolean isNextToAMeld(int index)
    {
        int     last      = meld_.length - 1;
        boolean meldBelow = index == 0    ? false : !isEmpty(index - 1);
        boolean meldAbove = index == last ? false : !isEmpty(index + 1);
        return meldBelow || meldAbove;
    }
    
    // If {@code card} is a joker, checks if {@code index} is empty. Otherwise
    // checks if {@code index} is correct for the rank of {@code card}.
    private boolean canPlayTo(Card card, int index)
    {
        boolean canPlay;
        if (isJoker(card))
            canPlay = isEmpty(index);
        else
            canPlay = index == card.getRank().getValue() - 1;
            // Scaling for min index = 0, min rank = 1 (Ace)
        return canPlay;
    }
    
    // Other methods
    // Places {@code cards} from {@code hand} as a run, starting at 
    // {@code index}, picking up any jokers as necessary
    private void place(CardBank hand, int index, Card... cards)
    {
        for (Card card : cards) {
            if (!isEmpty(index))
                hand.add(meld_[index]);
            
            meld_[index] = card;
            hand.discard(card);
            index++;
        }
    }
    
    // Appends {@code options} with a new option
    private void addOption(List<PlayOption> options, int index, Card... cards)
    {
        PlayOption option = new PlayOption(this, cards);
        option.setIndex(index);
        options.add(option);
    }
}