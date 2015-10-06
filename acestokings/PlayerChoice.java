package cardgame.acestokings;

import cardgame.PlayerIO;

enum PlayerChoice
{
    DECK        (Type.DRAW),
    DISCARD_PILE(Type.DRAW),
    
    PLAY_ONE  (Type.TURN),
    PLAY_THREE(Type.TURN),
    END_TURN  (Type.TURN),
    
    RUN      (Type.DESTINATION),
    SAME_FACE(Type.DESTINATION),
    
    // Future note: Remove the following and use Card.Suit in implementations
    CLUBS   (Type.SUIT),
    DIAMONDS(Type.SUIT),
    HEARTS  (Type.SUIT),
    SPADES  (Type.SUIT),
    
    // Future note: Remove the following and use Card.Face in implementations
    ACE  (Type.FACE),
    TWO  (Type.FACE),
    THREE(Type.FACE),
    FOUR (Type.FACE),
    FIVE (Type.FACE),
    SIX  (Type.FACE),
    SEVEN(Type.FACE),
    EIGHT(Type.FACE),
    NINE (Type.FACE),
    TEN  (Type.FACE),
    JACK (Type.FACE),
    QUEEN(Type.FACE),
    KING (Type.FACE),
    
    CARD_ZERO (Type.CARD),
    CARD_ONE  (Type.CARD),
    CARD_TWO  (Type.CARD),
    CARD_THREE(Type.CARD),
    CARD_FOUR (Type.CARD),
    CARD_FIVE (Type.CARD),
    CARD_SIX  (Type.CARD),
    CARD_SEVEN(Type.CARD);
    
    enum Type
    {
        DRAW, TURN, DESTINATION, CARD, SUIT, FACE;
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