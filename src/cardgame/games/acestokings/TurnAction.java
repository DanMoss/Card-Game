package cardgame.games.acestokings;

import java.util.ArrayList;
import java.util.List;

import cardgame.player.Selectable;

/**
 * The possible actions a player may make on their turn.
 * 
 * @see Turn
 * @see Selectable
 */
enum TurnAction
    implements Selectable
{
    END_TURN (1, "End your turn and discard."),
    ADD_CARD (2, "Add a card to a meld."),
    PLAY_MELD(2, "Play a set of cards.");
    
    private final int    minHandSizeToPlay_;
    private final String message_;
    
    // Constructor
    private TurnAction(int minHandSizeToPlay, String message)
    {
        this.minHandSizeToPlay_ = minHandSizeToPlay;
        this.message_           = message;
    }
    
    /* (non-Javadoc)
     * @see cardgame.player.Selectable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return message_;
    }
    
    // Returns a list of the possible {@code TurnAction}s that can be performed
    // based on the {@code handSize}.
    // 
    // @param  handSize the size of the hand
    // @return the possible {@code TurnAction}s
    static List<TurnAction>findPossibleActions(int handSize)
    {
        TurnAction[]     allActions = TurnAction.values();
        List<TurnAction> options    = new ArrayList<TurnAction>();
        for (TurnAction anAction : allActions) {
            if (anAction.minHandSizeToPlay_ <= handSize)
                options.add(anAction);
        }
        return options;
    }
}
