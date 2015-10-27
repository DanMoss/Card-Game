package cardgame.games.acestokings;

import java.util.ArrayList;
import java.util.List;

import cardgame.card.Bank;
import cardgame.card.Rank;
import cardgame.player.Player;

class Game
{
    private static final int JOKER_CARD_VALUE = 15;
    
    private final List<Player> players_;
    private final int          nPlayers_;
    private final Board        board_;
    
    // Constructor
    public Game(int nPlayers)
    {
        players_  = new ArrayList<Player>(nPlayers);
        nPlayers_ = nPlayers;
        board_    = new Board(nPlayers);
        createPlayers();
    }
    
    // Plays through the game
    public void play()
    {
        int nRounds = Rank.values().length;
        for (int i = 0; i < nRounds; i++) {
            resetPlayerHands();
            int startingPlayer = board_.initialiseRound(players_);
            playTurns(startingPlayer);
            distributePoints();
        }
        board_.stopListening();
    }
    
    // Resets the {@code players_}' hands to be empty
    private void resetPlayerHands()
    {
        for (int i = 0; i < nPlayers_; i++) {
            List<Bank> list = players_.get(i).getCardBanks();
            list.clear();
            list.add(new Bank(CardBanks.HAND));
        }
    }
    
    // Plays through the turns of a round, starting with {@code startingPlayer}
    private void playTurns(int startingPlayer)
    {
        boolean roundOver;
        int     currentPlayer = startingPlayer;
        do {
            Player player = players_.get(currentPlayer);
            Turn   turn   = new Turn(player, board_);
            turn.play();
            roundOver     = player.findCardBank(CardBanks.HAND).isEmpty();
            currentPlayer = (currentPlayer + 1) % nPlayers_;
        } while (!roundOver);
    }
    
    // Distributes points to the {@code players_} based on the cards left in
    // hand.
    private void distributePoints()
    {
        for (int i = 0; i < nPlayers_; i++) {
            int      points = 0;
            Player   player = players_.get(i);
            Bank hand   = player.findCardBank(CardBanks.HAND);
            int      nCards = hand.size();
            
            for (int j = 0; j < nCards; j++) {
                Rank rank = hand.getCard(j).getRank();
                if (rank == board_.getJokerRank())
                    points += JOKER_CARD_VALUE;
                else
                    points += rank.getValue();
            }
            
            player.getPoints().add(points);
        }
    }
    
    // Creates the players that will play through the game
    private void createPlayers()
    {
        // On the to-do list
    }
    
    // Currently for testing purposes only
    public static void main(String[] args)
    {
        Game   game = new Game(1);
        Player me   = new Player(Player.Type.CONSOLE);
        game.players_.add(me);
        game.play();
    }
}