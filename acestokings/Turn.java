package cardgame.acestokings;

import java.util.ArrayList;
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
        hand_        = player.getCardBank(Round.PLAYER_HAND);
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
        throws RuntimeException
    {
        String message = "You may draw from the deck, or you may take the "
                       + discardPile_.getCard(0) + " from the discard pile.";
        playerIO_.sendMessage(message);
        
        PlayerChoice decision;
        decision = PlayerChoice.makeChoice(playerIO_, PlayerChoice.Type.DRAW);
        
        switch (decision) {
            case DECK:
                hand_.transfer(deck_, 0, 1);
                break;
            
            case DISCARD_PILE:
                hand_.transfer(discardPile_, 0, 1);
                break;
            
            default:
                throw new RuntimeException("Unexpected draw source: "
                                           + decision);
        }
    }
    
    // Manages the discard phase of the player's turn
    private void discard()
    {
        playerIO_.sendMessage("You must discard a card to end your turn.");
        int cardIndex = chooseCard();
        discardPile_.transfer(hand_, cardIndex, 1);
    }
    
    // Methods for managing the playing of cards from the hand
    // Offers the player a choice of what to do this turn
    private boolean playCards()
        throws RuntimeException
    {
        playerIO_.sendMessage("What would you like to do?");
        PlayerChoice decision = makeChoice(playerIO, PlayerChoice.Type.TURN);
        
        if (decision != END_TURN) {
            String message = "Would you like to play to a run of cards of the "
                           + "same suit, or a set of cards of the same face?";
            playerIO_.sendMessage(message);
            PlayerChoice destination = makeChoice(playerIO,
                                           playerChoice.Type.DESTINATION);
        }
        
        boolean turnOver;
        
        switch (decision) {
            case PLAY_ONE:
                playOne(destination);
                turnOver = false;
                break;
            
            case PLAY_THREE:
                // The player must still be able to discard afterwards
                boolean canPlay = hand_.getSize() > MELD_SIZE;
                if (canPlay) {
                    playThree(destination);
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
    private void playOne(PlayerChoice destination)
        throws RuntimeException
    {
        playerIO_.sendMessage("Please select a card.");
        int handIndex = chooseCard();
        
        switch (destination) {
            case RUN:
                playOneToRun(handIndex);
                break;
            
            case SAME_FACE:
                playOneToSameFace(handIndex);
                break;
            
            default:
                throw new RuntimeException("Unexpected destination: "
                                           + destination);
        }
    }
    
    // Chooses three cards and calls the appropriate method to play 
    private void playThree(PlayerChoice destination)
        throws RuntimeException
    {
        playerIO_.sendMessage("Please select three cards.");
        int[] handIndices = new handIndices[MELD_SIZE];
        
        for (int i = 0; i < MELD_SIZE; i++) {
            handIndices[i] = chooseCard();
        }
        
        switch (destination) {
            case RUN:
                playThreeToRun(handIndices);
                break;
            
            case SAME_FACE:
                playThreeToSameFace(handIndices);
                break;
            
            default:
                throw new RuntimeException("Unexpected destination: "
                                           + destination);
        }
    }
    
    // Attempts to play a card to a run of cards
    private void playOneToRun(int handIndex)
    {
        Card.Face face        = hand_.getCard(handIndex).getFace();
        Card.Suit suit        = hand_.getCard(handIndex).getSuit();
        boolean   isRoundCard = face == roundFace_;
        
        face = isRoundCard ? chooseFace() : face;
        suit = isRoundCard ? chooseSuit() : suit;
        
        int     index      = face.getValue() - 1;
        RunMeld meld       = runMelds_[suit.getValue() - 1];
        boolean isPlayable = meld.canAdd(hand_, handIndex, index);
        
        if (isPlayable)
            meld.add(hand_, handIndex, index);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Attempts to play three cards as a run of cards
    private void playThreeToRun(int[] handIndices)
    {
        Card.Face firstFace   = hand_.getCard(handIndices[0]).getFace();
        Card.Suit firstSuit   = hand_.getCard(handIndices[0]).getSuit();
        boolean   isRoundCard = firstFace == roundFace_;
        
        firstFace = isRoundCard ? chooseFace() : firstFace;
        firstSuit = isRoundCard ? chooseSuit() : firstSuit;
        
        int     index      = firstFace.getValue() - 1;
        RunMeld meld       = runMelds_[firstSuit.getValue() - 1];
        boolean isPlayable = meld.canPlay(hand_, handIndices, index);
        
        if (isPlayable)
            meld.play(hand_, handIndices, index);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Attempts to play a card to a set of card with the same face
    private void playOneToSameFace(int handIndex)
    {
        Card.Face face        = hand_.getCard(handIndex).getFace();
        boolean   isRoundCard = face == roundFace_;
        
        face = isRoundCard ? chooseFace() : face;
        
        FaceMeld meld       = faceMelds_[face.getValue() - 1];
        boolean  isPlayable = meld.canAdd(hand_, handIndex);
        
        if (isPlayable)
            meld.add(hand_, handIndex);
        else
            playerIO_.sendMessage("Invalid move.");
    }
    
    // Attempts to play three cards as a set of cards with the same face
    private void playThreeToSameFace(int handIndices)
    {
        Card.Face firstFace   = hand_.getCard(handIndices[0]
        boolean   isRoundCard = face == roundFace_;
        
        firstFace = isRoundCard ? chooseFace() : firstFace;
        
        FaceMeld meld       = faceMelds_[firstFace.getValue() - 1];
        boolean  isPlayable = meld.canPlay(hand_, handIndices);
        
        if (isPlayable)
            meld.play(hand_, handIndices);
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
        PlayerChoice face = PlayerChoice.makeChoice(playerIO_,
                                                    PlayerChoice.Type.FACE);
        Card.Face convertedFace;
        
        switch (face) {
            case ACE:    convertedFace = Card.Face.ACE;    break;
            case TWO:    convertedFace = Card.Face.TWO;    break;
            case THREE:  convertedFace = Card.Face.THREE;  break;
            case FOUR:   convertedFace = Card.Face.FOUR;   break;
            case FIVE:   convertedFace = Card.Face.FIVE;   break;
            case SIX:    convertedFace = Card.Face.SIX;    break;
            case SEVEN:  convertedFace = Card.Face.SEVEN;  break;
            case EIGHT:  convertedFace = Card.Face.EIGHT;  break;
            case NINE:   convertedFace = Card.Face.NINE;   break;
            case TEN:    convertedFace = Card.Face.TEN;    break;
            case JACK:   convertedFace = Card.Face.JACK;   break;
            case QUEEN:  convertedFace = Card.Face.QUEEN;  break;
            case KING:   convertedFace = Card.Face.KING;   break;
            
            default:
                throw new RuntimeException("Unexpected face choice: " + face);
        }
        
        return convertedFace;
    }
    
    // Chooses a suit for a round card to mimic
    private Card.Suit chooseSuit()
    {
        String message = "You have chosen to play a round card. Please select "
                       + "a suit for this card to mimic.";
        playerIO_.sendMessage(message);
        PlayerChoice suit = PlayerChoice.makeChoice(playerIO_,
                                                    PlayerChoice.Type.SUIT);
        Card.Suit convertedSuit;
        
        switch (suit) {
            case CLUBS:     convertedSuit = Card.Suit.CLUBS;     break;
            case DIAMONDS:  convertedSuit = Card.Suit.DIAMONDS;  break;
            case HEARTS:    convertedSuit = Card.Suit.HEARTS;    break;
            case SPADES:    convertedSuit = Card.Suit.SPADES;    break;
            
            default:
                throw new RuntimeException("Unexpected suit choice: " + suit);
        }
        
        return convertedSuit;
    }
    
    // Manages the selection of a card by the player
    private int chooseCard()
        throws RuntimeException
    {
        boolean cardExists;
        int     cardIndex;
        
        do {
            PlayerChoice decision;
            decision = PlayerChoice.makeChoice(playerIO_,
                                               PlayerChoice.Type.CARD);
            
            switch (decision) {
                case CARD_ZERO:   cardIndex = 0;  break;
                case CARD_ONE:    cardIndex = 1;  break;
                case CARD_TWO:    cardIndex = 2;  break;
                case CARD_THREE:  cardIndex = 3;  break;
                case CARD_FOUR:   cardIndex = 4;  break;
                case CARD_FIVE:   cardIndex = 5;  break;
                case CARD_SIX:    cardIndex = 6;  break;
                case CARD_SEVEN:  cardIndex = 7;  break;
                
                default:
                    throw new RuntimeException("Unexpected card choice: "
                                               + decision);
            }
            
            cardExists = (cardIndex < hand_.getSize());
            if (!cardExists)
                playerIO_.sendMessage("You don't have that many cards!");
            
        } while (!cardExists);
        
        return cardIndex;
    }