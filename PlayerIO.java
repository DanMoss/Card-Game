package cardgame;

public interface PlayerIO
{
    public void sendMessage(String message);
    
    // lowerBound inclusive, upperBound exclusive
    public int chooseInt(int lowerBound, int upperBound);
}