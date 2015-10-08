package cardgame.acestokings;

import cardgame.PlayerIO;

enum PlayerChoice
{
    PLAY_ONE  (Type.TURN),
    PLAY_THREE(Type.TURN),
    END_TURN  (Type.TURN),
    
    RUN      (Type.DESTINATION),
    SAME_FACE(Type.DESTINATION);
    
    enum Type
    {
        TURN, DESTINATION;
    }
    
    private Type type_;
    
    // Constructor
    private PlayerChoice(Type type)
    {
        type_ = type;
    }
    
    // Prompts the player to make a choice and returns the result
    public static PlayerChoice makeChoice(PlayerIO playerIO, Type type)
    {
        String         message  = "You have the following options:";
        PlayerChoice[] choices  = createChoices(type);
        int            nChoices = choices.length;
        
        for (int i = 0; i < nChoices; i++) {
            message += "\n  " + i + ": " + choices[i];
        }
        
        playerIO.sendMessage(message);
        playerIO.sendMessage("Please make your choice.");
        int decision = playerIO.chooseInt(0, nChoices);
        
        return choices[decision];
    }
    
    // Creates an array of choices of the given type
    private static PlayerChoice[] createChoices(Type type)
    {
        int            nChoices      = 0;
        PlayerChoice[] possibilities = PlayerChoice.values();
        
        for (PlayerChoice choice : possibilities) {
            if (type == choice.type_)
                nChoices++;
        }
        
        PlayerChoice[] choices = new PlayerChoice[nChoices];
        
        int i = 0;
        for (PlayerChoice choice : possibilities) {
            if (type == choice.type_) {
                choices[i] = choice;
                i++;
            }
        }
        
        return choices;
    }
}