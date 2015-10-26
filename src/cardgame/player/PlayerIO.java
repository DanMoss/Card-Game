package cardgame.player;

public abstract class PlayerIO
{
    public abstract void sendMessage(String message);
    
    // lowerBound inclusive, upperBound exclusive
    public abstract int chooseInt(int lowerBound, int upperBound);
}