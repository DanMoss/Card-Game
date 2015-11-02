package cardgame.player;

import java.util.List;

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
     * @param  message  the initial string to send to the {@code PlayerIO}
     *                  before presenting them with their options
     * @param  options  the {@code Selectable}s to choose from
     * @return the chosen {@code Selectable}
     */
    public static <T extends Selectable> T
        select(PlayerIO aPlayerIO, String message, List<T> options)
    {
        aPlayerIO.sendMessage(message);
        String details = Selector.constructDetails(options);
        aPlayerIO.sendMessage(details);
        aPlayerIO.sendMessage("Please make your choice:");
        int choice = aPlayerIO.chooseInt(0, options.size());
        return options.get(choice);
    }
    
    // Constructs the message that will detail the options to the
    // {@code PlayerIO}.
    private static String constructDetails(List<? extends Selectable> options)
    {
        int    nOptions       = options.size();
        int    optionsPadding = String.valueOf(nOptions).length();
        String message        = "You have the following options:";
        
        for (int i = 0; i < nOptions; i++) {
            String index = String.format("%1$" + optionsPadding + "s", i);
            message     += "\n  " + index + ": " + options.get(i).getMessage();
        }
        
        return message;
    }
}
