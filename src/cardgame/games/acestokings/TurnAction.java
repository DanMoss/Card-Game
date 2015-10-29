package cardgame.games.acestokings;

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
    
    // Returns the possible {@code TurnAction}s that can be performed based on
    // the {@code handSize}.
    // 
    // First loops through all {@code TurnAction}s to count the possibilities,
    // then creates an array and adds each possibility to it.
    // 
    // @param  handSize 
    // @return the possible {@code TurnAction}s
    static TurnAction[] findPossibleActions(int handSize)
    {
        TurnAction[] allActions = TurnAction.values();
        int          nActions   = 0;
        for (TurnAction anAction : allActions) {
            if (anAction.minHandSizeToPlay_ <= handSize)
                nActions++;
        }
        
        TurnAction[] options = new TurnAction[nActions];
        int          index   = 0;
        for (TurnAction anAction : allActions) {
            if (anAction.minHandSizeToPlay_ <= handSize) {
                options[index] = anAction;
                index++;
            }
        }
        
        return options;
    }
}
