package cardgame.acestokings;

import cardgame.Card;
import cardgame.CardBank;

class RunMeld
{
    private Card.Suit suit_;
    private Card.Face roundFace_;
    private Card[]    meld_;
    
    // Constructor
    public RunMeld(Card.Suit suit, Card.Face roundFace)
    {
        int nFaces = Card.Face.values().length;
        suit_      = suit;
        roundFace_ = roundFace;
        meld_      = new Card[nFaces];
    }
    
    // Accessors
    public Card.Suit getMeldType()
    {
        return suit_;
    }
    
    // Other methods
    // Adds a single card to the meld and replaces a round card if necessary
    public void add(CardBank hand, Card card, int destinationIndex)
    {
        Card destination = meld_[destinationIndex];
        
        if (!isRoundCard(card) && destination != null)
            hand.add(destination);
        
        meld_[destinationIndex] = card;
        hand.discard(card);
    }
    
    // Plays a run of cards of the same suit, replacing round cards if
    // necessary.
    // Note that {@code cards} should list the cards in ascending order.
    public void play(CardBank hand, Card[] cards, int initialMeldIndex)
    {
        for (int i = 0; i < cards.length; i++) {
            add(hand, cards[i], initialMeldIndex + i);
        }
    }
    
    // Calls isViableToAdd to check the card is appropriate, and then checks if
    // there is an existing meld to attach the card to.
    public boolean canAdd(CardBank hand, Card card, int destinationIndex)
    {
        boolean canAdd    = isViableToAdd(card, destinationIndex);
        int     lastIndex = meld_.length - 1;
        boolean meldBelow;
        boolean meldAbove;
        
        if (destinationIndex == 0) {
            meldBelow = false;
            meldAbove = meld_[destinationIndex + 1] != null;
        }
        else if (destinationIndex == lastIndex) {
            meldBelow = meld_[destinationIndex - 1] != null;
            meldAbove = false;
        }
        else {
            meldBelow = meld_[destinationIndex - 1] != null;
            meldAbove = meld_[destinationIndex + 1] != null;
        }
        
        canAdd = canAdd && (meldBelow || meldAbove);
        
        return canAdd;
    }
    
    // Checks all necessary conditions to play a meld. In order these are:
    // There is space for a meld, that the set of cards is a run, that all the
    // given cards are different, and that the cards are appropriate for the
    // meld.
    // Note that {@code cards} should list the cards in ascending order.
    public boolean canPlay(CardBank hand, Card[] cards, int initialMeldIndex)
    {
        boolean canPlay = isViableRun(hand, cards, initialMeldIndex);
        canPlay         = canPlay && allCardsDifferent(cards);
        
        if (canPlay) {
            for (int i = 0; i < cards.length; i++) {
                Card card      = cards[i];
                int  meldIndex = initialMeldIndex + i;
                canPlay        = canPlay && isViableToAdd(card, meldIndex);
            }
        }
        
        return canPlay;
    }
    
    // Checks that the selected card is of the correct suit and is being added
    // to the correct index (or that the index is free if it is a round card).
    private boolean isViableToAdd(Card card, int destinationIndex)
    {
        boolean isRoundCard  = isRoundCard(card);
        boolean isOfMeldSuit = card.getSuit() == suit_;
        boolean isValidSuit  = isRoundCard || isOfMeldSuit;
        boolean isValidDestination;
        boolean isViableToAdd;
        
        if (isRoundCard) {
            Card destination   = meld_[destinationIndex];
            isValidDestination = destination == null;
        }
        else {
            isValidDestination = (destinationIndex ==
                                  card.getFace().getValue() - 1);
        }
        
        isViableToAdd = isValidSuit && isValidDestination;
        
        return isViableToAdd;
    }
    
    // Checks that there is enough space for the meld, and that the meld is
    // actually a run of cards
    private boolean isViableRun(CardBank hand, Card[] cards,
                                int initialMeldIndex)
    {
        int     availableSlots   = meld_.length - initialMeldIndex;
        boolean isViableRun      = cards.length < availableSlots;
        int     previousCardFace = initialMeldIndex + 1; // Face value = index + 1
        int     currentCardFace;
        
        for (int i = 1; i < cards.length; i++) {
            Card testCard = cards[i];
            
            if (isRoundCard(testCard)) {
                currentCardFace = previousCardFace + 1;
            }
            else {
                currentCardFace = testCard.getFace().getValue();
                isViableRun     = isViableRun && (currentCardFace -
                                                  previousCardFace == 1);
            }
            
            previousCardFace = currentCardFace;
        }
        
        return isViableRun;
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
        return card.getFace() == roundFace_;
    }
}