package cardgame.games.acestokings;

import java.util.ArrayList;
import cardgame.player.Player;
import cardgame.card.Rank;

class AcesToKings
{
    private final ArrayList<Player> players_;
    
    // Constructor
    public AcesToKings()
    {
        players_ = new ArrayList<Player>(1);
    }
    
    // Other methods
    public void play()
    {
        for (Rank roundRank : Rank.values()) {
            Round round = new Round(players_, roundRank);
            round.play();
        }
    }
    
    public static void main(String[] args)
    {
        AcesToKings game = new AcesToKings();
        Player      me   = new Player(Player.Type.CONSOLE);
        
        game.players_.add(me);
        game.play();
    }
}