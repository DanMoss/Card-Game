package cardgame.games.acestokings.melds;

import cardgame.card.Suit;
import cardgame.card.Rank;
import cardgame.card.Card;
import cardgame.card.CardBank;
import cardgame.player.Player;
import cardgame.player.PlayerIO;
import cardgame.games.acestokings.Selector;
import cardgame.games.acestokings.CardBanks;

public class RunMeld extends AbstractMeld
{
    private final Suit   meldSuit_;
    private final Card[] meld_;
    
    // Constructor
    public RunMeld(Suit meldSuit, Rank jokerRank)
    {
        super(jokerRank);
        meldSuit_  = meldSuit;
        int nRanks = Rank.values().length;
        meld_      = new Card[nRanks];
    }
    
    // Implementations of abstract methods
    // Attempts to add {@code card} to an existing meld
    protected boolean addCardToMeld(Player player, Card card)
    {
        int     destination = findIndex(player, card);
        boolean canPlay     = canPlaySingle(destination, card);
        if (canPlay)
            place(player, destination, card);
        
        return canPlay;
    }
    
    // Attempts to play {@code cards} as a meld
    protected boolean playMeld(Player player, Card... cards)
    {
        int     firstIndex = findIndex(player, cards[0]);
        boolean canPlay    = canPlayMeld(firstIndex, cards);
        if (canPlay)
            place(player, firstIndex, cards);
        
        return canPlay;
    }
    
    // Other methods
    // Finds the index that {@code card} will be attempted to be played to.
    // Note rank = index + 1 (minimum rank = 1 (Ace); minimum index = 0)
    private int findIndex(Player player, Card card)
    {
        Rank rank  = isJoker(card) ? chooseRank(player) : card.getRank();
        int  index = rank.getValue() - 1;
        return index;
    }
    
    // Places {@code cards} starting at {@code firstIndex}, picking up any
    // jokers as necessary.
    private void place(Player player, int firstIndex, Card... cards)
    {
        CardBank hand  = player.findCardBank(CardBanks.HAND);
        int      index = firstIndex;
        
        for (Card card : cards) {
            if (!isEmpty(index))
                hand.add(meld_[index]);
            
            meld_[index] = card;
            hand.discard(card);
            index++;
        }
    }
    
    // Validity checking methods
    // Checks if {@code card} is playable as a single card at
    // {@code destination}.
    private boolean canPlaySingle(int destination, Card card)
    {
        boolean canPlay = isValidDestination(destination, card);
        canPlay = canPlay && allCorrectSuit(card);
        canPlay = canPlay && adjacentMeldExists(destination);
        return canPlay;
    }
    
    // Checks if {@code cards} are playable as a meld starting at
    // {@code firstIndex}.
    private boolean canPlayMeld(int firstIndex, Card... cards)
    {
        boolean canPlay = allCardsDifferent(cards);
        canPlay = canPlay && allCorrectSuit(cards);
        canPlay = canPlay && isARun(firstIndex, cards);
        canPlay = canPlay && hasSpace(firstIndex, cards);
        
        if (canPlay) {
            int i = firstIndex;
            for (Card card : cards) {
                canPlay = canPlay && isValidDestination(i, card);
                i++;
            }
        }
        
        return canPlay;
    }
    
    // Checks if a meld is adjacent to {@code destination}
    private boolean adjacentMeldExists(int destination)
    {
        int     last = meld_.length - 1;
        boolean meldBelow;
        boolean meldAbove;
        meldBelow = (destination == 0)    ? false : isEmpty(destination - 1);
        meldAbove = (destination == last) ? false : isEmpty(destination + 1);
        boolean adjacentMeldExists = meldBelow || meldAbove;
        return adjacentMeldExists;
        
    }
    
    // Checks that {@code cards} are all of the correct suit for the meld
    private boolean allCorrectSuit(Card... cards)
    {
        boolean allCorrect = true;
        for (Card card : cards) {
            boolean singleCorrect = card.getSuit() == meldSuit_;
            singleCorrect         = singleCorrect || isJoker(card);
            allCorrect            = allCorrect    && singleCorrect;
        }
        return allCorrect;
    }
    
    // Checks that {@code card}'s {@code destination} is empty/correct
    private boolean isValidDestination(int destination, Card card)
    {
        boolean isValidDestination;
        
        if (isJoker(card)) {
            isValidDestination = isEmpty(destination);
        }
        else {
            // Rank = index + 1 (minimum rank = 1 (Ace); minimum index = 0)
            int validDestination = card.getRank().getValue() - 1;
            isValidDestination   = destination == validDestination;
        }
        
        return isValidDestination;
    }
    
    // Checks that {@code cards} is a run, assuming the first card is playable
    // at {@code firstIndex}.
    private boolean isARun(int firstIndex, Card... cards)
    {
        // Rank = index + 1 (minimum rank = 1 (Ace); minimum index = 0)
        int     previousRank = firstIndex + 1;
        boolean isARun       = true;
        
        for (int i = 1; i < cards.length; i++) {
            Card card = cards[i];
            int  currentRank;
            
            if (isJoker(card)) {
                currentRank = previousRank + 1;
            }
            else {
                currentRank             = card.getRank().getValue();
                boolean concurrentRanks = currentRank - previousRank == 1;
                isARun                  = isARun && concurrentRanks;
            }
            
            previousRank = currentRank;
        }
        
        return isARun;
    }
    
    // Checks that the {@code cards} won't overflow the meld
    private boolean hasSpace(int firstIndex, Card... cards)
    {
        int nCards         = cards.length;
        int availableSpace = meld_.length - firstIndex;
        return nCards <= availableSpace;
    }
    
    // Checks if there is a card at {@code index}
    private boolean isEmpty(int index)
    {
        Card nullTest = meld_[index];
        return nullTest == null;
    }
    
    // Chooses a rank for a joker to mimic
    private Rank chooseRank(Player player)
    {
        PlayerIO playerIO = player.getPlayerIO();
        String   message  = "You have chosen to play a joker. Please select a "
                          + "rank for this card to mimic.";
        playerIO.sendMessage(message);
        return Selector.select(playerIO, Rank.values());
    }
}