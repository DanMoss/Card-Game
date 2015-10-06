package cardgame.acestokings;

import java.util.ArrayList;
import cardgame.Player;
import cardgame.Card;

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
        for (Card.Face roundFace : Card.Face.values()) {
            Round round = new Round(players_, roundFace);
            round.play();
        }
    }
    
    public static void main(String[] args)
    {
        AcesToKings game = new AcesToKings();
        Player me = new Player(Player.Type.CONSOLE);
        game.players_.add(me);
        game.play();
    }
}