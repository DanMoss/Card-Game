package cardgame.player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import cardgame.card.Bank;

/**
 * A player of card games.
 * 
 * @see PlayerIO
 * @see Points
 * @see cardgame.card.Bank
 */
public class Player
    implements Comparable<Player>
{
    private static int nPlayers_ = 0;
    
    private String     name_;
    private PlayerIO   playerIO_;
    private Points     points_;
    private List<Bank> banks_;
    
    /**
     * Secondary constructor. Provides the {@code Player} with a default name
     * based on the total number of {@code Player}s.
     * 
     * @param aPlayerIO the {@code PlayerIO} to use
     * @param nPoints   the initial number of {@code Points} owned
     * @param minPoints the minimum number of {@code Points} the {@code Player}
     *                  can have
     */
    public Player(PlayerIO aPlayerIO, int nPoints, int minPoints)
    {
        this("Player " + nPlayers_, aPlayerIO, nPoints, minPoints);
    }
    
    /**
     * Main constructor.
     * 
     * @param name      the name of the {@code Player}
     * @param aPlayerIO the {@code PlayerIO} to use
     * @param nPoints   the initial number of {@code Points} owned
     * @param minPoints the minimum number of {@code Points} the {@code Player}
     *                  can have
     */
    public Player(String name, PlayerIO aPlayerIO, int nPoints, int minPoints)
    {
        this.name_     = name;
        this.playerIO_ = aPlayerIO;
        this.points_   = new Points(nPoints, minPoints);
        this.banks_    = new ArrayList<Bank>();
        nPlayers_++;
    }
    
    /**
     * Returns a string representation of this {@code Player}'s name.
     * 
     * @return this {@code Player}'s name as a string
     */
    public String getName()
    {
        return this.name_;
    }
    
    /**
     * Returns the {@code PlayerIO} used by this {@code Player}.
     * 
     * @return the {@code PlayerIO} used by this {@code Player}
     */
    public PlayerIO getPlayerIO()
    {
        return this.playerIO_;
    }
    
    /**
     * Returns the total number of {@code Points} owned by this {@code Player}.
     * 
     * @return this {@code Player}'s {@code Points} total
     */
    public int getPointTotal()
    {
        return this.points_.getAmount();
    }
    
    /**
     * Modifies this {@code Player}'s {@code Points}.
     * 
     * @param increment the number of {@code Points} to add/subtract
     */
    public void modifyPoints(int increment)
    {
        this.points_.modify(increment);
    }
    
    /**
     * Compares this {@code Player} with {@code aPlayer} for ordering. Returns
     * a negative integer, zero, or a positive integer as this {@code Player}
     * is less than, equal to, or greater than {@code aPlayer}.
     * <p>
     * {@code Player}s are first compared by their number of {@code Points},
     * ordering least to most. If the {@code Player}s have the same number of
     * {@code Points}, then they are ordered alphabetically.
     * 
     * @param aPlayer the {@code Player} to be compared with
     * @see   java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Player aPlayer)
    {
        Integer thisPoints  = new Integer(this.getPointTotal());
        Integer otherPoints = new Integer(aPlayer.getPointTotal());
        int     result      = thisPoints.compareTo(otherPoints);
        if (result == 0)
            result = this.getName().compareTo(aPlayer.getName());
        return result;
    }
    
    /**
     * Gives this {@code Player} a specified {@code Bank}.
     * 
     * @param aBank the {@code Bank} to give this {@code Player}
     */
    public void addBank(Bank aBank)
    {
        this.banks_.add(aBank);
    }
    
    /**
     * Returns the first occurrence of an owned {@code Bank} with the specified
     * name.
     * <p>
     * Searches through this {@code Player}'s {@code Bank}s. When a
     * {@code Bank} is found with a name matching that of {@code aName}, it is
     * returned.
     * 
     * @param  aName the name of the {@code Bank}
     * @return the requested {@code Bank}
     * @throws NoSuchElementException if the named {@code Bank} does not exist
     */
    public Bank findBank(String aName)
        throws NoSuchElementException
    {
        boolean isFound  = false;
        int     i        = 0;
        Bank    testBank = new Bank("temp"); // Initialisation for returning
        
        while (!isFound) {
            try {
                testBank = this.banks_.get(i);
            }
            catch (IndexOutOfBoundsException exception) {
                throw new NoSuchElementException("That bank does not exist!");
            }
            
            String testName = testBank.toString();
            if (testName.equals(aName))
                isFound = true;
            i++;
        }
        
        return testBank;
    }
}
