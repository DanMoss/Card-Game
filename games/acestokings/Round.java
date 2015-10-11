package cardgame.games.acestokings;

import cardgame.player.Player;
import cardgame.card.CardBank;
import cardgame.card.Deck;
import cardgame.card.Card;
import cardgame.card.Rank;
import cardgame.card.Suit;
import cardgame.player.Points;
import cardgame.games.acestokings.melds.*;
import java.util.ArrayList;

public class Round //public is temporary until the below are moved
{
    public static final String DECK         = "Deck"; // Move to their own class
    public static final String DISCARD_PILE = "Discard Pile";
    public static final String PLAYER_HAND  = "Hand";
    
    private static final int STARTING_HAND_SIZE = 7;
    private static final int JOKER_CARD_VALUE   = 15;
    
    private final ArrayList<Player> players_;
    private final Rank              jokerRank_;
    private final Deck              deck_;
    private final CardBank          discardPile_;
    private final RankMeld[]        rankMelds_;
    private final RunMeld[]         runMelds_;
    
    // Constructor
    public Round(ArrayList<Player> players, Rank jokerRank)
    {
        players_     = players;
        jokerRank_   = jokerRank;
        deck_        = new Deck(DECK);
        int nRanks   = Rank.values().length;
        int nSuits   = Suit.values().length;
        discardPile_ = new CardBank(DISCARD_PILE, nRanks * nSuits);
        rankMelds_   = new RankMeld[nRanks];
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
        for (Rank rank : Rank.values()) {
            rankMelds_[i] = new RankMeld(rank, jokerRank_);
            i++;
        }
        
        int j = 0;
        for (Suit suit : Suit.values()) {
            runMelds_[j] = new RunMeld(suit, jokerRank_);
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
        int roundNumber    = jokerRank_.getValue();
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
            Turn   turn = new Turn(p, jokerRank_, deck_, discardPile_,
                                   rankMelds_, runMelds_);
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
            deck_.transferFrom(discardPile_, 1, discardPile_.size() - 1);
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
                Rank rank = hand.getCard(j).getRank();
                if (rank == jokerRank_)
                    points += JOKER_CARD_VALUE;
                else
                    points += rank.getValue();
            }
            p.getPoints().add(points);
        }
    }
}