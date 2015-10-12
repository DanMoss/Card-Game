package cardgame.games.acestokings;

import java.util.ArrayList;
import cardgame.games.acestokings.melds.MeldsManager;
import cardgame.player.Player;
import cardgame.player.Points;
import cardgame.card.Rank;
import cardgame.card.Suit;
import cardgame.card.Card;
import cardgame.card.CardBank;
import cardgame.card.Deck;

class Round
{
    private static final int HAND_SIZE        =  7;
    private static final int JOKER_CARD_VALUE = 15;
    
    private final ArrayList<Player> players_;
    private final Rank              jokerRank_;
    private final Deck              deck_;
    private final CardBank          discardPile_;
    private final MeldsManager      meldsManager_;
    
    // Constructor
    public Round(ArrayList<Player> players, Rank jokerRank)
    {
        players_      = players;
        jokerRank_    = jokerRank;
        deck_         = new Deck(CardBanks.DECK);
        int nRanks    = Rank.values().length;
        int nSuits    = Suit.values().length;
        discardPile_  = new CardBank(CardBanks.DISCARD_PILE, nRanks * nSuits);
        meldsManager_ = new MeldsManager(jokerRank);
    }
    
    // Other methods
    // Starts play of the round
    public void play()
    {
        deal();
        int startingPlayer = findStartingPlayer();
        playTurns(startingPlayer);        
        distributePoints();
    }
    
    // Deals HAND_SIZE cards to each player, then takes the top card of the
    // deck and places it on the discard pile.
    private void deal()
    {
        deck_.shuffle();
        for (int i = 0; i < players_.size(); i++) {
            Player   player = players_.get(i);
            CardBank hand   = new CardBank(CardBanks.HAND, HAND_SIZE + 1); // Revisit hand size
            hand.transferFrom(deck_, 0, HAND_SIZE);
            
            ArrayList<CardBank> cardBanks = player.getCardBanks();
            cardBanks.clear();
            cardBanks.add(hand);
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
            Player player = players_.get(currentPlayer);
            Turn   turn   = new Turn(player, deck_, discardPile_,
                                     meldsManager_);
            turn.play();
            fillDeckIfNecessary();
            roundOver     = player.findCardBank(CardBanks.HAND).isEmpty();
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
            Player   player = players_.get(i);
            CardBank hand   = player.findCardBank(CardBanks.HAND);
            
            for (int j = 0; j < hand.size(); j++) {
                Rank rank = hand.getCard(j).getRank();
                if (rank == jokerRank_)
                    points += JOKER_CARD_VALUE;
                else
                    points += rank.getValue();
            }
            
            player.getPoints().add(points);
        }
    }
}