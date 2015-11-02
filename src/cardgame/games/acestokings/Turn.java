package cardgame.games.acestokings;

import java.util.ArrayList;
import java.util.List;

import cardgame.card.CardCollection;
import cardgame.card.Hand;
import cardgame.card.PlayingCard;
import cardgame.player.PlayerIO;
import cardgame.player.Selector;

/**
 * A turn in the game Aces to Kings.
 */
class Turn
{
    private final PlayerIO          playerIO_;
    private final Hand<PlayingCard> hand_;
    private final Board             board_;
    private       PlayingCard       topCardOfDiscards_;
    
    // Constructor
    public Turn(PlayerIO aPlayerIO, Hand<PlayingCard> hand, Board aBoard)
    {
        this.playerIO_          = aPlayerIO;
        this.hand_              = hand;
        this.board_             = aBoard;
        this.topCardOfDiscards_ = aBoard.peekAtDiscards();
    }
    
    // Plays the turn out. Returns true if the hand has no cards left.
    boolean play()
    {
        PlayingCard drawnCard = this.board_.draw(this.playerIO_);
        this.hand_.add(drawnCard);
        this.playerIO_.sendMessage("You drew the " + drawnCard);
        boolean turnOver;
        
        do {
            this.hand_.sort(null);
            turnOver = processAction(chooseAction());
        } while (!turnOver);
        discard();
        
        return this.hand_.size() == 0;
    }
    
    // Discards a {@code PlayingCard} from the player's hand. Note that if the
    // {@code PlayingCard} was drawn from the discard pile this turn, then it
    // can not be discarded this turn.
    private void discard()
    {
        boolean     cannotDiscard;
        PlayingCard aCard;

        do {
            aCard         = chooseCards(1)[0];
            cannotDiscard = aCard.equals(this.topCardOfDiscards_);
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
        int              handSize = this.hand_.size();
        List<TurnAction> options  = TurnAction.findPossibleActions(handSize);
        String message = "What would you like to do?";
        TurnAction choice = Selector.select(this.playerIO_, message, options);
        return choice;
    }
    
    // Prompts the {@code PlayerIO} to choose {@code nCards} from their hand.
    private PlayingCard[] chooseCards(int nCards)
    {
        List<PlayingCard> handCopy = new ArrayList<PlayingCard>();
        int               handSize = this.hand_.size();
        for (int i = 0; i < handSize; i++)
            handCopy.add(this.hand_.get(i));
        
        PlayingCard[] choices = new PlayingCard[nCards];
        String        message = "Which card would you like to play?";
        for (int j = 0; j < nCards; j++) {
            PlayingCard aCard = Selector.select(this.playerIO_, message,
                                                handCopy);
            handCopy.remove(aCard);
            choices[j] = aCard;
        }
        
        return choices;
    }
    
    // Prompts the {@code PlayerIO} to choose the size of the meld.
    private int chooseMeldSize()
    {
        int            maxSize = this.hand_.size();
        List<MeldSize> options = new ArrayList<MeldSize>();
        for (int i = 0; i < maxSize; i++)
            options.add(new MeldSize(i));
        String   message   = "How many cards would you like to use?";
        MeldSize selection = Selector.select(this.playerIO_, message, options);
        return selection.getSize();
    }
    
    // Checks that the player will still be able to discard after playing the
    // specified {@code PlayingCard}s.
    //
    // Essentially this is enforcing that a player can not discard a card they
    // drew from the discard pile on the turn that they drew it.
    private boolean verifyCanDiscardAfter(PlayingCard... cards)
    {
        boolean canDiscardAfter = true;
        
        CardCollection<PlayingCard> temp = new Hand<PlayingCard>("");
        this.hand_.transferTo(temp, cards);
        
        // Only card left in hand is the drawn card
        boolean cardWasInHand = this.hand_.remove(this.topCardOfDiscards_);
        if (cardWasInHand && this.hand_.size() == 0) {
            canDiscardAfter = false;
            this.hand_.add(this.topCardOfDiscards_);
        }
            
        temp.transferTo(this.hand_, cards);
        
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
            
            PlayingCard[] cards = chooseCards(nCardsToPlay);
            if (verifyCanDiscardAfter(cards))
                this.board_.playToMeld(this.playerIO_, this.hand_, cards);
        }
        
        return turnOver;
    }
}
