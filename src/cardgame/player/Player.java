package cardgame.player;

import java.lang.Comparable;
import java.util.List;
import java.util.ArrayList;
import cardgame.card.Bank;

public class Player
    implements Comparable<Player>
{
    public enum Type
    {
        GAMEMASTER, AI, CONSOLE, SKYPE;
    }
    
    private static int nPlayers_ = 0;
    
    private String         name_;
    private List<Bank> cardBanks_;
    private PlayerIO       playerIO_;
    private Points         points_;
    
    // Constructors
    public Player(Type type)
    {
        this("Player " + nPlayers_, type, 0, 0);
    }
    
    public Player(String name, Type type)
    {
        this(name, type, 0, 0);
    }
    
    public Player(String name, Type type, int nPoints, int minNPoints)
    {
        name_      = name;
        cardBanks_ = new ArrayList<Bank>();
        points_    = new Points(nPoints, minNPoints);
        setPlayerIO(type);
        nPlayers_++;
    }
    
    // Accessors
    public String getName()
    {
        return name_;
    }
    
    public List<Bank> getCardBanks()
    {
        return cardBanks_;
    }
    
    public PlayerIO getPlayerIO()
    {
        return playerIO_;
    }
    
    public Points getPoints()
    {
        return points_;
    }
    
    // Mutator
    private void setPlayerIO(Type type)
    {
        switch (type) {
            case CONSOLE:
                playerIO_ = new ConsolePlayerIO();
                break;
            
            case SKYPE:
                //playerIO_ = new SkypePlayerIO();
                break;
            
            default:
                break;
        }
    }
    
    // Implementation of Comparable
    // Compares two players, ordering by most points to least. If the players
    // have the same amount of points, then they are sorted alphabetically.
    public int compareTo(Player player)
    {
        int thisPoints   = getPoints().getAmount();
        int playerPoints = player.getPoints().getAmount();
        int result;
        if (thisPoints == playerPoints)
            result = getName().compareTo(player.getName());
        else
            result = thisPoints > playerPoints ? -1 : 1;
        return result;
    }
    
    // Other methods
    public Bank findCardBank(String name)
    {
        int     nCardBanks = cardBanks_.size();
        boolean isFound    = false;
        
        int i = 0;
        while (!isFound && i < nCardBanks) {
            String testName = cardBanks_.get(i).toString();
            
            if (testName.equals(name))
                isFound = true;
            else
                i++;
        }
        
        return cardBanks_.get(i);
    }
}