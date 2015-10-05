package cardgame;

import java.util.ArrayList;

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
    public CardBank getCardBank(String name)
    {
        int     nCardBanks = cardBanks_.size();
        boolean isFound    = false;
        String  testName;
        
        int i = 0;
        while (!(isFound) && (i < nCardBanks)) {
            testName = cardBanks_.get(i).getName();
            
            if (testName.equals(name))
                isFound = true;
            else
                i++;
        }
        
        return cardBanks_.get(i);
    }
    
    public Points getPoints()
    {
        return points_;
    }
    
    public PlayerIO getPlayerIO()
    {
        return playerIO_;
    }
    
    private void setPlayerIO(Type type)
    {
        switch (type) {
            case CONSOLE:
                playerIO_ = new ConsolePlayerIO();
                break;
            
            case SKYPE:
                playerIO_ = new SkypePlayerIO();
                break;
            
            default:
                break;
        }
    }
    
    // Other methods
    public void addCardBank(CardBank cardBank)
    {
        cardBanks_.add(cardBank);
    }
    
    public void wipeCardBanks()
    {
        cardBanks_.clear();
    }
}