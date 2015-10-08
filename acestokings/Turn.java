package cardgame.acestokings;

import java.util.ArrayList;
import cardgame.Deck;
import cardgame.Player;
import cardgame.PlayerIO;
import cardgame.CardBank;
import cardgame.Card;

class Turn
{
    private static final int MELD_SIZE = 3;
    
    private final PlayerIO   playerIO_;
    private final CardBank   hand_;
    private final Card.Face  roundFace_;
    private final Deck       deck_;
    private final CardBank   discardPile_;
    private final FaceMeld[] faceMelds_;
    private final RunMeld[]  runMelds_;
    
    // Constructor
    public Turn(Player player, Card.Face roundFace, Deck deck,
                CardBank discardPile, FaceMeld[] faceMelds, RunMeld[] runMelds)
    {
        playerIO_    = player.getPlayerIO();
        hand_        = player.findCardBank(Round.PLAYER_HAND);
        roundFace_   = roundFace;
        deck_        = deck;
        discardPile_ = discardPile;
        faceMelds_   = faceMelds;
        runMelds_    = runMelds;
    }
    
    // Other methods
    // Starts play of the turn
    public void play()
    {
        boolean turnOver;
        draw();
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
    
    // Methods for managing the playing of cards from the hand
    // Offers the player a choice of what to do this turn
    private boolean playCards()
        throws RuntimeException
    {
        playerIO_.sendMessage("What would you like to do?");
        PlayerChoice decision = PlayerChoice.makeChoice(playerIO_,
                                    PlayerChoice.Type.TURN);
        boolean      turnOver;
        
        switch (decision) {
            case PLAY_ONE:
                // The player must still be able to discard afterwards
                boolean canPlayOne = hand_.size() > 1;
                
                if (canPlayOne) {
                    playOne();
                }
                else {
                    String message = "You must be able to discard to end your "
                                   + "turn!";
                    playerIO_.sendMessage(message);
                }
                
                turnOver = false;
                break;
            
            case PLAY_THREE:
                boolean canPlayThree = hand_.size() > MELD_SIZE;
                
                if (canPlayThree) {
                    playThree();
                }
                else {
                    String message = "You must be able to discard to end your "
                                   + "turn!";
                    playerIO_.sendMessage(message);
                }
                
                turnOver = false;
                break;
            
            case END_TURN:
                turnOver = true;
                break;
            
            default:
                throw new RuntimeException("Unexpected turn action: "
                                           + decision);
        }
        
        return turnOver;
    }
    
    // Chooses a card and calls the appropriate method to play it
    private void playOne()
        throws RuntimeException
    {
        String message = "Would you like to play to a run of cards of the "
                       + "same suit, or a set of cards of the same face?";
        playerIO_.sendMessage(message);
        PlayerChoice destination = PlayerChoice.makeChoice(playerIO_,
                                       PlayerChoice.Type.DESTINATION);
        
        playerIO_.sendMessage("Please select a card.");
        Card card = Selector.select(playerIO_, hand_.toArray());
        
        switch (destination) {
            case RUN:
                playOneToRun(card);
                break;
            
            case SAME_FACE:
                playOneToSameFace(card);
                break;
            
            default:
                throw new RuntimeException("Unexpected destination: "
                                           + destination);
        }
    }
    
    // Chooses three cards and calls the appropriate method to play 
    private void playThree()
        throws RuntimeException
    {
        String message = "Would you like to play to a run of cards of the "
                       + "same suit, or a set of cards of the same face?";
        playerIO_.sendMessage(message);
        PlayerChoice destination = PlayerChoice.makeChoice(playerIO_,
                                       PlayerChoice.Type.DESTINATION);
        
        playerIO_.sendMessage("Please select three cards.");
        Card[] cards = new Card[MELD_SIZE];
        
        for (int i = 0; i < MELD_SIZE; i++) {
            cards[i] = Selector.select(playerIO_, hand_.toArray());
        }
        
        switch (destination) {
            case RUN:
                playThreeToRun(cards);
                break;
            
            case SAME_FACE:
                playThreeToSameFace(cards);
                break;
            
            default:
                throw new RuntimeException("Unexpected destination: "
                                           + destination);
        }
    }
    
    // Attempts to play a card to a run of cards
    private void playOneToRun(Card card)
    {
        Card.Face face        = card.getFace();
        Card.Suit suit        = card.getSuit();
        boolean   isRoundCard = face == roundFace_;
        
        face = isRoundCard ? chooseFace() : face;
        suit = isRoundCard ? chooseSuit() : suit;
        
        int     index      = face.getValue() - 1;
        RunMeld meld       = runMelds_[suit.getValue() - 1];
        boolean isPlayable = meld.canAdd(hand_, card, index);
        
        if (isPlayable)
            meld.add(hand_, card, index);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Attempts to play three cards as a run of cards
    private void playThreeToRun(Card[] cards)
    {
        Card.Face firstFace   = cards[0].getFace();
        Card.Suit firstSuit   = cards[0].getSuit();
        boolean   isRoundCard = firstFace == roundFace_;
        
        firstFace = isRoundCard ? chooseFace() : firstFace;
        firstSuit = isRoundCard ? chooseSuit() : firstSuit;
        
        int     index      = firstFace.getValue() - 1;
        RunMeld meld       = runMelds_[firstSuit.getValue() - 1];
        boolean isPlayable = meld.canPlay(hand_, cards, index);
        
        if (isPlayable)
            meld.play(hand_, cards, index);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Attempts to play a card to a set of card with the same face
    private void playOneToSameFace(Card card)
    {
        Card.Face face        = card.getFace();
        boolean   isRoundCard = face == roundFace_;
        
        face = isRoundCard ? chooseFace() : face;
        
        FaceMeld meld       = faceMelds_[face.getValue() - 1];
        boolean  isPlayable = meld.canAdd(hand_, card);
        
        if (isPlayable)
            meld.add(hand_, card);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Attempts to play three cards as a set of cards with the same face
    private void playThreeToSameFace(Card[] cards)
    {
        Card.Face firstFace   = cards[0].getFace();
        boolean   isRoundCard = firstFace == roundFace_;
        
        firstFace = isRoundCard ? chooseFace() : firstFace;
        
        FaceMeld meld       = faceMelds_[firstFace.getValue() - 1];
        boolean  isPlayable = meld.canPlay(hand_, cards);
        
        if (isPlayable)
            meld.play(hand_, cards);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Methods for managing round cards
    // Chooses a face for a round card to mimic
    private Card.Face chooseFace()
    {
        String message = "You have chosen to play a round card. Please select "
                       + "a face for this card to mimic.";
        playerIO_.sendMessage(message);
        return Selector.select(playerIO_, Card.Face.values());
    }
    
    // Chooses a suit for a round card to mimic
    private Card.Suit chooseSuit()
    {
        String message = "You have chosen to play a round card. Please select "
                       + "a suit for this card to mimic.";
        playerIO_.sendMessage(message);
        return Selector.select(playerIO_, Card.Suit.values());
    }
}