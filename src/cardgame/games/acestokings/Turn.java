package cardgame.games.acestokings;

import cardgame.card.Bank;
import cardgame.card.Card;
import cardgame.card.CardCollection;
import cardgame.player.PlayerIO;
import cardgame.player.Selector;

/**
 * A turn in the game Aces to Kings.
 */
class Turn
{
    private final PlayerIO playerIO_;
    private final Bank     hand_;
    private final Board    board_;
    private       Card     drawnCard_;
    
    // Constructor
    public Turn(PlayerIO aPlayerIO, Bank hand, Board aBoard)
    {
        this.playerIO_ = aPlayerIO;
        this.hand_     = hand;
        this.board_    = aBoard;
    }
    
    // Plays the turn out. Returns true if the hand has no cards left.
    boolean play()
    {
        this.drawnCard_ = this.board_.draw(this.playerIO_);
        this.hand_.add(this.drawnCard_);
        this.playerIO_.sendMessage("You drew the " + this.drawnCard_);
        boolean turnOver;
        
        do {
            this.hand_.sort();
            turnOver = processAction(chooseAction());
        } while (!turnOver);
        discard();
        
        return this.hand_.size() == 0;
    }
    
    // Discards a {@code Card} from the player's hand. Note that if the
    // {@code Card} was drawn from the discard pile this turn, then it can not
    // be discarded this turn.
    private void discard()
    {
        boolean cannotDiscard;
        Card    aCard;
        boolean drewFromDiscards = this.board_.checkIfLastDrewFromDiscards();

        do {
            aCard         = chooseCards(1)[0];
            cannotDiscard = drewFromDiscards && aCard.equals(this.drawnCard_);
            if (cannotDiscard) {
                String message = "You may not discard a card you drew this "
                               + "turn from the discard pile.";
                this.playerIO_.sendMessage(message);
            }
        } while (cannotDiscard);
        
        this.hand_.remove(aCard);
        this.board_.addToDiscards(aCard);
    }
    
    // Prompts the {@code PlayerIO} to decide what to do
    private TurnAction chooseAction()
    {
        int          handSize  = this.hand_.size();
        TurnAction[] options   = TurnAction.findPossibleActions(handSize);
        this.playerIO_.sendMessage("What would you like to do?");
        TurnAction   selection = Selector.select(this.playerIO_, options);
        return selection;
    }
    
    // Prompts the {@code PlayerIO} to choose {@code nCards} from their hand.
    //
    // A temporary array is created to hold chosen {@code Card}s, which are
    // removed temporarily to prevent duplicate selections.
    private Card[] chooseCards(int nCards)
    {
        CardCollection temp  = new Bank("");
        Card[]         cards = new Card[nCards];
        
        for (int i = 0; i < nCards; i++) {
            Card aCard = Selector.select(this.playerIO_, this.hand_.toArray());
            cards[i]   = aCard;
            this.hand_.transferTo(temp, aCard);
        }
        temp.transferTo(this.hand_, cards);
        
        return cards;
    }
    
    // Prompts the {@code PlayerIO} to choose the size of the meld.
    private int chooseMeldSize()
    {
        int        maxSize = this.hand_.size();
        MeldSize[] options = new MeldSize[maxSize];
        for (int i = 0; i < maxSize; i++)
            options[i] = new MeldSize(i);
        this.playerIO_.sendMessage("How many cards would you like to use?");
        MeldSize selection = Selector.select(this.playerIO_, options);
        return selection.getSize();
    }
    
    // Checks that the player will still be able to discard after playing the
    // specified {@code Card}s.
    //
    // Essentially this is enforcing that a player can not discard a card they
    // drew from the discard pile on the turn that they drew it.
    private boolean verifyCanDiscardAfter(Card... cards)
    {
        boolean drewFromDiscards = this.board_.checkIfLastDrewFromDiscards();
        boolean canDiscardAfter  = true;
        
        if (drewFromDiscards) {
            CardCollection temp = new Bank("");
            this.hand_.transferTo(temp, cards);
            // Only card left in hand is the drawn card
            if (this.hand_.remove(this.drawnCard_) && this.hand_.size() == 0) {
                canDiscardAfter = false;
                this.hand_.add(this.drawnCard_);
            }
            
            temp.transferTo(this.hand_, cards);
        }
        
        return canDiscardAfter;
    }
    
    // Processes the {@code TurnAction} chosen.
    private boolean processAction(TurnAction anAction) {
        boolean turnOver;
        
        if (anAction == TurnAction.END_TURN) {
            turnOver = true;
        }
        else {
            turnOver = false;
            int nCardsToPlay;
            
            if (anAction == TurnAction.ADD_CARD)
                nCardsToPlay = 1;
            else
                nCardsToPlay = chooseMeldSize();
            
            Card[] cards = chooseCards(nCardsToPlay);
            if (verifyCanDiscardAfter(cards))
                this.board_.playToMeld(this.playerIO_, this.hand_, cards);
        }
        
        return turnOver;
    }
}
