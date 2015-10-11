package cardgame.games.acestokings;

import java.util.ArrayList;
import cardgame.card.Deck;
import cardgame.player.Player;
import cardgame.player.PlayerIO;
import cardgame.card.CardBank;
import cardgame.card.Card;
import cardgame.card.Rank;
import cardgame.card.Suit;
import cardgame.games.acestokings.melds.*;

class Turn
{
    private final Player     player_;
    private final PlayerIO   playerIO_;
    private final CardBank   hand_;
    private final Rank       jokerRank_;
    private final Deck       deck_;
    private final CardBank   discardPile_;
    private final RankMeld[] rankMelds_;
    private final RunMeld[]  runMelds_;
    
    // Constructor
    public Turn(Player player, Rank jokerRank, Deck deck,
                CardBank discardPile, RankMeld[] rankMelds, RunMeld[] runMelds)
    {
        player_      = player;
        playerIO_    = player.getPlayerIO();
        hand_        = player.findCardBank(CardBanks.HAND);
        jokerRank_   = jokerRank;
        deck_        = deck;
        discardPile_ = discardPile;
        rankMelds_   = rankMelds;
        runMelds_    = runMelds;
    }
    
    // Other methods
    // Starts play of the turn
    public void play()
    {
        draw();
        boolean turnOver;
        do {
            turnOver = playCards();
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
        playerIO_.sendMessage("You must discard a card to end your turn.");
        Card card = Selector.select(playerIO_, hand_.toArray());
        hand_.discard(card);
        discardPile_.add(0, card);
    }
    
    // Offers the player a choice of what to do this turn
    private boolean playCards()
        throws RuntimeException
    {
        playerIO_.sendMessage("What would you like to do?");
        TurnOption action = Selector.select(playerIO_, TurnOption.values());
        boolean    turnOver;
        
        if (action == TurnOption.END_TURN) {
            turnOver = true;
        }
        else {
            Card[]       cards = chooseCards(action.getNCardsToPlay());
            AbstractMeld meld  = findDestinationMeld(cards[0]);
            meld.play(player_, cards);
            turnOver = false;
        }
        
        return turnOver;
    }
    
    // Finds which meld the player wants to target with {@code firstCard}
    private AbstractMeld findDestinationMeld(Card firstCard)
        throws RuntimeException
    {
        playerIO_.sendMessage("Where would you like to play to?");
        MeldType type    = Selector.select(playerIO_, MeldType.values());
        boolean  isJoker = firstCard.getRank() == jokerRank_;
        AbstractMeld meld;
        
        switch (type) {
            case RANK:
                Rank meldRank = isJoker ? chooseRank() : firstCard.getRank();
                meld = rankMelds_[meldRank.getValue() - 1];
                break;
            
            case RUN:
                Suit meldSuit = isJoker ? chooseSuit() : firstCard.getSuit();
                meld  = runMelds_[meldSuit.getValue() - 1];
                break;
            
            default:
                throw new RuntimeException("Unexpected MeldType: " + type);
        }
        
        return meld;
    }
    
    // Promps the player to choose {@code nCardsToPlay} from their hand, though
    // not necessarily different cards
    private Card[] chooseCards(int nCardsToPlay)
    {
        Card[] cards = new Card[nCardsToPlay];
        
        for (int i = 0; i < nCardsToPlay; i++)
            cards[i] = Selector.select(playerIO_, hand_.toArray());
        
        return cards;
    }
    
    // Methods for managing jokers
    // Chooses a rank for a joker to mimic
    private Rank chooseRank()
    {
        String message = "You have chosen to play a joker. Please select a "
                       + "rank for this card to mimic.";
        playerIO_.sendMessage(message);
        return Selector.select(playerIO_, Rank.values());
    }
    
    // Chooses a suit for a joker to mimic
    private Suit chooseSuit()
    {
        String message = "You have chosen to play a joker. Please select a "
                       + "suit for this card to mimic.";
        playerIO_.sendMessage(message);
        return Selector.select(playerIO_, Suit.values());
    }
}