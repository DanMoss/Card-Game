package cardgame.games.acestokings;

import cardgame.card.Card;
import cardgame.card.Bank;
import cardgame.games.acestokings.melds.MeldsManager;
import cardgame.player.Player;
import cardgame.player.PlayerIO;
import cardgame.player.Selectable;
import cardgame.player.Selector;

class Turn
{
    private enum Action
        implements Selectable
    {
        END_TURN ("End your turn and discard.", 0),
        ADD_CARD ("Add a card to a set.", 1),
        PLAY_MELD("Play a set of cards.", MeldsManager.MINIMUM_MELD_SIZE);
        
        private final String message_;
        private final int    nCardsToPlay_;
        
        // Constructor
        private Action(String message, int nCardsToPlay)
        {
            message_      = message;
            nCardsToPlay_ = nCardsToPlay;
        }
        
        // Accessor
        public int getNCardsToPlay()
        {
            return nCardsToPlay_;
        }
        
        // Implementation of Selectable
        // Used by Selector to convey the option to the player
        public String getMessage()
        {
            return message_;
        }
        
        // Finds the possible actions a player may be able to make on their
        // turn, keeping in mind that they must be able to discard at the end.
        public static Action[] findOptions(Bank hand)
        {
            Action[] allActions = values();
            int      nOptions   = 0;
            
            for (Action action : allActions) {
                boolean canDiscard = action.getNCardsToPlay() < hand.size();
                if (canDiscard)
                    nOptions++;
            }
            
            Action[] options = new Action[nOptions];
            for (int i = 0; i < nOptions; i++)
                options[i] = allActions[i];
            
            return options;
        }
    }
    
    private final PlayerIO playerIO_;
    private final Bank hand_;
    private final Board    board_;
    private final Card     discardPickUp_;
    
    // Constructor
    public Turn(Player player, Board board)
    {
        playerIO_      = player.getPlayerIO();
        hand_          = player.findBank(CardBanks.HAND);
        board_         = board;
        discardPickUp_ = board_.getDiscards().getCard(0);
    }
    
    // Other methods
    // Manages the player's turn
    public void play()
    {
        draw();
        boolean turnOver;
        do {
            hand_.sort();
            turnOver = chooseAction();
        } while (!turnOver);
        discard();
    }
    
    // Manages the draw phase of the player's turn
    private void draw()
    {
        Bank discards = board_.getDiscards();
        String   message  = "Would you like to take the " + discardPickUp_
                          + " from the discard pile, or draw from the deck?";
        playerIO_.sendMessage(message);
        
        Bank[] options    = {board_.getDeck(), discards};
        Bank   drawSource = Selector.select(playerIO_, options);
        
        hand_.transferFrom(drawSource, 0, 1);
    }
    
    // Manages the discard phase of the player's turn
    private void discard()
    {
        boolean isDiscardPickUp;
        Card    card;
        do {
            card            = chooseCards(1)[0];
            isDiscardPickUp = card.equals(discardPickUp_);
            if (isDiscardPickUp) {
                String message = "You may not discard a card you drew this "
                               + "turn from the discard pile.";
                playerIO_.sendMessage(message);
            }
        } while (isDiscardPickUp);
        hand_.discard(card);
        board_.getDiscards().add(0, card);
    }
    
    // Prompts the player to choose what to do this turn
    private boolean chooseAction()
    {
        playerIO_.sendMessage("What would you like to do?");
        Action  action = Selector.select(playerIO_, Action.findOptions(hand_));
        boolean turnOver;
        
        if (action == Action.END_TURN) {
            turnOver = true;
        }
        else {
            Card[] cards = chooseCards(action.getNCardsToPlay());
            if (canDiscardAfterwards(cards))
                board_.getMelds().play(playerIO_, hand_, cards);
            turnOver = false;
        }
        
        return turnOver;
    }
    
    // Prompts the player to choose {@code nCards} from their hand
    private Card[] chooseCards(int nCards)
    {
        Bank temp  = new Bank("");
        Card[]   cards = new Card[nCards];
        
        for (int i = 0; i < nCards; i++) {
            Card card = Selector.select(playerIO_, hand_.toArray());
            temp.add(card);
            hand_.discard(card);
            cards[i] = card;
        }
        hand_.transferFrom(temp, 0, nCards);
        
        return cards;
    }
    
    // Checks that the player will still be able to discard if they drew from
    // the discard pile.
    private boolean canDiscardAfterwards(Card... cards)
    {
        Bank temp = new Bank("");
        for (Card card : cards) {
            temp.add(card);
            hand_.discard(card);
        }
        boolean canDiscard = hand_.size() > 1
                          || !(hand_.getCard(0).equals(discardPickUp_));
        hand_.transferFrom(temp, 0, temp.size());
        return canDiscard;
    }
}