//package cardgame.games.acestokings;
package cardgame.player;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import cardgame.player.Player;
import cardgame.player.Points;

class Leaderboard
{
    private final List<Player> players_;
    private final int          maxNameLength_;
    
    public Leaderboard(List<Player> players)
    {
        players_ = new ArrayList<Player>(players.size());
        players_.addAll(players);
        maxNameLength_ = findMaxNameLength(players);
    }
    
    @Override
    public String toString()
    {
        // Sorts most points to least points
        Collections.sort(players_);
        String leaderboard = "The players are ranked as follows:";
        int    nPlayers    = players_.size();
        
        int ranking = 1;
        // The player with the least amount of points is in the best position
        for (int i = nPlayers - 1; i >= 0; i--) {
            Player player  = players_.get(i);
            String name    = player.getName();
            name           = String.format("%1$-" + maxNameLength_ + "s", name);
            int    nPoints = player.getPoints().getAmount();
            leaderboard   += "\n  " + ranking + ": " + name + " with "
                           + nPoints + " points";
            ranking++;
        }
        
        return leaderboard;
    }
    
    private int findMaxNameLength(List<Player> players)
    {
        int maxNameLength = 0;
        for (Player player : players) {
            int nameLength = player.getName().length();
            maxNameLength  = maxNameLength < nameLength ? nameLength
                                                        : maxNameLength;
        }
        return maxNameLength;
    }
    
    public static void main(String[] args)
    {
        Player.Type c = Player.Type.CONSOLE;
        Player p1 = new Player(c);
        Player p2 = new Player(c);
        Player p3 = new Player("Player 3", c, 50, 0);
        Player p4 = new Player("Kelly", c, 2, 0);
        Player p5 = new Player("Bob", c, -100, -100);
        Player p6 = new Player("Aaron", c, -100, -100);
        
        List<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        players.add(p6);
        
        Leaderboard test = new Leaderboard(players);
        System.out.println(test);
    }
}