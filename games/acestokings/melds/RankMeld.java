package cardgame.games.acestokings.melds;

import java.util.ArrayList;
import cardgame.card.Rank;
import cardgame.card.Suit;
import cardgame.card.Card;
import cardgame.card.CardBank;

public class RankMeld extends AbstractMeld
{
    private Rank            rank_;
    private Rank            roundRank_;
    private ArrayList<Card> meld_;
    private int             nRoundCards_;
    
    // Constructor
    public RankMeld(Rank rank, Rank roundRank)
    {
        int nSuits   = Suit.values().length;
        rank_        = rank;
        roundRank_   = roundRank;
        meld_        = new ArrayList<Card>(nSuits);
        nRoundCards_ = 0;
    }
    
    // Accessors
    public Rank getMeldType()
    {
        return rank_;
    }
    
    // Other methods
    // Adds a single card to the meld and replaces a round card if necessary
    // Note round cards are always placed at the front of the meld
    public void add(CardBank hand, Card card)
    {
        if (isRoundCard(card)) {
            meld_.add(0, card);
            nRoundCards_++;
        }
        else {
            if (nRoundCards_ > 0) {
                hand.add(meld_.get(0));
                meld_.remove(0);
                nRoundCards_--;
            }
            
            meld_.add(card);
        }
        
        hand.discard(card);
    }
    
    // Plays a set of cards with the same rank
    // Note round cards are always placed at the front of the meld
    public void play(CardBank hand, Card[] cards)
    {
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            
            if (isRoundCard(card)) {
                meld_.add(0, card);
                nRoundCards_++;
            }
            else {
                meld_.add(card);
            }
            
            hand.discard(card);
        }
    }
    
    // Checks that the meld exists and if the card is appropriate for the meld
    public boolean canAdd(CardBank hand, Card card)
    {
        int     nCards = meld_.size();
        boolean canAdd = nCards > 0;
        
        if (isRoundCard(card)) {
            //  nSuits = Max arraylist size
            int nSuits = Suit.values().length;
            canAdd     = canAdd && (nCards < nSuits);
        }
        else {
            canAdd = canAdd && isViableToAdd(card);
        }
        
        return canAdd;
    }
    
    // Checks all necessary conditions to play a meld. In order these are:
    // All the given cards are different, the meld doesn't already exist, and 
    // that the cards selected are a mix of round cards and viable rank cards.
    public boolean canPlay(CardBank hand, Card[] cards)
    {
        boolean canPlay = allCardsDifferent(cards) && meld_.size() == 0;
        
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            canPlay   = canPlay && isViableToAdd(card);
        }
        
        return canPlay;
    }
    
    // Checks that the card is a round card or has the appropriate rank for the
    // meld.
    private boolean isViableToAdd(Card card)
    {
        boolean isViableToAdd;
        
        if (isRoundCard(card))
            isViableToAdd = true;
        else
            isViableToAdd = card.getRank() == rank_;
        
        return isViableToAdd;
    }
    
    // Checks that all of the cards given are different
    private boolean allCardsDifferent(Card[] cards)
    {
        boolean allCardsDifferent = true;
        boolean completedSearch;
        int     nCards = cards.length;
        
        int i = 0;
        do {
            for (int j = i + 1; j < nCards; j++) {
                if (cards[i].equals(cards[j]))
                    allCardsDifferent = false;
            }
            
            i++;
            completedSearch = !allCardsDifferent || (i == nCards);
        } while (!completedSearch);
        
        return allCardsDifferent;
    }
    
    private boolean isRoundCard(Card card)
    {
        return card.getRank() == roundRank_;
    }
}