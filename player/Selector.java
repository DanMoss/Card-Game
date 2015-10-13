package cardgame.player;

public class Selector
{
    // Constructor
    private Selector()
    {
    }
    
    // Prompts the player to choose one of the {@code options} and returns the
    // choice
    public static <Type extends Selectable> Type
        select(PlayerIO playerIO, Type[] options)
    {
        String message  = "You have the following options:";
        int    nOptions = options.length;
        
        for (int i = 0; i < nOptions; i++)
            message += "\n  " + i + " :  " + options[i].getMessage();
        
        playerIO.sendMessage(message);
        playerIO.sendMessage("Please make your choice:");
        int choice = playerIO.chooseInt(0, nOptions);
        
        return options[choice];
    }
}