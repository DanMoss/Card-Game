package cardgame.acestokings;

import cardgame.Player;
import cardgame.CardBank;
import cardgame.Deck;
import cardgame.Card;
import cardgame.Points;
import java.util.ArrayList;

class Round
{
    public static final String DECK         = "Deck"; // Move to their own class
    public static final String DISCARD_PILE = "Discard Pile";
    public static final String PLAYER_HAND  = "Hand";
    
    private static final int STARTING_HAND_SIZE = 7;
    private static final int ROUND_CARD_VALUE   = 15;
    
    private final ArrayList<Player> players_;
    private final Card.Face         roundFace_;
    private final Deck              deck_;
    private final CardBank          discardPile_;
    private final FaceMeld[]        faceMelds_;
    private final RunMeld[]         runMelds_;
    
    // Constructor
    public Round(ArrayList<Player> players, Card.Face roundFace)
    {
        players_     = players;
        roundFace_   = roundFace;
        deck_        = new Deck(DECK);
        int nFaces   = Card.Face.values().length;
        int nSuits   = Card.Suit.values().length;
        discardPile_ = new CardBank(DISCARD_PILE, nFaces * nSuits);
        faceMelds_   = new FaceMeld[nFaces];
        runMelds_    = new RunMeld[nSuits];
    }
    
    // Other methods
    // Starts play of the round
    public void play()
    {
        createMelds();
        deal();
        int startingPlayer = findStartingPlayer();
        playTurns(startingPlayer);        
        distributePoints();
    }
    
    // Creates the meld holders
    private void createMelds()
    {
        int i = 0;
        for (Card.Face face : Card.Face.values()) {
            faceMelds_[i] = new FaceMeld(face, roundFace_);
            i++;
        }
        
        int j = 0;
        for (Card.Suit suit : Card.Suit.values()) {
            runMelds_[j] = new RunMeld(suit, roundFace_);
            j++;
        }
    }
    
    // Deals STARTING_HAND_SIZE cards to each player, then takes the top card
    // of the deck and places it on the discard pile.
    private void deal()
    {
        deck_.shuffle();
        for (int i = 0; i < players_.size(); i++) {
            Player   p    = players_.get(i);
            CardBank hand = new CardBank(PLAYER_HAND, STARTING_HAND_SIZE + 1);
            hand.transferFrom(deck_, 0, STARTING_HAND_SIZE);
            
            ArrayList<CardBank> pCardBanks = p.getCardBanks();
            pCardBanks.clear();
            pCardBanks.add(hand);
        }
        discardPile_.transferFrom(deck_, 0, 1);
    }
    
    // Finds the players who plays first
    private int findStartingPlayer()
    {
        int roundNumber    = roundFace_.getValue();
        // Correcting for player 0 to play first in the first round.
        int startingPlayer = (roundNumber - 1) % players_.size();
        
        return startingPlayer;
    }
    
    // Loop method to let players have their turns
    private void playTurns(int startingPlayer)
    {
        boolean roundOver;
        int     currentPlayer = startingPlayer;
        do {
            Player p    = players_.get(currentPlayer);
            Turn   turn = new Turn(player, roundFace_, deck_, discardPile_,
                                   faceMelds_, runMelds_);
            turn.play();
            fillDeckIfNecessary();
            roundOver     = p.findCardBank(PLAYER_HAND).isEmpty();
            currentPlayer = (currentPlayer + 1) % players_.size();
        } while (!roundOver);
    }
    
    // Shuffles the discard pile into the deck when it is out of cards
    private void fillDeckIfNecessary()
    {
        if (deck_.isEmpty()) {
            deck_.transferFrom(discardPile_, 1, discardPile_.getSize() - 1);
            deck_.shuffle();
        }
    }
    
    // Distributes points to the players based on the cards left in hand
    private void distributePoints()
    {
        for (int i = 0; i < players_.size(); i++) {
            int      points = 0;
            Player   p      = players_.get(i);
            CardBank hand   = p.findCardBank(PLAYER_HAND);
            
            for (int j = 0; j < hand.size(); j++) {
                Card.Face face = hand.getCard(j).getFace();
                if (face == roundFace_)
                    points += ROUND_CARD_VALUE;
                else
                    points += face.getValue();
            }
            player.getPoints().add(points);
        }
    }
}