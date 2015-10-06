package cardgame.acestokings;

import java.util.ArrayList;
import cardgame.Card;
import cardgame.CardBank;

class FaceMeld
{
    private Card.Face       face_;
    private Card.Face       roundFace_;
    private ArrayList<Card> meld_;
    private int             nRoundCards_;
    
    // Constructor
    public FaceMeld(Card.Face face, Card.Face roundFace)
    {
        int nSuits   = Card.Suit.values().length;
        face_        = face;
        roundFace_   = roundFace;
        meld_        = new ArrayList<Card>(nSuits);
        nRoundCards_ = 0;
    }
    
    // Accessors
    public Card.Face getMeldType()
    {
        return face_;
    }
    
    // Other methods
    // Adds a single card to the meld and replaces a round card if necessary
    // Note round cards are always placed at the front of the meld
    public void add(CardBank hand, int handIndex)
    {
        Card card = hand.getCard(handIndex);
        
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
        
        hand.discard(handIndex);
    }
    
    // Plays a set of cards with the same face
    // Note round cards are always placed at the front of the meld
    public void play(CardBank hand, int[] handIndices)
    {
        for (int i = 0; i < handIndices.length; i++) {
            Card card = hand.getCard(handIndices[i]);
            
            if (isRoundCard(card)) {
                meld_.add(0, card);
                nRoundCards_++;
            }
            else {
                meld_.add(card);
            }
            
            hand.discard(handIndices[i]);
        }
    }
    
    // Checks that the meld exists and if the card is appropriate for the meld
    public boolean canAdd(CardBank hand, int handIndex)
    {
        Card    card   = hand.getCard(handIndex);
        int     nCards = meld_.size();
        boolean canAdd = nCards > 0;
        
        if (isRoundCard(card)) {
            //  nSuits = Max arraylist size
            int nSuits = Card.Suit.values().length;
            canAdd     = canAdd && (nCards < nSuits);
        }
        else {
            canAdd = canAdd && isViableToAdd(card);
        }
        
        return canAdd;
    }
    
    // Checks all necessary conditions to play a meld. In order these are:
    // All the given cards are different, the meld doesn't already exist, and 
    // that the cards selected are a mix of round cards and viable face cards.
    public boolean canPlay(CardBank hand, int[] handIndices)
    {
        boolean canPlay = allCardsDifferent(handIndices) && meld_.size() == 0;
        
        for (int i = 0; i < handIndices.length; i++) {
            Card card = hand.getCard(handIndices[i]);
            canPlay   = canPlay && isViableToAdd(card);
        }
        
        return canPlay;
    }
    
    // Checks that the card is a round card or has the appropriate face for the
    // meld.
    private boolean isViableToAdd(Card card)
    {
        boolean isViableToAdd;
        
        if (isRoundCard(card))
            isViableToAdd = true;
        else
            isViableToAdd = card.getFace() == face_;
        
        return isViableToAdd;
    }
    
    // Checks that all of the cards given are different
    private boolean allCardsDifferent(int[] handIndices)
    {
        boolean allCardsDifferent = true;
        boolean completedSearch;
        int     nCards = handIndices.length;
        int     i      = 0;
        
        do {
            for (int j = i + 1; j < nCards; j++) {
                if (handIndices[i] == handIndices[j])
                    allCardsDifferent = false;
            }
            
            i++;
            completedSearch = !allCardsDifferent || (i == nCards);
        } while (!completedSearch);
        
        return allCardsDifferent;
    }
    
    private boolean isRoundCard(Card card)
    {
        return card.getFace() == roundFace_;
    }
}