package cardgame.games.acestokings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cardgame.card.Hand;
import cardgame.card.PlayingCard;
import cardgame.card.Rank;
import cardgame.player.ConsolePlayerIO;
import cardgame.player.Player;
import cardgame.player.PlayerIO;

/**
 * The card game Aces to Kings
 */
public class Game
{
    private static final int    JOKER_CARD_VALUE = 15;
    private static final int    ROUNDS_TO_PLAY   = 13;
    private static final String PLAYER_HAND      = "Hand";
    
    private final List<Player<PlayingCard>> players_;
    private final Board                     board_;
    private int                             startingPlayer_;
    
    // Constructor
    public Game(int nPlayers)
    {
        this.players_        = new ArrayList<Player<PlayingCard>>(nPlayers);
        this.board_          = new Board();
        Random rng           = new Random();
        this.startingPlayer_ = rng.nextInt(nPlayers);
        createPlayers(nPlayers);
    }
    
    // Plays through the game
    public void play()
    {
        int nPlayers = this.players_.size();
        for (int i = 0; i < Game.ROUNDS_TO_PLAY; i++) {
            this.board_.setUpNextRound();
            for (Player<PlayingCard> aPlayer : this.players_)
                this.board_.dealInitialHand(aPlayer.findHand(Game.PLAYER_HAND));
            playTurns();
            distributePoints();
            this.startingPlayer_ = (this.startingPlayer_ + 1) % nPlayers;
        }
    }
    
    // Plays through the turns of a round, starting with {@code startingPlayer}
    private void playTurns()
    {
        boolean roundOver;
        int     currentPlayer = startingPlayer_;
        int     nPlayers      = this.players_.size();
        do {
            Player<PlayingCard> aPlayer  = this.players_.get(currentPlayer);
            PlayerIO            playerIO = aPlayer.getPlayerIO();
            
            Hand<PlayingCard> hand = aPlayer.findHand(Game.PLAYER_HAND);
            Turn              turn = new Turn(playerIO, hand, this.board_);
            roundOver              = turn.play();
            currentPlayer          = (currentPlayer + 1) % nPlayers;
        } while (!roundOver);
    }
    
    // For each {@code Player}, discards the remaining {@code PlayingCard}s in
    // their hand and adds {@code Points} to the {@code Player}'s total
    // depending on the {@code Rank}s of the {@code PlayingCard}s.
    private void distributePoints()
    {
        for (Player<PlayingCard> aPlayer : this.players_) {
            int               points = 0;
            Hand<PlayingCard> hand   = aPlayer.findHand(Game.PLAYER_HAND);
            int               nCards = hand.size();
            
            for (int i = 0; i < nCards; i++) {
                PlayingCard aCard = hand.get(0);
                Rank        aRank = aCard.getRank();
                hand.remove(aCard);
                if (aRank == Rank.JOKER)
                    points += Game.JOKER_CARD_VALUE;
                else
                    points += aRank.getValue();
            }
            
            aPlayer.modifyPoints(points);
        }
    }
    
    // Creates the players that will play through the game
    private void createPlayers(int nPlayers)
    {
        for (int i = 0; i < nPlayers; i++) {
            PlayerIO            type    = new ConsolePlayerIO();
            Player<PlayingCard> aPlayer = new Player<PlayingCard>(type, 0, 0);
            aPlayer.addHand(new Hand<PlayingCard>(Game.PLAYER_HAND));
            this.players_.add(aPlayer);
        }
    }
    
    // Currently for testing purposes only
    public static void main(String[] args)
    {
        Game   game = new Game(1);
        game.play();
    }
}
