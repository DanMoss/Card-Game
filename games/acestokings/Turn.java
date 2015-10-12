package cardgame.games.acestokings;

import cardgame.games.acestokings.melds.MeldsManager;
import cardgame.player.Player;
import cardgame.player.PlayerIO;
import cardgame.player.Selector;
import cardgame.card.Card;
import cardgame.card.CardBank;
import cardgame.card.Deck;

class Turn
{
    private enum Action
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
        
        @Override
        public String toString()
        {
            return message_;
        }
        
        public int getNCardsToPlay()
        {
            return nCardsToPlay_;
        }
    }
    
    private final PlayerIO     playerIO_;
    private final CardBank     hand_;
    private final Deck         deck_;
    private final CardBank     discardPile_;
    private final MeldsManager meldsManager_;
    
    // Constructor
    public Turn(Player player, Deck deck, CardBank discardPile,
                MeldsManager meldsManager)
    {
        playerIO_     = player.getPlayerIO();
        hand_         = player.findCardBank(CardBanks.HAND);
        deck_         = deck;
        discardPile_  = discardPile;
        meldsManager_ = meldsManager;
    }
    
    // Other methods
    // Manages the player's turn
    public void play()
    {
        draw();
        boolean turnOver;
        do {
            turnOver = chooseAction();
        } while (!turnOver);
        discard();
    }
    
    // Manages the draw phase of the player's turn
    private void draw()
    {
        String message = "Would you like to draw from the deck, or take the "
                       + discardPile_.getCard(0) + " from the discard pile?";
        playerIO_.sendMessage(message);
        CardBank[] options    = {deck_, discardPile_};
        CardBank   drawSource = Selector.select(playerIO_, options);
        hand_.transferFrom(drawSource, 0, 1);
    }
    
    // Manages the discard phase of the player's turn
    private void discard()
    {
        Card card = chooseCards(1)[0];
        hand_.discard(card);
        discardPile_.add(0, card);
    }
    
    // Prompts the player to choose what to do this turn
    private boolean chooseAction()
    {
        playerIO_.sendMessage("What would you like to do?");
        Action  action = Selector.select(playerIO_, Action.values());
        boolean turnOver;
        
        if (action == Action.END_TURN) {
            turnOver = true;
        }
        else {
            Card[] cards = chooseCards(action.getNCardsToPlay());
            meldsManager_.play(playerIO_, hand_, cards);
            turnOver = false;
        }
        
        return turnOver;
    }
    
    // Prompts the player to choose {@code nCardsToPlay} from their hand
    private Card[] chooseCards(int nCardsToPlay)
    {
        CardBank temp  = new CardBank("", nCardsToPlay);
        Card[]   cards = new Card[nCardsToPlay];
        
        for (int i = 0; i < nCardsToPlay; i++) {
            Card card = Selector.select(playerIO_, hand_.toArray());
            temp.add(card);
            hand_.discard(card);
            cards[i] = card;
        }
        hand_.transferFrom(temp, 0, nCardsToPlay);
        
        return cards;
    }
}