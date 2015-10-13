package cardgame.player;

import java.util.ArrayList;
import cardgame.card.CardBank;

public class Player
{
    public enum Type
    {
        GAMEMASTER, AI, CONSOLE, SKYPE;
    }
    
    private static int nPlayers_ = 0;
    
    private String              name_;
    private ArrayList<CardBank> cardBanks_;
    private PlayerIO            playerIO_;
    private Points              points_;
    
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
        cardBanks_ = new ArrayList<CardBank>();
        points_    = new Points(nPoints, minNPoints);
        setPlayerIO(type);
        nPlayers_++;
    }
    
    // Accessors
    public String getName()
    {
        return name_;
    }
    
    public ArrayList<CardBank> getCardBanks()
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
    
    // Other methods
    public CardBank findCardBank(String name)
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