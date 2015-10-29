package cardgame.player;

/**
 * A selector of selectables.
 * 
 * @see Selectable
 * @see PlayerIO
 */
public class Selector
{
    // Preventing class instantiation
    private Selector() {}
    
    /**
     * Returns a {@code Selectable} from a specified list of
     * {@code Selectable}s.
     * <p>
     * Given a list of {@code Selectable} {@code options}, a formatted string
     * message is constructed to detail the choices. This message is passed
     * through to {@code aPlayerIO} and the {@code aPlayerIO} is asked to
     * choose an integer representing a {@code Selectable}. Finally, this
     * {@code Selectable} that has been chosen is returned.
     * 
     * @param  playerIO the {PlayerIO} to communicate through
     * @param  options  the {@code Selectable}s to choose from
     * @return the chosen {@code Selectable}
     */
    public static <Type extends Selectable> Type
        select(PlayerIO aPlayerIO, Type[] options)
    {
        String message = Selector.constructMessage(options);
        aPlayerIO.sendMessage(message);
        aPlayerIO.sendMessage("Please make your choice:");
        int choice = aPlayerIO.chooseInt(0, options.length);
        return options[choice];
    }
    
    // Constructs the message to send to the player
    private static String constructMessage(Selectable... options)
    {
        int    nOptions       = options.length;
        int    optionsPadding = String.valueOf(nOptions).length();
        String message        = "You have the following options:";
        
        for (int i = 0; i < nOptions; i++) {
            String index = String.format("%1$" + optionsPadding + "s", i);
            message     += "\n  " + index + ": " + options[i].getMessage();
        }
        
        return message;
    }
}
