package cardgame.player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import cardgame.card.Card;
import cardgame.card.Hand;

/**
 * A player of card games.
 * 
 * @param <T> the type of {@code Card}s this {@code Player} will use
 * @see   PlayerIO
 * @see   Points
 * @see   cardgame.card.Hand
 */
public class Player<T extends Card>
    implements Comparable<Player<?>>
{
    private static int nPlayers_ = 0;
    
    private final String        name_;
    private final PlayerIO      playerIO_;
    private final Points        points_;
    private final List<Hand<T>> hands_;
    
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
        this("Player " + Player.nPlayers_, aPlayerIO, nPoints, minPoints);
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
        this.hands_    = new ArrayList<Hand<T>>();
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
    public int compareTo(Player<?> aPlayer)
    {
        Integer thisPoints  = new Integer(this.getPointTotal());
        Integer otherPoints = new Integer(aPlayer.getPointTotal());
        int     result      = thisPoints.compareTo(otherPoints);
        if (result == 0)
            result = this.getName().compareTo(aPlayer.getName());
        return result;
    }
    
    /**
     * Gives this {@code Player} a specified {@code Hand}.
     * 
     * @param aHand the {@code Hand} to give this {@code Player}
     */
    public void addHand(Hand<T> aHand)
    {
        this.hands_.add(aHand);
    }
    
    /**
     * Returns the first occurrence of an owned {@code Hand} with the specified
     * name.
     * <p>
     * Searches through this {@code Player}'s {@code Hand}s. When a
     * {@code Hand} is found with a name matching that of {@code aName}, it is
     * returned.
     * 
     * @param  aName the name of the {@code Hand}
     * @return the requested {@code Hand}
     * @throws NoSuchElementException if the named {@code Hand} does not exist
     */
    public Hand<T> findHand(String aName)
        throws NoSuchElementException
    {
        boolean isFound = false;
        int     index   = 0;
        Hand<T> aHand   = new Hand<T>(""); // Initialisation for returning
        
        while (!isFound) {
            try {
                aHand = this.hands_.get(index);
            }
            catch (IndexOutOfBoundsException exception) {
                throw new NoSuchElementException("That hand does not exist!");
            }
            
            String handName = aHand.toString();
            if (handName.equals(aName))
                isFound = true;
            index++;
        }
        
        return aHand;
    }
}
